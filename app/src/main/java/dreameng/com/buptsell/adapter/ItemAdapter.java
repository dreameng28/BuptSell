package dreameng.com.buptsell.adapter;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dreameng.com.buptsell.R;
import dreameng.com.buptsell.activity.MainActivity;
import dreameng.com.buptsell.beans.UserItem;


public class ItemAdapter extends BaseAdapter {
	
	private Context context;
	
	public ItemAdapter(Context context) {
		super();
		this.context = context;
	}
	

	public Context getContext() {
		return context;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.userItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return MainActivity.userItems.get(position);
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
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemcell, null);
			viewHolder.nickname = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.description = (TextView) convertView.findViewById(R.id.item_dpn);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.collectionNum = (TextView) convertView.findViewById(R.id.collection_num);
			viewHolder.user_icon = (ImageView) convertView.findViewById(R.id.user_icon);
			viewHolder.img1 = (ImageView) convertView.findViewById(R.id.item_pic1);
//			viewHolder.img2 = (ImageView) convertView.findViewById(R.id.item_pic2);
//			viewHolder.img3 = (ImageView) convertView.findViewById(R.id.item_pic3);
			convertView.setTag(viewHolder);
		}
		
		UserItem mUserItem = (UserItem) getItem(position);
		//TextView mTextView = (TextView) mRelativeLayout.findViewById(R.id.item_dpn);
		viewHolder = (ViewHolder) convertView.getTag();
		
//			mUserItem.getItemPics().get(0).loadImage(getContext(), viewHolder.img1);
			String url_itemPic = mUserItem.getItemPic();
			Picasso.with(getContext()).load(url_itemPic).resizeDimen(R.dimen.image_horizontal, R.dimen.image_vertical)
			.centerInside().into(viewHolder.img1);
			
//			String url2 = mUserItem.getItemPics().get(1);
//			Picasso.with(getContext()).load(url2).resizeDimen(R.dimen.image_horizontal, R.dimen.image_vertical)
//			.centerInside().into(viewHolder.img2);
//		
//			String url3 = mUserItem.getItemPics().get(2);
//			Picasso.with(getContext()).load(url3).resizeDimen(R.dimen.image_horizontal, R.dimen.image_vertical)
//			.centerInside().into(viewHolder.img3);
//		if((mUserItem.getItemPics().size() > 1) && (mUserItem.getItemPics().get(1) != null)){
//			mUserItem.getItemPics().get(0).loadImageThumbnail(getContext(), viewHolder.img2, 100, 120, 70);
//		}
//		
//		if((mUserItem.getItemPics().size() > 2) && (mUserItem.getItemPics().get(2) != null)){
//			mUserItem.getItemPics().get(0).loadImageThumbnail(getContext(), viewHolder.img3, 100, 120, 70);
//		}
		
		//mUserItem.getItemPics().get(0).loadImageThumbnail(getContext(), viewHolder.img2, 100, 120, 70);
		//mUserItem.getItemPics().get(0).loadImageThumbnail(getContext(), viewHolder.img3, 100, 120, 70);

		//mUserItem.getItemPic2().loadImageThumbnail(getContext(), viewHolder.img2, 100, 120, 70);
		//mUserItem.getItemPic3().loadImageThumbnail(getContext(), viewHolder.img3, 100, 120, 70);
		viewHolder.description.setText(mUserItem.getItemDpn());
		viewHolder.date.setText(mUserItem.getCreatedDate());
		viewHolder.nickname.setText(mUserItem.getUser().getNickname());
		viewHolder.collectionNum.setText(mUserItem.getCollectionNum() + "人收藏");
		String gender = mUserItem.getUser().getGender();
		
		if(gender.equals("男")){
			viewHolder.nickname.setTextColor(Color.BLUE);
			if(mUserItem.getUser().getUser_icon() == null){
				viewHolder.user_icon.setImageResource(R.drawable.icon_boy);
			}
		}
		
		else{
			viewHolder.nickname.setTextColor(Color.parseColor("#FF33CC"));
			if(mUserItem.getUser().getUser_icon() == null){
				viewHolder.user_icon.setImageResource(R.drawable.icon_girl);
			}
		}
		
		if(mUserItem.getUser().getUser_icon() != null){
			String url_userIcon = mUserItem.getUser().getUser_icon();
			Picasso.with(getContext()).load(url_userIcon).resizeDimen(R.dimen.icon_img, R.dimen.icon_img)
			.centerInside().into(viewHolder.user_icon);
		}
		Log.e("user", mUserItem.getUser().getNickname() + " " + gender);
		
		return convertView;
	}
	
}
	
	class ViewHolder{
		TextView nickname, description, date, collectionNum;
		ImageView user_icon, img1;
	}
