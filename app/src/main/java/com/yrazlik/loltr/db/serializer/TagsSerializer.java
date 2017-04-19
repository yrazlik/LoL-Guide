package com.yrazlik.loltr.db.serializer;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by yrazlik on 14/04/17.
 */

public class TagsSerializer extends TypeSerializer{
    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(Object data) {
        if(data == null) {
            return null;
        }

        try {
            return new Gson().toJson(data);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object deserialize(Object data) {
        try {
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            return new Gson().fromJson((String) data, listType);
        } catch (Exception e) {
            return null;
        }
    }
}
