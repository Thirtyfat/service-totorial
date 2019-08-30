package com.mglj.totorial.framework.tool.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@JsonComponent
public class LongListJsonDeserializer extends JsonDeserializer<List<Long>> {

	private static final Logger logger = LoggerFactory.getLogger(LongListJsonDeserializer.class);
	
	@Override
	public List<Long> deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		List<Long> list = new ArrayList<Long>();
		String[] value = p.readValueAs(String[].class);
		if(value != null && value.length > 0) {
			Long l;
			for(String item : value) {
				try {
					l = (item == null || "".equals(item.trim())) ? null : Long.parseLong(item.toString());
					list.add(l);
				} catch (NumberFormatException e) {
					logger.error("解析长整形错误: " + item, e);
				}
			}
		}
		return list;
	}

}
