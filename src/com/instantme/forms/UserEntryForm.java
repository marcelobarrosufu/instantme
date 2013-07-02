/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.forms;

import com.instantme.util.BackStack;
import com.instantme.util.DisplaySpecs;
import com.instantme.util.IAnimation;
import com.instantme.api.InstagramAPI;
import com.instantme.util.TaskHelper;
import com.instantme.entries.FullUserEntry;
import com.instantme.entries.CountsEntry;
import com.instantme.entries.UserEntry;
import com.instantme.entries.TaskEntry;
import com.instantme.items.WaitItem;
import com.instantme.locales.Locale;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import tube42.lib.imagelib.ImageUtils;

public class UserEntryForm extends Form implements Runnable, IAnimation, CommandListener, ItemCommandListener {

    private FullUserEntry user = null;
    
    private Command cancelCommand;
    private Command followCommand;
    
    private TaskHelper tasks = null;
    private WaitItem waitAnim = null;
    
    private final static int TASK_GET_FULL_USER_INFO = 1;
    
    public UserEntryForm(String title, UserEntry user) {
        super(title);
        this.user = new FullUserEntry(user);
        
        cancelCommand = new Command(Locale.getInst().getStr(Locale.BACK_MENU), Command.BACK, 0);
        addCommand(cancelCommand); 
        setCommandListener(this);

        waitAnim = new WaitItem();        
        tasks = new TaskHelper(this,this);
        tasks.push(new TaskEntry(TASK_GET_FULL_USER_INFO));            
    }

    private void updateList() {

        String imgCap = user.getName();
        Image image;
        int s = DisplaySpecs.getProfileImageSize();

        try {
            image = ImageUtils.resize(user.getImage(),s,s,true,false);
        } catch (Exception ex) {
            image = null;
        }  

        if(image == null) {
            try {
                image = ImageUtils.resize(Image.createImage("/res/warning.png"),s,s,true,false);
                imgCap += " (" + Locale.getInst().getStr(Locale.ERROR) + ")";
            } catch (IOException ex) {
                ex.printStackTrace();
            }             
        }          

        ImageItem imageItem = new ImageItem(imgCap,image,Item.LAYOUT_DEFAULT,"");
        imageItem.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_CENTER); // Center the image
        this.append(imageItem);
        
        if(user.getBio().length() > 0) {
            StringItem item = new StringItem(Locale.getInst().getStr(Locale.BIO),user.getBio());
            item.setLayout(Item.LAYOUT_EXPAND);
            this.append(item);
        }
        
        if(user.getWebsite().length() > 0) {
            StringItem item = new StringItem(Locale.getInst().getStr(Locale.WEBSITE),user.getWebsite());
            item.setLayout(Item.LAYOUT_EXPAND);
            this.append(item);            
        }
        
        CountsEntry cnts = user.getCounts();
        
        StringItem a = new StringItem(Locale.getInst().getStr(Locale.FOLLOWING),String.valueOf(cnts.getFollowing()));
        a.setLayout(Item.LAYOUT_EXPAND);
        this.append(a);            
        
        StringItem b = new StringItem(Locale.getInst().getStr(Locale.FOLLOWEDBY),String.valueOf(cnts.getFollowedBy()));
        b.setLayout(Item.LAYOUT_EXPAND);
        this.append(b);            

        StringItem c = new StringItem(Locale.getInst().getStr(Locale.MEDIA),String.valueOf(cnts.getMedia()));
        c.setLayout(Item.LAYOUT_EXPAND);
        this.append(c);            
        
    } 
    
    public void commandAction(Command c, Displayable d) {
        BackStack bs = BackStack.getInstance();
        if(tasks.isRunning()){
            showAlert(Locale.getInst().getStr(Locale.WAIT),Locale.getInst().getStr(Locale.WAIT_OPERATION));
        } else{
            if (c == cancelCommand) {
                bs.back();
            }
        }
    }

    public void commandAction(Command c, Item item) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private void showAlert(String title, String msg) {
        BackStack bs = BackStack.getInstance();
        Alert a = new Alert(title);
        a.setString(msg);
        a.setType(AlertType.INFO);
        bs.getCurrentDisplay().setCurrent(a);             
    }
    
    public void run() {
        InstagramAPI oai = InstagramAPI.getInstance(); 
        TaskEntry te = (TaskEntry) tasks.pop();

        switch(te.getID()) {
            case TASK_GET_FULL_USER_INFO:
                if(oai.getFullUserInfo(user,user.getUserID(),this)) {
                    updateList();
                }
                else {
                    showAlert(Locale.getInst().getStr(Locale.FAILED),Locale.getInst().getStr(Locale.PRIVATE_PROFILE));
                }
                break;

            default:
                break;
        }
    }
    
    public void start() {
        insert(0,waitAnim);
        waitAnim.start();
    }

    public void stop() {
        delete(0);
        waitAnim.updateProgress("");
        waitAnim.stop();
    }

    public void updateProgress(String msg, int perc) {
        waitAnim.updateProgress(msg);
    }

    public void updateProgress(String msg) {
        waitAnim.updateProgress(msg);
    }    
}
