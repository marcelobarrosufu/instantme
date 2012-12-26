/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.util;

import com.instantme.entries.TaskEntry;
import java.util.Stack;

public class TaskHelper implements Runnable {
    
    private Stack taskFifo = null;
    private Runnable client = null;
    private boolean running = false;
    private IAnimation animation = null;
    
    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }
    
    public TaskHelper(Runnable client, IAnimation animation) {
        this.taskFifo = new Stack();
        this.client = client;
        this.running = false;
        this.animation = animation;
    }
    
    public synchronized boolean isFinished() {
        return taskFifo.isEmpty();
    }
    
    public synchronized TaskEntry pop() {
        if(taskFifo.empty()) {
            return null;
        }
        else {
            return (TaskEntry) taskFifo.pop();
        }
    }
    
    public synchronized void push(TaskEntry te) {
        taskFifo.insertElementAt(te,0);
        if(isRunning() == false) {
            setRunning(true);
            //System.out.println("Starting task");
            Thread t = new Thread(this);
            t.start();
        }
    }
    
    public void run() {
        if(client != null) {
            synchronized (this) {
                setRunning(true);
                if(animation != null) {
                    animation.start();
                }
                while(!isFinished()) {
                    try {
                        client.run();         
                        wait(250);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if(animation != null) {
                    animation.stop();
                }
                setRunning(false);
            }
        }
    }

    
}
