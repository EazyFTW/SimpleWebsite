package com.eazyftw.simplewebsite.web.mapping;

import com.eazyftw.simplewebsite.web.Request;
import com.eazyftw.simplewebsite.web.response.AbstractResponse;
import com.eazyftw.simplewebsite.web.response.Response;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class Mappings {

    private final HashMap<String, AbstractResponse> urlMappings;

    public Mappings() {
        this.urlMappings = new HashMap<>();
    }

    public AbstractResponse getMap(String key) {
        return this.urlMappings.get(key);
    }

    public void addMap(String method, String url, AbstractResponse resp) {
        this.urlMappings.put(method + "_" + url, resp);
    }

    public void addMap(Mapping mapping) {
        if(mapping.getFileName() == null) {
            addMap(mapping.getMethod().name(), mapping.getUrl(), mapping.getResponse());
        } else {
            addMap(mapping.getMethod().name(), mapping.getUrl(), mapping.getFileName());
        }
    }

    public void addMap(String method, String url, String filepath) {
        addMap(method, url, new AbstractResponse() {
            @Override
            public Response getResponse(Request req) {
                StringBuilder res = new StringBuilder();
                try {
                    FileReader fr = new FileReader(filepath);
                    int c;

                    for (c = fr.read(); c != -1; c = fr.read())
                        res.append((char) c);
                } catch (FileNotFoundException fnfe) {
                    return new Response("<html><body>Unable to find resource [" + url + "]</body></html>");
                } catch (IOException ioe) {
                    return new Response("<html><body>Unable to read resource [" + url + "]</body></html>");
                }
                res = new StringBuilder(replaceRequestAttribute(res.toString(), req));

                return new Response(res.toString());
            }
        });
    }

    private String replaceRequestAttribute(String res, Request req) {
        Iterator<String> itr = req.getAttributeIterator();

        while (itr.hasNext()) {
            String key = itr.next();
            String val = req.getAttribute(key);
            res = res.replace("${" + key + "}", val);
        }

        return res;
    }

    public HashMap<String, AbstractResponse> getUrlMappings() {
        return urlMappings;
    }
}