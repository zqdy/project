/**
 * 
 * @fileName: DatabaseAdapter.java
 * @description: Database helper class for Processing data
 * @author Jin Pang
 * @date： 2013/12/15
 * @version: 1.0
 *
 */

package com.zjgr.fund.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zjgr.fund.Constant;
import com.zjgr.fund.FundApplication;
import com.zjgr.fund.models.User;
import com.zjgr.fund.utils.SqlUtils;

public class DatabaseAdapter {
	public static String TAG = DatabaseAdapter.class.getSimpleName();
	public static final String DB_NAME = "fund.db"; // 数据库名称
	private static final int version = 1; // 数据库版本

	private static DatabaseHelper mDbHelper;         //用来创建数据库
	private static SQLiteDatabase mSqlLiteDb;		 //用来获得操作的数据库（读写）	
	private static DatabaseAdapter mInstance;		 //获得数据库实例，对数据库中的表进行操作	

	
	/**
	 * 不推荐调用
	 * @param ctx
	 */
	private DatabaseAdapter(Context ctx) {
		mDbHelper = new DatabaseHelper(ctx);
		mSqlLiteDb = mDbHelper.getWritableDatabase();
	}

	/**
	 * 获取数据库操作适配器
	 * @return
	 */
	public static synchronized DatabaseAdapter getDbAdapter(){
		if(mInstance == null){			
			mInstance = new DatabaseAdapter(FundApplication.getContext());
		}
		
		//防止返回后再次启动应用，数据库已经关闭；数据库没打开,则重新打开数据库
		if(null==mDbHelper){
			mDbHelper = new DatabaseHelper(FundApplication.getContext());
		}
		
		if(null == mSqlLiteDb || !mSqlLiteDb.isOpen()){
			mSqlLiteDb = mDbHelper.getWritableDatabase();
		}
		
		return mInstance;
	}
	
	
	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {

			super(context, DB_NAME, null, version);

			// TODO Auto-generated constructor stub

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//cleanDatabase();
			if (Constant.IS_DEBUG) Log.i("DatabaseAdapter===========", "run");
			db.execSQL(User.TABLE_CREATE);
		}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
		}

	}

	/** open database */
	public DatabaseAdapter open() throws SQLException {
//		db = mDbHelper.getWritableDatabase();
		return this;
	}

	/** close database */
	public void close() {
//		mDbHelper.close();
	}
	
	public void destroy(){
		if (Constant.IS_DEBUG) Log.i(TAG,TAG+" destroy!============================");
		mDbHelper.close();
	}

	/** delete row by id */
	public boolean delete(long rowId, String TABLE_NAME) {
		int re = -1;
		synchronized (mSqlLiteDb) {
			re = mSqlLiteDb.delete(TABLE_NAME, "id" + "=" + rowId, null);
		}
		return re > 0;
	}
	
	/** delete row by id */
	public boolean delete(String TABLE_NAME) {
		int re = -1;
		synchronized (mSqlLiteDb) {
			re = mSqlLiteDb.delete(TABLE_NAME, null, null);
		}
		return re > 0;
	}

	/** 
	 * 删除按指定排序条件排序后的前几条记录，使数据库表中的记录个数保持为maxCount条
	 * */
	public boolean delete(String TABLE_NAME,String orderBy,int maxCount) {
		if(maxCount<=0){
			return false;
		}
		//查询数据库
		Cursor cursor = mSqlLiteDb.query(TABLE_NAME,null,null,null,null,null,orderBy,null);
		
		//数据库有记录
		if(null != cursor && cursor.moveToFirst()){
			//设置删除的个数
			int count = cursor.getCount()-maxCount;
			//开始删除
			for(int i=0;i<count;i++){
				delete((long)(cursor.getInt(cursor.getColumnIndex("id"))),TABLE_NAME);
				cursor.moveToNext();
			}
		}
		
		return true;
	}
	
	/** query all row */
	public Cursor queryAll(String TABLE_NAME, String[] columns,String selection, String[] selectionArgs, String groupBy
			,String having, String orderBy, String limit) {
		Cursor cursor = mSqlLiteDb.query(TABLE_NAME, columns, selection, selectionArgs,groupBy, having, orderBy, limit);	
		return cursor;    
	}

	/** query all row */
	@SuppressWarnings({ "rawtypes"})
	public List queryAll(Class<?> clazz, String TABLE_NAME, String[] columns,String selection, String[] selectionArgs, String groupBy
			,String having, String orderBy, String limit) {
		Cursor cursor = null;
		List list = null;
		try {
			cursor = mSqlLiteDb.query(TABLE_NAME, columns, selection, selectionArgs,groupBy, having, orderBy, limit);
			list = SqlUtils.cursor2PrivateVOList(cursor, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR @：queryAll");
		} finally {
			if(cursor != null){
			   cursor.close();
			}
		}
		return list;
	}
	
	/** query row by id */
	public Cursor queryById(Integer id, String TABLE_NAME, String[] columns) {
		Cursor cursor = null;
		cursor=mSqlLiteDb.query(TABLE_NAME, columns, "id=?",new String[] { id.toString() }, null, null, null);	
		return cursor;
	}
	
	/** query row by id */
	public Class<?> queryById(Class<?> clazz, Integer id, String TABLE_NAME, String[] columns) {
		Object obj = null;
		Cursor cursor = null;
		try {
			cursor=mSqlLiteDb.query(TABLE_NAME, columns, "id=?",new String[] { id.toString() }, null, null, null);	
			obj = SqlUtils.cursor2PrivateVO(cursor, clazz);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR @：queryById");
		} finally {
			if(cursor != null){
			   cursor.close();
			}
		}
		return (obj != null ? (Class<?>)obj : null);
	}

	/** insert data */
	public long insert(String TABLE_NAME, ContentValues values) {
		long re = -1;
		re = mSqlLiteDb.insert(TABLE_NAME, null, values);
		return re;

	}

	/** database is open state */
	public boolean isOpen() {
		if (null == mSqlLiteDb)
			return false;
		else
			return mSqlLiteDb.isOpen();
	}

	/** delete data for where */
	public boolean delete(String table, String whereClause, String[] whereArgs) {
		int re = -1;
		re = mSqlLiteDb.delete(table, whereClause, whereArgs);
		return 0 < re;
	}

	public void beginTransaction() {
		mSqlLiteDb.beginTransaction();
	}

	public void setTransactionSuccessful() {
		mSqlLiteDb.setTransactionSuccessful();
	}

	public void endTransaction() {
		mSqlLiteDb.endTransaction();
	}
	
	public void commitTransaction(){
		setTransactionSuccessful();
		endTransaction();
	}

	public void execSQL(String sql) {
		mSqlLiteDb.execSQL(sql);
	}
	
	public Cursor querySQL(String sql,String [] selectionArgs) {
		Cursor curson = null;
		curson = mSqlLiteDb.rawQuery(sql, selectionArgs);
		return curson;
	}

	/** update data */
	public void update(String TABLE_NAME, Integer id, ContentValues values) {
		mSqlLiteDb.update(TABLE_NAME, values, "id=?", new String[] { id.toString() });
	}
	
	//更新数据库表，指定条件
	public void update(String TABLE_NAME, ContentValues values,String whereClause,String[] whereArgs ) {
		mSqlLiteDb.update(TABLE_NAME, values, whereClause, whereArgs);
	}
	
	//获得数据库表
	public static SQLiteDatabase getDatabase() {
		getDbAdapter();
		return mSqlLiteDb;
	}
	
	/** 清空表中的数据 */
	public static boolean cleanTable(String TABLE_NAME) {
		int re = -1;
		re = mSqlLiteDb.delete(TABLE_NAME, null, null);
		return re > 0;
	}
	

	
	//清空数据库中表的记录
	public static void cleanDatabase(){
		if (Constant.IS_DEBUG) Log.i(TAG,TAG+" entry cleanDatabase");
		if(null==mInstance){
			getDbAdapter();
		}
		cleanTable(User.TABLE_NAME);
	}
}
