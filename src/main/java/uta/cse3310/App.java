package uta.cse3310;

// This is example code provided to CSE3310 Fall 2022
// You are free to use as is, or changed, any of the code provided

// Please comply with the licensing requirements for the
// open source packages being used.

// This code is based upon, and derived from the this repository
//            https:/thub.com/TooTallNate/Java-WebSocket/tree/master/src/main/example

// http server include is a GPL licensed package from
//            http://www.freeutils.net/source/jlhttp/

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.Session;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class App extends WebSocketServer {
  Vector<WordSearchGame> ActiveGames = new Vector<WordSearchGame>();
  Set<Player> activeUsers = new HashSet<>();
  int GameId = 1;

  // Keep track of connected sessions
  private static final Map<String, WebSocket> userSessions = new HashMap<>();
  // private static final CopyOnWriteArrayList<String> activeUsers = new
  // CopyOnWriteArrayList<>();

  private Puzzle wordGenerator;

  public App(int port) {
    super(new InetSocketAddress(port));

  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  private static Map<Session, Player> players = new HashMap<>();

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    userSessions.put(conn.getRemoteSocketAddress().toString(), conn);
    ServerEvent E = new ServerEvent();
    // search for a game needing a player
    WordSearchGame G = null;
    for (WordSearchGame i : ActiveGames) {
      if (i.players == uta.cse3310.PlayerType.Player1) {
        G = i;
        System.out.println("found a match");
      }
    }

    // No matches ? Create a new Game.
    if (G == null) {
      G = new WordSearchGame();
      G.GameId = GameId;
      GameId++;
      // Add the first player
      G.players = uta.cse3310.PlayerType.Player1;
      ActiveGames.add(G);
      System.out.println(" creating a new Game");
    } else {
      // join an existing game
      System.out.println(" not a new game");
      G.players = uta.cse3310.PlayerType.Player2;
      G.startGame();
    }
    System.out.println("G.players is " + G.Players);
    // create an event to go to only the new player
    // E.YouAre = G.Players;
    E.GameId = G.GameId;

    // allows the websocket to give us the Game when a message arrives
    conn.setAttachment(G);
    Gson gson = new Gson();
    // Note only send to the single connection
    conn.send(gson.toJson(E));
    System.out.println(gson.toJson(E));

    // The state of the game has changed, so lets send it to everyone
    String jsonString;
    jsonString = gson.toJson(G);

    System.out.println(jsonString);
    broadcast(jsonString);

  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("Client disconnected: " + session.getId());
    players.remove(session);
  }

  public void onOpen(Session session) {
    System.out.println("Client connected: " + session.getId());
  }

  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    userSessions.remove(conn.getRemoteSocketAddress().toString());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println(conn + ": " + message);
    Gson gson = new Gson();
    Message receivedMessage = gson.fromJson(message, Message.class);

    if ("login".equals(receivedMessage.getType())) {
      handleLogin(conn, receivedMessage.getUsername());
    } else if ("chatMessage".equals(receivedMessage.getType())) {
      broadcastChatMessage(gson.toJson(receivedMessage)); // Broadcast the chat message
    }
  }

  private void handleLogin(WebSocket conn, String username) {
    if (!isUsernameTaken(username)) {
      Player player = new Player(username);
      activeUsers.add(player);
      broadcastActiveUsers();
    } else {
      Gson gson = new Gson();
      conn.send(gson.toJson(new Message("usernameTaken", username)));
    }
  }

  private void broadcastChatMessage(String messageJson) {
    for (WebSocket session : userSessions.values()) {
      session.send(messageJson);
    }
  }

  // Method to check if a username is already taken
  private boolean isUsernameTaken(String username) {

    for (Player player : activeUsers) {
      if (player.getName().equals(username)) {
        return true;
      }
    }
    return false;
  }

  // Broadcast active users to all clients
  private void broadcastActiveUsers() {
    List<String> activeUsernames = new ArrayList<>();
    Gson gson = new Gson();
    for (Player player : activeUsers) {
      activeUsernames.add(player.getName());
    }
    Message message = new Message("activeUsersUpdate", activeUsernames);
    broadcast(gson.toJson(message));
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
  }

  public static void main(String[] args) {

    // Set up the http server
    int port = 9080;
    HttpServer H = new HttpServer(port, "./public");
    H.start();
    System.out.println("http Server started on port:" + port);

    // create and start the websocket server

    port = 9880;
    App A = new App(port);
    A.start();
    System.out.println("websocket Server started on port: " + port);

    String sep = "=".repeat(50);

    WordList wordList = new WordList();

    Puzzle puzzle = new Puzzle(50, 50);
    puzzle.displayPuzzle();
    System.out.println("\n" + sep + "\n");
    wordList.displayWordList();

  }
}
