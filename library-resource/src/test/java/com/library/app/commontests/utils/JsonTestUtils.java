package com.library.app.commontests.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;

@Ignore
public class JsonTestUtils {
    public static final String BASE_JSON_DIR = "json/";

    private JsonTestUtils() {
    }

    public static String readJsonFile(final String relativePath) {
        Scanner s = null;

        try (InputStream is = new FileInputStream(JsonTestUtils.class.getClassLoader().getResource(".").getPath()
                + BASE_JSON_DIR + relativePath)) {
            s = new Scanner(is);
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
        } catch (final JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void assertJsonMatchesFileContent(String actualJson, String fileNameWithExpectedJson) {
        assertJsonMatchesExpectedJson(actualJson, readJsonFile(fileNameWithExpectedJson));
    }

    public static Long getIdFromJson(String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
        return JsonReader.getLongOrNull(jsonObject, "id");
    }

}
