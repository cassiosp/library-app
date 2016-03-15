package com.library.app.category.services;

import com.library.app.category.expetion.CategoryExistentException;
import com.library.app.category.model.Category;
import com.library.app.common.exception.FieldNotValidException;

public interface CategoryServices {

    Category add(Category category) throws FieldNotValidException, CategoryExistentException;

}
