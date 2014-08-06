package org.motechproject.vxml.it;

/**
 * todo
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.Ignore;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Super Simple HTTP Server
 *
 * inspired from from http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
 */
@Ignore
public class SimpleHttpServer {

    private String resource;
    private int port;
    private HttpServer server;

    public SimpleHttpServer(String resource, int port) throws IOException {
        this.resource = resource;
        this.port = port;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(String.format("/%s", resource), new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String response = "OK";
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        server.setExecutor(null);
        server.start();
    }
}