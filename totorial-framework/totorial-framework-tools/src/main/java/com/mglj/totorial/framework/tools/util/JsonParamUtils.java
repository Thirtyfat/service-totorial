package com.mglj.totorial.framework.tools.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonParamUtils {
	
	public static Map<String, Long> checkLongByKey(JsonNode jsonNode, String... params) {
		Map<String, Long> ret = new HashMap<String, Long>();
		if (params == null || params.length == 0) {
			return null;
		}
		for (String param : params) {
			if (!StringUtils.hasText(param))
				break;
			JsonNode jn = jsonNode.get(param);
			if (jn == null)
				break;
			boolean canLong = jn.canConvertToLong();
			if (canLong) {
				long l = jn.asLong(0);
				if (l > 0l)
					ret.put(param, l);
			}
		}
		if (Objects.equals(ret.size(), params.length)) {
			return ret;
		}
		return null;
	}
	
	public static Map<String, String> checkStringByKey(JsonNode jsonNode, String... params) {
		Map<String, String> ret = new HashMap<String, String>();
		if (params == null || params.length == 0) {
			return null;
		}
		for (String param : params) {
			if (!StringUtils.hasText(param))
				break;
			JsonNode jn = jsonNode.get(param);
			if (jn == null)
				break;
			String asText = jn.asText("");
			if (!"".equals(asText)) {
				ret.put(param, asText);
			}
		}
		if (Objects.equals(ret.size(), params.length)) {
			return ret;
		}
		return null;
	}
	

}
