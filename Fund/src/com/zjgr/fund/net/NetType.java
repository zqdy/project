/**
* @NetType.java
* @description:
* 网络类型
* @author: Jin Pang 2013/12/15
* @version: 1.0.0
*/

package com.zjgr.fund.net;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class NetType {
	/**
	 * 中国电信ctwap
	 */
	public static String CTWAP = "ctwap";

	/**
	 * 中国电信ctnet
	 */
	public static String CTNET = "ctnet";

	/**
	 * 中国移动cmwap
	 */
	public static String CMWAP = "cmwap";

	/**
	 * 中国移动cmnet
	 */
	public static String CMNET = "cmnet";

	/**
	 * 3G wap 中国联通3gwap APN
	 */
	public static String GWAP_3 = "3gwap";

	/**
	 * 3G net 中国联通3gnet APN
	 */
	public static String GNET_3 = "3gnet";

	/**
	 * uni wap 中国联通uni wap APN
	 */
	public static String UNIWAP = "uniwap";

	/**
	 * uni net 中国联通uni net APN
	 */
	public static String UNINET = "uninet";
	
	
	/**
    * APN数据访问地址
    */
   private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

   /**
    * 判断是否为3G网络，只有网络接入点是cmnet、ctnet和3gnet才是3G网络
    * 
    * @param context
    * @return
    */
   public static boolean is3G(Context context) {
       String type = getApnType(context);

       return type.equals(CMNET) || type.equals(CTNET) || type.equals(GNET_3);
   }

   /**
    * 获取手机网络接入点类型
    * 
    * @param context
    * @return
    */
   public static String getApnType(Context context) {
       String type = "unknown";
       Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
       c.moveToFirst();
       try {
           String apn = c.getString(c.getColumnIndex("apn"));
           if (TextUtils.isEmpty(apn)) {
               type = "unknown";
           } else if (apn.startsWith(CTNET)) {
               type = CTNET;
           } else if (apn.startsWith(CTWAP)) {
               type = CTWAP;
           } else if (apn.startsWith(CMWAP)) {
               type = CMWAP;
           } else if (apn.startsWith(CMNET)) {
               type = CMNET;
           } else if (apn.startsWith(GWAP_3)) {
               type = GWAP_3;
           } else if (apn.startsWith(GNET_3)) {
               type = GNET_3;
           } else if (apn.startsWith(UNIWAP)) {
               type = UNIWAP;
           } else if (apn.startsWith(UNINET)) {
               type = UNINET;
           }
       } catch (Exception e) {
           e.printStackTrace();
           type = "unknown";
       }

       return type;
   }

}
