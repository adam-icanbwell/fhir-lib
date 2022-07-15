package com.bwell.fhir.exception;

public class FhirException extends Throwable {
    public FhirException() {
        super();
    }

    public FhirException(String message) {
        super(message);
    }

    public FhirException(String message, Throwable cause) {
        super(message, cause);
    }

    public FhirException(Throwable cause) {
        super(cause);
    }
}
