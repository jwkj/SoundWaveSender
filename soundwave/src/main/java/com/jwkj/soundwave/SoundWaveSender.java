package com.jwkj.soundwave;

import android.content.Context;

import com.hdl.udpsenderlib.UDPReceiver;
import com.hdl.udpsenderlib.UDPResult;
import com.hdl.udpsenderlib.UDPResultCallback;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDK;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDKListener;


public class SoundWaveSender {
    private String ssid;
    private String pwd;
    private static SoundWaveSender sender;
    private Context mContext;
    private int port = 9988;
    private ResultCallback callback;

    private SoundWaveSender() {
    }

    public static SoundWaveSender getInstance() {
        if (sender == null) {
            sender = new SoundWaveSender();
        }
        return sender;
    }

    public SoundWaveSender with(Context context) {
        this.mContext = context;
        EMTMFSDK.getInstance(context).setListener(emtmfsdkListener);
        return this;
    }

    public SoundWaveSender setPort(int port) {
        this.port = port;
        return this;
    }

    public SoundWaveSender setWifiSet(String ssid, String pwd) {
        this.ssid = ssid;
        this.pwd = pwd;
        return this;
    }

    public void send(final ResultCallback callback) {
        this.callback = callback;

        EMTMFSDK.getInstance(mContext).sendWifiSet(mContext, ssid, pwd);
        UDPReceiver.getInstance().with(mContext).setPort(port).receive(new UDPResultCallback() {
            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onNext(UDPResult udpResult) {
                callback.onNext(udpResult);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                callback.onError(throwable);
            }
        });
    }
    public SoundWaveSender stopSend() {
        EMTMFSDK.getInstance(mContext).stopSend();
//        UDPSender.getInstance().stop();
        UDPReceiver.getInstance().with(mContext).stopReceive();
        return this;
    }

    private EMTMFSDKListener emtmfsdkListener = new EMTMFSDKListener() {

        public void didEndOfPlay() {
            callback.onStopSend();
        }
    };
}
