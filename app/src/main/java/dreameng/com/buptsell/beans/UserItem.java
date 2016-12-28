package dreameng.com.buptsell.beans;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class UserItem extends BmobObject {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String itemDpn, createdDate, itemID;
	private String itemPic, itemPic2, itemPic3, itemPic4, itemPic5;
	private String itemPhoneNumber;
	private UserInfo user;
	private BmobRelation collectionUsers, itemComments;
	private Integer collectionNum;
	
	public String getItemDpn() {
		return itemDpn;
	}

	public void setItemDpn(String itemDpn) {
		this.itemDpn = itemDpn;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	
	public String getItemPic() {
		return itemPic;
	}

	public void setItemPic(String itemPic) {
		this.itemPic = itemPic;
	}


	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public BmobRelation getCollectionUsers() {
		return collectionUsers;
	}

	public void setCollectionUsers(BmobRelation collectionUsers) {
		this.collectionUsers = collectionUsers;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	public String getItemPic3() {
		return itemPic3;
	}

	public void setItemPic3(String itemPic3) {
		this.itemPic3 = itemPic3;
	}

	public String getItemPic2() {
		return itemPic2;
	}

	public void setItemPic2(String itemPic2) {
		this.itemPic2 = itemPic2;
	}

	public String getItemPhoneNumber() {
		return itemPhoneNumber;
	}

	public void setItemPhoneNumber(String itemPhoneNumber) {
		this.itemPhoneNumber = itemPhoneNumber;
	}

	public String getItemPic5() {
		return itemPic5;
	}

	public void setItemPic5(String itemPic5) {
		this.itemPic5 = itemPic5;
	}

	public String getItemPic4() {
		return itemPic4;
	}

	public void setItemPic4(String itemPic4) {
		this.itemPic4 = itemPic4;
	}

	public BmobRelation getItemComments() {
		return itemComments;
	}

	public void setItemComments(BmobRelation itemComments) {
		this.itemComments = itemComments;
	}
	
}
