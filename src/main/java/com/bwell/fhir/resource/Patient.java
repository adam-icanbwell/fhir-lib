package com.bwell.fhir.resource;

import com.bwell.fhir.exception.FhirException;
import com.bwell.fhir.model.SupportedResourceType;
import com.bwell.fhir.parser.Parser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Iterator;

@NoArgsConstructor
@SuperBuilder
public class Patient extends BaseResource {
    @Override
    protected IBaseResource deserialize() throws FhirException {
        IBaseResource deserialized;
        try {
            //Deserialize the string
            deserialized = fhirVersion.newContext().newJsonParser().parseResource(fhir);
        }catch (Throwable e) {
            /*
                Since Patients have contained items in them, we will need
                to parse each on of those and check to see if they can pass
                as well - they also might have special cases
             */
            try {
                //Parse the FHIR String
                JsonNode rootNode = Parser.readTree(fhir);

                //Grab the Contained off of it
                JsonNode node = Parser.findJsonNode(rootNode, Parser.CONTAINED);
                ArrayNode arrayNode = Parser.createArrayNode();

                //Loop through contained and parse each one
                for (Iterator<JsonNode> it = node.elements(); it.hasNext();) {
                    JsonNode innerNode = it.next();
                    String innerFhir = innerNode.toString();

                    /*
                        Try to parse into FHIR - if it fails, see if we support special cases
                        and parse that way - if either/both fail, fail it all
                     */
                    try {
                        //If successful - just move on, it didn't cause the error for parsing
                        fhirVersion.newContext().newJsonParser().parseResource(innerFhir);
                    }catch (Throwable ee) {
                        /*
                            Since we are here, it erred; so, we need to check if a special way exists
                            and try it - if not, just error
                         */
                        String resourceType = Parser.findJsonNode(innerNode, Parser.RESOURCE_TYPE).asText();

                        //Check if we support this ResourceType and try to parse it
                        BaseResource resource = SupportedResourceType.getNewResource(resourceType);
                        if(resource == null){
                            //Means nothing special to do, just error
                            throw new FhirException(String.format("Failed to parse %s", resourceType), ee);
                        }

                        //Deserialize the JSON, and as we want to try and change the values
                        BaseResource parsedResource = resource.parse(fhirVersion, innerFhir);
                        JsonNode parsedNode = Parser.readTree(parsedResource.getFhir());

                        //Remove the changed node, and add the new one
                        it.remove();
                        arrayNode.add(parsedNode);
                    }
                }

                //Add ArrayNode of Contained items to the node
                ((ObjectNode) rootNode).set(Parser.CONTAINED, arrayNode);

                //Update the FHIR String
                fhir = rootNode.toString();
            }catch (Throwable ee) {
                throw new FhirException(ee);
            }

            //Now that all items have been parsed, we need to deserialize again
            deserialized = fhirVersion.newContext().newJsonParser().parseResource(fhir);
        }

        return deserialized;
    }

    @Override
    protected void read() {

    }
}
