package org.mifosplatform.companymodule.exceptions.data;

public class EntityException {

    private int status;
    //private String code;
    private String message;
    private String developerMessage;

    public EntityException(int status, String message, String developerMessage) {
        this.status = status;
        this.message = message;
        this.developerMessage = developerMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}

