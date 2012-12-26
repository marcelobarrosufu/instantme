/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import com.instantme.api.IHTTPResponseHandler;
import com.instantme.api.IImageDownload;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;

public class DownloadImageResponse implements IHTTPResponseHandler {

    private IImageDownload data = null;
    
    public DownloadImageResponse(IImageDownload data) {
        this.data = data;
    }
    public boolean Response30X(HttpConnection connection, Hashtable cookies) {
        return false;
    }
    public boolean Response20X(HttpConnection connection, Hashtable cookies) {
        
        boolean result = false;
        
        try {
            
            int len = (int) connection.getLength();
            InputStream is = connection.openInputStream();
            byte[] d = new byte[len];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(d);
            data.setImage(Image.createImage(d, 0, d.length));
            dis.close();
            is.close();
            result = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }
    public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
        return false;
    }
}


