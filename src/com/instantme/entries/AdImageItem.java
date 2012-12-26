/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.entries;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

public class AdImageItem extends ImageItem {

    private String url;
    
    public AdImageItem(String label, Image img, int layout, String altText) {
        super("", img, layout, altText);
        url = label;
    }

    public String getLabel() {
        return url;
    }
    
    public void setLabel(String label) {
        url = label;
        super.setLabel("");
    }
}
