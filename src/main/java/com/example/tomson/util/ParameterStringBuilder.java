package com.example.tomson.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ParameterStringBuilder {

    private static final Logger log = LoggerFactory.getLogger(ParameterStringBuilder.class);

    public static String getParamsString(Map<String, Object> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            Gson gson = new Gson();
            String jsonValue = gson.toJson(entry.getValue());
            log.info("ParameterStringBuilder - {}", jsonValue);
            result.append(jsonValue);
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
               ? resultString.substring(0, resultString.length() - 1)
               : resultString;
    }
}