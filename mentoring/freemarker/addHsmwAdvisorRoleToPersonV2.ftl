<#import "lib-vivo-form.ftl" as lvf>

<#assign htmlForElements = editConfiguration.pageData.htmlForElements />
<#assign sparqlForAcFilter = "" />
<#assign sparqlQueryUrl = "${urls.base}/ajax/sparqlQuery" />
<#assign editMode = "add" />
<#assign blankSentinel = ">SUBMITTED VALUE WAS BLANK<" />
<#assign flagClearLabelForExisting = "flagClearLabelForExisting" />
<#assign requiredHint = "<span class='requiredHint'> *</span>" />
<#assign choseType = "${i18n().chose_type_for_advsior_role}" />
<#assign submitButtonText = "${i18n().create_entry}" />
<#assign modificationTime = .now>
<#assign modificationDate = modificationTime?date>

<!-- get variables for existing advisor relationship -->
<#assign wissArbeitUri = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitUri") />
<#assign wissArbeitTitle = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitTitle") />
<#assign wissArbeitAdviseeName = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitAdviseeName") />
<#assign wissArbeitStart = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitStart") />
<#assign wissArbeitEnd = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitEnd") />
<#assign wissArbeitType = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitType") />
<#assign adviseeUri = lvf.getFormFieldValue(editSubmission, editConfiguration, "adviseeUri") />
<#assign wissArbeitAdviseeLabel = lvf.getFormFieldValue(editSubmission, editConfiguration, "wissArbeitAdviseeLabel") />

<#if editConfiguration.objectUri?has_content>
        <#assign title=": ${i18n().edit_entry_for_advisor_role} (${wissArbeitTitle})"/>
        <#assign editMode = "edit" />
        <#assign submitButtonText = "${i18n().save_changes}" />
<#else>
        <#assign title=": ${i18n().create_entry_for_advisor_role}"/>
        <#assign submitButtonText = "${i18n().create_entry}" />
</#if>

<#assign hsmwTypeSelect = [
			[ 'https://vivo.hs-mittweida.de/vivo/ontology/hsmw#Bachelorarbeit', i18n().bachelor ],
			[ 'https://vivo.hs-mittweida.de/vivo/ontology/hsmw#Diplomarbeit', i18n().diplom ],
			[ 'https://vivo.hs-mittweida.de/vivo/ontology/hsmw#Masterarbeit', i18n().master ],
            [ 'http://kerndatensatz-forschung.de/owl/Basis#Promotion', i18n().promotion ] 
		]
/>

<#if adviseeUri != "">
  <#assign wissArbeitAdviseeName = wissArbeitAdviseeLabel />
</#if>

<h2>${editConfiguration.subjectName}${title}</h2>

<div class="container-hsmw-white">

<!-- div class="errors" role="error" style="display: none;" id="errors" name="errors"></div><br / -->

<#if editSubmission.validationErrors?has_content>
    <section id="error-alert" role="alert">
        <img src="${urls.images}/iconAlert.png" width="24" height="24" alert="${i18n().error_alert_icon}" />
        <p>
            ${i18n().error_select_advisee} 
        </p>
    </section>
</#if>

<form id="addAdvisorRoleToPerson" class="customForm noIE67" action="${submitUrl}"  role="add/edit Presentation">

    <input type="hidden" id="editKey" name="editKey" value="${editKey}"/>
    <input type="hidden" name="wissArbeitTypePredicate" id="wissArbeitTypePredicate" value="https://vivo.hs-mittweida.de/vivo/ontology/hsmw#NameStudent">

    <#if editMode == "edit">
        <input type="hidden" id="wissArbeitUri" name="wissArbeitUri" value="${wissArbeitUri}"/>
        <input type="hidden" id="modificationDate" name="modificationDate" value="${modificationDate?iso_utc}"/>
    </#if>

        <div class="row">
            <span class="col-sm-2"><label for="wissArbeitType" class="hsmwLabel">${requiredHint} ${i18n().art_der_betreuung}</label></span>
            <#if editMode == "add">
                <span class="col-sm-10"><select width="90%" name="wissArbeitType" id="wissArbeitType" <#if wissArbeitType == "">class="acSelectorWithHelpText" onClick="removeStyle(this, 'acSelectorWithHelpText');"</#if>  onChange="setPredicate();" required data-msg="${choseType}">
                        <option value="">${i18n().select_one}</option>
                        <#list hsmwTypeSelect as type>
                            <option value="${type[0]}" <#if wissArbeitType == type[0]>selected</#if>>${type[1]}</option>
                        </#list>
                    </select>
                </span>
            <#else>
                <span class="col-sm-10">
                        <#list hsmwTypeSelect as type>
                            <#if wissArbeitType == type[0]>
                                <#assign displayLabel = type[1] />
                            </#if>
                        </#list>
                        ${displayLabel}
                </span>
            </#if>
        </div>
        <div class="row">
            <span class="col-sm-2"><label for="wissArbeitTitle" class="hsmwLabel">${requiredHint} ${i18n().titel_arbeit}</label></span>
            <span class="col-sm-10"><input size="60" name="wissArbeitTitle" id="wissArbeitTitle" type="text" value=<#if wissArbeitTitle == "">Titel<#else>"${wissArbeitTitle}"</#if> <#if wissArbeitTitle == "">class="acSelectorWithHelpText" onClick="clearElement(this); removeStyle(this, 'acSelectorWithHelpText');"</#if> required></span>
        </div>
        <div class="row">
            <span class="col-sm-2"><label for="advisee" class="hsmwLabel">${requiredHint} ${i18n().bearbeiter}</label></span>
            <span class="col-sm-10"><input size="60" class="acSelector" acGroupName="advisee" name="wissArbeitAdviseeName" id="advisee" type="text" value="${wissArbeitAdviseeName}" class="required" required><input class="display" type="hidden" id="adviseeDisplay" name="adviseeDisplay" acGroupName="advisee" value="${wissArbeitAdviseeName}"></span>
        </div>
        <div class="acSelection row hsmwAcDiv" acGroupName="advisee">
                <p class="inline">
                   <label for="acSelectionInfo" class="hsmwAcLabel">${i18n().selected_advisee}</label>
                        <span id="acSelectionInfo" class="acSelectionInfo hsmwAcSpan"></span>
                        <a href="" class="verifyMatch hsmwAcLink"  title="Übereinstimmung verifizieren">(${i18n().verify_match_capitalized}</a> oder
                    <a href="#" class="changeSelection hsmwAcLink" id="changeSelection">${i18n().change_selection})</a>
                </p>
                <input class="acUriReceiver" type="hidden" id="adviseeUri" name="adviseeUri" value="${adviseeUri}" ${flagClearLabelForExisting}="true" />
        </div>
        <div class="row">
            <span class="col-sm-2"><label for="wissArbeitStart" class="hsmwLabel">${i18n().start_bearbeitungszeitraum}</label></span>
            <span class="col-sm-10"><input name="wissArbeitStart" id="wissArbeitStart" <#if wissArbeitStart == "">class="hsmwDate acSelectorWithHelpText" onClick="removeStyle(this, 'acSelectorWithHelpText');"</#if> type="date" value="${wissArbeitStart}" placeholder="JJJJ-MM-TT"></span>
        </div>
        <div class="row">
            <span class="col-sm-2"><label for="wissArbeitEnd" class="hsmwLabel">${i18n().ende_bearbeitungszeitraum}</label></span>
            <span class="col-sm-10"><input name="wissArbeitEnd" id="wissArbeitEnd" <#if wissArbeitEnd == "">class="hsmwDate acSelectorWithHelpText" onClick="removeStyle(this, 'acSelectorWithHelpText');"</#if> type="date" value="${wissArbeitEnd}" placeholder="JJJJ-MM-TT"></span>
        </div>
         <div class="row"><span class="col-sm-12">&nbsp;</span></div>
         <div class="row">
            <span class="col-sm-12"><input type="submit" id="submit" value="${submitButtonText}"/><span class="or"> ${i18n().or} </span><a class="cancel" href="${cancelUrl}" title="${i18n().cancel_title}">${i18n().cancel_link}</a></span>
        </div>
        <p id="requiredLegend" class="requiredHint">* ${i18n().required_fields}</p>
    </form>
</div>

<script type="text/javascript">
    var customFormData  = {
        sparqlForAcFilter: '${sparqlForAcFilter}',
        sparqlQueryUrl: '${sparqlQueryUrl}',
        acUrl: '${urls.base}/autocomplete?tokenize=true',
        acTypes: {advisee: 'http://xmlns.com/foaf/0.1/Person'},
        editMode: '${editMode}',
        defaultTypeName: ' ', // used in repair mode to generate button text
        // multipleTypeNames: {advisee: 'Bearbeiter', wissArbeit: 'Abschlussarbeit'},
        baseHref: '${urls.base}/individual?uri=',
        blankSentinel: '${blankSentinel}',
        flagClearLabelForExisting: '${flagClearLabelForExisting}'
    };
    var i18nStrings = {
        selectAnExisting: '${i18n().select_an_advisee}',
        orCreateNewOne: ' ',
        selectedString: '${i18n().selected}'
    };
    
    function clearElement(element) {
        element.value = "";
    }

    function removeStyle(element, style) {
        element.classList.remove(style)
    }
    
    </script>


${stylesheets.add('<link rel="stylesheet" href="${urls.base}/js/jquery-ui/css/smoothness/jquery-ui-1.12.1.css" />',
					'<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/customForm.css" />',
					'<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/autocomplete.css" />',
					'<link rel="stylesheet" href="${urls.base}/templates/freemarker/edit/forms/css/manageDragDropList.css" />')}

 ${scripts.add('<script type="text/javascript" src="${urls.base}/js/jquery-ui/js/jquery-ui-1.12.1.min.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/customFormUtils.js"></script>',
              '<script type="text/javascript" src="${urls.base}/js/browserUtils.js"></script>',
              '<script type="text/javascript" src="${urls.theme}/js/HsmwCustomFormWithAutocomplete.js"></script>')}


<script>$("#addAdvisorRoleToPerson").validate({ errorLabelContainer: "#errors", errorClass: "error-label", debug: false });</script>

<script>
    function setPredicate() {
        sel = document.getElementById("wissArbeitType");
        hid = document.getElementById("wissArbeitTypePredicate");
        if (sel.value == "http://kerndatensatz-forschung.de/owl/Basis#Promotion") {
            hid.value = "https://vivo.hs-mittweida.de/vivo/ontology/hsmw#NameDesPromovenden";
        } else {
            hid.value = "https://vivo.hs-mittweida.de/vivo/ontology/hsmw#NameStudent";
        }
    }
</script>
