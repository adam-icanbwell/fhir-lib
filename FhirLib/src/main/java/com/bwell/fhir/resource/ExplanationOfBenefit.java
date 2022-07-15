package com.bwell.fhir.resource;

import com.bwell.fhir.exception.FhirException;
import com.bwell.fhir.parser.Parser;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

@NoArgsConstructor
@SuperBuilder
public class ExplanationOfBenefit extends BaseResource {
    public static String USE = "use";
    public static String OUTCOME = "outcome";

    @Override
    protected IBaseResource deserialize() throws FhirException {
        IBaseResource deserialized;
        try {
            //Deserialize the string
            deserialized = fhirVersion.newContext().newJsonParser().parseResource(fhir);
        }catch (Exception e) {
            /*
                There could be special cases around certain capital or lowercase items
                Let's try and get those to work
             */
            try {
                //Parse the FHIR String
                JsonNode rootNode = Parser.readTree(fhir);

                //Correct Use
                rootNode = Parser.changeJsonValue(rootNode, USE, (jsonNode) -> jsonNode.asText().toLowerCase());

                //Correct Outcome
                rootNode = Parser.changeJsonValue(rootNode, OUTCOME, (jsonNode) -> jsonNode.asText().toLowerCase());

                //Update the FHIR String
                fhir = rootNode.toString();
            }catch (Throwable ee) {
                throw new FhirException(ee);
            }

            //Try to Deserialize again and see if it's fixed
            deserialized = fhirVersion.newContext().newJsonParser().parseResource(fhir);
        }

        return deserialized;
    }

    @Override
    protected void read() {

    }
}
