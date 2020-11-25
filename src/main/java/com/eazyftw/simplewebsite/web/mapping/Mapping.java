package com.eazyftw.simplewebsite.web.mapping;

import com.eazyftw.simplewebsite.web.response.AbstractResponse;

public class Mapping {

    private final Method method;
    private final String url, fileName;
    private final AbstractResponse response;

    public Mapping(Method method, String url, String fileName) {
        this.method = method;
        this.url = url;
        this.fileName = fileName;
        this.response = null;
    }

    public Mapping(Method method, String url, AbstractResponse response) {
        this.method = method;
        this.url = url;
        this.fileName = null;
        this.response = response;
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public AbstractResponse getResponse() {
        return response;
    }

    public enum Method {

        GET,
        POST;

        public static Method fromString(String s) {
            try {
                return Method.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return GET;
            }
        }
    }
}
