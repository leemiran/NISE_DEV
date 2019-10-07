/*
 * @(#)Encryption.java	1.0 2008. 11. 27
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.library;

import java.security.MessageDigest;

/**
 * 암호화 관련 Util Class
 * 
 * @version 1.0, 2008/11/27
 * @author Chung Jin-pil
 */
public class Encryption {

	private static Encryption instance = null; 
	
	private Encryption() {
	}
	
	public static Encryption getInstance() {
		if ( instance == null ) {
			instance = new Encryption();
		}

		return instance;
	}

	/**
	 * 복호화
	 * 
	 * @param str
	 * @return
	 */
	public String decode(String str) {
		
		int length = str.length();
		StringBuffer resultStr = new StringBuffer();

		try {
			for (int i = length; i >= 1; i--) {
				resultStr.append((char) (((int) str.charAt(i - 1)) - (i % 3)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultStr.toString();
	}

	/**
	 * 암호화
	 * 
	 * @param str
	 * @return
	 */
	public String encode(String str) {
		
		int length = str.length();
		StringBuffer resultStr = new StringBuffer();
		
		try {
			for (int i = 1; i <= length; i++) {
				resultStr.append((char) (((int) str.charAt(length - i)) + (i % 3)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultStr.toString();
	}
	
	/**
	 * md5 암호화
	 * 
	 * @param s
	 * @return
	 */
	public String md5Encode(String str) {
		
		String resultStr = "";
		
		try {
			byte[] byteArray = str.getBytes();

			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteArray);
			
			resultStr = toHex(md5.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;

	}

	/**
	 * 16진수 변경
	 * 
	 * @param digest
	 * @return
	 */
	private String toHex(byte[] digest) {
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < digest.length; i++)
			sb.append(Integer.toHexString((int) digest[i] & 0x00ff));
		
		return sb.toString();
	}	
}
