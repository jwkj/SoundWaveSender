package com.jwkj.soundwave;

import android.content.Context;

import com.hdl.udpsenderlib.UDPResult;
import com.hdl.udpsenderlib.UDPResultCallback;
import com.hdl.udpsenderlib.UDPSender;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDK;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDKListener;


public class SoundWaveSender {
    private String ssid;
    private String pwd;
    private static SoundWaveSender sender;
    private Context mContext;
    private int port = 9988;
    private ResultCallback callback;
    private boolean isCanReceive = true;//时候可以接收

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
        checkContextIsNull();
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

    public void send(ResultCallback callback) {
        checkContextIsNull();
//        if (this.callback == null) {
            this.callback = callback;
//        }
        EMTMFSDK.getInstance(mContext).sendWifiSet(mContext, ssid, pwd);
        if (isCanReceive) {
            isCanReceive = false;
            startReceive();
        }
    }

    /**
     * 开始接收数据，单次任务只能调用一次
     */
    private void startReceive() {
        checkContextIsNull();
        UDPSender.getInstance()
                .setInstructions(new byte[]{0, 1, 1, 1})
                .setReceiveTimeOut(10 * 60 * 1000)
                .setLocalReceivePort(port)
                .setTargetPort(port)
                .start(new UDPResultCallback() {
                    @Override
                    public void onNext(UDPResult result) {
                        if (result.getResultData()[0] == 1) {
                            callback.onNext(result);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        callback.onError(throwable);
                    }

                    @Override
                    public void onCompleted() {
                        callback.onCompleted();
                    }
                });
    }

    /**
     * 停止接收数据
     */
    private void stopReceive() {
        checkContextIsNull();
        isCanReceive = true;//停止任务了要复位
        if (UDPSender.getInstance() != null) {
            UDPSender.getInstance().stop();
        }
    }

    /**
     * 检测上下文对象是否为空
     */
    private void checkContextIsNull() {
        if (mContext == null) {
            throw new NullPointerException("context is null,please call SoundWaveSender.getInstance().with(context).***");
        }
    }

    /**
     * 停止发送声波
     *
     * @return
     */
    public SoundWaveSender stopSend() {
        if (EMTMFSDK.getInstance(mContext) != null) {
            EMTMFSDK.getInstance(mContext).stopSend();
        }
        stopReceive();
        return this;
    }

    private EMTMFSDKListener emtmfsdkListener = new EMTMFSDKListener() {

        public void didEndOfPlay() {
            callback.onStopSend();
        }
    };
}
