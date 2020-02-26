package org.web.servlet;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static Map<String, List<String>> splitQuery(String url) {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<>();
        final String[] pairs = url.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0
                               ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8)
                               : pair;
            if (!queryPairs.containsKey(key)) {
                queryPairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1
                                 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8)
                                 : null;
            queryPairs.get(key).add(value);
        }
        return queryPairs;
    }

}
