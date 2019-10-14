import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TcpMultiServer {
    static Logger logger = Logger.getLogger (TcpMultiServer.class.getName ( ));
    private static final String MAGIC_WORD = "PLEASE";
    private ServerSocket serverSocket;

    public void start(int port) {
        logger.info ("TCP Multi-Server Started");
        try {
            serverSocket = new ServerSocket (port);
            while (true) {
                new TcpServer (serverSocket.accept ( )).start ( );
            }
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

    private static class TcpServer extends Thread {
        static Logger logger = Logger.getLogger (TcpServer.class.getName ( ));
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public TcpServer(Socket socket) {
            this.clientSocket = socket;
            logger.info ("New Client connected to server");
        }

        public void run() {
            try {
                out = new PrintWriter (clientSocket.getOutputStream ( ), true);
                in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ( )));
                String inputLine;
                out.println ("Welcome To Adrian TCP Server");
                out.println ("\t MENU \n1. Enter ALL to see all collected data \n2.Enter which field's values you want to see \n3.Enter STOP to close connection \n ATTENTION: Don't forget the magic word\n");
                while ((inputLine = in.readLine ( )) != null) {
                    if (Pattern.compile (Pattern.quote ("please"), Pattern.CASE_INSENSITIVE).matcher (inputLine).find ( )) {
                        inputLine = inputLine.toLowerCase ( );
                        logger.info ("Magic word exist : " + inputLine);
                        if (!processInput (inputLine)) {
                            break;
                        }
                    } else {
                        out.println ("ERROR: MAGIC WORD DOESN'T EXIST");
                        logger.error ("MAGIC WORD DOESN'T EXIST");
                    }


                }
                in.close ( );
                out.close ( );
                clientSocket.close ( );
                logger.info ("Session is Finished");
            } catch (IOException e) {
                e.printStackTrace ( );
            }


        }

        private boolean processInput(String input) {
            DataManager dataManager = new DataManager ( );
            ArrayList <String> words = new ArrayList <String> (Arrays.asList (input.split ("\\W+")));
            switch (words.size ( )) {
                case 2: {
                    if (Pattern.compile (Pattern.quote ("stop"), Pattern.CASE_INSENSITIVE).matcher (input).find ( )) {
                        out.println ("Session is Finished");
                        return false;
                    } else if (Pattern.compile (Pattern.quote ("all"), Pattern.CASE_INSENSITIVE).matcher (input).find ( )) {
                        for (String str : Request.dataList) {
                            out.println (str);
                        }
                    } else {
                        for (String w : words) {
                            if (!w.equals ("please")) {
                                List <String> values = dataManager.getValuesOfKey (w, Request.dataList);
                                if (!values.isEmpty ()) {
                                    for (String val : values) {
                                        out.println (val);
                                    }
                                }
                                else{
                                    logger.info ("No results found ");
                                    out.println ("WARN: NO RESULTS FOUND \n");
                                }
                            }
                        }
                    }
                    break;
                }

            }
            return true;
        }
    }
}
