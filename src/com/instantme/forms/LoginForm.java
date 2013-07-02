package com.instantme.forms;


import com.instantme.util.BackStack;
import com.instantme.model.DataModel;
import com.instantme.util.IAnimation;
import com.instantme.api.InstagramAPI;
import com.instantme.util.TaskHelper;
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
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

public class LoginForm extends Form implements CommandListener, Runnable, IAnimation {

    private Command loginCommand;
    private Command cancelCommand;
    private TextField username;
    private TextField password;
    private TaskHelper tasks = null;
    
    private final int TEXT_SIZE = 42;
    private final static int TASK_TRY_LOGIN = 0;
    
    private WaitItem waitAnim = null;
    private boolean logged = false;
    
    public LoginForm() {
        super("Instagram Login");
        
        tasks = new TaskHelper(this,this);
        waitAnim = new WaitItem();

        username = new TextField(Locale.getInst().getStr(Locale.USERNAME),"",TEXT_SIZE,TextField.ANY);
        username.setLayout(Item.LAYOUT_EXPAND );
        append(username);

        password = new TextField(Locale.getInst().getStr(Locale.PASSWORD),"",TEXT_SIZE,TextField.PASSWORD);
        password.setLayout(Item.LAYOUT_EXPAND);
        append(password);

        StringItem info = new StringItem(Locale.getInst().getStr(Locale.DISCLAIMER),Locale.getInst().getStr(Locale.DISCLAIMER_NOTE));
        info.setLayout(Item.LAYOUT_EXPAND);
        append(info);
        
        loginCommand = new Command(Locale.getInst().getStr(Locale.LOGIN_MENU), Command.OK, 0);
        cancelCommand = new Command(Locale.getInst().getStr(Locale.CANCEL_MENU), Command.BACK, 0);
        
        addCommand(loginCommand);
        addCommand(cancelCommand);
        setCommandListener(this);
        
    }

    private void saveCredentials() {
        InstagramAPI oai = InstagramAPI.getInstance();
        DataModel dm = DataModel.getInstance();
        dm.setToken(oai.getAuthAccessToken());
        dm.setUserID(oai.getAuthUserID());
        
        dm.saveLoginCredentials();
    }

    private void showAlert(String title, String msg) {
        BackStack bs = BackStack.getInstance();
        Alert a = new Alert(title);
        a.setString(msg);
        a.setType(AlertType.INFO);
        bs.getCurrentDisplay().setCurrent(a);             
    }
    
    public void commandAction(Command c, Displayable d) {
        if(tasks.isRunning()) {
            showAlert(Locale.getInst().getStr(Locale.WAIT),Locale.getInst().getStr(Locale.WAIT_OPERATION));
        } else {
            if (c == cancelCommand) {
                BackStack bs = BackStack.getInstance();
                bs.back();
            } else if (c == loginCommand) {
                username.setString(username.getString().trim());
                password.setString(password.getString().trim());
                if (username.getString().length() > 0 && password.getString().length() > 0) {
                    tasks.push(new TaskEntry(TASK_TRY_LOGIN));
                } else {
                    showAlert(Locale.getInst().getStr(Locale.ERROR),Locale.getInst().getStr(Locale.ERROR_USR_PWD_EMPTY));
                }
            }            
        }
    }

    public void run() {
        InstagramAPI oai = InstagramAPI.getInstance(); 
        TaskEntry te = (TaskEntry) tasks.pop();

        switch(te.getID()) {
            case TASK_TRY_LOGIN:
                updateProgress(Locale.getInst().getStr(Locale.LOGGING));
                if (oai.login(username.getString(), password.getString(),this)) {
                    saveCredentials();
                    showAlert(Locale.getInst().getStr(Locale.SUCCESS),Locale.getInst().getStr(Locale.SUCCESS_UPDT_TIMELINE));
                    logged = true;
                }                
                else {
                    showAlert(Locale.getInst().getStr(Locale.FAILED),Locale.getInst().getStr(Locale.FAILED_CHECK_USR_PWD_NET));
                    logged = false;
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
        if(logged) {
            synchronized (this) {
                try {
                    wait(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            BackStack bs = BackStack.getInstance();
            bs.back();
        }
    }

    public void updateProgress(String msg, int perc) {
        waitAnim.updateProgress(msg);
    }

    public void updateProgress(String msg) {
        waitAnim.updateProgress(msg);
    }      
}
