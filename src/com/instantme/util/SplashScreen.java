/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.util;

import com.instantme.locales.Locale;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class SplashScreen extends Canvas {

    Image logo;

    private final static int COLOR_BLUE = 0x003366;
    private final static int COLOR_WHITE = 0xffffff;
    
    public SplashScreen() {
        super();
        setFullScreenMode(true);
        try {
            logo = Image.createImage("/res/instantmesplash.png");            
        } catch (IOException ex) {
            logo = null;
        }
    }
    
    protected void paint(Graphics g) {
        
        g.setColor(COLOR_WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        
        Font f1 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        Font f2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        
        int px = getWidth()/2;
        
        g.setColor(COLOR_BLUE);
        g.setFont(f1); 
        g.drawString(Locale.getInst().getStr(Locale.SPLASH_TOP), px, 0, Graphics.TOP | Graphics.HCENTER);

        g.setFont(f2); 
        g.drawString(Locale.getInst().getStr(Locale.SPLASH_BOTTOM), px, getHeight(), Graphics.BOTTOM | Graphics.HCENTER);
        
        if(logo != null) {
            g.drawImage(logo, getWidth()/2, getHeight()/2, Graphics.HCENTER | Graphics.VCENTER);    
        }
    }
}
