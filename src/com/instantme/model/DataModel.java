/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
/* 
 * Based on Nokia DataModel class fro Worpress project
 * 
 * Copyright © 2011 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners. 
 * See LICENSE.TXT for license information.
 */ 

package com.instantme.model;

import javax.microedition.rms.RecordStore;

/**
 * Contains the data used in the application (session id and user id)
 */
public class DataModel {

    private static final String DB_NAME = "instantme";
    private static final int RECORD_TOKEN = 1;
    private static final int RECORD_USER_ID = 2;

    private String token = "";
    private String userID = "";

    public static DataModel getInstance() {
        return DataModelHolder.INSTANCE;
    }
    
    private static class DataModelHolder {
        private static final DataModel INSTANCE = new DataModel();
    }  
    
    private DataModel() {
        loadLoginCredentials();
    }
    
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String sessionID) {
        this.token = sessionID;
    }

    /**
     * Load login credentials from the record store.
     */
    public void loadLoginCredentials() {
        RecordStore store = null;
        try {
            //RecordStore.deleteRecordStore(DB_NAME);
            
            store = RecordStore.openRecordStore(DB_NAME, true);

            if (store.getNextRecordID() == 1) {
                //System.out.println("init");
                byte[] bytes = token.getBytes();
                store.addRecord(bytes, 0, bytes.length);
                bytes = userID.getBytes();
                store.addRecord(bytes, 0, bytes.length);
            }

            byte[] buffer = new byte[128];

            int length = store.getRecord(RECORD_TOKEN, buffer, 0);
            String string = new String(buffer, 0, length);
            token = string;

            length = store.getRecord(RECORD_USER_ID, buffer, 0);
            string = new String(buffer, 0, length);
            userID = string;
            
        } catch (javax.microedition.rms.RecordStoreException ex) {
            System.out.println("loadSettings " + ex.getMessage());
        } finally {
            if (store != null) {
                try {
                    store.closeRecordStore();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Save login credentials the record store.
     */
    public void saveLoginCredentials() {
        RecordStore store = null;
        try {
            store = RecordStore.openRecordStore(DB_NAME, true);

            byte[] bytes = token.getBytes();
            store.setRecord(RECORD_TOKEN, bytes, 0, bytes.length);

            bytes = userID.getBytes();
            store.setRecord(RECORD_USER_ID, bytes, 0, bytes.length);
            
        } catch (javax.microedition.rms.RecordStoreException ex) {
            System.out.println("saveSettings " + ex.getMessage());
        } finally {
            if (store != null) {
                try {
                    store.closeRecordStore();
                } catch (Exception ex) {
                }
            }
        }
    }
}
