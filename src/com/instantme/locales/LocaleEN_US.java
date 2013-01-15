/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.locales;

public class LocaleEN_US implements ILocale {
    
    private String texts[] = { 
        "ok", 
        "back", 
        "exit",
        "about",
        "InstantME V1.0.1. Developed by Shining Bits.\nSupport: support@shiningbits.com",
        "update",
        "Wait",
        "Please, wait while the current operation is executed.",
        "User Info",
        "Updating following ...",
        "Failed",
        "Impossible to update. Please, try again.",
        "Updating followed by ...",
        "Logging in - Step 1/4 ...",
        "Logging in - Step 2/4 ...",
        "Logging in - Step 3/4 ...",
        "Logging in - Step 4/4 ...",
        "Requesting ...",
        "Downloading user info ...",
        "Downloading user picture ...",
        "Downloading photo ",
        "s",
        "m",
        "h",
        "d",
        "Username",
        "Password",
        "Disclaimer",
        "This application accesses your photos, comments, likes and friends on your behalf." +
            " Your login credentials will not be stored on this phone." +
            "\nYou must have a valid Instagram account before using this program. " +
            "Create it from an Android phone or iPhone/iPad.",
        "cancel",
        "login",
        "Error",
        "Username or password can not be empty!",
        "Logging in ...",
        "Success",
        "Logged in. Use the update menu to refresh your timeline.",
        "Impossible to login. Check your username/password and network connection.",
        "Error when downloading",
        "Likes",
        "Likes (You)",
        "Comments",
        "like",
        "add comment",
        "del comment",
        "delete",
        "send",
        "clear",
        "You can not delete any comment in this post.",
        "No comments to delete.",
        "Comment",
        "Updating comments ...",
        "Deleting comments ...",
        "Adding like ...",
        "Removing like ...",
        "Adding comment ...",
        "Impossible to load comments. Please, try again.",
        "Error when adding the comment. Please, try again.",
        "Error when removing the comment. Please, try again.",
        "older",
        "newer",
        "me",
        "friends",
        "About",
        "Please, log in first.",
        "My Info",
        "Please, provide your Instagram credential via login menu.",
        "Login failed. Restart the application or provide your Instagram credential via login menu.",
        "Updating feed ...",
        "Friends",
        "following",
        "followed by",
        "Following",
        "Followed by",
        "Bio",
        "Website",
        "Media",
        "Impossible to update (private profile?). Please, try again.",
        "Loading ...",
        "Analyzing ...",
        "InstantME",
        "Shining Bits",
        "open",
        "help",
        "Help",
        "You must have a valid Instagram account before using this application. " +
            "It can be created from an Android phone or iPhone/iPad.\n\n" +
            "Using this account information, use the option 'login' to login into Instagram network.\n\n" +
            "You can navigate using the options 'older' or 'newer' and check your followers/following " +
            "using the option 'friends'.\n\nYou account details is displayed using the option 'me'.\n\n" +
            "Finally, the option 'update' shows what is new in your timeline."
        
    };
    
    public String getStr(int id) {
        if(id < 0 || id >= texts.length)
            return "UNDEF";  
        else
            return texts[id];
    }
}
