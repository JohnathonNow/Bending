package com.johnwesthoff.bending;

import java.net.URISyntaxException;

public class Main {
    private static String ip_opt = Constants.DEFAULT_SERVER;

    public static void main(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
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
        reload();
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
        System.out.printf("Usage: java -jar %s [OPTIONS]\n\n", jarName);
        System.out.println("Optional flags are:");
        System.out.println("  -i, --ip                the IP of the server to connect to");
        System.out.println("  -h, --help              print this help message");
        System.exit(exit_code);
    }

}
