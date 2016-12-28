package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.SaveListener;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.beans.UserInfo;

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText nickname, emailAddress, password;
	private Button register;
	private RadioButton male;
	BmobUserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);

		nickname = (EditText) findViewById(R.id.nickname);
		emailAddress = (EditText) findViewById(R.id.email_account);
		password = (EditText) findViewById(R.id.password);
		register = (Button) findViewById(R.id.register);
		male = (RadioButton) findViewById(R.id.male);
		userManager = BmobUserManager.getInstance(this);

		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.register:
				if(!(TextUtils.isEmpty(nickname.getText().toString().trim()))&&analyseEmail()&&analysePassword()&&analyseNickname()){

					registerStepOneSuccess();

				}

				else
					registerStepOneFailure();

				break;

		}

	}

	private Boolean analyseNickname(){
		String register_nickname;
		register_nickname = nickname.getText().toString();
		return (register_nickname.length() != 0);
	}


	private Boolean analyseEmail(){

		String register_email;
		register_email = emailAddress.getText().toString().trim();

		return analyseInput("^[0-9a-z][a-z0-9._-]{1,}@[a-z0-9-]{1,}[a-z0-9].[a-z.]{1,}[a-z]$", register_email);
	}

	private Boolean analysePassword(){
		String register_password;
		register_password = password.getText().toString();

		return (register_password.length() >= 6);
	}



	private void registerStepOneSuccess(){

		String register_nickname, register_password;
		final String register_email;
		String register_gender;

		register_nickname = nickname.getText().toString();
		register_password = password.getText().toString();
		register_email = emailAddress.getText().toString();
		register_gender = male.isChecked()? "男" : "女";

		final UserInfo userInfo = new UserInfo();

		userInfo.setUsername(register_email);
		userInfo.setEmail(register_email);
		userInfo.setPassword(register_password);
		userInfo.setNickname(register_nickname);
		userInfo.setGender(register_gender);
		userInfo.signUp(RegisterActivity.this, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(RegisterActivity.this, "注册成功请到" + register_email + "验证", Toast.LENGTH_SHORT).show();
				Intent i = new Intent();
				i.putExtra("account", register_email);
				userManager.bindInstallationForRegister(userInfo.getUsername());
				setResult(RESULT_OK, i);
				RegisterActivity.this.finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(RegisterActivity.this, "注册失败: " + arg1 + "请重试", Toast.LENGTH_LONG).show();
			}
		});


	}


	private void registerStepOneFailure(){

		if(TextUtils.isEmpty(nickname.getText().toString().trim())){
			Toast.makeText(RegisterActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
		}

		else if(!analyseEmail()){
			Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
		}

		else if(!analysePassword()){
			Toast.makeText(RegisterActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
		}

	}

	public static Boolean analyseInput(String reg, String input){
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

}