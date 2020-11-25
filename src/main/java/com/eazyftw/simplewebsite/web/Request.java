package com.eazyftw.simplewebsite.web;

import java.util.HashMap;
import java.util.Iterator;

public final class Request {

    private final String req;
    private String method;
    private String url;
    private String httpVersion;
    private final HashMap<String, String> attributes;

    public Request(String req) {
        this.req = req;
        this.attributes = new HashMap<>();

        parse();
    }

    private void parse() {
        String[] temp = req.split("\r\n");
        String firstLine = temp[0];
        String[] firstLineSplit = firstLine.split(" ");

        if (firstLineSplit.length == 3) {
            this.method = firstLineSplit[0];
            this.httpVersion = firstLineSplit[2];

            if (method.equals("POST")) {
                this.url = firstLineSplit[1];
                setAttributes(temp[temp.length - 1]);
            } else if (method.equals("GET")) {
                String[] arr = firstLineSplit[1].split("[?]");

                if (arr.length == 2) {
                    this.url = arr[0];
                    setAttributes(arr[1]);
                } else {
                    this.url = firstLineSplit[1];
                }
            } else {
                this.url = firstLineSplit[1];
            }
        }
    }

    private void setAttributes(String rawAttributes) {
        String[] attribs = rawAttributes.split("&");
        for (String attrib : attribs) {
            String[] attr = attrib.split("=");

            if (attr.length == 2)
                setAttribute(attr[0], attr[1].replace("+", " "));
        }
    }

    public String getAttribute(String key) {
        String ret = attributes.get(key);
        if (ret == null)
            return "null";

        return ret;
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public Iterator<String> getAttributeIterator() {
        return attributes.keySet().iterator();
    }

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getUrl() {
        return url;
    }

    public String toString() {
        return req;
    }
}