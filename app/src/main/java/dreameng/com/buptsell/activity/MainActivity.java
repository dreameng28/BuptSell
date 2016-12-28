package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.adapter.ItemAdapter;
import dreameng.com.buptsell.beans.UserInfo;
import dreameng.com.buptsell.beans.UserItem;
import dreameng.com.buptsell.tools.MyCamera;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {
	BmobQuery<UserItem> query;
	AlertDialog alertDialog;   
	private LinearLayout mSettingLinearLayout;
	private Uri imageUri;
	private String picPath;
	private Bitmap bitmap;
	private static int flag;
	private RelativeLayout mMenuTitleRelativeLayout;
	private boolean mFlag;
	private int itemsNum;
	private Button uploadBtn, mMenu;
	UserInfo currentUser;
	private ImageView currentUserIcon;
	// private ProgressBar mUploadPrograssBar;
	private TextView titleTV, menuTitleTV, myUpload, mostCollection,
			latestUpload, myCollection;
	private PullToRefreshListView mListView;
	private DrawerLayout mDrawerLayout;
	public static List<UserItem> userItems = new ArrayList<UserItem>();
	// public static List<UserItem> userItems2 = new ArrayList<UserItem>();
	// public List[] userItems = {userItems1, userItems2};
	// private int flag = 0;
	ItemAdapter mItemAdapter = new ItemAdapter(MainActivity.this);

	// public final static int UPLOAD = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_drawer);
//		BmobUpdateAgent.initAppVersion(this);
		BmobUpdateAgent.setUpdateOnlyWifi(false);
		BmobUpdateAgent.update(this);
		BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

		    	 @Override
		    	 public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
		    	     // TODO Auto-generated method stub
		    	     //根据updateStatus来判断更新是否成功
		    		 Log.e("regrade", "" + updateStatus);
		    	 }
		    	});
		    
		    BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {
				
				@Override
				public void onClick(int arg0) {
					// TODO Auto-generated method stub
					switch (arg0) {
		            case UpdateStatus.Update:
//		                Toast.makeText(ActAutoUpdate.this, "点击了立即更新按钮" , Toast.LENGTH_SHORT).show();
		                break;
		            case UpdateStatus.NotNow:
//		                Toast.makeText(ActAutoUpdate.this, "点击了以后再说按钮" , Toast.LENGTH_SHORT).show();
		                break;
		            case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
//		                Toast.makeText(ActAutoUpdate.this, "点击了对话框关闭按钮" , Toast.LENGTH_SHORT).show();
		                break;
		            }
				}
			});
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);

		// mUploadPrograssBar = (ProgressBar)
		// findViewById(R.id.upload_prograssBar);
		flag = 1;
		query = new BmobQuery<UserItem>();
		mMenuTitleRelativeLayout = (RelativeLayout) findViewById(R.id.menu_title_layout);
		mMenuTitleRelativeLayout.setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.list);
		mListView.setMode(Mode.BOTH);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mItemAdapter);
		userItems.clear();
		query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
		queryAll();
		mItemAdapter.notifyDataSetChanged();
		uploadBtn = (Button) findViewById(R.id.upload);
		uploadBtn.setOnClickListener(this);
		myUpload = (TextView) findViewById(R.id.my_upload);
		myUpload.setOnClickListener(this);
		myCollection = (TextView) findViewById(R.id.my_collection);
		myCollection.setOnClickListener(this);
		mostCollection = (TextView) findViewById(R.id.most_collection);
		mostCollection.setOnClickListener(this);
		latestUpload = (TextView) findViewById(R.id.latest_upload);
		latestUpload.setOnClickListener(this);
		latestUpload.setBackgroundColor(Color.BLUE);
		latestUpload.setTextColor(Color.WHITE);
		titleTV = (TextView) findViewById(R.id.title_name);
		titleTV.setOnClickListener(this);
		// mListView.setMode(Mode.MANUAL_REFRESH_ONLY);
		menuTitleTV = (TextView) findViewById(R.id.menu_title);
		menuTitleTV.setText(BmobUser.getCurrentUser(this, UserInfo.class)
				.getNickname());
		currentUserIcon = (ImageView) findViewById(R.id.menu_user_icon);
//		currentUserIcon.setOnClickListener(this);
		currentUser = BmobUser.getCurrentUser(this, UserInfo.class);
		String gender = currentUser.getGender();
		if (currentUser.getUser_icon() != null) {
			String url_userIcon = currentUser.getUser_icon();
			Picasso.with(getBaseContext())
					.load(url_userIcon)
					.resizeDimen(R.dimen.icon_img,
							R.dimen.icon_img).centerInside()
					.into(currentUserIcon);
		}
		if (gender.equals("男")) {
			menuTitleTV.setTextColor(Color.BLUE);
			if (currentUser.getUser_icon() == null) {
				currentUserIcon.setImageResource(R.drawable.icon_boy);
			}
		}

//		logoutTV = (TextView) findViewById(R.id.logout);
//		logoutTV.setOnClickListener(this);
		mSettingLinearLayout = (LinearLayout) findViewById(R.id.menu_setting);
		mSettingLinearLayout.setOnClickListener(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerOpened(View arg0) {
				// TODO Auto-generated method stub
				uploadBtn.setBackgroundResource(R.drawable.left_arrow);
			}

			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				uploadBtn.setBackgroundResource(R.drawable.upload);
			}
		});

		mMenu = (Button) findViewById(R.id.menu);
		mMenu.setOnClickListener(this);

		Log.e("onCreat", "执行");

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				query = new BmobQuery<UserItem>();
				query.setCachePolicy(CachePolicy.NETWORK_ONLY);
				query.setCachePolicy(CachePolicy.IGNORE_CACHE);
				itemsNum = userItems.size();



				if (refreshView.isShownHeader()) {
					mListView.getLoadingLayoutProxy()
							.setRefreshingLabel("正在刷新");
					mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
					mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
					mFlag = true;
					query.setSkip(0);
					queryAll();

				}

				if (refreshView.isShownFooter()) {
					mListView.getLoadingLayoutProxy()
							.setRefreshingLabel("正在加载");
					mListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
					mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
					mFlag = false;
					query.setSkip(itemsNum);
					queryAll();
				}

			}
		});

	}

	public void queryAll() {
		query.setLimit(10);
		query.include("user");
		switch (flag) {
		case 1:
			query.order("-createdAt");
			break;
		case 2:
			query.order("-collectionNum, -createdAt");
			break;
		case 3:
//			String[] currentUserName = { currentUser.getObjectId() };
//			query.order("-createdAt");
//			query.addWhereContainedIn("user", Arrays.asList(currentUserName));
			query.order("-createdAt");
			query.addWhereRelatedTo("userItems", new BmobPointer(currentUser));
			break;
		case 4:
			query.order("-createdAt");
			query.addWhereRelatedTo("myCollections", new BmobPointer(currentUser));
			break;
		}
		
	
		query.findObjects(this, new FindListener<UserItem>() {

			@Override
			public void onSuccess(List<UserItem> arg0) {
				
				// TODO Auto-generated method stub
				if(arg0.size() == 0){
					Toast.makeText(MainActivity.this, "没有更多了", Toast.LENGTH_SHORT)
					.show();
				}
				for (UserItem mUserItem : arg0) {
					final UserItem userItem = new UserItem();
					userItem.setItemID(mUserItem.getObjectId());
					userItem.setItemDpn(mUserItem.getItemDpn());
					userItem.setCreatedDate(mUserItem.getCreatedAt());
					userItem.setItemPic(mUserItem.getItemPic());
					userItem.setUser(mUserItem.getUser());
					userItem.setCollectionNum(mUserItem.getCollectionNum());
					userItems.add(userItem);
					Log.e("nickname", mUserItem.getUser().getNickname() + "");
				}
				
//				Iterator<UserItem> mIterator = userItems.iterator();
				if(mFlag == true){
					for (int i = 0; i < itemsNum; i++) {
						if(userItems!=null && userItems.size()>0){
							userItems.remove(0);
						}
					}
				}
				
				mItemAdapter.notifyDataSetChanged();
				mListView.onRefreshComplete();		
				
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, arg1, Toast.LENGTH_SHORT)
						.show();
				mListView.onRefreshComplete();				
			}
		});
		query = null;
		System.gc();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		itemsNum = userItems.size();
		switch (v.getId()) {
		case R.id.upload:
			Intent i = new Intent(this, UploadActivity2.class);
			startActivity(i);
			break;	
		case R.id.menu:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.menu_setting:
			Intent settingIntent = new Intent(this, SettingActivity.class);
			startActivityForResult(settingIntent, 10);
			break;
		case R.id.menu_title_layout:
			AlertDialog.Builder builder;     
			LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.gallery_or_camera, null);
			ImageButton mCameraImageButton = (ImageButton) mLinearLayout.findViewById(R.id.select_camera);
			ImageButton mGaleryImageButton = (ImageButton) mLinearLayout.findViewById(R.id.select_galery);
			mCameraImageButton.setOnClickListener(this);
			mGaleryImageButton.setOnClickListener(this);
			builder = new AlertDialog.Builder(this);     
	        builder.setView(mLinearLayout);     
	        alertDialog = builder.create();     
	        alertDialog.show();  
			break;
			
		case R.id.select_camera:
			MyCamera myCamera = new MyCamera();
			imageUri = myCamera.takePic(1, this, 1);
			alertDialog.dismiss();
			break;
			
		case R.id.select_galery:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 4);
			alertDialog.dismiss();
			break;
			
		case R.id.latest_upload:
			mFlag = true;
			titleTV.setText("首页");
			mostCollection.setBackgroundColor(Color.WHITE);
			mostCollection.setTextColor(Color.GRAY);
			myCollection.setBackgroundColor(Color.WHITE);
			myCollection.setTextColor(Color.GRAY);
			latestUpload.setBackgroundColor(Color.BLUE);
			latestUpload.setTextColor(Color.WHITE);
			myUpload.setBackgroundColor(Color.WHITE);
			myUpload.setTextColor(Color.GRAY);
//			userItems.clear();
//			mItemAdapter.notifyDataSetChanged();
			
			query = new BmobQuery<UserItem>();
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
			flag = 1;
			queryAll();
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mListView.setAdapter(mItemAdapter);
			break;
		case R.id.my_upload:
			mFlag = true;
			titleTV.setText("我的萌物");
			mostCollection.setBackgroundColor(Color.WHITE);
			mostCollection.setTextColor(Color.GRAY);
			myCollection.setBackgroundColor(Color.WHITE);
			myCollection.setTextColor(Color.GRAY);
			latestUpload.setBackgroundColor(Color.WHITE);
			latestUpload.setTextColor(Color.GRAY);
			myUpload.setBackgroundColor(Color.BLUE);
			myUpload.setTextColor(Color.WHITE);
			flag = 3;
//			userItems.clear();
//			mItemAdapter.notifyDataSetChanged();
			
			query = new BmobQuery<UserItem>();
			query.setCachePolicy(CachePolicy.NETWORK_ONLY);
			queryAll();
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mListView.setAdapter(mItemAdapter);
			break;
		case R.id.most_collection:
			mFlag = true;
			titleTV.setText("收藏最多");
			mostCollection.setBackgroundColor(Color.BLUE);
			mostCollection.setTextColor(Color.WHITE);
			myCollection.setBackgroundColor(Color.WHITE);
			myCollection.setTextColor(Color.GRAY);
			latestUpload.setBackgroundColor(Color.WHITE);
			latestUpload.setTextColor(Color.GRAY);
			myUpload.setBackgroundColor(Color.WHITE);
			myUpload.setTextColor(Color.GRAY);
			flag = 2;
//			userItems.clear();
//			mItemAdapter.notifyDataSetChanged();
			query = new BmobQuery<UserItem>();
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
			queryAll();
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mListView.setAdapter(mItemAdapter);
			break;
		case R.id.my_collection:
			mFlag = true;
			titleTV.setText("我的收藏");
			mostCollection.setBackgroundColor(Color.WHITE);
			mostCollection.setTextColor(Color.GRAY);
			myCollection.setBackgroundColor(Color.BLUE);
			myCollection.setTextColor(Color.WHITE);
			latestUpload.setBackgroundColor(Color.WHITE);
			latestUpload.setTextColor(Color.GRAY);
			myUpload.setBackgroundColor(Color.WHITE);
			myUpload.setTextColor(Color.GRAY);
			flag = 4;
//			userItems.clear();
//			mItemAdapter.notifyDataSetChanged();
			query = new BmobQuery<UserItem>();
			query.setCachePolicy(CachePolicy.NETWORK_ONLY);
			queryAll();
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mListView.setAdapter(mItemAdapter);
			break;
		case R.id.menu_user_icon:

			break;
		case R.id.title_name:
			// query.setCachePolicy(CachePolicy.CACHE_ONLY);
			mListView.setAdapter(mItemAdapter);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if((resultCode == RESULT_OK) && (requestCode < 4)){
    		picPath = Environment.getExternalStorageDirectory() + "/NeuqSellItemPic1.jpeg";
 			Intent intent = new Intent("com.android.camera.action.CROP");
 			intent.setDataAndType(imageUri, "image/*");
 			intent.putExtra("scale", true);
 			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
 			//Bitmap bitmap;
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					switch(requestCode){
					case 1:
//						currentUserIcon.setImageBitmap(bitmap);
//						mImageView2.setVisibility(View.VISIBLE);
						break;
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
    	}
    	
    	if((resultCode == RESULT_OK) && ((requestCode >= 4))&&(requestCode<10)){
    		Uri uri = data.getData();  
            ContentResolver cr = this.getContentResolver();  
            try {  
            	bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                /* 将Bitmap设定到ImageView */ 
                switch(requestCode){
                case 4:
//                	currentUserIcon.setImageBitmap(bitmap);
                	String[] proj = {MediaStore.Images.Media.DATA};
                	 
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(data.getData(), proj, null, null, null); 
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    picPath = cursor.getString(column_index);
 					break;
                }
               

            } catch (FileNotFoundException e) {
            	e.printStackTrace();
            }    
    		
    	}
    	
    	String path = null;
		if(UploadActivity2.getSmallBitmap(picPath) != null){
			try {
				path = UploadActivity2.saveBitmap(UploadActivity2.getSmallBitmap(picPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.e("path", "" + path);
			final BmobFile bmobFile = new BmobFile(new File(path));
			bmobFile.uploadblock(getBaseContext(), new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					UserInfo newUser = new UserInfo();
					newUser.setUser_icon(bmobFile.getFileUrl(MainActivity.this));
					newUser.update(MainActivity.this,currentUser.getObjectId(), new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							currentUserIcon.setImageBitmap(bitmap);
							Toast.makeText(MainActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, arg1, Toast.LENGTH_SHORT).show();
						}
					});
					
					picPath = null;
					System.gc();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});			
		}
		
		if((requestCode == 10)&&(resultCode == RESULT_OK)){
			if((data.getExtras().getString("settingResult")).equals("logout")){
				BmobUser.getCurrentUser(this, UserInfo.class).logOut(this);
				Intent logoutIntent = new Intent(this, LoginActivity.class);
				SharedPreferences sharedPreferences = getSharedPreferences(
						"login_data", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				startActivity(logoutIntent);
				finish();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.e("position", "" + arg2);
		Intent i = new Intent(MainActivity.this, ItemDisplayActivity.class);
		UserItem currentUserItem = userItems.get(arg2 - 1);
		String currentItemID = currentUserItem.getItemID();
		i.putExtra("itemID", currentItemID + "");
		Log.e("ID", currentItemID);
		startActivity(i);
	}

}
