/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.util;

import com.instantme.InstantME;
import javax.microedition.lcdui.Font;

public final class DisplaySpecs {
    
    public final static int FONT_DEFAULT = Font.FONT_STATIC_TEXT;
    public final static int CUSTOMITEM_BORDER = 20;

    public static int getThumbnailImageSize() {
        BackStack bs = BackStack.getInstance();   
        InstantME m = (InstantME) bs.getRunningMidlet();
        int v = (int) (Math.min(m.getScreenHeight(),m.getScreenWidth())*0.4);
        return v;
    }
    
    public static int getFullImageSize() {
        return getScreenWidthSize() - 2*CUSTOMITEM_BORDER;
    }

    public static int getProfileImageSize() {
        BackStack bs = BackStack.getInstance();   
        InstantME m = (InstantME) bs.getRunningMidlet();
        int v = 2*Math.min(m.getScreenHeight(),m.getScreenWidth())/3;
        return v;
    }
    
    public static int getScreenWidthSize() {
        BackStack bs = BackStack.getInstance();   
        InstantME m = (InstantME) bs.getRunningMidlet();
        return m.getScreenWidth();
    }    
}

