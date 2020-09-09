/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.Destruct;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author John
 */
public final class OrderedOutputStream implements Runnable {
    private final OutputStream output;
    private final Thread me = new Thread(this);
    private final PriorityBlockingQueue<Message> stack;
    private boolean active = true;
    private long mesID = 0;
    private boolean access = false;
    Object holding = this;

    public OrderedOutputStream(final OutputStream output) {
        this.output = output;
        stack = new PriorityBlockingQueue<>();
        start();
    }

    public final void start() {
        me.start();
    }

    @Override
    public void run() {
        while (active) {
            try {
                while (!stack.isEmpty()) {
                    Message toSend = stack.poll();
                    output.write(toSend.getID());
                    // output.flush();
                    Server.writeByteBuffer(toSend.getBytes(), output);
                }
                Thread.sleep(1);
            } catch (IOException | InterruptedException ex) {
                active = false;
                Logger.getLogger(OrderedOutputStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addMesssage(ByteBuffer BB, byte ID) throws IOException {
        while (access) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        access = true;
        stack.add(new Message(BB, ID));
        access = false;
        if (!active) {
            throw new IOException();
        }
    }

    public void addMesssage(ByteBuffer BB, byte ID, Object e) throws IOException {
        while (access && holding != e) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        access = true;
        stack.add(new Message(BB, ID));
        access = false;
        if (!active) {
            throw new IOException();
        }
    }

    public void close() throws IOException {
        output.close();
        stack.clear();
        active = false;
    }

    public void blockAccess(Object e) {
        access = true;
        holding = e;
    }

    public void unBlockAccess() {
        access = false;
        holding = this;
    }

    void addMesssage(ByteBuffer bb, int ID) throws IOException {
        this.addMesssage(bb, (byte) ID);
    }

    private final class Message implements Comparable {
        private final ByteBuffer BB;
        private final byte ID;
        private final long constructionTime;

        public Message(final ByteBuffer BB, final byte ID) {
            this.BB = BB;
            this.ID = ID;
            constructionTime = mesID++;
        }

        public ByteBuffer getBytes() {
            return BB;
        }

        public byte getID() {
            return ID;
        }

        public long constructTime() {
            return constructionTime;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Message) {
                Message oos = (Message) o;
                if (oos.getID() == Server.ID) {
                    return 1;
                }
                if (oos.constructTime() == this.constructionTime) {
                    return 0;
                }
                if (oos.constructTime() > this.constructionTime) {
                    return -1;
                }
                if (oos.constructTime() < this.constructionTime) {
                    return 1;
                }
            }
            return 1;
        }
    }
}
