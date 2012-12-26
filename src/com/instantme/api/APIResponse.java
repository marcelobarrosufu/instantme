/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import com.instantme.util.IAnimation;
import com.instantme.locales.Locale;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import org.json.me.JSONException;
import org.json.me.JSONObject;

class APIResponse implements IHTTPResponseHandler {

    private IEntry data = null;
    private String response = null;
    private IAnimation animation = null;
    
    public APIResponse(IEntry data) {
        this(data,null);
    }

    public APIResponse(IEntry data, IAnimation anim) {
        this.data = data;
        this.animation = anim;
    }
    
    private void updateProgress(String msg) {
        if(animation != null) {
            animation.updateProgress(msg);
        }
    }
    public boolean Response30X(HttpConnection connection, Hashtable cookies) {
        return false;
    }
    public boolean Response20X(HttpConnection connection, Hashtable cookies) {
        
        boolean result = false;
        StringBuffer strf = null;
        response = null;
        
        try {
            updateProgress(Locale.getInst().getStr(Locale.LOADING));
            InputStream is = connection.openInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");

            int ch;
            strf = new StringBuffer();
            while ((ch = isr.read()) != -1) {
                strf.append((char) ch);
            }
            
            response = strf.toString();
            strf = null;
            updateProgress(Locale.getInst().getStr(Locale.ANALYZING));
            // TODO do not decode before checking reponse code
            try {
                JSONObject jobj = new JSONObject(response);
                result = data.fromJSONObject(jobj,animation);
            } catch (JSONException ex) {
                ex.printStackTrace();
                result = false;
            }
            
            response = null;
            isr.close();
            is.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }
    public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
        return false;
    }
}


