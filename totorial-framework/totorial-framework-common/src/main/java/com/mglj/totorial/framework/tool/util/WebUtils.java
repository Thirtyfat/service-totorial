package com.mglj.totorial.framework.tool.util;

import javax.servlet.http.HttpServletRequest;

/**
 * web工具包
 * 
 * @author dingshuangxi
 *
 */
public class WebUtils {

	/**
	 * 获取Web访问域名
	 * 
	 * @param request
	 * @return
	 */
	public static String getWebPath(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName());
		int serverPort = request.getServerPort();
		if (serverPort != 80) {
			sb.append(":" + serverPort);
		}
		sb.append(request.getContextPath());
		return sb.toString();
	}

}
