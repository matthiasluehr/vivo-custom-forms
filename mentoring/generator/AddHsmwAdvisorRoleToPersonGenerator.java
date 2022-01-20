/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.jena.vocabulary.XSD;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.HTML5DateTimeVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.HTML5DatepickerValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.AutocompleteRequiredInputValidator;
// import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.UniqueValueValidator;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ChildVClassesWithParent;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;


public class AddHsmwAdvisorRoleToPersonGenerator extends VivoBaseGenerator implements EditConfigurationGenerator {

    public AddHsmwAdvisorRoleToPersonGenerator() {}

    @Override
    public EditConfigurationVTwo getEditConfiguration(VitroRequest vreq, HttpSession session) throws Exception {

        EditConfigurationVTwo conf = new EditConfigurationVTwo();

        initBasics(conf, vreq);
        initPropertyParameters(vreq, session, conf);
        initObjectPropForm(conf, vreq);

        conf.setTemplate("addHsmwAdvisorRoleToPersonV2.ftl");
    
        conf.setVarNameForSubject("person");
        conf.setVarNameForPredicate("predicate");
        conf.setVarNameForObject("advisorrole");

        conf.setUrisOnform(Arrays.asList("wissArbeitType", "wissArbeitTypePredicate", "wissArbeitUri", "adviseeUri"));
        conf.setLiteralsOnForm(Arrays.asList("wissArbeitTitle", "wissArbeitAdviseeName", "wissArbeitStart", "wissArbeitEnd", "modificationDate"));


        // conf.setN3Required( Arrays.asList( n3ForNewAdvisorRole ) );
        conf.setN3Optional( Arrays.asList( getN3ForNewAdvisorRole(),
                                           n3ForModificationDate,
                                           n3ForWissArbeitTitle,
                                           n3ForAdviseeName,
                                           n3ForAdviseeRole,
                                           n3ForStart,
                                           n3ForEnd
        ));
        
        
        // conf.setN3Optional( Arrays.asList( n3ForRoleLabelAssertion,
		// 								   n3ForNewWissArbeit,
        //                                   n3ForExistingWissArbeit,
        //                                   n3ForStart,
        //                                   n3ForEnd,
        //                                   // n3ForNewConferenceInterval,
        //                                   n3ForNewPresentationInterval ) );

        conf.addNewResource("advisorrole", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("wissArbeitUri", DEFAULT_NS_FOR_NEW_RESOURCE);
        conf.addNewResource("adviseerole", DEFAULT_NS_FOR_NEW_RESOURCE);

        conf.addSparqlForExistingUris("wissArbeitUri", wissArbeitUriQuery);
        conf.addSparqlForExistingUris("wissArbeitType", wissArbeitTypeQuery);
        conf.addSparqlForExistingUris("adviseeUri", wissArbeitAdviseeUriQuery);
        conf.addSparqlForExistingUris("adviseerole", wissArbeitAdviseeRoleQuery);


        conf.addSparqlForExistingLiteral("modificationDate", modificationDateQuery);
        conf.addSparqlForExistingLiteral("wissArbeitTitle", wissArbeitTitleQuery);
        conf.addSparqlForExistingLiteral("wissArbeitStart", wissArbeitStartQuery);
        conf.addSparqlForExistingLiteral("wissArbeitAdviseeName", wissArbeitAdviseeNameQuery);
        conf.addSparqlForExistingLiteral("wissArbeitEnd", wissArbeitEndQuery);
        conf.addSparqlForExistingLiteral("wissArbeitAdviseeLabel", wissArbeitAdviseeLabelQuery);


        conf.addField(new FieldVTwo().
                setName("wissArbeitTitle")
                .setRangeDatatypeUri( XSD.xstring.toString() ).
                setValidators( list("datatype:" + XSD.xstring.toString()) )
                );
        
        conf.addField(new FieldVTwo().
                setName("wissArbeitAdviseeName")
                .setRangeDatatypeUri( XSD.xstring.toString() ).
                setValidators( list("datatype:" + XSD.xstring.toString()) )
                );

        conf.addField(new FieldVTwo().
                setName("wissArbeitStart")
                .setRangeDatatypeUri(XSD.date.toString() ).
                setValidators( list("datatype:" + XSD.date.toString()) )
                );

        conf.addField(new FieldVTwo().
                setName("wissArbeitEnd")
                .setRangeDatatypeUri(XSD.date.toString() ).
                setValidators( list("datatype:" + XSD.date.toString()) )
                );

        conf.addField(new FieldVTwo().
                setName("modificationDate")
                .setRangeDatatypeUri(XSD.date.toString() ).
                setValidators( list("datatype:" + XSD.date.toString()) )
                );

        // conf.addField(new FieldVTwo().setName("wissArbeitStart").
        //        setEditElement(new HTML5DateTimeVTwo(null,
        //                        VitroVocabulary.Precision.DAY.uri(),
        //                        VitroVocabulary.Precision.DAY.uri())                
        //        ));

        // conf.addField( new FieldVTwo().setName("wissArbeitEnd").
        //         setEditElement(new HTML5DateTimeVTwo(null,
        //                        VitroVocabulary.Precision.DAY.uri(),
        //                        VitroVocabulary.Precision.DAY.uri())                
        //        )); 

        
        // conf.addValidator(new UniqueValueValidator("wissArbeitName", "vivo:Equipment", "hsmw:InventarNummer"));

        // conf.addValidator(new HTML5DatepickerValidator("wissArbeitStart"));
        // conf.addValidator(new HTML5DatepickerValidator("wissArbeitEnd"));        
        conf.addValidator(new AutocompleteRequiredInputValidator("adviseeUri", "wissArbeitAdviseeName"));
        conf.addValidator(new AntiXssValidation());

        prepare(vreq, conf);
        return conf;
    }

    /* N3 assertions  */
    private String getN3ForNewAdvisorRole() {
        return "?person <http://purl.obolibrary.org/obo/RO_0000053> ?advisorrole . \n" +
        "?advisorrole <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#RolleDesBetreuers>. \n" +
        "?advisorrole <http://purl.obolibrary.org/obo/RO_0000052> ?person . " +
        "?wissArbeitUri <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?wissArbeitType ." +
        "?wissArbeitUri <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#createdAt> " + this.getCurrentTime() + " ." +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri ." ;
    }

    final static String n3ForModificationDate =
         "?wissArbeitUri <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#modifiedAt> ?modificationDate .";

    final static String n3ForWissArbeitTitle = 
        "?wissArbeitUri <http://www.w3.org/2000/01/rdf-schema#label> ?wissArbeitTitle ." ;

    final static String n3ForAdviseeName =
        "?wissArbeitUri ?wissArbeitTypePredicate ?wissArbeitAdviseeName ." ;

    final static String n3ForAdviseeRole =
        "?wissArbeitUri <http://vivoweb.org/ontology/core#contributingRole> ?adviseerole ." +
        "?adviseerole <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#RolleDesBetreuten> ." +
        "?adviseerole <http://purl.obolibrary.org/obo/RO_0000052> ?adviseeUri ." +
        "?adviseeUri  <http://purl.obolibrary.org/obo/RO_0000053> ?adviseerole .";

    final static String n3ForStart =
        "?wissArbeitUri  <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#StartWissArbeit> ?wissArbeitStart ." ;

    final static String n3ForEnd =
        "?wissArbeitUri  <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#EndeWissArbeit> ?wissArbeitEnd ." ;

    final static String modificationDateQuery =
        "SELECT ?modificationDate WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#modifiedAt> ?modificationDate . }";

    final static String wissArbeitTitleQuery =
        "SELECT ?wissArbeitTitle WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri <http://www.w3.org/2000/01/rdf-schema#label> ?wissArbeitTitle . }";

    final static String wissArbeitAdviseeNameQuery =
        "SELECT ?wissArbeitAdviseeName WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri  <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#NameStudent> ?wissArbeitAdviseeName . }";

    final static String wissArbeitStartQuery =
        "PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#>  \n" +
        "SELECT ?wissArbeitStart WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri  <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#StartWissArbeit> ?wissArbeitStartDt . \n" +
        "BIND(xsd:date(?wissArbeitStartDt) AS ?wissArbeitStart) }";

    final static String wissArbeitEndQuery =
        "PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#>  \n" +
        "SELECT ?wissArbeitEnd WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri  <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#EndeWissArbeit> ?wissArbeitEndDt . \n" +
        "BIND(xsd:date(?wissArbeitEndDt) AS ?wissArbeitEnd) }";
 
    final static String wissArbeitUriQuery =
        "SELECT ?wissArbeitUri WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . }";

    final static String wissArbeitTypeQuery =
        "SELECT ?wissArbeitType WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri  <http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType> ?wissArbeitType . }";

    final static String wissArbeitAdviseeUriQuery =
        "SELECT ?adviseeUri WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri <http://vivoweb.org/ontology/core#contributingRole> ?wissArbeitAdviseeRole . \n" +
        "?wissArbeitAdviseeRole <http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType> <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#RolleDesBetreuten> . \n" +
        "?wissArbeitAdviseeRole <http://purl.obolibrary.org/obo/RO_0000052> ?adviseeUri . }";   

    final static String wissArbeitAdviseeRoleQuery =
        "SELECT ?adviseerole WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri <http://vivoweb.org/ontology/core#contributingRole> ?adviseerole . \n" +
        "?adviseerole <http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType> <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#RolleDesBetreuten> .}" ;
    
    final static String wissArbeitAdviseeLabelQuery =
        "SELECT ?wissArbeitAdviseeLabel WHERE { \n" +
        "?advisorrole <http://vivoweb.org/ontology/core#roleContributesTo> ?wissArbeitUri . \n" +
        "?wissArbeitUri <http://vivoweb.org/ontology/core#contributingRole> ?wissArbeitAdviseeRole . \n" +
        "?wissArbeitAdviseeRole <http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType> <https://vivo.hs-mittweida.de/vivo/ontology/hsmw#RolleDesBetreuten> . \n" +
        "?wissArbeitAdviseeRole <http://purl.obolibrary.org/obo/RO_0000052> ?wissArbeitAdviseeUri . \n" +
        "?wissArbeitAdviseeUri <http://www.w3.org/2000/01/rdf-schema#label> ?wissArbeitAdviseeLabel . }";    

}
