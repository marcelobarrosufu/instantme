/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme;

import com.instantme.util.SplashScreen;
import com.instantme.util.BackStack;
import com.instantme.forms.PhotoListForm;
import com.instantme.locales.Locale;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class InstantME extends MIDlet {
   
    private PhotoListForm photoList;
    private Display display;
    private boolean midletPaused = false;
    private int screenWidth = 0;
    private int screenHeight = 0;
    
    public void startMIDlet()  { 
        
        BackStack bs = BackStack.getInstance();
        bs.setRunningMidlet(this);
        
        display = getDisplay();
        
        SplashScreen s = new SplashScreen();
        display.setCurrent(s);

        synchronized (this) {
            try {
                wait(2500);
            } catch (InterruptedException ex) {
            }
        }    
        
        screenWidth = s.getWidth();
        screenHeight = s.getHeight();
        photoList = new PhotoListForm(Locale.getInst().getStr(Locale.SPLASH_TOP));
        display.setCurrent(photoList); 
        s = null;
        
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }
        
    public Display getDisplay() {
        return Display.getDisplay(this);
    }
    
    public void resumeMIDlet() {
        
    }
    
    protected void startApp() {
        
        if (midletPaused) {
            resumeMIDlet();
        } else {
            startMIDlet();
        }
        
        midletPaused = false;
    }

    public void exitMIDlet() {
        display.setCurrent(null);
        destroyApp(true);
    }
    
    protected void pauseApp() {
        midletPaused = true;
    }

    
    protected void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
}
