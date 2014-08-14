package org.motechproject.vxml.it;

/**
 * todo
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.motechproject.vxml.CallInitiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SimpleHttpServer(String resource, int responseCode, String responseBody) {
        logger.debug(String.format("SimpleHttpServer()", resource, responseCode, responseBody));
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.resource = resource;
        this.port = 8080;
        int maxTries = 1000;

        // Ghetto low tech: loop 1000 times to try to find an open port starting at 8080
        do {
            try {
                server = HttpServer.create(new InetSocketAddress(port), 0);
            }
            catch (Exception e) {
                port++;
                maxTries--;
            }
        } while (null == server && maxTries > 0);

        if (maxTries > 0) {
            logger.debug("HttpServer created in {} tries", 1001-maxTries);
            start();
        }
        else {
            throw new RuntimeException("Unable to find an open port");
        }
    }

    private boolean execAndCompareHttpRequest(HttpGet request, int expectedStatusCode) {
        HttpResponse response;
        try {
            response = new DefaultHttpClient().execute(request);
        }
        catch (Exception e) {
            return false;
        }
        StatusLine statusLine = response.getStatusLine();
        return (statusLine.getStatusCode() == expectedStatusCode);
    }

    private void start() {
        logger.debug("start()");
        try {
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
        catch (Exception e) {
            throw new RuntimeException("Unable to start server: " + e);
        }

        // Ghetto low tech: loop 1000 times to make sure the server is working
        int maxTries = 1000;
        do {
            HttpGet httpGet = new HttpGet(getUri());
            if (execAndCompareHttpRequest(httpGet, responseCode)) {
                logger.debug("HttpServer functional in {} tries", 1001-maxTries);
                return;
            }
            maxTries--;
        } while (maxTries > 0);
        throw new RuntimeException("Unable to start server: server not functional.");
    }

    public String getUri() {
        return String.format("http://localhost:%d/%s", port, resource);
    }
}