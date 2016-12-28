package dreameng.com.buptsell.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dreameng.com.buptsell.beans.ItemComment;
import dreameng.com.buptsell.beans.UserInfo;
import dreameng.com.buptsell.beans.UserItem;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import dreameng.com.buptsell.R;
import dreameng.com.buptsell.tools.MyCamera;

public class ItemDisplayActivity extends Activity implements OnClickListener{
	AlertDialog alertDialog;   
	private Uri imageUri;
	private ListView commentListView;
	private EditText commentEditText;
	private ImageView itemPic,itemPic2,itemPic3,itemPic4, itemPic5, itemSellerIcon, collectionIcon;
	private Button connectSeller, backBtn, addPicBtn, sendComment;
	private Button deleteItem;
	private TextView itemSellerName, collectionNumTV;
	private String itemUserID;
	private int collectionNum;
	private String picPath;
	private Bitmap bitmap;
	private int addPicFlag = 0; 
	String itemID, itemUserPhoneNumber;
	private List<ItemComment> itemComments = new ArrayList<ItemComment>();
	UserItem mUserItem = new UserItem();
	CommentAdapter mCommentAdapter = new CommentAdapter();
	UserInfo currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentUser = BmobUser.getCurrentUser(this, UserInfo.class);
		Intent i = getIntent();
		itemID = i.getStringExtra("itemID");
		Log.e("user", currentUser.getObjectId());
		BmobQuery<UserItem> query = new BmobQuery<UserItem>();
		query.include("user");
		query.getObject(this, itemID, new GetListener<UserItem>() {

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
				setContentView(R.layout.blank);
			}

			@Override
			public void onSuccess(UserItem arg0) {
				// TODO Auto-generated method stub
				itemUserID = arg0.getUser().getObjectId();
				Log.e("ID", currentUser.getObjectId() + " " + itemUserID);
				if(currentUser.getObjectId().equals(itemUserID)){
//					RelativeLayout collectionRelativeLayout = (RelativeLayout) findViewById(R.id.display_title_layout);
//					collectionRelativeLayout.setVisibility(View.GONE);
					setContentView(R.layout.my_item_display);
					backBtn = (Button) findViewById(R.id.back_to_main);
					backBtn.setOnClickListener(ItemDisplayActivity.this);
					addPicBtn = (Button) findViewById(R.id.add_description);
					addPicBtn.setVisibility(View.VISIBLE);
					addPicBtn.setOnClickListener(ItemDisplayActivity.this);
					deleteItem = (Button) findViewById(R.id.delete_item);
					deleteItem.setOnClickListener(ItemDisplayActivity.this);
					itemPic = (ImageView) findViewById(R.id.my_display_item_pic);
					itemPic2 = (ImageView) findViewById(R.id.my_display_item_pic2);
					itemPic3 = (ImageView) findViewById(R.id.my_display_item_pic3);

					Picasso.with(getBaseContext()).load(arg0.getItemPic()).into(itemPic);
					if(arg0.getItemPic2() != null){
						Picasso.with(getBaseContext()).load(arg0.getItemPic2()).into(itemPic2);
						addPicFlag = 1;
					}
					if(arg0.getItemPic3() != null){
						Picasso.with(getBaseContext()).load(arg0.getItemPic3()).into(itemPic3);
						addPicFlag = 2;
					}

					mUserItem = arg0;
				}
				else{
					setContentView(R.layout.item_display);
					backBtn = (Button) findViewById(R.id.back_to_main);
					backBtn.setOnClickListener(ItemDisplayActivity.this);
					itemUserPhoneNumber = arg0.getItemPhoneNumber();

					itemSellerName = (TextView) findViewById(R.id.item_seller_name);
					itemPic = (ImageView) findViewById(R.id.display_item_pic);
					itemPic2 = (ImageView) findViewById(R.id.display_item_pic2);
					itemPic3 = (ImageView) findViewById(R.id.display_item_pic3);
					itemPic4 = (ImageView) findViewById(R.id.display_item_pic4);
					itemPic5 = (ImageView) findViewById(R.id.display_item_pic5);

					itemSellerIcon = (ImageView) findViewById(R.id.item_seller_icon);

					Picasso.with(getBaseContext()).load(arg0.getItemPic()).into(itemPic);
					if(arg0.getItemPic2() != null){
						Picasso.with(getBaseContext()).load(arg0.getItemPic2()).into(itemPic2);
						addPicFlag = 1;
					}
					if(arg0.getItemPic3() != null){
						Picasso.with(getBaseContext()).load(arg0.getItemPic3()).into(itemPic3);
						addPicFlag = 2;
					}

					itemSellerName.setText(arg0.getUser().getNickname());
					Log.e("name", "" + arg0.getUser().getNickname());
					String gender = arg0.getUser().getGender();
					if(arg0.getUser().getUser_icon() != null){
						String url_userIcon = arg0.getUser().getUser_icon();
						Picasso.with(getBaseContext()).load(url_userIcon).resizeDimen(R.dimen.icon_img, R.dimen.icon_img)
						.centerInside().into(itemSellerIcon);
					}
					if(gender.equals("男")){
						itemSellerName.setTextColor(Color.BLUE);
						if(arg0.getUser().getUser_icon() == null){
							itemSellerIcon.setImageResource(R.drawable.icon_boy);
						}
					}
					
					collectionIcon = (ImageView) findViewById(R.id.display_collection_icon);
					collectionIcon.setTag("before_collection");
					collectionIcon.setImageResource(R.drawable.collection1);
					BmobQuery<UserItem> mQuery = new BmobQuery<UserItem>();
					mQuery.addWhereRelatedTo("myCollections", new BmobPointer(currentUser));
					mQuery.findObjects(ItemDisplayActivity.this, new FindListener<UserItem>() {

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(List<UserItem> arg0) {
							// TODO Auto-generated method stub
							for(UserItem mUserItem : arg0){
								if(mUserItem.getObjectId().equals(itemID)){
//									Log.e("ID", "" + mUserItem.getObjectId());
//									Log.e("mID", "" + itemID);
									collectionIcon.setTag("after_collection");
									collectionIcon.setImageResource(R.drawable.collection2);
									break;
								}
							}
							
						}

					});
					
//					collectionIcon.setTag("before_collection");
					collectionIcon.setOnClickListener(ItemDisplayActivity.this);
					collectionNumTV = (TextView) findViewById(R.id.display_collection_num);
					collectionNumTV.setText("" + arg0.getCollectionNum());
					Log.e("num", "" + arg0.getCollectionNum());
					collectionNum = arg0.getCollectionNum();
				}
				
				mUserItem = arg0; 

				queryComments(); 
			}
		});
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back_to_main:
			finish();
			break;
		case R.id.add_description: 
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
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(i, 4);
			alertDialog.dismiss();
			break;
			
		case R.id.display_collection_icon:
			if(collectionIcon.getTag().equals("before_collection")){
				collectionIcon.setImageResource(R.drawable.collection2);
				collectionIcon.setTag("after_collection");
				collectionNum++;
				collectionNumTV.setText("" + collectionNum);
//				mUserItem.setCollectionNum(collectionNum);
				mUserItem.increment("collectionNum");
				BmobRelation collectionUsers = new BmobRelation();
				UserInfo currentUser = BmobUser.getCurrentUser(this, UserInfo.class);
				collectionUsers.add(currentUser);
				mUserItem.setCollectionUsers(collectionUsers);
				BmobRelation myCollections = new BmobRelation();
				myCollections.add(mUserItem);
				currentUser.setMyCollections(myCollections);
//				currentUser.update(this, currentUser.getObjectId(), new UpdateListener() {
//					
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onFailure(int arg0, String arg1) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				
				
				
				mUserItem.update(this, itemID, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
				
				currentUser.update(ItemDisplayActivity.this, currentUser.getObjectId(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(ItemDisplayActivity.this, "�ղسɹ�", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
			else {
				collectionIcon.setImageResource(R.drawable.collection1);
				collectionIcon.setTag("before_collection");
				collectionNum--;
				collectionNumTV.setText("" + collectionNum);
//				mUserItem.setCollectionNum(collectionNum);
				mUserItem.increment("collectionNum", -1);
				BmobRelation collectionUsers = new BmobRelation();
				UserInfo currentUser = BmobUser.getCurrentUser(this, UserInfo.class);
				collectionUsers.remove(currentUser);
				mUserItem.setCollectionUsers(collectionUsers);
				BmobRelation myCollections = new BmobRelation();
				myCollections.remove(mUserItem);
				currentUser.setMyCollections(myCollections);
				mUserItem.update(this, itemID, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
//						Toast.makeText(ItemDisplayActivity.this, "ȡ���ղسɹ�", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
				});
				currentUser.update(ItemDisplayActivity.this, currentUser.getObjectId(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(ItemDisplayActivity.this, "", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;

		
		case R.id.connect_seller:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + itemUserPhoneNumber));
			startActivity(intent);
			break;
			
		case R.id.delete_item:
			UserItem mUserItem = new UserItem();
			mUserItem.setObjectId(itemID);
			mUserItem.delete(this, new DeleteListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(ItemDisplayActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			finish();
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
    	
    	if((resultCode == RESULT_OK) && (requestCode >= 4)){
    		Uri uri = data.getData();  
            ContentResolver cr = this.getContentResolver();  
            try {  
            	bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                /* ��Bitmap�趨��ImageView */ 
                switch(requestCode){
                case 4:
//                	currentUserIcon.setImageBitmap(bitmap);
                	String[] proj = {MediaStore.Images.Media.DATA};
                	 
                    //������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
                    Cursor cursor = managedQuery(data.getData(), proj, null, null, null); 
                    //���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //�����������ͷ ���������Ҫ����С�ĺ���������Խ��
                    cursor.moveToFirst();
                    //����������ֵ��ȡͼƬ·��
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
					UserItem newUserItem = new UserItem();
					switch(addPicFlag){
					case 0:
						newUserItem.setItemPic2(bmobFile.getFileUrl(ItemDisplayActivity.this));
						break;
					case 1:
						newUserItem.setItemPic3(bmobFile.getFileUrl(ItemDisplayActivity.this));
						break;
					case 2:
						newUserItem.setItemPic4(bmobFile.getFileUrl(ItemDisplayActivity.this));
						break;
					case 3:
						newUserItem.setItemPic5(bmobFile.getFileUrl(ItemDisplayActivity.this));
						break;
					}
					
					newUserItem.update(ItemDisplayActivity.this, itemID, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							switch(addPicFlag){
							case 0:
								itemPic2.setImageBitmap(bitmap);
								addPicFlag++;
								break;
							case 1:
								itemPic3.setImageBitmap(bitmap);
								addPicFlag++;
								break;
							case 2:
								itemPic4.setImageBitmap(bitmap);
								addPicFlag++;
								break;
							case 3:
								itemPic5.setImageBitmap(bitmap);
								addPicFlag++;
								addPicBtn.setVisibility(View.GONE);
								break;
							}
							Toast.makeText(ItemDisplayActivity.this, "�ɹ������һ��ͼƬ", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
						}
					});
					
					picPath = null;
					System.gc();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(ItemDisplayActivity.this, arg1, Toast.LENGTH_SHORT).show();
				}
			});
			
		}
	}
	
//	private RelativeLayout add(Context context, int i, int pad, String[] strs,  
//            String[] strs1, int[] color) {  
//        // ����һ������  
//        RelativeLayout commentLayout = (RelativeLayout) LayoutInflater.from(context)  
//                .inflate(R.layout.item_comment_cell, null);  
//        // �����ʾ�û�����¥�������û��������ݵ�TextView  
//        TextView name = (TextView) commentLayout.findViewById(R.id.comment_user_name);  
//        TextView date = (TextView) commentLayout.findViewById(R.id.comment_date);  
//        TextView floor = (TextView) commentLayout.findViewById(R.id.comment_floor_num);
//        TextView comment = (TextView) commentLayout.findViewById(R.id.comment_content);  
//        // ������ʾ�û�����¥�������û���������TextView������  
//        name.setText(strs[i]);  
//        comment.setText(strs1[i]);  
//        // ��̬����һ��LinearLayout��װ�ػ�õĲ���  
//        RelativeLayout layout = new RelativeLayout(context);  
//        layout.setBackgroundColor(color[i]);  
//        layout.setPadding(pad, pad, pad, pad);  
//        // ��i��ֵΪ��ʱ���ݹ����  
//        if (i != 0) {  
//            layout.addView(add(context, --i, pad, strs, strs1, color));  
//        }  
//        layout.addView(commentLayout);  
//        return layout;  
//    }  

	private class CommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemComments.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemComments.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = (LinearLayout) LayoutInflater.from(getBaseContext())  
		                .inflate(R.layout.item_comment_cell, null); 
				viewHolder.userName = (TextView) convertView.findViewById(R.id.comment_user_name);  
			    viewHolder.date = (TextView) convertView.findViewById(R.id.comment_date);  
			    viewHolder.floorNum = (TextView) convertView.findViewById(R.id.comment_floor_num);
			    viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_content);
			    viewHolder.user_icon = (ImageView) convertView.findViewById(R.id.comment_user_icon);
			    convertView.setTag(viewHolder);
			}
			
			ItemComment mItemComment = itemComments.get(position);
			viewHolder = (ViewHolder) convertView.getTag();
			if(mItemComment.getCommentUserInfo().getGender().equals("��")){
				viewHolder.userName.setTextColor(Color.BLUE);
				if(mItemComment.getCommentUserInfo().getUser_icon() == null){
					viewHolder.user_icon.setBackgroundResource(R.drawable.icon_boy);
				}
			}
			
			else{
				viewHolder.userName.setTextColor(Color.parseColor("#FF33CC"));
				if(mItemComment.getCommentUserInfo().getUser_icon() == null){
					viewHolder.user_icon.setBackgroundResource(R.drawable.icon_girl);
				}
			}
			if(mItemComment.getCommentUserInfo().getUser_icon() != null){
				Picasso.with(getBaseContext()).load(mItemComment.getCommentUserInfo().getUser_icon()).resizeDimen(R.dimen.icon_img, R.dimen.icon_img)
				.centerInside().into(viewHolder.user_icon);
			}
			viewHolder.userName.setText(mItemComment.getCommentUserInfo().getNickname());
			
			viewHolder.floorNum.setText(position + 1 + "");
			if(position == 0){
				viewHolder.floorNum.setText("ɳ��");
			}
			
			if(position == 1){
				viewHolder.floorNum.setText("���");
			}
			
			viewHolder.comment.setText(mItemComment.getComment());
			viewHolder.date.setText(mItemComment.getCreatedDate());
			
			return convertView;
		}
		
	} 
	
	class ViewHolder{
		TextView userName, comment, date, floorNum;
		ImageView user_icon;
	}
	
	
	public static void setListViewHeightBasedOnChildren(ListView listView) { 
	    if(listView == null) return;

	    CommentAdapter listAdapter = (CommentAdapter) listView.getAdapter(); 
	    if (listAdapter == null) { 
	        // pre-condition 
	        return; 
	    } 

	    int totalHeight = 0; 
	    for (int i = 0; i < listAdapter.getCount(); i++) { 
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0); 
	        totalHeight += listItem.getMeasuredHeight(); 
	    } 

	    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	    listView.setLayoutParams(params); 
	}
	
	private void queryComments(){
		BmobQuery<ItemComment> itemCommentBmobQuery = new BmobQuery<ItemComment>();
		itemCommentBmobQuery.setLimit(15);
		itemCommentBmobQuery.include("commentUserInfo");
//		itemCommentBmobQuery.include("commentUserItem");
		itemCommentBmobQuery.order("createdAt");
		itemCommentBmobQuery.addWhereRelatedTo("itemComments", new BmobPointer(mUserItem));
		itemCommentBmobQuery.findObjects(getBaseContext(), new FindListener<ItemComment>() {
			
			@Override
			public void onSuccess(List<ItemComment> arg0) {
				// TODO Auto-generated method stub
				for (ItemComment mComment : arg0) {
					final ItemComment itemComment = new ItemComment();
					itemComment.setComment(mComment.getComment());
					itemComment.setCommentUserInfo(mComment.getCommentUserInfo());
					itemComment.setCreatedDate(mComment.getCreatedAt());
					itemComments.add(itemComment);
				}
				
				mCommentAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(commentListView);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
}
