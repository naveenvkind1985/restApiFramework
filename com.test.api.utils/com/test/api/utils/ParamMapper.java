package com.test.api.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * A simple mapper that uses a tokenizer to
 * separate by new line character and map key
 * value pairs separated by colon operator(:)
 */
public class ParamMapper {


    public Map<String, String> map(String params) {

        Map<String, String> paramMap = new HashMap<>();
        String[] keyValue = params.split("[\\r\\n]+");
        for (String values : keyValue) {
            String[] pairs = values.trim().split(":");
            paramMap.put(pairs[0], pairs[1]);
        }
        return paramMap;
    }
}

