package com.library.app.common.json;

import com.google.gson.Gson;
import com.library.app.common.model.OperationResult;

public final class OperationResultJsonWriter {

    private OperationResultJsonWriter() {
    }

    public static String toJson(OperationResult operationResult) {
        if (operationResult.getEntity() == null) {
            final Gson gson = new Gson();
            return gson.toJson(operationResult);
        }

        final Gson gson = new Gson();
        return gson.toJson(operationResult.getEntity());
    }
}
