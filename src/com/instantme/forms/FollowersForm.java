/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.forms;

import com.instantme.util.BackStack;
import com.instantme.util.IAnimation;
import com.instantme.api.InstagramAPI;
import com.instantme.util.TaskHelper;
import com.instantme.model.UserEntryModel;
import com.instantme.items.CustomStringItem;
import com.instantme.entries.UserEntry;
import com.instantme.entries.TaskEntry;
import com.instantme.items.WaitItem;
import com.instantme.locales.Locale;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;

public class FollowersForm extends Form implements CommandListener, Runnable, IAnimation, ItemCommandListener {
   
    private UserEntryModel model;

    private TaskHelper tasks = null;
    private WaitItem waitAnim = null;
    private int type;
    
    private final static int TASK_GET_FOLLOWING = 0;
    private final static int TASK_GET_FOLLOWEDBY = 1;
    private Command cancelCommand;
    private Command updateCommand;
    
    public final static int FOLLOWING = 0;
    public final static int FOLLOWEDBY = 1;
    
    public FollowersForm(String title, int type) {
        super(title);
    
        this.type = type;
        model = new UserEntryModel();
        waitAnim = new WaitItem();
        
        tasks = new TaskHelper(this,this);
        
        cancelCommand = new Command(Locale.getInst().getStr(Locale.BACK_MENU), Command.BACK, 0);
        updateCommand = new Command(Locale.getInst().getStr(Locale.UPDATE_MENU), Command.OK, 1);
        
        addCommand(cancelCommand); 
        addCommand(updateCommand);
        
        setCommandListener(this);
        
        updateAction();
    }
        
    private void updateAction() {
        if(type == FOLLOWING) {
            tasks.push(new TaskEntry(TASK_GET_FOLLOWING));
        }
        else {
            tasks.push(new TaskEntry(TASK_GET_FOLLOWEDBY));
        }        
    }

    private void updateList() {
        if (model.size() > 0) {
            for (int n = 0; n < model.size(); n++) {
                UserEntry ue = model.elementAt(n);
                //System.out.println("-"+ue.getName()+"-");
                CustomStringItem item = new CustomStringItem("",ue.getName(),Item.HYPERLINK);
                item.setPrivateData(model.elementAt(n));
                item.setItemCommandListener(this);
                item.setDefaultCommand(new Command(String.valueOf(n),Command.ITEM,n));
                item.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_NEWLINE_BEFORE);
                append(item);
            }
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        BackStack bs = BackStack.getInstance();
        if(tasks.isRunning()){
            showAlert(Locale.getInst().getStr(Locale.WAIT),Locale.getInst().getStr(Locale.WAIT_OPERATION));
        } else{
            if (c == cancelCommand) {
                bs.back();
            }
            else if (c == updateCommand) {
                deleteAll();
                updateAction();
            }
        }
    }
    
    public void commandAction(Command c, Item item) {
        BackStack bs = BackStack.getInstance();
        UserEntry ue = (UserEntry) ((CustomStringItem) item).getPrivateData();
        UserEntryForm f = new UserEntryForm(Locale.getInst().getStr(Locale.USER_INFO), ue);
        bs.forward(f);
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
            case TASK_GET_FOLLOWING:
                updateProgress(Locale.getInst().getStr(Locale.UPDATE_FOLLOWING));
                if(oai.getFollowing(model)) {
                    updateList();
                }
                else {
                    showAlert(Locale.getInst().getStr(Locale.FAILED),Locale.getInst().getStr(Locale.CAN_NOT_UPDATE));
                }
                break;

            case TASK_GET_FOLLOWEDBY:
                updateProgress(Locale.getInst().getStr(Locale.UPDATE_FOLLOWED));
                if(oai.getFollowedBy(model)) {
                    updateList();
                }
                else {
                    showAlert(Locale.getInst().getStr(Locale.FAILED),Locale.getInst().getStr(Locale.CAN_NOT_UPDATE));
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
