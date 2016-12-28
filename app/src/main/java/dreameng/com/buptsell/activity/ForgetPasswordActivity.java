package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordListener;
import dreameng.com.buptsell.R;

public class ForgetPasswordActivity extends Activity implements OnClickListener{
	private TextView mTitleTextView;
	private EditText mEmailEditText;
	private Button mBackButton, sendEmailButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		mTitleTextView = (TextView) findViewById(R.id.display_title_tv);
		mTitleTextView.setText("修改密码");
		mEmailEditText = (EditText) findViewById(R.id.find_password_email);
		mBackButton = (Button) findViewById(R.id.back_to_main);
		mBackButton.setOnClickListener(this);
		sendEmailButton =(Button) findViewById(R.id.send_email);
		sendEmailButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back_to_main:
				finish();
				break;
			case R.id.send_email:
				if(analyseEmail()){
					BmobUser.resetPassword(this, mEmailEditText.getText().toString(), new ResetPasswordListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Toast.makeText(ForgetPasswordActivity.this, "重置密码请求成功，请到" + mEmailEditText.getText().toString() + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onFailure(int code, String e) {
							// TODO Auto-generated method stub
							Toast.makeText(ForgetPasswordActivity.this, e, Toast.LENGTH_SHORT).show();
						}
					});
					finish();
				}
				else{
					Toast.makeText(ForgetPasswordActivity.this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private Boolean analyseEmail(){

		String register_email;
		register_email = mEmailEditText.getText().toString();
		return RegisterActivity.analyseInput("^[0-9a-z][a-z0-9._-]{1,}@[a-z0-9-]{1,}[a-z0-9].[a-z.]{1,}[a-z]$", register_email);
	}
}