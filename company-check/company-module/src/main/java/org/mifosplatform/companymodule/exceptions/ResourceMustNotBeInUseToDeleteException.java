package org.mifosplatform.companymodule.exceptions;

public class ResourceMustNotBeInUseToDeleteException extends RuntimeException {

    private final String messageCode;
    private final String defaultUserMessage;
    private final Long args;

    public ResourceMustNotBeInUseToDeleteException(Long sourceId, String resourceName) {

        this.messageCode = "error.msg."+resourceName+".cannot.be.deleted";
        this.defaultUserMessage = resourceName+" with identifier " + sourceId + " cannot be deleted as it is in use by another entity.";
        this.args = sourceId;

    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getDefaultUserMessage() {
        return defaultUserMessage;
    }

}
