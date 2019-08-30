package com.mglj.totorial.framework.tool.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

//@JsonComponent
public class LongListJsonSerializer extends JsonSerializer<List<Long>> {

	@Override
	public void serialize(List<Long> value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if(value != null && value.size() > 0) {
			Object obj = value.get(0);
			if(!(obj instanceof  String)) {
				return;
			}
			String str;
			gen.writeStartArray();
			for(Long item : value) {
				str = item == null ? null : String.valueOf(item);
				gen.writeString(str);
			}
			gen.writeEndArray();
		}
	}

}
