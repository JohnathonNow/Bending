package com.johnwesthoff.bending;

import java.net.URISyntaxException;

import com.johnwesthoff.bending.logic.AIClient;

public class Main {
    private static String ip_opt = Constants.DEFAULT_SERVER;

    private enum Mode {
        None, Client, Server, AI
    }

    public static void main(String[] args) {
        Mode mode = Mode.None;
        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                case "client":
                    if (mode != Mode.None) {
                        help(3);
                    } else {
                        mode = Mode.Client;
                    }
                    break;
                case "server":
                    if (mode != Mode.None) {
                        help(3);
                    } else {
                        mode = Mode.Server;
                    }
                    break;
                case "ai":
                    if (mode != Mode.None) {
                        help(3);
                    } else {
                        mode = Mode.AI;
                    }
                    break;
                case "-i":
                case "--ip":
                    ip_opt = args[++i];
                    break;
                case "-h":
                case "--help":
                    help(0);
                    break;
                default:
                    help(1);
                    break;
                }
            }
        } catch (Exception e) {
            help(2);
        }
        switch (mode) {
        case None:
        case Client:
            reload();
            break;
        case Server:
            Server.launch();
            break;
        case AI:
            Session sess = Session.newInstance();
            sess.serverIP = ip_opt;
            AIClient.launch();
            break;
        default:
            break;
        }
    }

    public static void reload() {
        Session sess = Session.newInstance();
        sess.serverIP = ip_opt;
        ClientUI.launch();
    }

    public static void help(int exit_code) {
        String jarName = "bending.jar";
        try {
            jarName = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
        }
        System.out.println("Bending - an online 2d platforming shooter");
        System.out.printf("Usage: java -jar %s [COMMAND] [OPTIONS]\n\n", jarName);
        System.out.println("Commands are:");
        System.out.println("  client                  run in client mode (default)");
        System.out.println("  server                  run in server mode");
        System.out.println("  ai                      run in ai mode\n");
        System.out.println("Optional flags are:");
        System.out.println("  -i, --ip IP             the IP of the server for the client to connect to");
        System.out.println("                          defaults to game.johnwesthoff.com");
        System.out.println("  -h, --help              print this help message");
        System.exit(exit_code);
    }

}
