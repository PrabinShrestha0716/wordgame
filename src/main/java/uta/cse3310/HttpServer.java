package uta.cse3310;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import net.freeutils.httpserver.HTTPServer;
import net.freeutils.httpserver.HTTPServer.ContextHandler;
import net.freeutils.httpserver.HTTPServer.FileContextHandler;
import net.freeutils.httpserver.HTTPServer.Request;
import net.freeutils.httpserver.HTTPServer.Response;
import net.freeutils.httpserver.HTTPServer.VirtualHost;

// http server include is a GPL licensed package from
//            http://www.freeutils.net/source/jlhttp/

public class HttpServer {

    private static final String HTML = "./public";
    int port = 8080;
    String dirname = HTML;
    private Set<Player> activeUsers = new HashSet<>();

    public HttpServer(int portNum, String dirName) {
        System.out.println("creating http server port " + portNum);
        port = portNum;
        dirname = dirName;
    }

    public void start() {
        System.out.println("in httpd server start");
        try {
            File dir = new File(dirname);
            if (!dir.canRead())
                throw new FileNotFoundException(dir.getAbsolutePath());
            // set up server
            HTTPServer server = new HTTPServer(port);
            VirtualHost host = server.getVirtualHost(null); // default host
            host.setAllowGeneratedIndex(true); // with directory index pages
            host.addContext("/", new FileContextHandler(dir));
            host.addContext("/api/time", new ContextHandler() {
                public int serve(Request req, Response resp) throws IOException {
                    long now = System.currentTimeMillis();
                    resp.getHeaders().add("Content-Type", "text/plain");
                    resp.send(200, String.format("%tF %<tT", now));
                    return 0;
                }
            });

            host.addContext("/login", new ContextHandler() {
                public int serve(Request req, Response resp) throws IOException {
                    // if (!req.getMethod().equalsIgnoreCase("POST")) {
                    // resp.send(405, "Method Not Allowed");
                    // return -1;
                    // }

                    // Retrieve username from request parameters
                    String username = req.getParams().get("username");
                    if (username == null || username.isEmpty()) {
                        resp.send(400, "Bad Request");
                        return -1;
                    }

                    Player player = new Player(username);
                    activeUsers.add(player);

                    String playerInfo = "Logged in as: " + player.getName();
                    resp.getHeaders().add("Content-Type", "text/plain");
                    resp.send(200, playerInfo);
                    return 0;
                }
            });

            // Add hello endpoint
            host.addContext("/hello", new ContextHandler() {
                public int serve(Request req, Response resp) throws IOException {
                    String username = req.getParams().get("username");
                    resp.getHeaders().add("Content-Type", "text/plain");
                    resp.send(200, "Hello, " + username + "!");
                    return 0;
                }
            });
            // Inside the login endpoint in the HttpServer class
            host.addContext("/active-users", new ContextHandler() {
                public int serve(Request req, Response resp) throws IOException {
                    StringBuilder activeUserNames = new StringBuilder();
                    for (Player player : activeUsers) {
                        activeUserNames.append(player.getName()).append(", ");
                    }
                    // Remove the trailing comma and space
                    if (activeUserNames.length() > 0) {
                        activeUserNames.setLength(activeUserNames.length() - 2);
                    }
                    resp.getHeaders().add("Content-Type", "text/plain");
                    resp.send(200, "Active Users: " + activeUserNames);
                    return 0;
                }
            });

            server.start();
            System.out.println("HTTPServer is listening on port " + port);
        } catch (Exception e) {
            System.err.println("error: " + e);
        }

    }

}
