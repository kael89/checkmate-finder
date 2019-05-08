package org.kkarvounis.chasemate.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public interface JsonSerializableInterface {
    ObjectMapper jsonMapper = new ObjectMapper();

    static Object fromJson(String json, Class className) {
        try {
            return jsonMapper.readValue(json, className);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object fromJson(File file, Class className) {
        try {
            return jsonMapper.readValue(file, className);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object[] fromJsonArray(String json, Class className) {
        try {
            return (Object[]) jsonMapper.readValue(json, className);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object[] fromJsonArray(File file, Class className) {
        try {
            return (Object[]) jsonMapper.readValue(file, className);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    default String toJson() {
        try {
            return jsonMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
