package com.bwell.fhir.type;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

public enum SupportedFhirVersion {
    R4 {
        @Override
        public FhirContext fhirContext() {
            return fhirContext4;
        }

        @Override
        public FhirVersionEnum fhirVersionEnum() {
            return FhirVersionEnum.R4;
        }

        @Override
        public int version() {
            return 4;
        }
    };

    private static final FhirContext fhirContext4 = FhirContext.forR4();

    /**
     * Get FhirContext for the Version
     */
    public abstract FhirContext fhirContext();

    /**
     * Get FhirVersionEnum for the Version
     */
    public abstract FhirVersionEnum fhirVersionEnum();

    /**
     * Which version in Integer form
     */
    public abstract int version();
}
