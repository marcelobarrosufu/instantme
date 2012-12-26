/* 
    Copyright (c) Marcelo Barros de Almeida <marcelobarrosalmeida@gmail.com>

    This work is licensed under a Creative Commons 
    Attribution-ShareAlike 3.0 Unported License:

    http://creativecommons.org/licenses/by-sa/3.0/
 
 */
package com.instantme.util;

public interface IAnimation {
    public void start();
    public void stop();
    public void updateProgress(String msg, int perc);
    public void updateProgress(String msg);
}
