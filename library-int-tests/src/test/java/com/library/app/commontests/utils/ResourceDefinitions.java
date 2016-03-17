package com.library.app.commontests.utils;

public enum ResourceDefinitions {

    CATAGORY("categories");

    private String resourceName;

    private ResourceDefinitions(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

}
