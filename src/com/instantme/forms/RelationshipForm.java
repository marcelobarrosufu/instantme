/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.forms;

import com.instantme.util.BackStack;
import com.instantme.api.InstagramAPI;
import com.instantme.forms.FollowersForm;
import com.instantme.entries.FullUserEntry;
import com.instantme.entries.CountsEntry;
import com.instantme.locales.Locale;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

public class RelationshipForm extends Form implements CommandListener, ItemCommandListener {

    private StringItem following;
    private StringItem followed;
    private Command followingCmd;
    private Command followedCmd;
    private Command cancelCommand;
               
    public RelationshipForm() {
        super(Locale.getInst().getStr(Locale.FRIENDS)); 
        
        InstagramAPI oai = InstagramAPI.getInstance();
        FullUserEntry info = oai.getAuthUserInfo();
        CountsEntry counts = info.getCounts();
        
        followingCmd = new Command(Locale.getInst().getStr(Locale.FOLLOWING_MENU), Command.ITEM, 0);
        followedCmd = new Command(Locale.getInst().getStr(Locale.FOLLOWEDBY_MENU), Command.ITEM, 0);
        
        following = new StringItem(Locale.getInst().getStr(Locale.FOLLOWING),String.valueOf(counts.getFollowing()),Item.HYPERLINK);
        following.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_BEFORE);
        following.setDefaultCommand(followingCmd);
        following.setItemCommandListener(this);
        append(following);

        followed = new StringItem(Locale.getInst().getStr(Locale.FOLLOWEDBY),String.valueOf(counts.getFollowedBy()),Item.HYPERLINK);
        followed.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_BEFORE);
        followed.setDefaultCommand(followedCmd);
        followed.setItemCommandListener(this);
        append(followed);  
        
        cancelCommand = new Command(Locale.getInst().getStr(Locale.BACK_MENU), Command.BACK, 0);
        addCommand(cancelCommand);   
        setCommandListener(this);
               
    }
    
    public void commandAction(Command c, Displayable d) {
        BackStack bs = BackStack.getInstance();
        if (c == cancelCommand) {
            bs.back();
        } 
    }


    public void commandAction(Command c, Item item) {
        BackStack bs = BackStack.getInstance();
        if(c == followingCmd) {
            FollowersForm f = new FollowersForm(Locale.getInst().getStr(Locale.FOLLOWING),FollowersForm.FOLLOWING);
            bs.forward(f);
        }
        else if(c == followedCmd) {
            FollowersForm f = new FollowersForm(Locale.getInst().getStr(Locale.FOLLOWEDBY),FollowersForm.FOLLOWEDBY);
            bs.forward(f);
        }
    }
    
}
