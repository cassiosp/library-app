package com.library.app.common.model;

public class OperationResult {

    private Boolean success;
    private String errorIdentification;
    private String errorDescription;
    private Object entity;

    private OperationResult(Object entity) {
        this.entity = entity;
    }

    private OperationResult(String errorIdentification, String errorDescription) {
        this.success = false;
        this.errorIdentification = errorIdentification;
        this.errorDescription = errorDescription;
    }

    public static OperationResult success(Object entity) {
        return new OperationResult(entity);
    }

    public static OperationResult success() {
        return new OperationResult(null);
    }

    public static OperationResult error(String errorIdentification, String errorDescription) {
        return new OperationResult(errorIdentification, errorDescription);
    }

    public Boolean isSuccess() {
        return success;
    }

    public String getErrorIdentification() {
        return errorIdentification;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "OperationResult [sucess=" + success + ", errorIdentification=" + errorIdentification
                + ", errorDescription=" + errorDescription + ", entity=" + entity + "]";
    }

}
