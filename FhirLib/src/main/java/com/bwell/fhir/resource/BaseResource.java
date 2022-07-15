package com.bwell.fhir.resource;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.bwell.fhir.exception.FhirException;
import com.bwell.fhir.model.FhirResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseResource implements FhirResource {
    protected FhirVersionEnum fhirVersion;
    protected String fhir;
    protected IBaseResource baseResource;

    protected abstract IBaseResource deserialize() throws FhirException;
    protected abstract void read();

    public BaseResource parse(FhirVersionEnum fhirVersionEnum, String fhirString) throws FhirException {
        fhirVersion = fhirVersionEnum;
        fhir = fhirString;

        //Deserialize String
        baseResource = deserialize();

        //Read items from Resource
        read();

        return this;
    }
}
