package com.library.app.category.resource;

import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static com.library.app.commontests.utils.FileTestNameUtils.*;
import static com.library.app.commontests.utils.JsonTestUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.category.expetion.CategoryExistentException;
import com.library.app.category.expetion.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.HttpCode;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryResourceUTest {
    private CategoryResource categoryResource;

    private static final String PATH_RESOURCE = "categories";

    @Mock
    private CategoryServices categoryServices;

    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);
        categoryResource = new CategoryResource();

        categoryResource.categoryServices = categoryServices;
        categoryResource.categoryJsonConverter = new CategoryJsonConverter();
    }

    @Test
    public void addValidCategory() {
        when(categoryServices.add(java())).thenReturn(categoryWithId(java(), 1L));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    @Test
    public void addExistentCategory() {
        when(categoryServices.add(java())).thenThrow(new CategoryExistentException());

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void addCategoryWithNullName() {
        when(categoryServices.add(new Category())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateValidCategory() {
        final Response response = categoryResource.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{}");

        verify(categoryServices).update(categoryWithId(java(), 1L));
    }

    @Test
    public void updateCategoryWithExistingName() {
        doThrow(new CategoryExistentException()).when(categoryServices).update(categoryWithId(java(), 1L));

        final Response response = categoryResource.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    @Test
    public void updateCategoryWithNullName() {
        doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices).update(
                categoryWithId(new Category(), 1L));

        final Response response = categoryResource.update(1L, readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "categoryWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    @Test
    public void updateCategoryNotFound() {
        doThrow(new CategoryNotFoundException()).when(categoryServices).update(categoryWithId(java(), 2L));

        final Response response = categoryResource.update(2L, readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "category.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
        assertJsonResponseWithFile(response, "categoryNotFound.json");
    }

    private void assertJsonResponseWithFile(Response response, String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }
}
