package com.zjgr.fund.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;

public class BeanUtil {

	/**
	 * 拼接某属性的 get或者set方法
	 * 
	 * @param fieldName
	 *            字段名称
	 * @param methodType
	 *            方法类型
	 * @return 方法名称
	 */
	public static String parseMethodName(String fieldName, String methodType) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return methodType + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 *            引用方法的数组
	 * @param fieldMethod
	 *            方法名称
	 * @return true或者false
	 */
	public static boolean haveMethod(Method[] methods, String fieldMethod) {
		for (Method met : methods) {
			if (fieldMethod.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断字符数组中是否有name
	 * @param name
	 * @param source
	 * @return
	 */
	public static boolean haveString(String name, String [] source) {
		for (String sn : source) {
			if (sn.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * bean对象转Map
	 * 
	 * @param obj
	 *            实例对象
	 * @return map集合
	 */
	public static Map<String, String> beanToMap(Object obj) {
		Class<?> cls = obj.getClass();
		Map<String, String> valueMap = new HashMap<String, String>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parseMethodName(field.getName(), "get");
				if (!haveMethod(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls
						.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				String result = null;
				if ("Date".equals(fieldType)) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
					result = sdf.format((Date) fieldVal);

				} else {
					if (null != fieldVal) {
						result = String.valueOf(fieldVal);
					}
				}
				valueMap.put(field.getName(), result);
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;

	}
	
	
	/**
	 * bean对象转Map
	 * 
	 * @param obj
	 *            实例对象
	 * @return map集合
	 */
	public static Map<String, String> beanToMap(Object obj,String pref) {
		Class<?> cls = obj.getClass();
		Map<String, String> valueMap = new HashMap<String, String>();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parseMethodName(field.getName(), "get");
				if (!haveMethod(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls
						.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				String result = null;
				if ("Date".equals(fieldType)) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
					result = sdf.format((Date) fieldVal);

				} else {
					if (null != fieldVal) {
						result = String.valueOf(fieldVal);
					}
				}
				if("null".equals(result) || null == result){
					valueMap.put(pref + field.getName(), "");	
				}else{
					valueMap.put(pref + field.getName(), result);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return valueMap;

	}

	/**
	 * bean to ContentValues
	 * 
	 * @param obj
	 * @return
	 */
	public static ContentValues beanToContentValues(Object obj) {
		Class<?> cls = obj.getClass();
		ContentValues contentValues = new ContentValues();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods(); 
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parseMethodName(field.getName(), "get");
				if (!haveMethod(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls
						.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				if (null == fieldVal) {
					continue;
				}
				if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
					int i = Integer.parseInt(String.valueOf(fieldVal));
					if (i == -1) {
						continue;
					}
					contentValues.put(toDbColumName(field.getName()), i);
				} else {
					contentValues.put(toDbColumName(field.getName()),
							String.valueOf(fieldVal));
				}

			} catch (Exception e) {
				continue;
			}
		}
		return contentValues;

	}
	
	
	
	/**
	 * bean to ContentValues
	 * 
	 * @param obj
	 * @return
	 */
	public static String beanToQueryString(Object obj,String pref,String [] filterColums) {
		Class<?> cls = obj.getClass();
//		ContentValues contentValues = new ContentValues();
		StringBuffer sb = new StringBuffer();
		// 取出bean里的所有方法
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldType = field.getType().getSimpleName();
				String fieldGetName = parseMethodName(field.getName(), "get");
				String columName = toDbColumName(field.getName());
				if (!haveMethod(methods, fieldGetName) || haveString(columName,filterColums)) {
					continue;
				}
				
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
				if (null == fieldVal) {
					continue;
				}
				if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
					int i = Integer.parseInt(String.valueOf(fieldVal));
					if (i == -1 || i == 0) {
						continue;
					}
					sb.append(columName + "=" + i);
//					contentValues.put(toDbColumName(field.getName()), i);
				} else {
					sb.append(columName + "='" +String.valueOf(fieldVal) + "'");
				}
				sb.append(pref);
			} catch (Exception e) {
				continue;
			}
		}
		
		return sb.substring(0, sb.length() - pref.length());

	}
	
	
	/**
	 * 给对象的字段赋值
	 * @param obj	类实例
	 * @param fieldSetMethod	字段方法
	 * @param fieldType	字段类型
	 * @param value
	 */
	public static void setFiedlValue(Object obj,Method fieldSetMethod,String fieldType,Object value){
		   
        try {    
            if (null != value && !"".equals(value)) {    
            	if ("String".equals(fieldType)) {   
                	fieldSetMethod.invoke(obj, value.toString());   
                } else if ("Date".equals(fieldType)) {   
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);   
                    Date temp = sdf.parse(value.toString());    
                    fieldSetMethod.invoke(obj, temp);   
                } else if ("Integer".equals(fieldType)   
                        || "int".equals(fieldType)) {   
                    Integer intval = Integer.parseInt(value.toString());   
                    fieldSetMethod.invoke(obj, intval);   
                } else if ("Long".equalsIgnoreCase(fieldType)) {   
                    Long temp = Long.parseLong(value.toString());   
                    fieldSetMethod.invoke(obj, temp);   
                } else if ("Double".equalsIgnoreCase(fieldType)) {   
                    Double temp = Double.parseDouble(value.toString());   
                    fieldSetMethod.invoke(obj, temp);   
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {   
                    Boolean temp = Boolean.parseBoolean(value.toString());   
                    fieldSetMethod.invoke(obj, temp);   
                } else {   
                	fieldSetMethod.invoke(obj, value); 
//                	Log.e(TAG, TAG  + ">>>>setFiedlValue -> not supper type" + fieldType);   
                } 
			}
                
        } catch (Exception e) {   
//            Log.e(TAG, TAG  + ">>>>>>>>>>set value error.",e);
        	e.printStackTrace();
        }   
    
	}
	
	/**
	 * 给对象的字段赋值
	 * @param obj	类实例
	 * @param fieldSetMethod	字段方法
	 * @param fieldType	字段类型
	 * @param value
	 */
	public static void setFiedlValue(Object obj,Method fieldSetMethod,Field field,Cursor curson){
		   
        try {    
        	String fieldType = field.getType().getSimpleName();
        	int index = curson.getColumnIndex(toDbColumName(field.getName()));
        	if ("String".equals(fieldType)) {   
        		String value = curson.getString(index);
            	fieldSetMethod.invoke(obj, value + "");   
            } else if ("Date".equals(fieldType)) {   
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);   
                String value = curson.getString(index);
                Date temp = sdf.parse(value + "");    
                fieldSetMethod.invoke(obj, temp);   
            } else if ("Integer".equals(fieldType)   
                    || "int".equals(fieldType)) {   
            	Integer value = curson.getInt(index); 
                fieldSetMethod.invoke(obj, value);   
            } else if ("Long".equalsIgnoreCase(fieldType)
            		|| "long".equals(fieldType)) {   
            	Long value = curson.getLong(index);
                fieldSetMethod.invoke(obj, value);   
            } else if ("Double".equalsIgnoreCase(fieldType)
            		|| "double".equals(fieldType)) {   
                Double temp = curson.getDouble(index);   
                fieldSetMethod.invoke(obj, temp);   
            }  else {   
            	String value = curson.getString(index);
            	fieldSetMethod.invoke(obj, value);  
            }  
        } catch (Exception e) {   
        	e.printStackTrace();
        }   
    
	}
	
	/**
	 * 根据类型创建对象
	 * @param clazz	待创建实例的类型
	 * @return	实例对象
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T newInstance(Class<T> clazz) throws Exception {
		if (clazz == null)
			return null;
		T obj = null;
		if (clazz.isInterface()) {
			throw new Exception("unknown interface: " + clazz);
		}else{
			try {
				obj = clazz.newInstance();
			}catch (Exception e) {
				throw new Exception("unknown class type: " + clazz);
			}
		}	
		return obj;
	}
	
	/**
	 * bean to ContentValues
	 * 
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public static <T> T cursonToBean(Cursor curson,Class<T> clazz)  {
//		Class<?> cls = obj.getClass();
//		ContentValues contentValues = new ContentValues();
//		StringBuffer sb = new StringBuffer();
		// 取出bean里的所有方法
		Method[] methods = clazz.getDeclaredMethods();
		Field[] fields = clazz.getDeclaredFields();
		T obj = null;
		try {
			obj = newInstance(clazz);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(obj != null){
			for (Field f : fields) {			        
				String setMetodName = parseMethodName(f.getName(),"set");   
	            if (!haveMethod(methods, setMetodName)) {   
	                continue;   
	            }                 
				try {
					Method fieldMethod = clazz.getMethod(setMetodName, f.getType());
					setFiedlValue(obj, fieldMethod, f,curson);
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}
		}		
		return obj;

	}
	
	/**
	 * bean to ContentValues
	 * 
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public static <T> T cursonToBean(Cursor curson,T obj) {
		Class<?> clazz = obj.getClass();
//		ContentValues contentValues = new ContentValues();
//		StringBuffer sb = new StringBuffer();
		// 取出bean里的所有方法
		Method[] methods = clazz.getDeclaredMethods();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			String setMetodName = parseMethodName(f.getName(),"set");   
            if (!haveMethod(methods, setMetodName)) {   
                continue;   
            }                 
			try {
				Method fieldMethod = clazz.getMethod(setMetodName, f.getType());
				setFiedlValue(obj, fieldMethod, f,curson);
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		
		return obj;

	}

	static String toDbColumName(String name) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, size = name.length(); i < size; i++) {
			char c = name.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				sb.append("_" + Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
