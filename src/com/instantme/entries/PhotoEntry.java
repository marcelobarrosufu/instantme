/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.entries;

import com.instantme.util.IAnimation;
import com.instantme.api.IEntry;
import com.instantme.api.IImageDownload;
import com.instantme.api.InstagramDate;
import com.instantme.entries.UserEntry;
import javax.microedition.lcdui.Image;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class PhotoEntry implements IEntry, IImageDownload {

    private String thumbImg = "";
    private String caption = "";
    private UserEntry from = null;
    private Image image = null;
    private InstagramDate date;
    private boolean userHasLiked = false;
    private int numLikes = 0;
    private int numComments = 0;
    private String mediaID = "";

    public UserEntry getFrom() {
        return from;
    }

    public void setFrom(UserEntry from) {
        this.from = from;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public boolean isUserHasLiked() {
        return userHasLiked;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public void setUserHasLiked(boolean userHasLiked) {
        this.userHasLiked = userHasLiked;
    }

    public String getMediaID() {
        return mediaID;
    }

    public int getNumComments() {
        return numComments;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public InstagramDate getDate() {
        return date;
    }

    public void setImage(Image img) {
        image = img;
    }
    
    public Image getImage() {
        return image;
    }
    
    public String getCaption() {
        return caption;
    }

    public String getThumbImageUrl() {
        return thumbImg;
    }

    public boolean fromJSONObject(JSONObject obj, IAnimation anim) {

        boolean result = false;
        
        try {
            thumbImg = (String) obj.getJSONObject("images").getJSONObject("thumbnail").get("url");
            
            if (!obj.isNull("caption")) {
                caption = (String) obj.getJSONObject("caption").get("text");
            } else {
                caption = "";
            }
            UserEntry f = new UserEntry();
            f.fromJSONObject(obj.getJSONObject("user"),anim);
            setFrom(f);
            long ts = (long) obj.getInt("created_time");
            date = new InstagramDate(ts);
            //System.out.println(date);
            
            numLikes = obj.getJSONObject("likes").getInt("count");
            userHasLiked = obj.getBoolean("user_has_liked");
            numComments = obj.getJSONObject("comments").getInt("count");
            mediaID = obj.getString("id");
            
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        
        return result;
    }

    public String toString() {
        return "PhotoEntry{" + "thumbImg=" + thumbImg + ", caption=" + caption + getFrom().toString() + '}';
    }

    public String getImageURL() {
        return getThumbImageUrl();
    }

    
}
