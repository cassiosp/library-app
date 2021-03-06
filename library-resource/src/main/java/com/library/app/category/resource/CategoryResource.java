package com.library.app.category.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.expetion.CategoryExistentException;
import com.library.app.category.expetion.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.HttpCode;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.ResourceMessage;
import com.library.app.common.model.StandardsOperationResults;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("category");

    @Inject
    CategoryServices categoryServices;

    @Inject
    CategoryJsonConverter categoryJsonConverter;

    @POST
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

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, String body) {
        logger.debug("Updating a new category with body {}", body);
        final Category category = categoryJsonConverter.convertFrom(body);
        category.setId(id);

        OperationResult result = null;
        HttpCode httpCode = HttpCode.OK;

        try {
            categoryServices.update(category);
            result = OperationResult.success();
        } catch (final CategoryNotFoundException e) {
            logger.error("No category found for the given id", e);
            httpCode = HttpCode.NOT_FOUND;
            result = StandardsOperationResults.getOperationResultNotFound(RESOURCE_MESSAGE);
        } catch (final FieldNotValidException e) {
            logger.error("One of the fields of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = StandardsOperationResults.getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryExistentException e) {
            logger.error("There is already a category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = StandardsOperationResults.getOperationResultExistent(RESOURCE_MESSAGE, "name");
        }

        logger.debug("Returning the operation result after updating category: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        logger.debug("Find category: {}", id);
        OperationResult result = null;
        HttpCode httpCode = HttpCode.OK;

        try {
            final Category category = categoryServices.findById(id);
            result = OperationResult.success(categoryJsonConverter.convertToJsonElement(category));
            logger.debug("Category found: {}", category);
        } catch (final Exception e) {
            logger.error("No category found for id {}", id);
            result = StandardsOperationResults.getOperationResultNotFound(RESOURCE_MESSAGE);
            httpCode = HttpCode.NOT_FOUND;
        }

        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    @GET
    public Response findAll() {
        logger.debug("Find all categories");

        final List<Category> categories = categoryServices.findAll();

        final JsonElement jsonWithPagingAndEntries = getJsonWithPagingAndEntries(categories);

        return Response.status(HttpCode.OK.getCode())
                .entity(OperationResultJsonWriter.toJson(OperationResult.success(jsonWithPagingAndEntries))).build();
    }

    private JsonElement getJsonWithPagingAndEntries(List<Category> categories) {
        final JsonObject jsonWithPagingAndEntries = new JsonObject();

        final JsonObject jsonPaging = new JsonObject();
        jsonPaging.addProperty("totalRecords", categories.size());

        jsonWithPagingAndEntries.add("paging", jsonPaging);
        jsonWithPagingAndEntries.add("entries", categoryJsonConverter.convertToJsonElement(categories));

        return jsonWithPagingAndEntries;
    }
}
