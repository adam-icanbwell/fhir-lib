package com.bwell.fhir.model;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;

public interface FhirResource extends IBase {
    default boolean isEmpty() {
        return false;
    }
    default boolean hasFormatComment() {
        return false;
    }
    default List<String> getFormatCommentsPre() {
        return null;
    }
    default List<String> getFormatCommentsPost() {
        return null;
    }
    default Object getUserData(String theName) {
        return null;
    }
    default void setUserData(String theName, Object theValue) {

    }
}
