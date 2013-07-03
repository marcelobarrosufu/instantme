/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.model;

import com.instantme.util.IAnimation;
import com.instantme.api.IEntry;
import com.instantme.entries.PhotoEntry;
import java.util.Stack;
import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class PhotoEntryModel implements IEntry {

    private Vector data = null;
    private Stack cursor = null;
    private IAnimation animation = null;
    
    public PhotoEntryModel() {
        data = new Vector();
        cursor = new Stack();
    }
    
    public int size() {
        return data.size();
    }

    public int historySize() {
        return cursor.size();
    }
    
    public PhotoEntry elementAt(int pos) {
        return (PhotoEntry) data.elementAt(pos);
    }

    public String getNextUrl() {
        if(cursor.isEmpty()) {
            return null;
        }
        else {
            return (String) cursor.lastElement();
        }
    }
    
    public void clearHistory() {
        cursor.removeAllElements();
    }
    
    public String getPrevUrl() {
        if(!cursor.isEmpty()) {
            cursor.pop();
        }

        if(!cursor.isEmpty()) {
            cursor.pop();
        }
        
        if(cursor.isEmpty()) {
            return null;
        }
        else {
            return (String) cursor.lastElement();
        }
            
    }
    
    private void updateProgress(String msg) {
        if(animation != null) {
            animation.updateProgress(msg);
        }
    }

    public void setAnimation(IAnimation anim) {
        this.animation = anim;
    }
    
    public boolean fromJSONObject(JSONObject jobj, IAnimation anim) {
        boolean result = false;
        setAnimation(anim);
        try {
            Vector _data = new Vector();
            JSONArray feeds = jobj.getJSONArray("data");
            int numElements = feeds.length();
            
            if(feeds != null) {
                for(int n = 0 ; n < numElements; n++) {
                    //updateProgress( "Reading photo " + String.valueOf(n+1) + "/" + String.valueOf(numElements) + " ..." );
                    JSONObject entry = feeds.getJSONObject(n); 
                    PhotoEntry pe = new PhotoEntry();
                    pe.fromJSONObject(entry,anim);
                    _data.addElement(pe);                 
                }
            }
            data = _data;
            String url = jobj.getJSONObject("pagination").getString("next_url");
            cursor.push(url);
            //System.out.println("Stack [" + cursor.size() + "] = " + url);
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    public String toString() {
        
        StringBuffer strb = new StringBuffer();
        
        for(int n = 0 ; n < data.size() ; n++) {
            strb.append("\t");
            strb.append(((PhotoEntry) data.elementAt(n)).toString());
            strb.append("\n");
        }
        
        return "PhotoEntryArray{\n" + strb.toString() + "\n}";
    }

}
