package com.sien.lib.databmob.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sien.lib.databmob.utils.CPLogUtil;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sien
 * @description  JSON解析管理类
 * 
 **/
public class JsonMananger {

	private static final String tag = JsonMananger.class.getSimpleName();

	private static JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context) throws JsonParseException {
			return json == null ? null : new Date(json.getAsLong());
		}
	};

	private static Gson mgson = new GsonBuilder().registerTypeAdapter(Date.class, deser).setDateFormat(DateFormat.LONG).create();

	/**
	 * 将json字符串转换成java对象
	 * @param json
	 * @param cls
	 * @return
	 * @throws ParseDataException
	 */
	public static <T> T jsonToBean(String json, Class<T> cls) throws ParseDataException {
		return mgson.fromJson(json, cls);
	}

	/**
	 * 将json字符串转换成java List对象
	 * @param json
	 * @param cls
	 * @return
	 * @throws ParseDataException
	 */
	public static <T> List<T> jsonToList(String json, Class<T> cls) throws ParseDataException {
		return mgson.fromJson(json,new TypeToken<List<T>>(){}.getType());
	}
	
	/**
	 * 将bean对象转化成json字符串
	 * @param obj
	 * @return
	 * @throws ParseDataException
	 */
	public static String beanToJson(Object obj) throws ParseDataException{
		String result = mgson.toJson(obj);//.toString();
		CPLogUtil.logDebug(tag, "beanToJson: " + result);
		return result;
	}

	/**
	 * 将json字符串转化成JsonObject
	 * @param jsonstr
	 * @return
     */
	public static JsonObject stringToJsonObject(String jsonstr){
		JsonObject result = new JsonParser().parse(jsonstr).getAsJsonObject();
		return  result;
	}

	/**
	 * 将json字符串转化成JsonArray
	 * @param jsonstr
	 * @return
	 *
	 * String json = "[{\"username\":\"test\"},{\"username\":\"test2\"}]";
	   System.out.println(new JsonParser().parse(json).getAsJsonArray().get(0).getAsJsonObject().get("username").getAsString());
	 */
	public static JsonArray strToJsonArray(String jsonstr){
		JsonParser parser = new JsonParser();
		JsonArray result = parser.parse(jsonstr).getAsJsonArray();
		return result;
	}
	
}
