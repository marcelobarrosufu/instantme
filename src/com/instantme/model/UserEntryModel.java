/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.model;

import com.instantme.util.IAnimation;
import com.instantme.api.IEntry;
import com.instantme.entries.UserEntry;
import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class UserEntryModel implements IEntry {

    private Vector data = null;
    
    public UserEntryModel() {
        data = new Vector();
    }
        
    public int size() {
        return data.size();
    }
    
    public UserEntry elementAt(int pos) {
        return (UserEntry) data.elementAt(pos);
    }

    
    public boolean fromJSONObject(JSONObject jobj, IAnimation anim) {
       boolean result = false;
        
        try {
            Vector _data = new Vector();
            JSONArray feeds = jobj.getJSONArray("data");
            
            if(feeds != null) {
                for(int n = 0 ; n < feeds.length(); n++) {
                    //System.out.println("User " + n);
                    JSONObject entry = feeds.getJSONObject(n); 
                    UserEntry ue = new UserEntry();
                    ue.fromJSONObject(entry,anim);
                    _data.addElement(ue);
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
