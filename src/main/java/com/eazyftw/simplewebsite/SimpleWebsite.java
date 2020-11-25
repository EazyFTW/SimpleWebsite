package com.eazyftw.simplewebsite;

import com.eazyftw.simplewebsite.file.Config;
import com.eazyftw.simplewebsite.web.Request;
import com.eazyftw.simplewebsite.web.Server;
import com.eazyftw.simplewebsite.web.mapping.Mapping;
import com.eazyftw.simplewebsite.web.mapping.Mappings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleWebsite {

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        start(new ArrayList<>());
    }

    public static void start(List<Mapping> mapping) throws IOException {
        Mappings mappings = new Mappings();

        int port = Config.getInstance().getPort();
        mapping.forEach(mappings::addMap);
        Config.getInstance().getMappings().forEach(mappings::addMap);

        if(mappings.getUrlMappings().size() == 0) {
            System.out.println("ERROR - Couldn't find any mappings, stopping.");
            System.exit(0);
        } else {
            System.out.println("SUCCESS - Starting server with the port " + port + "!");
        }

        Server server;
        while (true) {
            server = new Server(port, mappings);

            Request req = server.accept();
            server.sendResponse(req);
            server.shut();
        }
    }
}
