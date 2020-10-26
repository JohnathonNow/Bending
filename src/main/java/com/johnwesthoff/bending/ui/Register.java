/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.johnwesthoff.bending.app.player.PlayerService;
import com.johnwesthoff.bending.app.player.PlayerServiceFactory;
import com.johnwesthoff.bending.util.network.ConnectToDatabase;

/**
 *
 * @author John
 */
public class Register extends javax.swing.JFrame implements KeyListener {

    private final PlayerService playerService;

    private static final long serialVersionUID = 5792213259181280823L;
    /**
     * Creates new form Register
     */

    public Register() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        playerService = PlayerServiceFactory.create();

        email.addKeyListener(this);
        username.addKeyListener(this);
        password.addKeyListener(this);
        password.setToolTipText("Can only contain letters and numbers.");
        username.setToolTipText("Can only contain letters and numbers.");
        email.setToolTipText("Should be a valid email.");
        register.setToolTipText("Register your account.");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        username = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        register = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Username: ");

        jLabel2.setText("Password: ");

        jLabel3.setText("Email:");

        password.addActionListener(event -> passwordActionPerformed(event));

        username.addActionListener(event -> usernameActionPerformed(event));

        email.addActionListener(event -> emailActionPerformed(event));

        register.setText("Register");
        register.addActionListener(event -> registerActionPerformed(event));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addComponent(jLabel1).addGap(0, 10, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(25, 25, 25)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 304,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 304,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 304,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)).addGroup(
                        layout.createSequentialGroup().addGap(160, 160, 160).addComponent(register).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1).addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2).addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3).addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18).addComponent(register)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_passwordActionPerformed

    }// GEN-LAST:event_passwordActionPerformed

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_usernameActionPerformed

    }// GEN-LAST:event_usernameActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_emailActionPerformed

    }// GEN-LAST:event_emailActionPerformed

    private void registerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_registerActionPerformed
        String username = this.username.getText();
        String password = this.password.getText();
        String email = this.email.getText();

        String verify = playerService.sanitizeAndRegister(username, password, email);

        /*
         * sendEmail(email.getText(),"Complete your registration!",
         * "<h1>Hello! To complete your registration<br>" +
         * "just use the type the following code<br>" +
         * "into the verification box: <b>"+verify+"</b><br>" +
         * "Alternatively, click the following link:<br>" +
         * "<a href = \"https://johnbot.net78.net/yes.php?name="
         * +verify+"\"> Verify Account </a></h1>");
         */
        PHPMail(email, verify);
        if (null != verify) {
            JOptionPane.showMessageDialog(rootPane, "Please check your email to verify your account!");
            this.email.setText("");
            this.password.setText("");
            this.username.setText("");
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sorry, the name " + username + " was taken. Please try again!");
        }
    }// GEN-LAST:event_registerActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField password;
    private javax.swing.JButton register;
    private javax.swing.JTextField username;

    // End of variables declaration//GEN-END:variables
    public void PHPMail(String to, String name) {
        try {
            URL steve = new URL("https://johnbot.net78.net/emailCredentials.php?to=" + to + "&name=" + name);
            steve.openConnection().getContent();
        } catch (Exception ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * public void sendEmail(String to, String title, String body) { String host =
     * "smtp.gmail.com";//"smtp.gmail.com";
     * 
     * String from = "JohnJWesthoff@gmail.com"; String subject = title; String
     * messageText = "", file = "", rec = "", p = "", s="";
     * 
     * messageText=body; p="m1ywtt5v9s88s@gmail.com"; s=
     * "Q0WE--wYx3XUIkRh+4wVer,5x<Thjk3CVx@Odfweh%Zkrx9gxHws5{>x_wGHd6e]gSDFPK53hrhl23l";
     * boolean sessionDebug = false; // Create some properties and get the default
     * Session. Properties props = System.getProperties();
     * props.put("mail.smtp.starttls.enable",true);
     * props.put("mail.smtp.auth",true); props.put("mail.host", host);
     * 
     * 
     * props.put("mail.smtp.port", 587); props.put("mail.smtp.host", host);
     * props.put("mail.transport.protocol", "smtp");
     * //props.put("mail.protocol.port",110); props.setProperty("mail.user", p);
     * props.setProperty("mail.password", s); Session session =
     * Session.getDefaultInstance(props,new ForcedAuthenticator(p,s)); // Set debug
     * on the Session so we can see what is going on // Passing false will not echo
     * debug info, and passing true // will. session.setDebug(sessionDebug);
     * //session.requestPasswordAuthentication(null,110, "SMTP","dfg",""); try { //
     * Instantiate a new MimeMessage and fill it with the // required information.
     * javax.mail.Message msg = new MimeMessage(session); msg.setFrom(new
     * InternetAddress(from)); InternetAddress[] address = {new
     * InternetAddress(to)}; msg.setRecipients(javax.mail.Message.RecipientType.TO,
     * address); msg.setSubject(subject); msg.setSentDate(new Date());
     * //msg.setText(messageText); javax.mail.internet.MimeBodyPart mbp1 = new
     * javax.mail.internet.MimeBodyPart(); mbp1.setText(messageText);
     * 
     * // create the second message part // javax.mail.internet.MimeBodyPart mbp2 =
     * new javax.mail.internet.MimeBodyPart();
     * 
     * // attach the file to the message // FileDataSource fds = new
     * FileDataSource(file); //mbp2.setDataHandler(new DataHandler(fds));
     * //mbp2.setFileName(fds.getName());
     * 
     * // create the Multipart and add its parts to it Multipart mp = new
     * javax.mail.internet.MimeMultipart(); mp.addBodyPart(mbp1);
     * //mp.addBodyPart(mbp2);
     * 
     * // add the Multipart to the message msg.setContent(body,"text/html" ); //
     * Hand the message to the default transport service // for delivery.
     * javax.mail.Transport.send(msg); } catch (javax.mail.MessagingException mex) {
     * JOptionPane.showMessageDialog(null,mex); } }
     */

    @Override
    public void keyTyped(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
        if (email.hasFocus()) {
            email.setText(email.getText().replaceAll(" ", "").replaceAll("\\+", "").replaceAll("~", "")
                    .replaceAll("\\*", "").replaceAll("#", ""));
        }
        if (username.hasFocus()) {
            username.setText(username.getText().replaceAll("[^A-Za-z0-9\\s]", "").replaceAll(" ", ""));
        }
        if (password.hasFocus()) {
            password.setText(password.getText().replaceAll("[^A-Za-z0-9\\s]", "").replaceAll(" ", ""));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
        keyTyped(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
        keyTyped(e);
    }
}
