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
        when(categoryServices.add(java())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = categoryResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "newCategory.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    private void assertJsonResponseWithFile(Response response, String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }
}
