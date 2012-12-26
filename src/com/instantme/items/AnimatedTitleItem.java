/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.items;

import com.instantme.util.BackStack;
import com.instantme.util.DisplaySpecs;
import com.instantme.util.IAnimation;
import com.instantme.InstantME;
import java.io.IOException;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class AnimatedTitleItem extends CustomItem implements Runnable, IAnimation {

    private final static int TEXT_BORDER = 3;
    private final static int IMAGE_SIZE = 20;
    
    private final static int COLOR_BLUE = 0x003366;
    private final static int COLOR_WHITE = 0xffffff;
    
    private String message;
    private Image images[];
    private Image curImage, likeImage, dislikeImage, commentImage;
    private boolean animated;

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
        repaint();
    }
    
    public AnimatedTitleItem() {
        super("");
        this.message = "";
        try {
            images = new Image[]{ 
                Image.createImage("/res/wait00.png"),
                Image.createImage("/res/wait01.png"),
                Image.createImage("/res/wait02.png"),
                Image.createImage("/res/wait03.png"),
                Image.createImage("/res/wait04.png"),
                Image.createImage("/res/wait05.png"),
                Image.createImage("/res/wait06.png"),
                Image.createImage("/res/wait07.png"),
                Image.createImage("/res/wait08.png"),
                Image.createImage("/res/wait09.png"),
                Image.createImage("/res/wait10.png"),                
            };
            
            likeImage = Image.createImage("/res/likes16.png");
            dislikeImage = Image.createImage("/res/likesbw16.png");
            commentImage = Image.createImage("/res/comments16.png");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        curImage = images[0];
    } 
        
    protected int getMinContentWidth() {
        BackStack bs = BackStack.getInstance();
        return ((InstantME)bs.getRunningMidlet()).getScreenWidth();
    }

    protected int getMinContentHeight() {
        Font f1 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font f2 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int h = 2*TEXT_BORDER + f1.getHeight() + f2.getHeight();
        return h;
    }

    public void setMessage(String message) {
        this.message = message;
        repaint();
    }
    
    protected int getPrefContentWidth(int height) {
        return getMinContentWidth();
    }

    protected int getPrefContentHeight(int width) {
        return getMinContentHeight();
    }
    
    protected void paint(Graphics g, int w, int h) {
                
            int py;
            int px;
            g.setColor(COLOR_BLUE);
            g.fillRect(0, 0,getMinContentWidth(),getMinContentHeight());

            py = TEXT_BORDER;
            px = TEXT_BORDER;

            Font f = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            g.setColor(COLOR_WHITE);
            g.setFont(f);   
            g.drawString(message, px, py, Graphics.TOP | Graphics.LEFT);

            if(isAnimated()) {
                px = getMinContentWidth() - IMAGE_SIZE - TEXT_BORDER;
                g.drawImage(curImage,px,py,Graphics.TOP | Graphics.LEFT);
            }
        
    }
    
    public void run() {
        while(animated) {
            for(int n = 1 ; n <= 10 ; n++) {
                curImage = images[n];
                repaint();
                if(animated == false) {
                    break;
                }
                synchronized(this) {
                    try {
                        wait(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void start() {
        animated = true;
        setMessage("");
        Thread t = new Thread(this);
        t.start(); 
        repaint();
    }

    public void stop() {
        animated = false;
        setMessage("");
        repaint();
    }

    public void updateProgress(String msg, int perc) {
    }

    public void updateProgress(String msg) {
    }
    
}
