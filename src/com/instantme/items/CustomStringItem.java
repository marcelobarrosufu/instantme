/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.items;

import javax.microedition.lcdui.StringItem;

public class CustomStringItem extends StringItem {

    private Object privateData = null;

    public Object getPrivateData() {
        return privateData;
    }

    public void setPrivateData(Object privateData) {
        this.privateData = privateData;
    }
    
    
    public CustomStringItem(String label, String text) {
        super(label, text);
    }

    public CustomStringItem(String label, String text, int appearanceMode) {
        super(label, text, appearanceMode);
    }
    
}
