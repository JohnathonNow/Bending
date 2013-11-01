/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package destruct;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
public  final class ConnectToDatabase {
    public static ConnectToDatabase INSTANCE()
    {
        return new ConnectToDatabase();
    }
    public ConnectToDatabase()
    {
    }
    String base = "http://71.175.73.85:1024";//"http://johnbot.net78.net";//"http://72.92.89.250:1024";//;//;//;//;//
        public boolean verify(String ver)
    {
        boolean tor = false;
        try {
            //String userQuery = "UPDATE Accounts SET Active=1 WHERE Verify = '"+ver+"'";
            //return connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery)>0; 
            
            URL steve = new URL(base+"/yes.php?name="+ver);
                //steve.openConnection().getContent();
                Scanner read = new Scanner(steve.openStream());
                    //System.out.println(read.next());
        } catch (Exception ex) {
            error(ex);
            return false;
        }
        return tor;
    }
        public boolean register(String username, String password, String email, String verify)
    {
        try {
            //String userQuery = "INSERT INTO Accounts (User,Pass,Email,Verify,Active) VALUES ('"+username+"','"+password+"','"+email+"','"+verify+"',"+0+")";
            //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL(base+"/register.php?username="+username+"&password="+password+"&email="+email+"&verify="+verify);
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
    public ArrayList<String> getServers()
    {
        try {
            //String userQuery = "SELECT ServerName,ServerIP FROM Pants";
            //return connect(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL(base+"/getServers.php");
            ArrayList<String> toReturn;
            try (Scanner read = new Scanner(steve.openStream())) {
                String e = read.next();
                toReturn = new ArrayList<>();
                while (!e.equals("<!--")){
                //System.out.println(e);
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
    public void updateCount(String ip, int c)
    {
        try {
            //String userQuery = "SELECT ServerName,ServerIP FROM Pants";
            //return connect(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL("/playerCount.php?ip="+ip+"&c="+c);
                //steve.openConnection().getContent();
               try (Scanner read = new Scanner(steve.openStream())) {
               //  System.out.println(read.next());
                read.close();
            }
        } catch (Exception ex) {
            error(ex);
        }
    }
    public boolean logIn(String user, String pass)
    {
        boolean tor = false;
        try {
            //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
            //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            //return signin.size()>1?true:false;
            URL steve = new URL(base+"/login.php?username="+user+"&password="+pass);
            // URL steve = new URL("http://192.168.137.66/login.php?username="+user+"&password="+pass);
            String req;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            }
                tor = (req == null ? user == null : req.equals(user));
                
                //System.out.println(tor);
        } catch (Exception ex) {
            error(ex);
        }
        return tor;
    }
    public int[][] getSpells(String user, String pass)
    {
        int tor[][];
        try {
            //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
            //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            //return signin.size()>1?true:false;
            URL steve = new URL(base+"/getSpells.php?username="+user+"&password="+pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
          //  System.out.println(req);
                StringTokenizer st = new StringTokenizer(req,",");
                if (st.countTokens()!=30)
                {
                    return new int[][]{{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
                }
                tor = new int[6][5];
                for (int y = 0; y < 5; y++)
                {
                    tor[5][y] = Integer.parseInt(st.nextToken());
                }
                for (int y = 0; y < 5; y++)
                {
                    for (int i = 0; i < tor[0].length; i++)
                    {
                        tor[y][i] = Integer.parseInt(st.nextToken());
                    }
                }
                //System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
            return new int[][]{{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
        }
        return tor;
    }
    public int getXP(String user, String pass)
    {
        try {
            //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
            //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            //return signin.size()>1?true:false;
            URL steve = new URL(base+"/getXP.php?username="+user+"&password="+pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.err.println(req);
            return Integer.parseInt(req); 
                
                //System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
            return 0;
        }
    }
                     public void postXP(int xp, String username)
    {
        try {
                //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
                //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
                //return signin.size()>1?true:false;
                URL steve = new URL(base+"/setXP.php?username="+username+"&spells="+xp);
                Scanner read = new Scanner(steve.openStream());
                       read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }
     public void getOutfit(String user, String pass)
    {
        int tor[];
        try {
            //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
            //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            //return signin.size()>1?true:false;
            URL steve = new URL(base+"/getOutfit.php?username="+user+"&password="+pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
           // System.out.println(req);
                StringTokenizer st = new StringTokenizer(req,",");
                int total = st.countTokens()/3;
                for (int i = 0; i < total; i++)
                {
                    APPLET.Clothing[i] = Byte.parseByte(st.nextToken());
                }
                for (int i = 0; i < total; i++)
                {
                    APPLET.Colors[i] = Integer.parseInt(st.nextToken());
                }
                 for (int i = 0; i < total; i++)
                {
                    APPLET.Colors2[i] = Integer.parseInt(st.nextToken());
                }
                //System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
        
        }
    }
    public void postSpells(String spells, String username)
    {
        try {
                //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
                //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
                //return signin.size()>1?true:false;
                URL steve = new URL(base+"/postSpells.php?username="+username+"&spells="+spells);
                Scanner read = new Scanner(steve.openStream());
                        read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }
    public void postUnlocks(String username)
    {
        try {
                //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
                //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
                //return signin.size()>1?true:false;
                URL steve = new URL(base+"/postUnlocks.php?username="+username+"&spells="+APPLET.unlocks);
                Scanner read = new Scanner(steve.openStream());
                        read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }
    public void getUnlocks(String user, String pass)
    {
        try {
            //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
            //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            //return signin.size()>1?true:false;
            URL steve = new URL(base+"/getUnlocks.php?username="+user+"&password="+pass);
            String req = null;
            try (Scanner read = new Scanner(steve.openStream())) {
                req = read.next();
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectToDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.err.println(req);
            APPLET.unlocks.construct(req);
                
                //System.out.println(tor);
        } catch (MalformedURLException | NumberFormatException ex) {
            error(ex);
        }
    }
            public void postOutfit(String outfit, String username)
    {
        try {
                //String userQuery = "SELECT User,Pass FROM Accounts WHERE User = '"+user+"' AND Pass='"+pass+"' AND Active=1";
                //ArrayList<String> signin = log(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
                //return signin.size()>1?true:false;
                URL steve = new URL(base+"/postOutfit.php?username="+username+"&spells="+outfit);
                Scanner read = new Scanner(steve.openStream());
                     read.next();
        } catch (IOException ex) {
            error(ex);
        }
    }
    public void addServer(String name, String IP)
    {
        try {
            //String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES ('"+name+"', '"+IP+"');";
            //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL(base+"/addServer.php?name="+name+"&ip="+IP);
               steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }
    public void removeServer(String IP)
    {
        //String userQuery = "DELETE FROM Pants WHERE ServerIP='"+IP+"'";
        //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
        try {
            //String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES ('"+name+"', '"+IP+"');";
            //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL(base+"/deleteServer.php?ip="+IP);
               steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }
        public void postRSSfeed(String title, String desc)
    {
        //String userQuery = "DELETE FROM Pants WHERE ServerIP='"+IP+"'";
        //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
        try {
            //String userQuery = "INSERT INTO Pants (ServerName, ServerIP) VALUES ('"+name+"', '"+IP+"');";
            //connectatize(Susername, Spassword, "jdbc:mysql://SQL09.FREEMYSQL.NET/waffles", userQuery); 
            URL steve = new URL(base+"/appendFeed.php?title="+title.replaceAll(" ", "_")+"&description="+desc.replaceAll(" ", "_"));
               steve.openConnection().getContent();
        } catch (IOException ex) {
            error(ex);
        }
    }
    public String uniqueID()
    {
        try {
            URL steve = new URL(base+"/getNumber.php");
              Scanner read;
            read = new Scanner(steve.openStream());
            int i = Integer.parseInt(read.next());
            i*=31;
            String yes = ""+Integer.reverse(Integer.rotateLeft(i, 234));
            //System.out.println(yes);
            while (yes.length()<8)
            {
                yes = yes+"0";
            }
            //System.out.println(yes);
            read.close();
            int e = Integer.parseInt(yes);
           return Long.toString(e, 34).toUpperCase().replaceAll("-", "LX");
        } catch (IOException ex) {
            error(ex);
            return "1234567890";
        }
    }
    public void error(Throwable ex)
    {
        if (base.equals("http://johnbot.net78.net"))
        {
            base = "http://72.92.89.250:1024";
        }
        else
        {
            base = "http://johnbot.net78.net";
        }
    }
}