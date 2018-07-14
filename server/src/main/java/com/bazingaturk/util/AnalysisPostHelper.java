package com.bazingaturk.util;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.Map;

public class AnalysisPostHelper {

    private static String baseurl = "http://localhost:8000";

    public static HttpResponse post(Map<String, String> map, String url) throws IOException {
        return Request.Post(baseurl + url)
                .bodyForm(Form.form()
                        .add("tags", map.get("tags"))
                        .add("boxes", map.get("boxes"))
                        .add("accuracies", map.get("accuracies"))
                        .add("attributeSize", map.get("attributeSize"))
                        .add("imageId", map.get("imageId"))
                        .build(), Consts.UTF_8)
                .execute()
                .returnResponse();
    }
}
