/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.logic;

import static com.johnwesthoff.bending.Client.shortJump;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.johnwesthoff.bending.Client;
import com.johnwesthoff.bending.ClientUI;
import com.johnwesthoff.bending.Constants;

/**
 * @author John
 */
public class ClientInputListener implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener {
    ClientUI pointer;

    public ClientInputListener(ClientUI pointer) {
        this.pointer = pointer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public int getStuff() {
        return -(pointer.getWidth() - pointer.getHeight()) / (2);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (pointer.world == null) {
            return;
        }
        int button = e.getButton();
        double scale = pointer.owner.getHeight() / (double) Constants.HEIGHT_INT;
        if (pointer.world != null) {
            pointer.world.mouseX = (int) ((e.getX()) / scale);
            pointer.world.mouseY = (int) (e.getY() / scale);
            pointer.world.pressX = pointer.world.mouseX;
            pointer.world.pressY = pointer.world.mouseY;
        }
        switch (button) {

            default:
            case MouseEvent.BUTTON1:
                pointer.world.MB1 = true;
                // if (pointer.world.pressX>pointer.world.x)
                // {
                // pointer.world.left = 1;
                // }
                // else
                // {
                // pointer.world.left = -1;
                // }
                break;
            case MouseEvent.BUTTON3:
                // ground.ShowData();
                pointer.world.MB3 = true;
                break;
            case MouseEvent.BUTTON2:
                pointer.world.MB2 = true;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double scale = pointer.owner.getHeight() / (double) Constants.HEIGHT_INT;
        if (pointer.world != null) {
            pointer.world.mouseX = (int) ((e.getX() + getStuff()) / scale);
            pointer.world.mouseY = (int) (e.getY() / scale);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (pointer.world != null) {
            double scale = pointer.owner.getHeight() / (double) Constants.HEIGHT_INT;
            pointer.world.mouseX = (int) ((scale * pointer.getWidth())
                    * ((e.getPoint().x - (pointer.getWidth() - pointer.getHeight())) / (double) pointer.getWidth()));
            // world.mouseX = ((e.getX()-(this.getWidth()-getHeight())/2)/scale);
            pointer.world.mouseY = (int) ((scale * pointer.getHeight())
                    * (e.getPoint().y / (double) pointer.getHeight()));
            pointer.world.mouseX = (int) (e.getX() / scale);
            pointer.world.mouseY = (int) (e.getY() / scale);
        }
    }

    public int setTo = -1;
    public int doublecast = 0;

    private double cast(int index) {
        pointer.spellList[pointer.spellBook][index].getEffectiveSpell(index).cast(pointer, index);
        return Client.pointDir(
                pointer.world.left == 1 ? (pointer.world.x - pointer.world.viewX) : pointer.world.mouseX,
                pointer.world.y - pointer.world.viewY,
                pointer.world.left == -1 ? (pointer.world.x - pointer.world.viewX) : pointer.world.mouseX,
                pointer.world.mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            if (pointer.world == null) {
                return;
            }
            double scale = pointer.owner.getHeight() / (double) Constants.HEIGHT_INT;
            pointer.world.mouseX = (int) ((scale * pointer.getWidth())
                    * ((e.getPoint().x - (pointer.getWidth() - pointer.getHeight())) / (double) pointer.getWidth()));
            // world.mouseX = ((e.getX()-(this.getWidth()-getHeight())/2)/scale);
            pointer.world.mouseY = (int) ((scale * pointer.getHeight())
                    * (e.getPoint().y / (double) pointer.getHeight()));
            pointer.world.mouseX = (int) (e.getX() / scale);
            pointer.world.mouseY = (int) (e.getY() / scale);
            // world.mouseY = (e.getY()/scale);

            if ((pointer.matchOver > 0) && e.getButton() == MouseEvent.BUTTON1) {

                if (pointer.world.mouseX * Constants.MULTIPLIER > 400
                        && pointer.world.mouseX * Constants.MULTIPLIER < 500) {
                    // System.out.println("H2");
                    if (pointer.world.mouseY * Constants.MULTIPLIER > 200
                            && pointer.world.mouseY * Constants.MULTIPLIER < 250) {
                        pointer.spellselection.XP.setText("XP: " + Client.XP);
                        pointer.spellselection.USER.setText("USER: " + Client.jtb.getText());
                        pointer.spellselection.setVisible(true);
                        Client.immaKeepTabsOnYou.setSelectedIndex(1);
                    }
                }
            }
            if ((!pointer.world.dead) && (pointer.matchOver <= 0)) {
                int d = (int) Math.ceil(pointer.world.mouseX / 33d);
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        pointer.world.MB1 = false;
                        if (pointer.world.mouseX < Constants.POINTER_WORLD_MOUSE_X_LIMIT
                                && pointer.world.mouseY < Constants.POINTER_WORLD_MOUSE_Y_LIMIT) {
                            setTo = -1;
                            pointer.leftClick = d - 1;
                            break;
                        }
                        if (setTo == -1) {
                            pointer.world.leftArmAngle = cast(pointer.leftClick);
                        } else {
                            pointer.leftClick = setTo;
                            setTo = -1;
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        pointer.world.MB2 = false;
                        if (pointer.world.mouseX < Constants.POINTER_WORLD_MOUSE_X_LIMIT
                                && pointer.world.mouseY < Constants.POINTER_WORLD_MOUSE_Y_LIMIT) {
                            setTo = -1;
                            pointer.midClick = d - 1;
                            break;
                        }
                        if (setTo == -1) {
                            pointer.world.leftArmAngle = cast(pointer.midClick);
                            pointer.world.rightArmAngle = pointer.world.leftArmAngle;
                        } else {
                            pointer.midClick = setTo;
                            setTo = -1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        pointer.world.MB3 = false;
                        if (pointer.world.mouseX < Constants.POINTER_WORLD_MOUSE_X_LIMIT
                                && pointer.world.mouseY < Constants.POINTER_WORLD_MOUSE_Y_LIMIT) {
                            setTo = -1;
                            pointer.rightClick = d - 1;
                            break;
                        }
                        if (setTo == -1) {
                            pointer.world.rightArmAngle = cast(pointer.rightClick);
                        } else {
                            pointer.rightClick = setTo;
                            setTo = -1;
                        }
                        break;
                }
            }
        } catch (Exception ex) {

        }
        // if (pointer.world!=null){pointer.world.mouseX =
        // (e.getX()*scale)+pointer.world.viewX+(pointer.getWidth()-pointer.getHeight())/2;
        // pointer.world.mouseY = (e.getY()*scale)+pointer.world.viewY;}
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // row new UnsupportedOperationException("Not supported yet.");
        char key = e.getKeyChar();
        if (key == KeyEvent.CHAR_UNDEFINED || key == 8) {
            return;
        }

        if (pointer.chatActive) {
            pointer.chatMessage += key;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (pointer.chatActive) {
            if (pointer.chatActive) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (pointer.chatMessage.length() > 0) {
                        int b = pointer.chatMessage.length() - 1;
                        pointer.chatMessage = pointer.chatMessage.substring(0, b);
                    }
                }
            }
            return;
        }
        try {
            // throw new UnsupportedOperationException("Not supported yet.");
            int E = e.getKeyCode();

            if (pointer.world == null || !pointer.isMyTurn) {
                return;
            }
            pointer.world.keys[E] = true;
            pointer.prevMove = pointer.world.move;
            switch (E) {
                case KeyEvent.VK_A:
                    if (pointer.world.move > -3) {
                        pointer.world.move = -2;
                        pointer.world.fr = 0;
                    }
                    break;
                case KeyEvent.VK_D:
                    if (pointer.world.move < 3) {
                        pointer.world.move = 2;
                        pointer.world.fr = 0;
                    }
                    break;
                case KeyEvent.VK_W:
                    if (!pointer.world.keys[KeyEvent.VK_S]) {
                        pointer.world.jump = (float) (Client.runningSpeed * Constants.JUMP_COEFFICIENT);

                    }
                    // sendMovement();
                    break;
                case KeyEvent.VK_1:
                    setTo = 0;
                    // sendMovement();
                    break;
                case KeyEvent.VK_2:
                    setTo = 1;
                    // sendMovement();
                    break;
                case KeyEvent.VK_3:
                    setTo = 2;
                    // sendMovement();
                    break;
                case KeyEvent.VK_4:
                    setTo = 3;
                    // sendMovement();
                    break;
                case KeyEvent.VK_5:
                    setTo = 4;
                    // sendMovement();
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (pointer.chatActive) {
                pointer.chatMessage = Client.username + ": " + pointer.chatMessage;
                pointer.sendMessage(pointer.chatMessage);
                if (pointer.chatMessage.contains("/suicide")) {
                    pointer.HP = 0;
                    pointer.world.status |= Constants.ST_FLAMING;
                }
                if (pointer.chatMessage.contains("/vanish")) {
                    pointer.world.status ^= Constants.ST_INVISIBLE;
                }
                if (pointer.chatMessage.contains("/embiggen")) {
                    Client.container.dispose();// You can't change the state from
                    // a listener, so kill it first
                    if (!Client.container.isUndecorated()) {
                        Client.container.setLocation(0, 0);
                        Client.container.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        Client.container.setUndecorated(true);
                    } else {
                        Client.container.setExtendedState(JFrame.NORMAL);
                        Client.container.setUndecorated(false);
                    }
                    Client.container.pack();
                    Client.container.setVisible(true);
                }
                if (pointer.chatMessage.contains("/quit")) {
                    Client.actioner.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "Restart"));
                }
                pointer.chatMessage = "";
                pointer.chatActive = false;
            } else {
                pointer.chatActive = true;
            }
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_SLASH && !pointer.chatActive) {
            pointer.chatActive = true;
            pointer.chatMessage = "/";
            return;
        }
        int E = e.getKeyCode();
        if (pointer.world == null) {
            return;
        }
        pointer.prevMove = pointer.world.move;
        pointer.world.keys[E] = false;
        switch (E) {
            case KeyEvent.VK_A:
                if (pointer.world.move < 0) {
                    pointer.world.fr = 0.1;
                }
                break;
            case KeyEvent.VK_D:
                if (pointer.world.move > 0) {
                    pointer.world.fr = 0.1;
                }
                break;
            case KeyEvent.VK_W:
                try {
                    if (pointer.world.vspeed < -3) {
                        pointer.world.vspeed /= 2;
                    }
                    pointer.sendMovement();
                } catch (Exception ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                pointer.world.jump = (byte) 0;
                break;
            case KeyEvent.VK_SPACE:

                break;
            case KeyEvent.VK_ESCAPE:

                break;
            case KeyEvent.VK_C:
                if (!pointer.chatActive) {
                    shortJump = !shortJump;
                }
                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int tick = 1;
        if (e.getWheelRotation() < 0) {
            tick = -1;
        }
        pointer.leftClick += tick;
        while (pointer.leftClick < 0) {
            pointer.leftClick += Constants.SPELL_SLOTS;
        }
        pointer.leftClick = pointer.leftClick % Constants.SPELL_SLOTS;

    }
}
