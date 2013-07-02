/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.items;

import com.instantme.util.BackStack;
import com.instantme.util.DisplaySpecs;
import com.instantme.util.IDetails;
import com.instantme.InstantME;
import com.instantme.entries.PhotoEntry;
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import tube42.lib.imagelib.ImageUtils;

public class PhotoItem extends CustomItem  {

    private final static int TEXT_BORDER = 2;
    private final static int IMAGE_BORDER = 2;
    private final static int IMAGE_BORDER_BASE = 2;
    private final static int IMAGE_SIZE = DisplaySpecs.getThumbnailImageSize();//150;

    private final static int COLOR_BLUE = 0x003366;
    private final static int COLOR_WHITE = 0xffffff;
    // image must be 150 x 150
    private static int count = 0;
    private PhotoEntry entry = null;
    private IDetails details = null;

    private int lastX = 0;
    private int lastY = 0;
    
    private Image img = null;

    public void addCommand(Command cmd) {
        super.addCommand(cmd);
    }
    public synchronized static int getIndex() {
        int i = PhotoItem.count++;
        return i % 5;
    }
    
    public PhotoItem(PhotoEntry entry, IDetails details) {
        super("");
        this.entry = entry;
        this.details = details;
        
        try {
            img = ImageUtils.resize(entry.getImage(),IMAGE_SIZE,IMAGE_SIZE,true,false);
        } catch (Exception ex) {
            img = null;
        }  

        if(img == null) {
            try {
                img = ImageUtils.resize(Image.createImage("/res/warning.png"),IMAGE_SIZE,IMAGE_SIZE,true,false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }             
        }
    }

    protected int getMinContentWidth() {
        BackStack bs = BackStack.getInstance();
        return ((InstantME)bs.getRunningMidlet()).getScreenWidth();
    }

    protected int getMinContentHeight() {
        Font f1 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font f2 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        return 5*TEXT_BORDER + IMAGE_SIZE + 2*f1.getHeight() + f2.getHeight() + 2*IMAGE_BORDER;
    }

    protected int getPrefContentWidth(int height) {
        return getMinContentWidth();
    }

    protected int getPrefContentHeight(int width) {
        return getMinContentHeight();
    }

    protected void paint(Graphics g, int w, int h) {
        
        int py = 0;
        int px = 0;
        g.setColor(COLOR_BLUE);
        g.fillRect(px, py,getMinContentWidth(),getMinContentHeight());
        
        py = TEXT_BORDER;
        px = TEXT_BORDER;
        
        Font f1 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setColor(COLOR_WHITE);
        g.setFont(f1);   
        
        String caption = entry.getFrom().getName() + " (" + entry.getDate().toString() + ")";
        g.drawString(caption, px, py, Graphics.TOP | Graphics.LEFT);
        
        int sx = img.getWidth()+2*IMAGE_BORDER;
        int sy = img.getHeight()+IMAGE_BORDER+IMAGE_BORDER_BASE;        
        py += f1.getHeight() + TEXT_BORDER;
        px = (getMinContentWidth() - sx - DisplaySpecs.CUSTOMITEM_BORDER)/2;
        g.fillRect(px, py, sx, sy);
        
        py += IMAGE_BORDER;
        px = (getMinContentWidth() - img.getWidth() - DisplaySpecs.CUSTOMITEM_BORDER)/2;
        g.drawImage(img,px,py,Graphics.TOP | Graphics.LEFT);
        
        py += img.getHeight() + IMAGE_BORDER_BASE + TEXT_BORDER;        
        px = TEXT_BORDER;
        
        Font f2 = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setFont(f2);
        
        String lks = String.valueOf(entry.getNumLikes());
        String cmts = String.valueOf(entry.getNumComments());
        int lksSize = f2.stringWidth(lks);

        Image lksImage = null;
        Image cmtsImage = null;
        
        try {
            if(entry.isUserHasLiked()) {
                lksImage = Image.createImage("/res/likes16.png");
            }
            else {
                lksImage = Image.createImage("/res/likesbw16.png");
            }
            cmtsImage = Image.createImage("/res/comments16.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }   
        
        g.drawImage(lksImage,px,py,Graphics.TOP | Graphics.LEFT);
        px += lksImage.getWidth() + TEXT_BORDER;
        g.drawString(lks, px, py, Graphics.TOP | Graphics.LEFT);
        px += lksSize + 2*TEXT_BORDER;
        g.drawImage(cmtsImage,px,py,Graphics.TOP | Graphics.LEFT);
        px += cmtsImage.getWidth() + TEXT_BORDER;
        g.drawString(cmts, px, py, Graphics.TOP | Graphics.LEFT);
        
        py += Math.max(f2.getHeight(),lksImage.getHeight());// + TEXT_BORDER;
        px = TEXT_BORDER;
        g.drawString(entry.getCaption(), px, py, Graphics.TOP | Graphics.LEFT);
        
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        if(keyCode == Canvas.FIRE) {
            details.showDetails(entry);
            repaint();
        }
    }
    
    protected void pointerPressed(int x, int y) {
        lastX = x;
        lastY = y;
    }

    protected void pointerReleased(int x, int y) {
        if((Math.abs(x-lastX) <10) && (Math.abs(y-lastY) < 10)) {
            details.showDetails(entry);
            repaint();
        }
    }
    
}

