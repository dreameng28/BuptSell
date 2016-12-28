package dreameng.com.buptsell.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobLog;

public class MyMessegeReceiver extends BroadcastReceiver {
	public static ArrayList<EventListener> ehList = new ArrayList<EventListener>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra("msg");
        BmobLog.i("�յ���message = " + json);
        //ʡ����������
    }
}
