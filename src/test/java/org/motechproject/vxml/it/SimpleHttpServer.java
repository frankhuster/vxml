package org.motechproject.vxml.it;

/**
 * todo
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Super Simple HTTP Server
 *
 * inspired from from http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
 */
public class SimpleHttpServer {

    private String resource;
    private int port;
    private HttpServer server;
    private int responseCode;
    private String responseBody;

    public SimpleHttpServer(String resource, int port, int responseCode, String responseBody) throws IOException {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.resource = resource;
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        start();
    }

    private void start() throws IOException {
        server.createContext(String.format("/%s", resource), new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(responseCode, responseBody.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(responseBody.getBytes());
                os.close();
            }
        });
        server.setExecutor(null);
        server.start();
    }

    public String getUri() {
        return String.format("http://localhost:%d/%s", port, resource);
    }
}