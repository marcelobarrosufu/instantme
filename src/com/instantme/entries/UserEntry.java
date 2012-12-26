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
import javax.microedition.lcdui.Image;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class UserEntry implements IEntry, IImageDownload {

    private Image image = null;
    private String userName = "";
    private String fullName = "";
    private String profilePicture = "";
    private String userID = "";

    public UserEntry() {
    }

    public String toString() {
        return "UserEntry{" + "userName=" + userName + ", fullName=" + fullName + ", profilePicture=" + profilePicture + ", userID=" + userID + '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }
    
    public String getName() {
        if(fullName.length() > 0) {
            return fullName;
        }
        else {
            return userName;
        }
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public boolean fromJSONObject(JSONObject obj, IAnimation anim) {
        boolean result = false;
        
        try {
            setUserName((String) obj.get("username"));
            // when using relations, we have first_name and last_name
            // when using comments, posts, we have full_name
            if(obj.has("first_name")) {
                setFullName((String) obj.get("first_name"));
            }
            
            if(obj.has("last_name")) {
                String ln = (String) obj.get("last_name");
                if(ln.length() > 0) {
                    setFullName(getFullName() + " " + ln);
                }
            }
            
            if(obj.has("full_name")) {
                String fn = (String) obj.get("full_name");
                if(fn.length() > 0) {
                    setFullName(fn);
                }
            }

            setUserID((String) obj.get("id"));
            setProfilePicture((String) obj.get("profile_picture"));
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
       
        return result;   
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }

    public String getImageURL() {
        return getProfilePicture();
    }
    
}

