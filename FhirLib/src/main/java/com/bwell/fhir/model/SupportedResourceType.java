package com.bwell.fhir.model;

import com.bwell.fhir.resource.BaseResource;
import com.bwell.fhir.resource.ExplanationOfBenefit;
import com.bwell.fhir.resource.Patient;

public enum SupportedResourceType {
    Patient {
        @Override
        public BaseResource newResource() {
            return new Patient();
        }
    },
    ExplanationOfBenefit {
        @Override
        public BaseResource newResource() {
            return new ExplanationOfBenefit();
        }
    };

    public abstract BaseResource newResource();

    public static BaseResource getNewResource(String resourceType) {
        try{
            return SupportedResourceType.valueOf(resourceType).newResource();
        }catch (Exception e) {
            //Just return null - as it's not supported
            return null;
        }
    }
}
