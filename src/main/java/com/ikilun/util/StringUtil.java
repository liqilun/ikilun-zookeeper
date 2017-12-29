package com.ikilun.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 判断字符是否是英文字母.
	 * 
	 * @param c
	 *            字符
	 * @return true/false
	 */
	public static boolean isAlpha(char c) {
		return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
	}
	public static String formatNumber(Number number) {
		if (number == null) {
			return "0";
		}
		NumberFormat formatter = new DecimalFormat("###,##0.##");  
		return formatter.format(number);  
	}
	/**
	 * 分转元
	 */
	public static BigDecimal changeF2Y(long amount) throws Exception{    
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100));    
    }
	
	public static List<Integer> getIdList(String ids) {
		List<Integer> idList = new ArrayList<Integer>();
		if (StringUtils.isBlank(ids)) {
			return idList;
		}
		List<String> sidList = Arrays.asList(ids.split(","));
		for (String sid : sidList) {
			idList.add(Integer.valueOf(sid));
		}
		return idList;
	}
	public static String getString(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
