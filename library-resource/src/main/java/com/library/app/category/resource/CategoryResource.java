package com.library.app.category.resource;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.app.category.expetion.CategoryExistentException;
import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.HttpCode;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.ResourceMessage;
import com.library.app.common.model.StandardsOperationResults;

public class CategoryResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("category");

    CategoryServices categoryServices;

    CategoryJsonConverter categoryJsonConverter;

    public Response add(String body) {
        logger.debug("Adding a new category with body {}", body);
        Category category = categoryJsonConverter.convertFrom(body);

        OperationResult result = null;
        HttpCode httpCode = HttpCode.CREATED;

        try {
            category = categoryServices.add(category);
            result = OperationResult.success(JsonUtils.getJsonElementWithId(category.getId()));
        } catch (final FieldNotValidException e) {
            logger.error("One of the fields of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = StandardsOperationResults.getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryExistentException e) {
            logger.error("There is already a category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = StandardsOperationResults.getOperationResultExistent(RESOURCE_MESSAGE, "name");
        }

        logger.debug("Returning the operation result after adding category: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }
}
