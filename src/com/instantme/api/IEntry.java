/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import com.instantme.util.IAnimation;
import org.json.me.JSONObject;

public interface IEntry {
    public boolean fromJSONObject(JSONObject obj, IAnimation anim);
}
