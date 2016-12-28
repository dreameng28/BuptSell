package dreameng.com.buptsell.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.bmob.v3.BmobUser;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.beans.UserInfo;

public class ChangeAccountActivity extends Activity implements OnClickListener {
	
	private ImageView mUserIconImageView;
	private TextView mTitleTextView, mNicknameTextView, mUserGenderTextView, mUserEmailTextView, mChangePasswordTextView;
	private Button backToSettingButton;
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.change_account);
			UserInfo mCurrentUser = BmobUser.getCurrentUser(this, UserInfo.class);
			mTitleTextView = (TextView) findViewById(R.id.display_title_tv);
			mTitleTextView.setText("账户信息");
			backToSettingButton = (Button) findViewById(R.id.back_to_main);
			backToSettingButton.setOnClickListener(this);
			mUserIconImageView = (ImageView) findViewById(R.id.info_user_icon);
			if(mCurrentUser.getUser_icon()!=null){
				Picasso.with(this).load(mCurrentUser.getUser_icon()).resizeDimen(R.dimen.icon_img, R.dimen.icon_img)
				.centerInside().into(mUserIconImageView);
			}
			if(mCurrentUser.getGender().equals("男")){
				if(mCurrentUser.getUser_icon() == null){
					mUserIconImageView.setImageResource(R.drawable.icon_boy);
				}
			}
			mNicknameTextView = (TextView) findViewById(R.id.info_user_nickname);
			mNicknameTextView.setText(mCurrentUser.getNickname());
			mUserGenderTextView = (TextView) findViewById(R.id.info_user_gender);
			mUserGenderTextView.setText(mCurrentUser.getGender().equals("男")?"帅哥":"美女");
			mUserEmailTextView = (TextView) findViewById(R.id.info_bind_email);
			mUserEmailTextView.setText(mCurrentUser.getEmail());
			mChangePasswordTextView = (TextView) findViewById(R.id.info_change_password);
			mChangePasswordTextView.setOnClickListener(this);
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_to_main:
			finish();
			break;
		case R.id.info_change_password:
			Intent changePasswordIntent = new Intent(this, ForgetPasswordActivity.class);
			startActivity(changePasswordIntent);
			break;
		}
	}
}
