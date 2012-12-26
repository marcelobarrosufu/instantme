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

public class CommentEntry implements IEntry  {
    
    private UserEntry from = null;
    private String comment = "";
    private String commentID = "";

    public UserEntry getFrom() {
        return from;
    }

    public void setFrom(UserEntry from) {
        this.from = from;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public CommentEntry() {
    }

    public boolean fromJSONObject(JSONObject obj, IAnimation anim) {
        boolean result = false;
        
        try {
            setComment((String) obj.get("text"));
            setCommentID((String) obj.get("id"));
            UserEntry f = new UserEntry();
            f.fromJSONObject(obj.getJSONObject("from"),anim);
            setFrom(f);
            result = true;
            
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        
        
        return result;            
    }
    
}
