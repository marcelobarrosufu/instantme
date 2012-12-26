/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;

import com.instantme.model.CommentEntryModel;
import com.instantme.util.IAnimation;
import com.instantme.model.PhotoEntryModel;
import com.instantme.model.UserEntryModel;
import com.instantme.entries.FullUserEntry;
import com.instantme.entries.CommentEntry;
import com.instantme.locales.Locale;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class InstagramAPI {
    
    private final String CLIENT_ID = "0dc7bd4b0bce48c08a4c6f5985d31e1a";
    private final String REDIRECT_URI = "file://client-token";
    private final String LOGIN_URL = "https://instagram.com/accounts/login/?next=/oauth/authorize/";
    private final String AUTH_URL = "https://instagram.com/oauth/authorize/";
    private final String DOMAIN = "instagram.com";
    private final String USER_AGENT = "Mozilla/5.0 (Series40)";
    
    private final String API_INSTAGRAM = "https://api.instagram.com/v1";
    private final String GET_SELF_FEED = API_INSTAGRAM + "/users/self/feed";
    private final String EP_MEDIA = API_INSTAGRAM + "/media/";
    private final String EP_USERS = API_INSTAGRAM + "/users/";
    private final String SCOPE_LIKES = "/likes";
    private final String SCOPE_COMMENTS = "/comments";
    
    private Hashtable cookies = new Hashtable();
    private String lastUrl = "";
    private String user = "";
    private String password = "";
    
    private FullUserEntry userInfo = new FullUserEntry();
    
    private boolean logged = false;
    private IAnimation animation = null;
    
    public static InstagramAPI getInstance() {
        return InstagramAPIHolder.INSTANCE;
    }
    
    private static class InstagramAPIHolder {
        private static final InstagramAPI INSTANCE = new InstagramAPI();
    }  
    
    private InstagramAPI() {

    }

    private void updateProgress(String msg) {
        if(animation != null) {
            animation.updateProgress(msg);
        }
    }

    public void setAnimation(IAnimation anim) {
        this.animation = anim;
    }    
    public boolean isLogged() {
        return logged;
    }

    public FullUserEntry getAuthUserInfo() {
        return userInfo;
    }
    
    public String getAuthUserID() {
        return (String) cookies.get("_auth_user_id");
    }
    
    public String getAuthAccessToken() {
        return (String) cookies.get("accessToken");
    }
    
    public boolean tokenLogin(String token, String userID) {
        
        cookies.clear();
        cookies.put("accessToken",token);
        cookies.put("_auth_user_id",userID);
        
        logged = getFullUserInfo(userInfo,getAuthUserID(),null);       
        return logged;
    }
    
    public boolean login(String user, String password, IAnimation anim) {
        
        IHTTPResponseHandler step1 = new IHTTPResponseHandler() {
            public boolean Response30X(HttpConnection connection, Hashtable cookies) {
                return false;
            }

            public boolean Response20X(HttpConnection connection, Hashtable cookies) {
                return cookies.get("csrftoken") != null;
            }

            public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
                return false;
            }
        };
        IHTTPResponseHandler step2 = new IHTTPResponseHandler() {
            public boolean Response30X(HttpConnection connection, Hashtable cookies) {
                if(cookies.get("sessionid") == null) {
                    return false;
                } else {
                    parseUserID();
                    return true;
                }
            }

            public boolean Response20X(HttpConnection connection, Hashtable cookies) {
                return false;
            }

            public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
                return false;
            }
        };
        IHTTPResponseHandler step3 = new IHTTPResponseHandler() {
            public boolean Response30X(HttpConnection connection, Hashtable cookies) {
                return cookies.get("accessToken") != null;
            }

            public boolean Response20X(HttpConnection connection, Hashtable cookies) {
                return true;
            }

            public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
                return false;
            }
        };
        IHTTPResponseHandler step4 = new IHTTPResponseHandler() {
            public boolean Response30X(HttpConnection connection, Hashtable cookies) {
                return cookies.get("accessToken") != null;
            }

            public boolean Response20X(HttpConnection connection, Hashtable cookies) {
                return false;
            }

            public boolean ResponseOthers(HttpConnection connection, Hashtable cookies) {
                return false;
            }
        };
        
        this.user = user;
        this.password = password;        
        // clear all cookies and session information
        cookies.clear();
        lastUrl = "";
        logged = false;
        
        Hashtable getParams = new Hashtable();
        getParams.put("client_id",CLIENT_ID);
        getParams.put("redirect_uri",REDIRECT_URI);
        getParams.put("response_type","token");
        getParams.put("scope","basic+likes+comments+relationships");
        
        println("Step 1");
        setAnimation(anim);
        updateProgress(Locale.getInst().getStr(Locale.LOGIN_1_4));
        logged = HttpLoginRequest(HttpConnection.GET,LOGIN_URL,getParams,null,step1);
        
        if(logged == false) {
            println("Failed at step 1");
            return false;
        }
        
        Hashtable postParams = new Hashtable();
        postParams.put("csrfmiddlewaretoken",cookies.get("csrftoken"));
        postParams.put("username",user);
        postParams.put("password",password);
        postParams.put("scope","basic+likes+comments+relationships");
        
        println("Step 2");
        updateProgress(Locale.getInst().getStr(Locale.LOGIN_2_4));
        logged = HttpLoginRequest(HttpConnection.POST,LOGIN_URL,getParams,postParams,step2);        
        
        if(logged == false) {
            println("Failed at step 2");
            return logged;
        }
        
        println("Step 3");
        updateProgress(Locale.getInst().getStr(Locale.LOGIN_3_4));
        logged = HttpLoginRequest(HttpConnection.GET,AUTH_URL,getParams,null,step3);   
        
        if(logged == false) {
            println("Failed at step 3");
            return logged;
        }
        
        step1 = step2 = step3 = null;
        
        updateProgress(Locale.getInst().getStr(Locale.LOGIN_4_4));
        if(cookies.get("accessToken") == null) {
            postParams.clear();
            postParams.put("csrfmiddlewaretoken",cookies.get("csrftoken"));
            postParams.put("allow","Authorize");
            postParams.put("scope","basic+likes+comments+relationships");

            println("Step 4");
            logged = HttpLoginRequest(HttpConnection.POST,AUTH_URL,getParams,postParams,step4); 

            if(logged == false) {
                println("Failed at step 4");
                return logged;
            }
        }
        
        step4 = null;
        
        // update user info
        getFullUserInfo(userInfo,getAuthUserID(),anim);
        
        return logged;
    }

    public boolean getPrevSelfFeed(PhotoEntryModel photos, IAnimation anim) {
        
        String url = photos.getPrevUrl();
        if(url == null) {
            return getSelfFeed(photos,10, anim);
        }
        
        APIResponse feedHandler = new APIResponse(photos,anim);

        setAnimation(anim);
        updateProgress(Locale.getInst().getStr(Locale.REQUESTING));
         
        //System.out.println("Get Prev Self Feed = " + url);
        boolean result = HttpApiRequest(HttpConnection.GET, url, null, null, feedHandler);

        if (result == false) {
            println("Get Next Self Feed failed");
            //photos = null;
        } else {
            // load images
            loadPhotEntryImages(photos);
        }
        
        return result;
    }

    private boolean getRelations(UserEntryModel users, String relation) {
        
        APIResponse handler = new APIResponse(users);
        String url = EP_USERS + getAuthUserID() + relation;
        Hashtable getParams = new Hashtable();
        getParams.put("access_token", getAuthAccessToken());
        
        boolean result = HttpApiRequest(HttpConnection.GET, url, getParams, null, handler);

        if (result == false) {
            println("getFollows failed");
        }
        
        return result;
    }
    
    public boolean getFollowing(UserEntryModel users) {
        return getRelations(users,"/follows");
    }
    
    public boolean getFollowedBy(UserEntryModel users) {
        return getRelations(users,"/followed-by");
    }
    
    public boolean getNextSelfFeed(PhotoEntryModel photos, IAnimation anim) {
        
        String url = photos.getNextUrl();
        if(url == null) {
            return getSelfFeed(photos,10, anim);
        }
        
        APIResponse feedHandler = new APIResponse(photos,anim);

        setAnimation(anim);
        updateProgress(Locale.getInst().getStr(Locale.REQUESTING));
        //System.out.println("Get Next Self Feed = " + url);
        boolean result = HttpApiRequest(HttpConnection.GET, url, null, null, feedHandler);

        if (result == false) {
            println("Get Next Self Feed failed");
            //photos = null;
        } else {
            // load images
            loadPhotEntryImages(photos);
        }
        
        return result;
    }
    
    public boolean getSelfFeed(PhotoEntryModel photos, int count, IAnimation anim) {
        
        APIResponse handler = new APIResponse(photos,anim);
        
        photos.clearHistory();
        Hashtable getParams = new Hashtable();
        getParams.put("access_token", getAuthAccessToken());
        getParams.put("count", Integer.toString(count));

        println("Get Self Feed");
        setAnimation(anim);
        updateProgress(Locale.getInst().getStr(Locale.REQUESTING));
        
        boolean result = HttpApiRequest(HttpConnection.GET, GET_SELF_FEED, getParams, null, handler);

        if (result == false) {
            println("Get Self Feed failed");
            //photos = null;
        } else {
            // load images
            loadPhotEntryImages(photos);
        }
        
        return result;
    }
    
    public boolean getComments(CommentEntryModel comments) {
        APIResponse handler = new APIResponse(comments);
        
        Hashtable getParams = new Hashtable();
        getParams.put("access_token", getAuthAccessToken());

        println("Get comments for media");
        String url = EP_MEDIA + comments.getMediaID() + SCOPE_COMMENTS;
        boolean result = HttpApiRequest(HttpConnection.GET, url, getParams, null, handler);

        if (result == false) {
            println("Get comments failed");
        }
        
        return result;        
    }
    
    public boolean addComment(String mediaID, CommentEntry comments) {
        SimpleAPIResponse handler = new SimpleAPIResponse();

        Hashtable params = new Hashtable();
        params.put("access_token", getAuthAccessToken());
        params.put("text",comments.getComment());

        println("Add comment for media " + mediaID + ": " + comments.getComment());
        String url = EP_MEDIA + mediaID + SCOPE_COMMENTS;
        boolean result = HttpApiRequest(HttpConnection.POST, url, null, params, handler);

        if (result == false) {
            println("Add comment failed");
        }
        
        return result;        
    }
    
    public boolean delComment(String mediaID, String commentID) {
        SimpleAPIResponse handler = new SimpleAPIResponse();

        Hashtable params = new Hashtable();
        //params.put("access_token", getAccessToken());
        params.put("_method","DELETE");

        Hashtable gparams = new Hashtable();
        gparams.put("access_token", getAuthAccessToken());
        //params.put("_method","DELETE");
        
        println("Del comment for media " + mediaID);
        String url = EP_MEDIA + mediaID + SCOPE_COMMENTS + "/" + commentID;
        boolean result = HttpApiRequest(HttpConnection.POST, url, gparams, params, handler);

        if (result == false) {
            println("del comment failed");
        }
        
        return result;        
    }
    
    public boolean getFullUserInfo(FullUserEntry user, String userID, IAnimation anim) {
        
        APIResponse handler = new APIResponse(user);
        
        Hashtable getParams = new Hashtable();
        getParams.put("access_token", getAuthAccessToken());

        setAnimation(anim);
        updateProgress(Locale.getInst().getStr(Locale.DOWNLD_USR_INFO));
        println("Get user info");
        String url = EP_USERS + userID;
        boolean result = HttpApiRequest(HttpConnection.GET, url, getParams, null, handler);

        if (result == false) {
            println("Get user info failed");
        }
        else {
            updateProgress(Locale.getInst().getStr(Locale.DOWNLD_USR_PICT));
            loadThumbImage(user);
        }

        return result;
    }
    
    public boolean setLike(String mediaID, boolean like) {
        SimpleAPIResponse handler = new SimpleAPIResponse();
        
        Hashtable params = new Hashtable();
        params.put("access_token", getAuthAccessToken());

        // not supporting dislike at this momment since httpconnection does not have
        // del method. Is must be implemented using TCP
        if(like == false) {
            params.put("_method","DELETE");
        }
        
        println("Set like for media " + mediaID + " as " + like);
        String url = EP_MEDIA + mediaID + SCOPE_LIKES;
        boolean result = HttpApiRequest(HttpConnection.POST, url, null, params, handler);

        if (result == false) {
            println("set comments failed");
        }
        
        return result;        
    }    
    
    // http://www.java2s.com/Tutorial/Java/0430__J2ME/LoadImagewithHttpConnection.htm

    private void loadPhotEntryImages(PhotoEntryModel pea) {
        int numElements = pea.size();
        for (int n = 0; n < numElements; n++) {
            updateProgress( Locale.getInst().getStr(Locale.DOWNLD_PHOTO) + String.valueOf(n+1) + "/" + String.valueOf(numElements) + " ..." );
            loadThumbImage(pea.elementAt(n));
        }
    }
    
    private void loadThumbImage(IImageDownload pe) {
        //System.out.println("Get Thumb Image");
        if(!HttpApiRequest(HttpConnection.GET, pe.getImageURL(), null, null, new DownloadImageResponse(pe))) {
            pe.setImage(null);
        }
    }
    

    private boolean HttpApiRequest(
            String requestType,
            String url, 
            Hashtable getParams, 
            Hashtable postParams,
            IHTTPResponseHandler handler) {
        
        boolean result = false;
        
        // process get params
        if(getParams != null) {
            url += "?" + encodeUrl(getParams);
        }
        
        // process post params
        byte[] postData = null;
        int postDataSize = 0;
        if(postParams != null) {
            try {        
                postData = encodeUrl(postParams).getBytes("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                postData = encodeUrl(postParams).getBytes();
            }
            postDataSize = postData.length;
            println("Post data size = " + postDataSize);
        }
        
        //System.out.println("Page requested: " + url);
        
        HttpConnection connection = null;
        
        try {
            
            connection = (HttpConnection) Connector.open(url,Connector.READ_WRITE,true);
            connection.setRequestMethod(requestType);
            
            // default headers
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Connection", "close");
            
            if(requestType.equalsIgnoreCase(HttpConnection.POST)) {
                connection.setRequestProperty("Content-Length", Integer.toString(postDataSize));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream outputStream = connection.openOutputStream();
                outputStream.write(postData);
                outputStream.flush();
                outputStream.close();
            }
            
            int code = connection.getResponseCode();
            //System.out.println("Response code = " + code);

            if(code == HttpConnection.HTTP_OK) {
                result = handler.Response20X(connection,null);
            } else if(code / 10 == 30) {
                url = connection.getHeaderField("Location");
                // should I test if url is null ? 30x code should always have a Location header ...
                println("Redirect to " + url);
                result = handler.Response30X(connection,null);
            } else {                
                result = handler.ResponseOthers(connection,null);
            }
            
            connection.close();
            connection = null;
            postData = null;
        }
        catch(IOException error) {
            System.out.println("IOException: " + error.toString());
        }
        
        return result;
    }

    
    private boolean HttpLoginRequest(
            String requestType,
            String url, 
            Hashtable getParams, 
            Hashtable postParams,
            IHTTPResponseHandler handler) {
        
        boolean result = false;
        
        // process get params
        if(getParams != null) {
            url += "?" + encodeUrl(getParams);
        }
        
        // process post params
        byte[] postData = null;
        int postDataSize = 0;
        if(postParams != null) {
            postData = encodeUrl(postParams).getBytes();        
            postDataSize = postData.length;
            println("Post data size = " + postDataSize);
        }
        
        println("Page requested: " + url);
        
        HttpConnection connection = null;
        
        try {
            
            // adding available cookies
            String cks = "";
            if(cookies.containsKey("csrftoken")) {
                cks += "csrftoken=" + cookies.get("csrftoken");
            }
            if(cookies.containsKey("sessionid")) {
                cks += "; " + "sessionid=" + cookies.get("sessionid");
            }
            //println("Cookies: " + cks);
            
            connection = (HttpConnection) Connector.open(url,Connector.READ_WRITE,true);
            connection.setRequestMethod(requestType);
            
            // defafault headers
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("Host", DOMAIN);
            connection.setRequestProperty("Accept-Encoding","identity");
            connection.setRequestProperty("Cookie", cks);
            connection.setRequestProperty("Referer", lastUrl);
            
            if(requestType.equalsIgnoreCase(HttpConnection.POST)) {
                connection.setRequestProperty("Content-Length", Integer.toString(postDataSize));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream outputStream = connection.openOutputStream();
                outputStream.write(postData);
                outputStream.flush();
                outputStream.close();
            }
            
            // save for referer header
            lastUrl = url;
            int code = connection.getResponseCode();
            println("Response code = " + code);

            updateCookies(connection);
            
            if(code == HttpConnection.HTTP_OK) {
                result = handler.Response20X(connection,cookies);
            } else if(code / 10 == 30) {
                url = connection.getHeaderField("Location");
                // should I test if url is null ? 30x code should always have a Location header ...
                println("Redirect to " + url);
                updateToken(url);
                result = handler.Response30X(connection,cookies);
            } else {                
                result = handler.ResponseOthers(connection,cookies);
            }
            
            connection.close();
            connection = null;
            postData = null;
        }
        catch(IOException error) {
            println("IOException: " + error.toString());
        }
        
        return result;
    }

    private void parseUserID() {
        String sessionid = (String) cookies.get("sessionid");
        sessionid = decodeUrl(sessionid);
        int a = sessionid.indexOf('{');
        int b = sessionid.indexOf('}');
        
        try {
            sessionid = sessionid.substring(a,b+1);
            JSONObject jobj = new JSONObject(sessionid);
            String id = jobj.getString("_auth_user_id");
            cookies.put("_auth_user_id",id);
        } catch (JSONException ex) {
            cookies.put("_auth_user_id","");
        }
    }
    
    private void updateCookies(HttpConnection connection) throws IOException {
        
        String cookieHeader = connection.getHeaderField("Set-Cookie");
        
        if(cookieHeader != null) {
            if(cookieHeader.length() > 0) {
                //println(cookieHeader);
                Vector cookieList = split(cookieHeader,";");
                for (int n = 0; n < cookieList.size(); n++) {
                    Vector cks = split((String)cookieList.elementAt(n),"=");
                    if(cks.size() == 2) {
                        String key = ((String)cks.elementAt(0)).trim();
                        String value = ((String)cks.elementAt(1)).trim();
                        //println("Adding cookie " + key + "=" + value);
                        cookies.put(key,value);
                    } else {
                        println("Problem in cookie " + cks);
                    }

                }
            }
        }
    }    
    
    private boolean updateToken(String tokenUrl) {
        boolean result = false;
        if(tokenUrl.startsWith(REDIRECT_URI)){
            Vector v = split(tokenUrl, "=");
            if (v.size() == 2) {
                String accessToken = (String) v.elementAt(1);
                if (accessToken.length() > 0) {
                    println("Access token = " + accessToken);
                    cookies.put("accessToken",accessToken);
                    result = true;
                }
            }
        }
        return result;
    }
    
    private Vector split(String str, String sep) {
        Vector tokens = new Vector();
        int pos = 0;
        int lastPos = 0;
        pos = str.indexOf(sep,lastPos);
        while(pos != -1) {
            tokens.addElement(str.substring(lastPos,pos));
            lastPos = pos + 1;
            pos = str.indexOf(sep,lastPos);
            if(pos == -1) {
                tokens.addElement(str.substring(lastPos,str.length()));
            }
        }
        
        return tokens;
    }    
    
    private String encodeUrl(Hashtable params) {

        StringBuffer encodedUrl = new StringBuffer();

        for (Enumeration elements = params.keys(); elements.hasMoreElements();) {
            String key = (String) elements.nextElement();
            String value = (String) params.get(key);
            //println("encodeUrl: " + key + "=" + value);
            encodedUrl.append(key);
            encodedUrl.append("=");
            encodedUrl.append(escapeUrl(value));
            encodedUrl.append("&");
        }
        // remove last "&"
        if(encodedUrl.length() > 0) {
            encodedUrl.deleteCharAt(encodedUrl.length() - 1);
        }
        
        return encodedUrl.toString();
    }
    
    private void println(String url) {

        if(false) {
            StringBuffer escapedStr = new StringBuffer();

            // we have a strange problem here when using system.out.println 
            // with some chars
            for (int i = 0; i < url.length() & i < 60; i++) {
                char c = url.charAt(i);
                if(c == '%') {
                    escapedStr.append("%");
                    escapedStr.append("%");
                } else if(c == '\\') {
                    escapedStr.append("\\");
                    escapedStr.append("\\");
                } else {            
                    escapedStr.append(c);
                }
            }

            //System.out.println(escapedStr.toString());
        }
    }
    
    private String decodeUrl(String url) {
        StringBuffer str = new StringBuffer();
        
        for(int i = 0 ; i < url.length(); i++) {
            char c = url.charAt(i);
            if((c == '%') && ((i + 2) < url.length())) {
                String s = url.substring(i+1, i+3);
                Integer v = Integer.valueOf(s, 16);
                char cc = (char)v.intValue();
                str.append(cc);//String.valueOf((char)v.intValue()));
                i += 2;
            } else {
                str.append(c);
            }
        }
        
        return str.toString();
    }
    
    private String escapeUrl(String url) {
        
        StringBuffer escapedUrl = new StringBuffer();

        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            switch (c) {
                case ' ':
                    escapedUrl.append("%20");
                    break;
                case '/':
                    escapedUrl.append("%2F");
                    break;
                case '<':
                    escapedUrl.append("%3C");
                    break;
                case '>':
                    escapedUrl.append("%3E");
                    break;
                case ':':
                    escapedUrl.append("%3A");
                    break;
               //case '-':
               //     escapedUrl.append("%2D");
               //     break;
                default:
                    escapedUrl.append(c);
                    break;
            }
        }
        
        return escapedUrl.toString();
    }
       
}
