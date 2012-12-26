/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.model;

import com.instantme.util.IAnimation;
import com.instantme.api.IEntry;
import com.instantme.entries.CommentEntry;
import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class CommentEntryModel implements IEntry {

    private Vector data = null;
    private String mediaID;
    private IAnimation animation = null;

    public String getMediaID() {
        return mediaID;
    }

    public synchronized void addElement(CommentEntry ce) {
        data.addElement(ce);
    }
    public synchronized void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }
    
    public CommentEntryModel() {
        data = new Vector();
    }
    
    public synchronized int size() {
        return data.size();
    }
    
    public synchronized void removeAllElements() {
        data.removeAllElements();
    }
    public synchronized CommentEntry elementAt(int pos) {
        return (CommentEntry) data.elementAt(pos);
    }    
    
    public void setAnimation(IAnimation anim) {
        this.animation = anim;
    }
    
    public boolean fromJSONObject(JSONObject jobj, IAnimation anim) {
        boolean result = false;
        setAnimation(anim);
        try {
            Vector _data = new Vector();
            JSONArray comments = jobj.getJSONArray("data");
            int numElements = comments.length();
            
            if(comments != null) {
                for(int n = 0 ; n < numElements; n++) {
                    JSONObject entry = comments.getJSONObject(n); 
                    CommentEntry c = new CommentEntry();
                    c.fromJSONObject(entry,anim);
                    _data.addElement(c);                 
                }
            }
            data = _data;
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
}
