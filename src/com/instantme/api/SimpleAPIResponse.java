/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import org.json.me.JSONException;
import org.json.me.JSONObject;

class SimpleAPIResponse implements IHTTPResponseHandler {

    private IEntry data = null;
    private String response = null;
    
    public SimpleAPIResponse() {
    }
    public boolean Response30X(HttpConnection connection, Hashtable cookies) {
        return false;
    }
    public boolean Response20X(HttpConnection connection, Hashtable cookies) {
        
        boolean result = false;
        StringBuffer strf = null;
        response = null;
        
        try {
            InputStream is = connection.openInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");

            int ch;
            strf = new StringBuffer();
            while ((ch = isr.read()) != -1) {
                strf.append((char) ch);
            }
            
            response = strf.toString();
            strf = null;
            int code = new JSONObject(response).getJSONObject("meta").getInt("code");
            result = code == 200;
            response = null;
            isr.close();
            is.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return result;
    }
    public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
        return false;
    }
}
