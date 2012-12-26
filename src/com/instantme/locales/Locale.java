/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.locales;

public class Locale {
    
    private ILocale loc;
    public static final int OK_MENU = 0;
    public static final int BACK_MENU = 1;
    public static final int EXIT_MENU = 2;
    public static final int ABOUT_MENU = 3;
    public static final int SUPPORT = 4;
    public static final int UPDATE_MENU = 5;
    public static final int WAIT = 6;
    public static final int WAIT_OPERATION = 7;
    public static final int USER_INFO = 8;
    public static final int UPDATE_FOLLOWING = 9;
    public static final int FAILED = 10;
    public static final int CAN_NOT_UPDATE = 11;
    public static final int UPDATE_FOLLOWED = 12;
    public static final int LOGIN_1_4 = 13;
    public static final int LOGIN_2_4 = 14;
    public static final int LOGIN_3_4 = 15;
    public static final int LOGIN_4_4 = 16;
    public static final int REQUESTING = 17;
    public static final int DOWNLD_USR_INFO = 18;
    public static final int DOWNLD_USR_PICT = 19;
    public static final int DOWNLD_PHOTO = 20;
    public static final int SEC_LETTER = 21;
    public static final int MIN_LETTER = 22;
    public static final int HOUR_LETTER = 23;
    public static final int DAY_LETTER = 24;
    public static final int USERNAME = 25;
    public static final int PASSWORD = 26;
    public static final int DISCLAIMER = 27;
    public static final int DISCLAIMER_NOTE = 28;
    public static final int CANCEL_MENU = 29;
    public static final int LOGIN_MENU = 30;
    public static final int ERROR = 31;
    public static final int ERROR_USR_PWD_EMPTY = 32;
    public static final int LOGGING = 33;
    public static final int SUCCESS = 34;
    public static final int SUCCESS_UPDT_TIMELINE = 35;
    public static final int FAILED_CHECK_USR_PWD_NET = 36;
    public static final int ERROR_DOWNLOADING = 37;
    public static final int LIKES = 38;
    public static final int LIKES_YOU = 39;
    public static final int COMMENTS = 40;
    public static final int LIKE_MENU = 41;
    public static final int ADD_CMT_MENU = 42;
    public static final int DEL_CMT_MENU = 43;
    public static final int DELETE_MENU = 44;
    public static final int SEND_MENU = 45;
    public static final int CLEAR_MENU = 46;
    public static final int ERROR_CAN_NOT_DEL_CMT = 47;
    public static final int ERROR_NO_CMT_TO_DEL = 48;
    public static final int COMMENT = 49;
    public static final int UPDATING_CMTS = 50;
    public static final int DELETING_CMTS = 51;
    public static final int ADDING_LIKE = 52;
    public static final int REMOVING_LIKE = 53;
    public static final int ADDING_CMT = 54;
    public static final int ERROR_CAN_NOT_LOAD_CMTS = 55;
    public static final int ERROR_WHEN_ADD_CMT = 56;
    public static final int ERROR_WHEN_DEL_CMT = 57;
    public static final int OLDER_MENU = 58;
    public static final int NEWER_MENU = 59;
    public static final int ME_MENU = 60;
    public static final int FRIENDS_MENU = 61;
    public static final int ABOUT = 62;
    public static final int LOGIN_FIRST = 63;
    public static final int MY_INFO = 64;
    public static final int PROVIDE_CRED = 65;
    public static final int RESTART_APP = 66;
    public static final int UPDATING_FEED = 67;
    public static final int FRIENDS = 68;
    public static final int FOLLOWING_MENU = 69;
    public static final int FOLLOWEDBY_MENU = 70;
    public static final int FOLLOWING = 71;
    public static final int FOLLOWEDBY = 72;    
    public static final int BIO = 73;
    public static final int WEBSITE = 74;
    public static final int MEDIA = 75;
    public static final int PRIVATE_PROFILE = 76;
    public static final int LOADING = 77;
    public static final int ANALYZING = 78;
    public static final int SPLASH_TOP = 79;
    public static final int SPLASH_BOTTOM = 80;
    public static final int OPEN_MENU = 81;
    
    private Locale() {
        String locale;
        try {
            locale = System.getProperty("microedition.locale");
        } catch(Exception e) {
            locale = "en";
        }
        
        if(locale.startsWith("pt")) {
            loc = (ILocale) new LocalePT_BR();
        }
        else {
            loc = (ILocale) new LocaleEN_US();
        }
    }
    
    public static Locale getInst() {
        return LocalesHolder.INSTANCE;
    }
    
    private static class LocalesHolder {
        private static final Locale INSTANCE = new Locale();
    }
    
    public String getStr(int id) {
        return loc.getStr(id);
    }
}
