/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
/* 
 * Based on Nokia BackStack class
 * 
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */
package com.instantme.util;

import java.util.Stack;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * A helper class to ease the navigation between views. Stores the previous
 * navigation steps in a Stack, has a reference to the running MIDlet and
 * manages the displaying of the views. Also quits the application when 
 * navigating back but the stack is empty.
 */
public class BackStack {

    private Stack backStack;
    private MIDlet midlet = null;

    public static BackStack getInstance() {
        return BackStackHolder.INSTANCE;
    }
    
    private static class BackStackHolder {
        private static final BackStack INSTANCE = new BackStack();
    }  
    
    private BackStack() {
        backStack = new Stack();
    }
    
    /**
     * Set the running midlet. It is required to be done once, at startup
    */
    public void setRunningMidlet(MIDlet c) {
        if(midlet == null) {
            midlet = c;
        }
    }

    public MIDlet getRunningMidlet() {
        return midlet;
    }
    
    public Display getCurrentDisplay() {
        return Display.getDisplay(midlet);
    }
    /**
     * Navigate forward to newView
     * @param newView 
     */
    public void forward(Displayable newView) {
        Display display = Display.getDisplay(midlet);
        Displayable view = display.getCurrent();
        if (view instanceof Alert && newView instanceof Alert) {
            //System.out.println("Alert");
            // both are Alerts replace the current one with the new one.
            Displayable previousView = backStack.isEmpty() ? null
                : (Displayable) backStack.peek();
            display.setCurrent((Alert) newView, previousView);
        }
        else {
            //System.out.println("View");
            backStack.push(view);
            display.setCurrent(newView);
        }
    }

    /**
     * Navigate back one view. Quit if on the last view.
     */
    public void back() {
        back(1);
    }

    /**
     * Backs up multiple levels. Useful with Alerts.
     * @param levels 
     */
    public void back(int levels) {
        Displayable view = null;
        for (int i = 0; i < levels; i++) {
            if (!backStack.isEmpty()) {
                view = (Displayable) backStack.pop();
            }
            else {
                view = null;
            }
        }
        if (view != null) {
            Display.getDisplay(midlet).setCurrent(view);
        }
        else {
            midlet.notifyDestroyed();
        }
    }
}
