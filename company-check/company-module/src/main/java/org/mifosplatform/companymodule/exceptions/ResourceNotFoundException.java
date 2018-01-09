package org.mifosplatform.companymodule.exceptions;

/**
 * A {@link RuntimeException} thrown when resources are not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String messageCode;
    private final String defaultUserMessage;
    private final Long args;

    public ResourceNotFoundException(final Long id, String resourceName) {
        this.messageCode = "error.msg."+resourceName+".id.invalid";
        this.defaultUserMessage = resourceName+" with identifier " + id + " does not exist";
        this.args = id;
    }
}
