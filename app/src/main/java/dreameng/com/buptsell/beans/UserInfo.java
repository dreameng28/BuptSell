package dreameng.com.buptsell.beans;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

public class UserInfo extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6237808065664856373L;
	
	private String nickname;
	
	private String gender;
	
	private BmobRelation userItems, myCollections, myComments;
	
	private String user_icon;
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BmobRelation getUserItems() {
		return userItems;
	}

	public void setUserItems(BmobRelation userItems) {
		this.userItems = userItems;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public BmobRelation getMyCollections() {
		return myCollections;
	}

	public void setMyCollections(BmobRelation myCollections) {
		this.myCollections = myCollections;
	}

	public BmobRelation getMyComments() {
		return myComments;
	}

	public void setMyComments(BmobRelation myComments) {
		this.myComments = myComments;
	}
	

}
