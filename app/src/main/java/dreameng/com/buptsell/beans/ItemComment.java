package dreameng.com.buptsell.beans;

import cn.bmob.v3.BmobObject;


public class ItemComment extends BmobObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserInfo commentUserInfo;
	private UserItem commentUserItem;
	private String comment, createdDate;
	public UserInfo getCommentUserInfo() {
		return commentUserInfo;
	}
	public void setCommentUserInfo(UserInfo commentUserInfo) {
		this.commentUserInfo = commentUserInfo;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public UserItem getCommentUserItem() {
		return commentUserItem;
	}
	public void setCommentUserItem(UserItem commentUserItem) {
		this.commentUserItem = commentUserItem;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
}
