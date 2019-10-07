/*
 * @(#)KTUtil.java	1.0 2008. 11. 27
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ziaan.library.Encryption;

/**
 * KT 에 관련된 Util Class
 * 
 * @version 1.0, 2008/11/27
 * @author Chung Jin-pil
 */
public class KTUtil {
	
	private static KTUtil instance = null;
	private Logger logger = Logger.getLogger(this.getClass());
	
	private KTUtil() {
	}
	
	public static KTUtil getInstance() {
		if ( instance == null ) {
			instance = new KTUtil();
		}
		
		return instance;
	}	
	
	/**
	 * md5 암호화한 문자열을 반환한다
	 * 
	 * @param str 암호화할 문자열
	 * @return md5 암호화된 문자열 
	 */
	public String md5Encode(String str) {
		Encryption enc = Encryption.getInstance();
		return enc.md5Encode(str);
	}

	/**
	 * Content Directory를 생성한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 생성 여부
	 * @throws Exception
	 */
	public boolean createDirectory(String contentPath, String contentCode) throws Exception {
		boolean result = false;
		
		File directory = new File( contentPath + contentCode );
		
		if ( !directory.exists() ) {
			result = directory.mkdir();
		}
		
		return result;
	}

	/**
	 * Content Directory를 삭제한다.
	 * 
	 * @param	contentPath	컨텐츠 경로
	 * @param	contentCode	컨텐츠 코드
	 * @return	Directory 삭제 여부
	 * @throws Exception
	 */
	public boolean deleteDirectory(String contentPath, String contentCode) throws Exception {
		boolean result = false;
		
		try {
			FileUtils.forceDelete(new File(contentPath + contentCode));
			result = true;
		} catch( IOException ioe ) {
			result =  false;
		}

		return result;
	}

	/**
	 * 심볼릭 링크를 생성한다. (linux system)
	 * 
	 * @param dir
	 * @param encryptedDir
	 * @return
	 */
	public boolean createLink(String dir, String encryptedDir) {
		boolean result = false;
		
		File srcDirectory = new File(dir);
		File destDirectory = new File(encryptedDir);
		
		if ( srcDirectory.exists() && !destDirectory.exists() ) {
			try {
				Runtime.getRuntime().exec("/bin/ln -s " + srcDirectory + " " + destDirectory );
				result = true;
			} catch( Exception ioe ) {
				result = false;
			}
		}
		
		return result;
	}
}
