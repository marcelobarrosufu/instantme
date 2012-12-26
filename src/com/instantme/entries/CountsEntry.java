/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.entries;

import com.instantme.util.IAnimation;
import com.instantme.api.IEntry;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class CountsEntry implements IEntry {
    
    private int media = 0;
    private int following =  0;
    private int followedBy = 0;

    public int getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(int followedBy) {
        this.followedBy = followedBy;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int follows) {
        this.following = follows;
    }


    public CountsEntry() {
    }

    public boolean fromJSONObject(JSONObject obj, IAnimation anim) {
        boolean result = false;
        
        try {
            setMedia(obj.getInt("media"));
            setFollowing(obj.getInt("follows"));
            setFollowedBy(obj.getInt("followed_by"));
            
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
       
        return result;  
    }

    public String toString() {
        return "CountsEntry{" + "media=" + media + ", follows=" + following + ", followedBy=" + followedBy + '}';
    }
    
}
