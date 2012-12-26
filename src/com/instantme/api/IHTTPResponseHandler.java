/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.api;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;

public interface IHTTPResponseHandler {
    public boolean Response30X(HttpConnection connection,Hashtable cookies);
    public boolean Response20X(HttpConnection connection,Hashtable cookies);
    public boolean ResponseOthers(HttpConnection connection,Hashtable cookies);
}
