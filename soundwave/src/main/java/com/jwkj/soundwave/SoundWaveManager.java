package com.jwkj.soundwave;

import android.content.Context;

import com.larksmart.emtmf.jni.EMTMFOptions;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDK;


public class SoundWaveManager {
    public static boolean init(Context context) {
        int errcode = EMTMFSDK.getInstance(context).initSDK(context, EMTMFInit.manufacturer,
                EMTMFInit.client, EMTMFInit.productModel,
                EMTMFInit.license);
        if (errcode == EMTMFOptions.INITSDK_ERRCOE_WIFIDISABLE) {
            return false;
        } else if (errcode == EMTMFOptions.INITSDK_INVAILDDATA) {
            return false;
        } else {
            return true;
        }
    }

    public static void onDestroy(Context context) {
        EMTMFSDK.getInstance(context).exitEMTFSDK(context);
    }
}
