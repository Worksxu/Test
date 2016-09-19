package com.ruiqi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.LoginActivity;
import com.ruiqi.works.MyOrderActivity;

public class JpushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String action = intent.getAction();
		if ("cn.jpush.android.intent.NOTIFICATION_OPENED".equals(action)) {
			
			
			if(SPUtils.contains(context, SPutilsKey.FLAG)){
				boolean flag = (Boolean) SPUtils.get(context, SPutilsKey.LUNCHFALG, false);
				if(flag){
					Intent i = new Intent(context, MyOrderActivity.class);
					i.putExtras(bundle);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i);
				}else{
					Intent i = new Intent(context, LoginActivity.class);
					i.putExtras(bundle);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i);
				}
			}
			
        	
        	String regId = JPushInterface.getRegistrationID(context);
        	
        	
        	
        	Log.e("注册id",regId );
		}
	}

}
