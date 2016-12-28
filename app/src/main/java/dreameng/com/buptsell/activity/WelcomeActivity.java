package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.beans.UserInfo;

public class WelcomeActivity extends Activity {

	private String account, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bmob.initialize(this, "31969283060599564b4d642e94af3f77");

//	    BmobUpdateAgent.initAppVersion(this);


//		BmobChat.DEBUG_MODE = true;
//        //BmobIM SDK初始化--只需要这一段代码即可完成初始化
//        BmobChat.getInstance(this).init("30d758d54f6bde2be276f4ee8660aaab");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
//		int welPic[] = {
//				R.drawable.add_pic
//		};

//		int radomNumb = (int) (Math.random()*1000);

//		ImageView neuqImg = (ImageView) findViewById(R.id.neuq_pic);

//		neuqImg.setImageResource(welPic[0]);

		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SharedPreferences sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
				account = sharedPreferences.getString("user_account", "");
				password = sharedPreferences.getString("user_password", "");
				UserInfo userInfo = new UserInfo();
				userInfo.setUsername(account);
				userInfo.setPassword(password);
				userInfo.login(WelcomeActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//Toast.makeText(WelcomeActivity.this, "hahahahahahahahahha", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(WelcomeActivity.this, MainActivity.class);

						startActivity(i);
						finish();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						//Toast.makeText(WelcomeActivity.this, arg1, Toast.LENGTH_SHORT).show();
						Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
						startActivity(i);
						finish();
					}
				});

			}
		};


		timer.schedule(timerTask, 1000 * 2);


	}

}