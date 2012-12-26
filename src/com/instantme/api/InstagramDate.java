/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import com.instantme.locales.Locale;
import java.util.Calendar;
import java.util.Date;

public class InstagramDate extends Date {

    public InstagramDate(long unixTimeStamp) {
        super(unixTimeStamp*1000);
    }   
    
    public String toString() {
        String str;
        Date d = new Date();
        // number of seconds
        long dt = (d.getTime() - getTime())/1000;
        if(dt < 0) {
            // some problema here ....
            //System.out.println("Negative time ... Please, call Einstein");
            dt = 0;
        }
        
        if(dt < 60) { // 1 min
            str = String.valueOf(dt) + Locale.getInst().getStr(Locale.SEC_LETTER);
        } else if(dt < 60*60) { // 1 hour
            str = String.valueOf(dt/60) + Locale.getInst().getStr(Locale.MIN_LETTER);
        } else if(dt <60*60*24) { // 1 day
            str = String.valueOf(dt/(60*60)) + Locale.getInst().getStr(Locale.HOUR_LETTER);
        } else if(dt <60*60*24*30) { // month
            str = String.valueOf(dt/(60*60*24)) + Locale.getInst().getStr(Locale.DAY_LETTER);
        } else {
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(this);
            StringBuffer date = new StringBuffer();
            date.append(cal.get(Calendar.YEAR)).append('/');
            date.append(cal.get(Calendar.MONTH)+1).append('/');
            date.append(cal.get(Calendar.DATE));
            
            str = date.toString();
        }
        
        return str;
    }
    
}
