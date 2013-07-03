/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.forms;

import com.instantme.api.InstagramAPI;
import com.instantme.entries.CommentEntry;
import com.instantme.entries.PhotoEntry;
import com.instantme.entries.TaskEntry;
import com.instantme.items.WaitItem;
import com.instantme.locales.Locale;
import com.instantme.model.CommentEntryModel;
import com.instantme.util.BackStack;
import com.instantme.util.DisplaySpecs;
import com.instantme.util.IAnimation;
import com.instantme.util.TaskHelper;
import com.nokia.mid.ui.IconCommand;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import tube42.lib.imagelib.ImageUtils;

public class PhotoEntryForm extends Form implements CommandListener, ItemCommandListener, Runnable, IAnimation  {

    // TODO verificar que elementos colocar como final
    private TextBox inputBox;
    private List commentList;
    private Command inputBoxOk;
    private Command dlgCancel;
    private Command commentListOk;
    private Command backCommand;
    private IconCommand likeCommand;
    private Command addComment;
    private Command delComment;
    private Command clearText;
    private PhotoEntry photoEntry;
    // TODO should I add this model to pe ?
    private CommentEntryModel commentsModel = null;
    private TaskHelper tasks = null;
    private Vector deleteMap;
    private StringItem likes;
    private StringItem comments;
    private WaitItem waitAnim = null;
    
    private final static int TASK_UPDATE_COMMENTS = 0;
    private final static int TASK_UPDATE_LIKES = 1;
    private final static int TASK_ADD_COMMENT = 2;
    private final static int TASK_ADD_LIKE = 3;
    private final static int TASK_DEL_LIKE = 4;
    private final static int TASK_DEL_COMMENT = 5;
    private final static int TASK_RESIZE_IMAGE = 6;
    
    private final int MAX_LIST_ITEM_SIZE = 60;
    
    public PhotoEntryForm(PhotoEntry pe) {
        super("");

        waitAnim = new WaitItem();
        deleteMap = new Vector();
        this.photoEntry = pe;
        
        Image image = null;        
        try {
            image = Image.createImage("/res/likeshb.png");
            likeCommand = new IconCommand(Locale.getInst().getStr(Locale.LIKE_MENU), image, null, Command.OK, 0);
        }
        catch (Exception oe) {    
            likeCommand = new IconCommand(Locale.getInst().getStr(Locale.LIKE_MENU), Command.OK, 0, IconCommand.ICON_OK);
        }
        
        StringItem caption = new StringItem(pe.getFrom().getName() + " (" + pe.getDate().toString() + ")",pe.getCaption());
        Font f = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        caption.setFont(f);
        caption.setLayout(Item.LAYOUT_EXPAND);
        this.append(caption);
        
        String lk;
        if(pe.isUserHasLiked()) {
            lk = Locale.getInst().getStr(Locale.LIKES_YOU);
        }
        else {
            lk = Locale.getInst().getStr(Locale.LIKES);
        }
                    
        likes = new StringItem(lk,String.valueOf(pe.getNumLikes()),Item.BUTTON);
        likes.setFont(f);
        likes.setLayout(Item.LAYOUT_EXPAND);
        this.append(likes);
        
        int nc = pe.getNumComments();
        commentsModel = new CommentEntryModel();
        commentsModel.setMediaID(photoEntry.getMediaID());            
        
        comments = new StringItem(Locale.getInst().getStr(Locale.COMMENTS),String.valueOf(pe.getNumComments()),Item.BUTTON);
        comments.setFont(f);
        comments.setLayout(Item.LAYOUT_EXPAND);
        this.append(comments);
                
        backCommand = new Command(Locale.getInst().getStr(Locale.BACK_MENU), Command.BACK, 0); 
        //likeCommand = new Command(Locale.getInst().getStr(Locale.LIKE_MENU), Command.OK, 0); 
        addComment = new Command(Locale.getInst().getStr(Locale.ADD_CMT_MENU), Command.ITEM, 0); 
        delComment = new Command(Locale.getInst().getStr(Locale.DEL_CMT_MENU), Command.ITEM, 1); 
        
        commentListOk = new Command(Locale.getInst().getStr(Locale.DELETE_MENU), Command.OK, 0); 
        inputBoxOk = new Command(Locale.getInst().getStr(Locale.SEND_MENU), Command.OK, 0); 
        dlgCancel = new Command(Locale.getInst().getStr(Locale.CANCEL_MENU), Command.BACK, 1); 
        clearText = new Command(Locale.getInst().getStr(Locale.CLEAR_MENU), Command.ITEM, 2); 
               
        setCommandListener(this);        
        this.addCommand(backCommand);
        this.addCommand(likeCommand);
        this.addCommand(addComment);
        this.addCommand(delComment);

        tasks = new TaskHelper(this,this);     
        tasks.push(new TaskEntry(TASK_RESIZE_IMAGE));
        if(nc > 0) {
            tasks.push(new TaskEntry(TASK_UPDATE_COMMENTS));
        }
    
    }

    private void handleDeleteComment() {
        // user can delete your own comments or any comment added to his pictures
        InstagramAPI oai = InstagramAPI.getInstance();
        String me = oai.getAuthUserID();
        int nc = commentsModel.size();
        boolean isPhotoOwner = photoEntry.getFrom().getUserID().equals(me);
        deleteMap.removeAllElements();
        if(nc > 0) {
            Font f = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
            commentList = new List(Locale.getInst().getStr(Locale.COMMENTS),List.MULTIPLE);
            commentList.setFitPolicy(List.TEXT_WRAP_ON);
            for(int n = 0, m = 0 ; n < nc ; n++) {
                    CommentEntry ce = commentsModel.elementAt(n);
                    boolean isCommentOwner = ce.getFrom().getUserID().equals(me);
                    if(isPhotoOwner || isCommentOwner) {                        
                        StringBuffer item = new StringBuffer();
                        
                        item.append(ce.getFrom().getName());
                        item.append(":\n");
                        
                        String cmt = ce.getComment();
                        if(cmt.length() > MAX_LIST_ITEM_SIZE) {
                            item.append(cmt.substring(0,MAX_LIST_ITEM_SIZE));
                            item.append(" ...");
                        }
                        else {
                            item.append(cmt);
                        }
                        
                        commentList.append(item.toString(), null);
                        commentList.setFont(m,f);
                        // we need to save the original position in the comment list
                        deleteMap.addElement(new Integer(n));
                        m++;
                    }

            }
            if(commentList.size() > 0) {
                commentList.addCommand(commentListOk);
                commentList.addCommand(dlgCancel);
                commentList.setCommandListener(this);

                BackStack bs = BackStack.getInstance();
                bs.forward(commentList);     
            }
            else {
                showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_CAN_NOT_DEL_CMT));
            }
        }
        else {
            showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_NO_CMT_TO_DEL));
        }
            
    }
    
    private void showAlert(String title, String msg) {
        BackStack bs = BackStack.getInstance();
        Alert a = new Alert(title);
        a.setString(msg);
        a.setType(AlertType.INFO);
        bs.getCurrentDisplay().setCurrent(a);             
    }
    
    public void commandAction(Command c, Displayable d) {
        BackStack bs = BackStack.getInstance();
        if (tasks.isRunning()) {
            showAlert(Locale.getInst().getStr(Locale.WAIT),Locale.getInst().getStr(Locale.WAIT_OPERATION));
        } else {
            if (c == backCommand) {
                bs.back();
            } else if (c == likeCommand) {
                if (photoEntry.isUserHasLiked() == false) {
                    tasks.push(new TaskEntry(TASK_ADD_LIKE));
                } else {
                    tasks.push(new TaskEntry(TASK_DEL_LIKE));
                }
            } else if (c == addComment) {
                if (inputBox == null) {
                    inputBox = new TextBox(Locale.getInst().getStr(Locale.COMMENT), "", 1024, TextField.ANY);
                    inputBox.addCommand(inputBoxOk);
                    inputBox.addCommand(dlgCancel);
                    inputBox.addCommand(clearText);
                    inputBox.setCommandListener(this);
                }
                bs.forward(inputBox);
            } else if (c == clearText) {
                inputBox.setString("");
            } else if (c == delComment) {
                handleDeleteComment();
            } else if (c == dlgCancel) {
                bs.back();
            } else if (c == inputBoxOk) {
                String str = inputBox.getString();
                str = str.trim();
                if (str.length() > 0) {
                    tasks.push(new TaskEntry(TASK_ADD_COMMENT, str));
                }
                bs.back();
            } else if (c == commentListOk) {
                boolean changed = false;
                for (int n = 0; n < commentList.size(); n++) {
                    if (commentList.isSelected(n)) {
                        changed = true;
                        int m = ((Integer) deleteMap.elementAt(n)).intValue();
                        tasks.push(new TaskEntry(TASK_DEL_COMMENT, new Integer(m)));
                    }
                }
                if (changed) {
                    tasks.push(new TaskEntry(TASK_UPDATE_COMMENTS));
                }
                bs.back();
            }
        }
    }

    public void commandAction(Command c, Item item) {
    }

    private synchronized void clearComments() {
        commentsModel.removeAllElements();
        for(int n = size() - 1 ; n >=6  ; n--) {
            delete(n);
        }
    }

    public void run() {
        InstagramAPI oai = InstagramAPI.getInstance();
        Font f = Font.getFont(DisplaySpecs.FONT_DEFAULT, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        TaskEntry te = (TaskEntry) tasks.pop();

        switch(te.getID()) {
            
            case TASK_RESIZE_IMAGE:
            {
                String imgCap = "";
                Image image;
                int s = DisplaySpecs.getFullImageSize();
                try {
                    image = ImageUtils.resize(photoEntry.getImage(),s,s,true,false);
                } catch (Exception ex) {
                    image = null;
                }  

                if(image == null) {
                    try {
                        image = ImageUtils.resize(Image.createImage("/res/warning.png"),s,s,true,false);
                        imgCap = Locale.getInst().getStr(Locale.ERROR_DOWNLOADING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }             
                }        

                ImageItem imageItem = new ImageItem(imgCap,image,Item.LAYOUT_DEFAULT,"");
                imageItem.setLayout(Item.LAYOUT_EXPAND | Item.LAYOUT_CENTER); // Center the image
                insert(1,imageItem);
                
                break;
            }
            case TASK_UPDATE_COMMENTS:
                clearComments();
                updateProgress(Locale.getInst().getStr(Locale.UPDATING_CMTS));
                if(oai.getComments(commentsModel)) {
                    int nc = commentsModel.size();
                    for (int n = 0; n < nc; n++) {
                        CommentEntry ce = commentsModel.elementAt(n);
                        String u = ce.getFrom().getName();
                        StringItem c = new StringItem(u, ce.getComment(), Item.BUTTON);
                        c.setFont(f);
                        c.setLayout(Item.LAYOUT_EXPAND);
                        append(c);
                    }
                    comments.setText(String.valueOf(nc));
                    photoEntry.setNumComments(nc);
                } else {
                        showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_CAN_NOT_LOAD_CMTS));
                }
                break;

            case TASK_ADD_LIKE:
                
                updateProgress(Locale.getInst().getStr(Locale.ADDING_LIKE));
                if(oai.setLike(photoEntry.getMediaID(), true) == true) {
                    // TODO: local update only ... may be a problem
                    photoEntry.setNumLikes(photoEntry.getNumLikes() + 1);
                    photoEntry.setUserHasLiked(true);
                    likes.setLabel(Locale.getInst().getStr(Locale.LIKES_YOU));
                }
                likes.setText(String.valueOf(photoEntry.getNumLikes()));
                break;

            case TASK_DEL_LIKE:

                updateProgress(Locale.getInst().getStr(Locale.REMOVING_LIKE));
                if(oai.setLike(photoEntry.getMediaID(), false) == true) {
                    // TODO: local update only ... may be a problem
                    int nl = photoEntry.getNumLikes() - 1;
                    nl = nl < 0 ? 0 : nl;
                    photoEntry.setNumLikes(nl);
                    photoEntry.setUserHasLiked(false);
                    likes.setLabel(Locale.getInst().getStr(Locale.LIKES));
                }
                likes.setText(String.valueOf(photoEntry.getNumLikes()));
                break;

            case TASK_ADD_COMMENT:

                updateProgress(Locale.getInst().getStr(Locale.ADDING_CMT));
                String msg = (String) te.getParam();
                CommentEntry ce = new CommentEntry();
                ce.setComment(msg);
                ce.setFrom(oai.getAuthUserInfo());
                if(oai.addComment(photoEntry.getMediaID(),ce) == false) {
                    showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_WHEN_ADD_CMT));
                }
                else {
                    tasks.push(new TaskEntry(TASK_UPDATE_COMMENTS));
                }
                break;

            case TASK_DEL_COMMENT:
                
                updateProgress(Locale.getInst().getStr(Locale.DELETING_CMTS));
                int n = ((Integer) te.getParam()).intValue();
                String commentID = commentsModel.elementAt(n).getCommentID();
                if(oai.delComment(photoEntry.getMediaID(),commentID) == false) {
                    showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_WHEN_DEL_CMT));
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

