package com.library.app.category.services.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;

public class CategoryServicesUTest {

    private CategoryServices categoryServices;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        categoryServices = new CategoryServicesImpl();
        ((CategoryServicesImpl) categoryServices).validator = validator;
    }

    @Test
    public void addCategoryWithNullName() {
        try {
            categoryServices.add(new Category());
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

}
