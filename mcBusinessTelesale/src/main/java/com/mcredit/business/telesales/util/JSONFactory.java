package com.mcredit.business.telesales.util;

import java.lang.reflect.Type;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author lhquang
 * @created 12/06/2018
 *
 */

public abstract class JSONFactory {
    private static JsonSerializer<Date> date2Timestamp = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    private static JsonDeserializer<Date> timestamp2Date = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
             JsonDeserializationContext context) throws JsonParseException {
          return json == null ? null : new Date(json.getAsLong());
        }
    };
    
    public static Gson create() {
        GsonBuilder localGsonBuilder = new GsonBuilder();
        localGsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        Gson gson = localGsonBuilder.registerTypeAdapter(Date.class, date2Timestamp)
                                    .registerTypeAdapter(Date.class, timestamp2Date)
                                    .disableHtmlEscaping().create();
        return gson;
    }

    public static String toJson(Object object) {        
        return create().toJson(object);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object fromJSON(String json, Class paramClass) throws JsonSyntaxException{
        return create().fromJson(json, paramClass);
    }
    public static <T extends Object> T fromJSON(String json, Type type) {
        return create().fromJson(json, type);
    }
}
