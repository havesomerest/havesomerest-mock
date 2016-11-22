package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TcpConnectionService implements Runnable {

    public static final int PORT = 4444;

    @Autowired
    private UrlMappingRepostitory urlMappingRepostitory;
    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);

            log.info("A socket for the Glorious Terminal has been initialized on port: " + serverSocket.getLocalPort());

            String inputLine, outputLine = "";

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    log.info("Awesome Client has connected to the Glorious Terminal! " + clientSocket.getRemoteSocketAddress());
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));

                    while ((inputLine = in.readLine()) != null) {
                        log.info("Interesting news! " + clientSocket.getRemoteSocketAddress() + " has just announced: " + inputLine);

                        JSONObject received = new JSONObject(inputLine);
                        String rawCommand = (String) received.get("command");

                        JSONObject jsonObject = new JSONObject();

                        Command command = Command.valueOf(rawCommand.toUpperCase());
                        switch (command) {
                            case LIST_REQUESTS:
                                int position = (int) received.get("position");
                                try {
                                    AcceptedRequest acceptedRequest = requestRepository.get(position);
                                    jsonObject = new JSONObject(acceptedRequest);
                                } catch (IndexOutOfBoundsException e) {
                                    ErrorResponse errorResponse = ErrorResponse.builder()
                                                                               .errorMessage("Index out of bounds!")
                                                                               .build();
                                    jsonObject = new JSONObject(errorResponse);
                                }

                                outputLine = jsonObject.toString();
                                break;
                            case LIST_TESTS:
                                String uri = (String) received.get("uri");
                                Optional<List<Test>> tests = urlMappingRepostitory.getUrlMapping(uri);

                                if (tests.isPresent()) {
                                    jsonObject = new JSONObject(tests.get());
                                } else {
                                    ErrorResponse errorResponse = ErrorResponse.builder()
                                                                               .errorMessage("No tests for this URI :(")
                                                                               .build();
                                    jsonObject = new JSONObject(errorResponse);
                                }

                                break;
                            default:
                                throw new NotImplementedException();
                        }

//                if ("getrequests:".equals(command)) {
//                    String urlMapping = inputLine.replace("getrequests:", "");
//                    Optional<List<Test>> mapping = urlMappingRepostitory.getUrlMapping(urlMapping);
//
//                    AcceptedRequest acceptedRequest = requestRepository.get(0);
//
//                    JSONObject jsonObject = new JSONObject(acceptedRequest);
//                    outputLine = jsonObject.toString();
//                } else if (inputLine.startsWith("getrequest[")) {
//
//                } else {
//                    //outputLine = inputLine.toUpperCase();
//                }
                        out.println(outputLine);
                        if (inputLine.equals("Bye.")) {
                            log.info("Awesome Client says Good Bye to the Glorious Terminal! " + clientSocket.getRemoteSocketAddress());
                            clientSocket.close();
                            break;
                        }
                        inputLine = null;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            log.info("ByeByeFinally");
        }
    }
}
