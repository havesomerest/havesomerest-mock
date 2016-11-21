package hu.hevi.havesomerest.mock;

import hu.hevi.havesomerest.test.Test;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

            Socket clientSocket = serverSocket.accept();
            log.info("Awesome Client has connected to the Glorious Terminal! " + clientSocket.getRemoteSocketAddress());
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));


            String inputLine, outputLine = "";

            while ((inputLine = in.readLine()) != null) {
                log.info("Interesting news! " + clientSocket.getRemoteSocketAddress() + " has just announced: " + inputLine);

                if (inputLine.startsWith("getrequest:")) {
                    String urlMapping = inputLine.replace("getrequests:", "");
                    Optional<List<Test>> mapping = urlMappingRepostitory.getUrlMapping(urlMapping);
                    if (mapping.isPresent()) {
                        List<Test> tests = mapping.get();
                        Test test = tests.get(0);
                        outputLine = new JSONObject(test).toString();
                    }
                } else if (inputLine.startsWith("getrequest[")) {

                } else {
                    outputLine = inputLine.toUpperCase();
                }
                out.println(outputLine);
                if (inputLine.equals("Bye."))
                    log.info("Awesome Client says Good Bye to the Glorious Terminal! " + clientSocket.getRemoteSocketAddress());
                    clientSocket.close();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
