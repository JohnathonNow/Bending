/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.network;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.johnwesthoff.bending.Server;

public final class OrderedOutputStream implements Runnable {
    private final OutputStream output;
    private final Thread me = new Thread(this);
    private final PriorityBlockingQueue<Message> stack;
    private boolean active = true;
    private long mesID = 0;

    private static final int STACK_CHECK_SLEEP = 1;

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
                    toSend.getBytes().send(output);
                }
                Thread.sleep(STACK_CHECK_SLEEP);
            } catch (IOException | InterruptedException ex) {
                active = false;
                Logger.getLogger(OrderedOutputStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Add a message to the stack. This method is thread-safe.
     * @param BB    The bytebuffer containing the data to be sent
     * @throws IOException If this {@link OrderedOutputStream} is no longer active
     */
    public void addMessage(NetworkMessage BB) throws IOException {
        synchronized (stack) {
            stack.add(new Message(BB));
        }
        if (!active) {
            throw new IOException();
        }
    }

    public void close() throws IOException {
        output.close();
        stack.clear();
        active = false;
    }

    public void addMessage(ByteBuffer bb, int ID) throws IOException {
        this.addMessage(bb, (byte) ID);
    }

    public final class Message implements Comparable<Object> {
        private final NetworkMessage BB;
        private final long constructionTime;

        public Message(final NetworkMessage BB) {
            this.BB = BB;
            constructionTime = mesID++;
        }

        public NetworkMessage getBytes() {
            return BB;
        }
        public long constructTime() {
            return constructionTime;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Message) {
                Message oos = (Message) o;

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
