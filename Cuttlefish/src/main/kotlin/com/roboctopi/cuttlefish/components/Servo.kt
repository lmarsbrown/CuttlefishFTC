package com.roboctopi.cuttlefish.components

interface Servo {
    fun setPosition(postion:Double);
    fun getPosition():Double;
}