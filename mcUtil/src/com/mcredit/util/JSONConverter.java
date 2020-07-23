package com.mcredit.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JSONConverter {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static String toJSON(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T toObject(String value, Class<T> actualObject) {
		try {
			return gson.fromJson(value, actualObject);
		} catch (JsonSyntaxException ex) {
			return null;
		}
	}

	public static <T> T toObject(String value, Type type) {
		try {
			return gson.fromJson(value, type);
		} catch (JsonSyntaxException ex) {
			return null;
		}
	}

	private static String getClassName(String input) {
		if (input == null)
			return "";
		int index = input.lastIndexOf(".");
		if (index != -1) {
			input = input.substring(index);
		}
		if (input.lastIndexOf(".") != -1) {
			input = input.replace(".", "");
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> classInput, String content) {
		Map<String, Object> r = gson.fromJson(content, Map.class);
		String className = getClassName(classInput.getName());
		String innerJson = gson.toJson(r.get(className));
		T _r = gson.fromJson(innerJson, classInput);
		return _r;
	}

	public static <T> ArrayList<T> jsonToList(String jsonString, Class<T> T) {
		JSONArray array = new JSONArray();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			array = jsonArray;
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject(jsonString);
			array.put(jsonObject);
			// TODO: handle exception
		}

		ArrayList<T> data = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			T t = JSONConverter.toObject(array.get(i).toString(), T);
			data.add(t);
		}
		return data;
	}
}
