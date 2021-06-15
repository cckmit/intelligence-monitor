package com.zhikuntech.intellimonitor.fanscada.domain.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author 滕楠
 * @className LongJsonSerializer  long - string
 * @create 2021/5/11 20:40
 **/
public class LongJsonSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        String text = (value == null ? null : String.valueOf(value));
        if (text!=null){
            jsonGenerator.writeString(text);
        }
    }
}