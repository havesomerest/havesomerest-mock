package hu.hevi.havesomerest.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Slf4j
public class TcpConnectionService implements Runnable {

    public static final int PORT = 4444;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);

            log.info("A socket for the Glorious Terminal has been initialized on port: " + serverSocket.getLocalPort());

            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));


            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                outputLine = inputLine.toUpperCase();
                out.println(outputLine);
                if (inputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
