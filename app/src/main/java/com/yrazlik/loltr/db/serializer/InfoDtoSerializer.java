package com.yrazlik.loltr.db.serializer;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import com.yrazlik.loltr.model.InfoDto;

/**
 * Created by yrazlik on 14/04/17.
 */

public class InfoDtoSerializer extends TypeSerializer{
    @Override
    public Class<?> getDeserializedType() {
        return InfoDto.class;
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
            return new Gson().fromJson((String) data, InfoDto.class);
        } catch (Exception e) {
            return null;
        }
    }
}
