package com.roboctopi.cuttlefish.Queue

interface Task {
    fun onBegin(): Boolean {return true;};
    fun loop():Boolean;
}