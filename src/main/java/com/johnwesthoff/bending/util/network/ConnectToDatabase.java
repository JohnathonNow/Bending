/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConnectToDatabase {
    String base = "https://72.92.89.110:1024";// "https://johnbot.net78.net";//"https://72.92.89.250:1024";//;//;//;//;//
    Properties p;
    boolean offline = true;

    public static ConnectToDatabase INSTANCE() {
        return new ConnectToDatabase();
    }

    public ConnectToDatabase() {
        p = new Properties();
        (new File(ResourceLoader.dir)).mkdirs();
        if (new File(ResourceLoader.dir + "login.xyz").exists()) {
            try {
                p.load(new FileInputStream(new File(ResourceLoader.dir + "login.xyz")));
            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (p.isEmpty()) {
            p.setProperty("servers", "Enter IP,enterip,0");
            p.setProperty("spells", "");
            p.setProperty("xp", "0");
            p.setProperty("outfit", "");
            p.setProperty("unlocks", "");
            try {
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        defaultBase();
    }

    private void defaultBase() {
        if (offline)
            return;
        InputStream is = null;
        try {
            URL WI = new URL("https://www.west-it.webs.com/yes.txt");// new
                                                                     // URL("https://static.cbslocal.com/schoolclosings/production/cbs/kyw/NEWSROOM/webexport_number.htm");
            is = WI.openStream();
            Scanner sc = new Scanner(is);
            base = sc.next();
            sc.close();
        } catch (IOException ex) {
        }
    }

    public boolean verify(String ver) {
        if (offline)
            return true;
        boolean tor = false;
        try {
            // String userQuery = "UPDATE Accounts SET Active=1 WHERE Verify = '"+ver+"'";
            // return connectatize(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery)>0;

            URL steve = new URL(base + "/yes.php?name=" + ver);
            // steve.openConnection().getContent();
            Scanner read = new Scanner(steve.openStream());
            // System.out.println(read.next());
        } catch (Exception ex) {
            error(ex);
            return false;
        }
        return tor;
    }

    public boolean register(String username, String password, String email, String verify) {
        try {
            // String userQuery = "INSERT INTO Accounts (User,Pass,Email,Verify,Active)
            // VALUES ('"+username+"','"+password+"','"+email+"','"+verify+"',"+0+")";
            // connectatize(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL(base + "/register.php?username=" + username + "&password=" + password + "&email="
                    + email + "&verify=" + verify);
            boolean t;
            try (Scanner read = new Scanner(steve.openStream())) {
                t = read.next().equals("true");
            }
            return (t);

        } catch (Exception ex) {
            error(ex);
            return false;
        }
    }

    public ArrayList<String> getServers() {
        ArrayList<String> toReturn;
        if (offline) {
            toReturn = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(p.getProperty("servers"));
            while (st.hasMoreElements()) {
                toReturn.add(st.nextToken(","));
            }
            return toReturn;
        }
        try {
            // String userQuery = "SELECT ServerName,ServerIP FROM Pants";
            // return connect(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL(base + "/getServers.php");

            try (Scanner read = new Scanner(steve.openStream())) {
                String e = read.next();
                toReturn = new ArrayList<>();
                while (!e.equals("<!--")) {
                    // System.out.println(e);
                    toReturn.add(e);
                    e = read.next();
                }
            }
            return toReturn;
        } catch (Exception ex) {
            error(ex);
            return null;
        }
    }

    public void updateCount(String ip, int c) {
        if (offline)
            return;
        try {
            // String userQuery = "SELECT ServerName,ServerIP FROM Pants";
            // return connect(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL("/playerCount.php?ip=" + ip + "&c=" + c);
            // steve.openConnection().getContent();
            try (Scanner read = new Scanner(steve.openStream())) {
                // System.out.println(read.next());
                read.close();
            }
        } catch (Exception ex) {
            error(ex);
        }
    }

    public boolean logIn(String user, String pass) {
        if (offline)
            return true;
        boolean tor = false;
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/login.php?username=" + user + "&password=" + pass);
            // URL steve = new
            // URL("https://192.168.137.66/login.php?username="+user+"&password="+pass);
            String req;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            }
            tor = (req == null ? user == null : req.equals(user));

            // System.out.println(tor);
        } catch (Exception ex) {
            error(ex);
        }
        return tor;
    }

    public int[][] getSpells(String user, String pass) {
        int tor[][];
        if (offline) {

            StringTokenizer st = new StringTokenizer(p.getProperty("spells"), ",");
            if (st.countTokens() != 30) {
                return new int[][] { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
                        { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };
            }
            tor = new int[6][5];
            for (int y = 0; y < 5; y++) {
                tor[5][y] = Integer.parseInt(st.nextToken());
            }
            for (int y = 0; y < 5; y++) {
                for (int i = 0; i < tor[0].length; i++) {
                    tor[y][i] = Integer.parseInt(st.nextToken());
                }
            }
            return tor;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/getSpells.php?username=" + user + "&password=" + pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            // System.out.println(req);
            StringTokenizer st = new StringTokenizer(req, ",");
            if (st.countTokens() != 30) {
                return new int[][] { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
                        { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };
            }
            tor = new int[6][5];
            for (int y = 0; y < 5; y++) {
                tor[5][y] = Integer.parseInt(st.nextToken());
            }
            for (int y = 0; y < 5; y++) {
                for (int i = 0; i < tor[0].length; i++) {
                    tor[y][i] = Integer.parseInt(st.nextToken());
                }
            }
            // System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
            return new int[][] { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };
        }
        return tor;
    }

    public int getXP(String user, String pass) {
        if (offline) {
            return Integer.parseInt(p.getProperty("xp"));
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/getXP.php?username=" + user + "&password=" + pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            // System.err.println(req);
            return Integer.parseInt(req);

            // System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
            return 0;
        }
    }

    public void postXP(int xp, String username) {
        if (offline) {
            try {
                p.setProperty("xp", "" + xp);
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/setXP.php?username=" + username + "&spells=" + xp);
            Scanner read = new Scanner(steve.openStream());
            read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void getOutfit(String user, String pass) {
        int tor[];
        if (offline) {
            StringTokenizer st = new StringTokenizer(p.getProperty("outfit"), ",");
            int total = st.countTokens() / 3;
            for (int i = 0; i < total; i++) {
                Client.Clothing[i] = Byte.parseByte(st.nextToken());
            }
            for (int i = 0; i < total; i++) {
                Client.Colors[i] = Integer.parseInt(st.nextToken());
            }
            for (int i = 0; i < total; i++) {
                Client.Colors2[i] = Integer.parseInt(st.nextToken());
            }
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/getOutfit.php?username=" + user + "&password=" + pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            // System.out.println(req);
            StringTokenizer st = new StringTokenizer(req, ",");
            int total = st.countTokens() / 3;
            for (int i = 0; i < total; i++) {
                Client.Clothing[i] = Byte.parseByte(st.nextToken());
            }
            for (int i = 0; i < total; i++) {
                Client.Colors[i] = Integer.parseInt(st.nextToken());
            }
            for (int i = 0; i < total; i++) {
                Client.Colors2[i] = Integer.parseInt(st.nextToken());
            }
            // System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);

        }
    }

    public void postSpells(String spells, String username) {
        if (offline) {
            try {
                p.setProperty("spells", spells);
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/postSpells.php?username=" + username + "&spells=" + spells);
            Scanner read = new Scanner(steve.openStream());
            read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void postUnlocks(String username) {
        if (offline) {
            try {
                p.setProperty("unlocks", "" + Client.unlocks);
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/postUnlocks.php?username=" + username + "&spells=" + Client.unlocks);
            Scanner read = new Scanner(steve.openStream());
            read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void getUnlocks(String user, String pass) {
        if (offline) {
            Client.unlocks.construct(p.getProperty("unlocks"));
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/getUnlocks.php?username=" + user + "&password=" + pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            // System.err.println(req);
            Client.unlocks.construct(req);

            // System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
        }
    }

    public void postOutfit(String outfit, String username) {
        if (offline) {
            try {
                p.setProperty("outfit", outfit);
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            // String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"'
            // AND Pass='"+pass+"' AND Active=1";
            // ArrayList<String> signin = log(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            // return signin.size()>1?true:false;
            URL steve = new URL(base + "/postOutfit.php?username=" + username + "&spells=" + outfit);
            Scanner read = new Scanner(steve.openStream());
            read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void addServer(String name, String IP) {
        if (offline) {
            try {
                p.setProperty("servers", p.getProperty("servers") + "," + name + "," + IP + "," + 0);
                p.store(new FileOutputStream(new File(ResourceLoader.dir + "login.xyz")), "");
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        try {
            // String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES
            // ('"+name+"', '"+IP+"');";
            // connectatize(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL(base + "/addServer.php?name=" + name + "&ip=" + IP);
            steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void removeServer(String IP) {
        // String userQuery = "DELETE FROM Pants WHERE ServerIP='"+IP+"'";
        // connectatize(Susername, Spassword,
        // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
        try {
            // String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES
            // ('"+name+"', '"+IP+"');";
            // connectatize(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL(base + "/deleteServer.php?ip=" + IP);
            steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public void postRSSfeed(String title, String desc) {
        // String userQuery = "DELETE FROM Pants WHERE ServerIP='"+IP+"'";
        // connectatize(Susername, Spassword,
        // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
        try {
            // String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES
            // ('"+name+"', '"+IP+"');";
            // connectatize(Susername, Spassword,
            // "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery);
            URL steve = new URL(base + "/appendFeed.php?title=" + title.replaceAll(" ", "_") + "&description="
                    + desc.replaceAll(" ", "_"));
            steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }

    public String uniqueID() {
        try {
            URL steve = new URL(base + "/getNumber.php");
            Scanner read;
            read = new Scanner(steve.openStream());
            int i = Integer.parseInt(read.next());
            i *= 31;
            String yes = "" + Integer.reverse(Integer.rotateLeft(i, 234));
            // System.out.println(yes);
            while (yes.length() < 8) {
                yes = yes + "0";
            }
            // System.out.println(yes);
            read.close();
            int e = Integer.parseInt(yes);
            return Long.toString(e, 34).toUpperCase().replaceAll("-", "LX");
        } catch (IOException ex) {
            error(ex);
            return "1234567890";
        }
    }

    public void error(Throwable ex) {
        if (base.equals("https://johnbot.net78.net")) {
            defaultBase();
        } else {
            // defaultBase();
            base = "https://johnbot.net78.net";
        }
    }
}