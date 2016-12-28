package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.beans.UserInfo;
import dreameng.com.buptsell.beans.UserItem;
import dreameng.com.buptsell.tools.MyCamera;

public class UploadActivity2 extends Activity implements OnClickListener, OnLongClickListener{
	private EditText mEditText, mItemPhoneNumberEditText;
	private Button mBtnCancel;
	private Button mBtnUpload;
	private ImageView mImageView1;
	private Uri imageUri;
	private String picPath;
	private Bitmap bitmap;


	private static final int TAKE_PHOTO1 = 1;
	private static final int SELECT_PHOTO = 4;

	MyCamera myCamera;
	private UserInfo user = new UserInfo();
	UserItem userItem = new UserItem();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);

		user = BmobUser.getCurrentUser(this, UserInfo.class);

		mEditText = (EditText) findViewById(R.id.description);
		mEditText.setOnClickListener(this);
		mItemPhoneNumberEditText = (EditText) findViewById(R.id.item_user_phone_number);
		mItemPhoneNumberEditText.setOnClickListener(this);
		mBtnUpload = (Button) findViewById(R.id.confirm_upload);
		mBtnUpload.setOnClickListener(this);
		mBtnCancel = (Button) findViewById(R.id.cancel_uoload);
		mBtnCancel.setOnClickListener(this);
		mImageView1 = (ImageView) findViewById(R.id.upload_pic1);
		mImageView1.setOnClickListener(this);
		mImageView1.setOnLongClickListener(this);

		myCamera = new MyCamera();

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){

			case R.id.description:
				mEditText.setCursorVisible(true);
				break;

			case R.id.item_user_phone_number:
				mItemPhoneNumberEditText.setCursorVisible(true);
				break;

			case R.id.upload_pic1:
				imageUri = myCamera.takePic(TAKE_PHOTO1, UploadActivity2.this, 1);
				break;
			case R.id.confirm_upload:
				Log.e("haha", "haha");
				if((TextUtils.isEmpty(mEditText.getText().toString().trim()))||(analysePhoneNumber() == false)){
					if(TextUtils.isEmpty(mEditText.getText().toString().trim())){
						Toast.makeText(this, "还没描述商品", Toast.LENGTH_SHORT).show();
					}
					else if(!analysePhoneNumber()){
						Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
					}
				}


				else{
					String path = null;
					if(getSmallBitmap(picPath) != null){
						try {
							path = saveBitmap(getSmallBitmap(picPath));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Log.e("path", "" + path);
						final BmobFile bmobFile = new BmobFile(new File(path));
						Log.e("bmobFile", bmobFile.toString());
						bmobFile.uploadblock(this, new UploadFileListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								uploadDescption(bmobFile.getFileUrl(getBaseContext()));
							}

							@Override
							public void onFailure(int arg0, String arg1) {

								// TODO Auto-generated method stub
								Log.e("上传失败: ", arg0 + " " + arg1);
								uploadDescption("http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg");
								Toast.makeText(UploadActivity2.this, "上传失败：" + arg1, Toast.LENGTH_SHORT).show();
							}
						});

						this.finish();
					}
					else{
						Toast.makeText(this, "插入一张图片", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case R.id.cancel_uoload:
				this.finish();
				break;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if((resultCode == RESULT_OK) && (requestCode < 4)){
			picPath = Environment.getExternalStorageDirectory() + "/BuptSellItemPic1.jpeg";
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(imageUri, "image/*");
			intent.putExtra("scale", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			//Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
				switch(requestCode){
					case TAKE_PHOTO1:
						mImageView1.setImageBitmap(bitmap);
						//						mImageView2.setVisibility(View.VISIBLE);
						break;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if((resultCode == RESULT_OK) && (requestCode >= 4)){
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver();
			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
	                /* 将Bitmap设定到ImageView */
				switch(requestCode){
					case 4:
						mImageView1.setImageBitmap(bitmap);
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
	}

	private void uploadDescption(String mBmobPics){
		String description, itemPhoneNumber;
		description = mEditText.getText().toString();
		itemPhoneNumber = mItemPhoneNumberEditText.getText().toString();
		userItem.setItemDpn(description);
		userItem.setItemPhoneNumber(itemPhoneNumber);
		userItem.setCollectionNum(0);
		userItem.setItemPic(mBmobPics);
		userItem.setUser(user);
		userItem.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(UploadActivity2.this, "上传成功", Toast.LENGTH_SHORT).show();
				addItemToUser();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(UploadActivity2.this,arg1, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void addItemToUser(){

		BmobRelation userItems = new BmobRelation();
		userItems.add(userItem);
		user.setUserItems(userItems);
		user.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				//		Toast.makeText(UploadActivity2.this, "绑定成功", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
	}
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		switch(v.getId()){
			case R.id.upload_pic1:
				startActivityForResult(i, SELECT_PHOTO);
				break;
		}
		return false;
	}

	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
		if(bitmap == null)
			return null ;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	public static Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if(bm == null){
			return  null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm,degree) ;
		ByteArrayOutputStream baos = null ;
		try{
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

		}finally{
			try {
				if(baos != null)
					baos.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm ;

	}

	public static String saveBitmap(Bitmap bitmap) throws IOException
	{
		// File file = new File("/sdcard/picture/"+"image");
		File file = new File(Environment.getExternalStorageDirectory(), "image.jpeg");
		try {
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		FileOutputStream out;
		try{
			out = new FileOutputStream(file);
			if(bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out))
			{
				out.flush();
				out.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	private Boolean analysePhoneNumber(){
		String mItemPhoneNumber;
		mItemPhoneNumber = mItemPhoneNumberEditText.getText().toString();
		return RegisterActivity.analyseInput("1(3|5|7|8)[0-9]{9}$", mItemPhoneNumber);
	}

}

	

