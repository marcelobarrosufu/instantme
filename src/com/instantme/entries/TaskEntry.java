/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.entries;

public class TaskEntry {
    private int ID;
    private Object param;
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public Object getParam() {
        return param;
    }
    public void setParam(Object param) {
        this.param = param;
    }
    public TaskEntry(int ID, Object param) {
        this.ID = ID;
        this.param = param;
    }
    public TaskEntry(int ID) {
        this(ID,"");
    }

    public String toString() {
        return "TaskEntry{" + "ID=" + ID + ", param=" + param + '}';
    }
}
