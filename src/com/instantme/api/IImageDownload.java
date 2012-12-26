/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import javax.microedition.lcdui.Image;

public interface IImageDownload {
    Image getImage();
    void setImage(Image img);
    String getImageURL();
}
