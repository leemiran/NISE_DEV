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
 * KT �� ���õ� Util Class
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
	 * md5 ��ȣȭ�� ���ڿ��� ��ȯ�Ѵ�
	 * 
	 * @param str ��ȣȭ�� ���ڿ�
	 * @return md5 ��ȣȭ�� ���ڿ� 
	 */
	public String md5Encode(String str) {
		Encryption enc = Encryption.getInstance();
		return enc.md5Encode(str);
	}

	/**
	 * Content Directory�� �����Ѵ�.
	 * 
	 * @param	contentPath	������ ���
	 * @param	contentCode	������ �ڵ�
	 * @return	Directory ���� ����
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
	 * Content Directory�� �����Ѵ�.
	 * 
	 * @param	contentPath	������ ���
	 * @param	contentCode	������ �ڵ�
	 * @return	Directory ���� ����
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
	 * �ɺ��� ��ũ�� �����Ѵ�. (linux system)
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
