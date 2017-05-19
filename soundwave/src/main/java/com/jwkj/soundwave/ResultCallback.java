package com.jwkj.soundwave;

import com.hdl.udpsenderlib.UDPResultCallback;


public abstract class ResultCallback extends UDPResultCallback {

    public abstract void onStopSend();
}
