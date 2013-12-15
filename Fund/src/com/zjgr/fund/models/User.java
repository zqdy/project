/**
 * 
 */
package com.zjgr.fund.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.zjgr.fund.utils.BeanUtil;

/**
 * @function User.java
 * @author Jin Pang
 * @date 2013-12-15
 */
public class User implements Parcelable {

	private Integer id;
	private String username;
	private String password;
	
    public static final String[] COLUMNS = new String[] {
        "id",
        "username",
        "password"
    };	

	public static final String TABLE_NAME = "user";
	
    public static final int INDEX_ID = 0;
    public static final int INDEX_USERNAME = 1;
    public static final int INDEX_PASSWORD = 2;
    
    public static final String TABLE_CREATE = "create table if not exists " + TABLE_NAME + " ("
            + COLUMNS[0] + " integer primary key AUTOINCREMENT,"
            + COLUMNS[1] + " text,"
            + COLUMNS[2] + " text"
            + ")";	
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		  dest.writeInt(id);
	      dest.writeString(username);
	      dest.writeString(password);
	}
	
	 /***
    *
    * 根据实体类对象，返回ContentValues对象
    * 
    * @param entity： entity的实体类
    */
   public static final ContentValues getContentValue(User entity) {
       ContentValues values = BeanUtil.beanToContentValues(entity);
       return values;
   }
   
   public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() { 
       public User createFromParcel(Parcel source) {  
           return new User();  
       }
       public User[] newArray(int size) {  
           return new User[size];  
       }
   };

}
