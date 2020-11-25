package com.eazyftw.simplewebsite.web;

import com.eazyftw.simplewebsite.web.mapping.Mappings;
import com.eazyftw.simplewebsite.web.response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class Server {

    private final int port;
    private final ServerSocket server;
    private Socket client;
    private final Mappings mappings;

    public Server(int port, Mappings mappings) throws IOException {
        this.port = port;
        this.server = new ServerSocket(port);
        this.mappings = mappings;
    }

    public Request accept() throws IOException {
        client = server.accept();
        InputStream is = client.getInputStream();

        int c;
        StringBuilder raw = new StringBuilder();
        do {
            c = is.read();
            raw.append((char) c);
        } while (is.available() > 0);

        return new Request(raw.toString());
    }

    public void shut() throws IOException {
        server.close();
    }

    private Response getResponse(Request req) {
        AbstractResponse respAbs = mappings.getMap(req.getMethod() + "_" + req.getUrl());
        if (respAbs == null)
            return new Response("<html><body><font color='red' size='2'>Invalid URL/method</font><br>URL: " + req.getUrl() + "<br>method: " + req.getMethod() + "</body></html>");

        return respAbs.getResponse(req);
    }

    public void sendResponse(Request req) throws IOException {
        Response resp = getResponse(req);

        OutputStream out = client.getOutputStream();
        out.write(resp.toString().getBytes());
    }
}