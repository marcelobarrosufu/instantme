/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.entries;

import com.instantme.util.IAnimation;
import com.instantme.entries.CountsEntry;
import com.instantme.entries.UserEntry;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class FullUserEntry extends UserEntry {
 
    private String bio = "";
    private String website = "";
    private CountsEntry counts =  null;

    public FullUserEntry(UserEntry ue) {
        
        setFullName(ue.getFullName());
        setProfilePicture(ue.getProfilePicture());
        setUserID(ue.getUserID());
        setUserName(ue.getUserName());
    }

    public String toString() {
        return "FullUserEntry{" + "bio=" + bio + ", website=" + website + super.toString() + ", counts=" + counts + '}';
    }

    public CountsEntry getCounts() {
        return counts;
    }

    public void setCounts(CountsEntry counts) {
        this.counts = counts;
    }

    public FullUserEntry() {
        super();
    }
    
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    public boolean fromJSONObject(JSONObject jobj, IAnimation anim) {
        
        boolean result = false;
                
        try {
            JSONObject obj = jobj.getJSONObject("data");
            if(super.fromJSONObject(obj,anim)) {
                setWebsite((String) obj.get("website"));
                setBio((String) obj.get("bio"));
                CountsEntry  ce = new CountsEntry();
                ce.fromJSONObject(obj.getJSONObject("counts"),anim);
                setCounts(ce);
                result = true;
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
  
        
        return result;   
    }    


}
