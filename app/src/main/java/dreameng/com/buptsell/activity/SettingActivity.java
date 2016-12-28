package dreameng.com.buptsell.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import dreameng.com.buptsell.R;


public class SettingActivity extends Activity implements OnClickListener, OnItemClickListener {

	private TextView mTextView, mAboutTextView;
	private ImageView mAppIconImageView;
	private Button mBackButton;
	private ListView mSettingListView;
	private int backFlag;
	private static final String[] settings = new String[] {"  关于卖萌", "  账户信息", "  检查更新", "  退出登录"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_page);


		backFlag = 0;
		mTextView = (TextView) findViewById(R.id.display_title_tv);
		mTextView.setText("设置");
		mBackButton = (Button) findViewById(R.id.back_to_main);
		mBackButton.setOnClickListener(this);
		mSettingListView = (ListView) findViewById(R.id.setting_list);
		mAppIconImageView = (ImageView) findViewById(R.id.about_app_icon);
		mAppIconImageView.setVisibility(View.GONE);
		ArrayAdapter<String> mSettingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
		mSettingListView.setAdapter(mSettingAdapter);
		mSettingListView.setOnItemClickListener(this);
		mAboutTextView = (TextView) findViewById(R.id.about_text);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back_to_main:
				if(backFlag == 0){
					finish();
				}
				else{
					backFlag = 0;
					mTextView.setText("设置");
					mSettingListView.setVisibility(View.VISIBLE);
					mAppIconImageView.setVisibility(View.GONE);
					mAboutTextView.setVisibility(View.GONE);
				}
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch(arg2){
			case 0:
				backFlag = 1;
				mTextView.setText("关于");
				mAppIconImageView.setVisibility(View.VISIBLE);
				mAboutTextView.setVisibility(View.VISIBLE);
				mSettingListView.setVisibility(View.GONE);
				try {
					mAboutTextView.setText("当前版本 : " + getVersionName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				Intent changeAccountIntent = new Intent(this, ChangeAccountActivity.class);
				startActivity(changeAccountIntent);
				break;
			case 2:
				BmobUpdateAgent.forceUpdate(this);
				BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

					@Override
					public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
						// TODO Auto-generated method stub
						//根据updateStatus来判断更新是否成功
						if (updateStatus == UpdateStatus.Yes) {//版本有更新

						}else if(updateStatus == UpdateStatus.No){
							Toast.makeText(SettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
						}else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
							Toast.makeText(SettingActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
						}else if(updateStatus==UpdateStatus.IGNORED){
							Toast.makeText(SettingActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
						}else if(updateStatus==UpdateStatus.ErrorSizeFormat){
							Toast.makeText(SettingActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
						}else if(updateStatus==UpdateStatus.TimeOut){
							Toast.makeText(SettingActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
						}
					}
				});
				break;

			case 3:
				Intent resaultIntent = new Intent();
				resaultIntent.putExtra("settingResult", "logout");
				//设置返回数据
				setResult(RESULT_OK, resaultIntent);
				//关闭Activity
				finish();
				break;

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		//如果按下的是返回键且次数为1且短信回复服务正在运行，弹出对话框
		if (keyCode == KeyEvent.KEYCODE_BACK && backFlag == 1) {
			backFlag = 0;
			mTextView.setText("设置");
			mSettingListView.setVisibility(View.VISIBLE);
			mAppIconImageView.setVisibility(View.GONE);
			mAboutTextView.setVisibility(View.GONE);
			return true;
		}

		//否则按照安卓的定义方法执行
		else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
		String version = packInfo.versionName;
		return version;
	}
}