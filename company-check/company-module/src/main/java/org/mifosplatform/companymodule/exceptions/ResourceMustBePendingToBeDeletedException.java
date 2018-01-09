package org.mifosplatform.companymodule.exceptions;

/**
 * A {@link RuntimeException} thrown when attempting to delete a resource.
 */
public class ResourceMustBePendingToBeDeletedException extends RuntimeException {

    private final String messageCode;
    private final String defaultUserMessage;
    private final Long args;

    public ResourceMustBePendingToBeDeletedException(final Long id, String resourceName) {
        this.messageCode = "error.msg."+resourceName+".cannot.be.deleted";
        this.defaultUserMessage = resourceName+" with identifier " + id + " cannot be deleted as it is not in `Pending` state.";
        this.args = id;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getDefaultUserMessage() {
        return defaultUserMessage;
    }

}
