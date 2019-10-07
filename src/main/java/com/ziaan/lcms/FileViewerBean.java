/*
 * @(#)FileViewerBean.java	1.0 2009. 03. 27
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ziaan.library.ConfigSet;

/**
 * ���Ϻ�� Bean
 *
 * @version		1.0, 2009/03/27
 * @author		Chung Jin-pil
 */
public class FileViewerBean {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static FileViewerBean instance = null;
	
	private static final String DEFAULT_DELIMETER = ";";

	private List allowedDirectoryList = null;
	
	private String absoultePath = null;
	private String rootDirectory = null;
	private String workDirectory = null;

	private FileViewerBean() {
		setConfiguation();
	}
	
	/**
	 * �⺻������ �Ѵ�
	 */
	private void setConfiguation() {
		try {
			ConfigSet conf = new ConfigSet();
			
			String absoultePath = conf.getProperty("fileviewer.absoulte.path");
			String allowedDirectory = conf.getProperty("fileviewer.allow.dir");
			
			setAbsoultePath(absoultePath);
			setAllowedDirectory(allowedDirectory);
		} catch( Exception e ) {
			String errorMsg = "��������(cresys.properties)�� �ҷ����� ���߿� ������ �߻��߽��ϴ�.  \n" +
							"fileviewer.absoulte.path �� fileviewer.allow.dir �� Ȯ�� �� �ּ���.";
			logger.error(errorMsg);
		}
	}

	public static FileViewerBean getInstance() {
		if ( instance == null )
			instance = new FileViewerBean();

		return instance;
	}

	/**
	 * File ����Ʈ�� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public File[] getFileList( String directory ) {
		
		if ( absoultePath == null )
			return null;
		
		File dir = new File( getAbsoulteDirectoryName(directory) );
		
		File[] files = null;
		if ( dir.isDirectory() ) {
			files = dir.listFiles();
		}

		return files;
	}

	/**
	 * ������ ���丮���� ��ȯ�Ѵ�.
	 * 
	 * @param directory
	 * @return
	 */
	public String getAbsoulteDirectoryName(String directory) {
		String dir = null;
		
		if ( absoultePath == null || absoultePath.equals("") ){
			dir = directory;
		} else if ( absoultePath.endsWith("/") ) {
			dir = absoultePath.substring(0, absoultePath.length()-1) + directory;
		} else if ( absoultePath.endsWith("\\\\") ) {
			dir = absoultePath.substring(0, absoultePath.length()-2) + directory;
		}
		else {
			dir = absoultePath + File.separator + directory;
		}

		return dir;
	}

	/**
	 * ���丮�� ������� ���丮 ��Ͽ� �ִ��� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param directory
	 * @return
	 */
	public boolean isAllowDirectory(String relativeDirectory) {
		if ( allowedDirectoryList == null )
			return false;
		
		for ( int i=0; i<allowedDirectoryList.size(); i++ ) {
			if (isAllow((String) allowedDirectoryList.get(i), getAbsoulteDirectoryName(relativeDirectory)))
				return true;
		}

		return false;
	}
	
	/**
	 * ���丮�� ������� ���丮���� ���θ� ��ȯ�Ѵ�.
	 * *(asterisk) ǥ���� ����, ���� ���丮 ������ ����Ѵ�.
	 * 
	 * @param allowedDirectory
	 * @param directory
	 * @return
	 */
	private boolean isAllow(String allowedDirectory, String absouluteDirectory) {

		boolean isAllow = false;

		if ( allowedDirectory.equals(absouluteDirectory) ) {
			isAllow = true;
		} else if ( allowedDirectory.endsWith("*") ) {
			String directoryExceptForAsteriskChar = allowedDirectory.substring(0, allowedDirectory.length()-1);

			if ( allowedDirectory.endsWith("/*") && !absouluteDirectory.endsWith("/") ) {
				absouluteDirectory = absouluteDirectory + "/";
			}

			isAllow = isMatchDirectory(directoryExceptForAsteriskChar, absouluteDirectory); 
		}

		logger.debug( "��� ���丮 : " + allowedDirectory + ", ��� ���丮 : " + absouluteDirectory );
		
		return isAllow;
	}

	/**
	 * ���丮 ��Ī ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param directoryExceptForAsteriskChar
	 * @param directory
	 * @return
	 */
	private boolean isMatchDirectory(String directoryExceptForAsteriskChar, String directory) {
		boolean isAllow = false;
		
		Pattern p = Pattern.compile( directoryExceptForAsteriskChar + ".*" );
		Matcher m = p.matcher(directory);

		if ( m.matches() )
			isAllow = true;
		
		return isAllow;
	}
	
	/**
	 * ��� ���丮 ����� �����Ѵ�.
	 * 
	 * @param allowedDirectoryList
	 */
	public void setAllowedDirectory(List allowedDirectoryList) {
		this.allowedDirectoryList = allowedDirectoryList; 
	}

	/**
	 * ��� ���丮�� ����Ѵ�. (�⺻ �����ڷ�)
	 * 
	 * @param allowedDirectory
	 */
	public void setAllowedDirectory(String allowedDirectory) {
		setAllowedDirectory(allowedDirectory, FileViewerBean.DEFAULT_DELIMETER );
	}

	/**
	 * ��� ���丮�� ����Ѵ�.
	 * 
	 * @param allowedDirectory
	 * @param delimeter
	 */
	public void setAllowedDirectory(String allowedDirectory, String delimeter) {
		allowedDirectoryList = new ArrayList();

		String[] dir = allowedDirectory.split(delimeter);
		
		for ( int i=0; i<dir.length; i++ ) {
			allowedDirectoryList.add(dir[i]);
		}
	}

	/**
	 * �⺻(����)��θ� �����Ѵ�.
	 * 
	 * @param absoultePath
	 */
	public void setAbsoultePath(String absoultePath) {
		this.absoultePath = absoultePath;
	
	}
	
	/**
	 * root ���丮�� �����Ѵ�.
	 * 
	 * @param rootDir
	 */
	public void setRootDir(String rootDir) {
		rootDirectory = rootDir;
	}

	/**
	 * root ���丮�� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public String getRootDir() {
		return rootDirectory;
	}

	/**
	 * work ���丮�� �����Ѵ�.
	 * 
	 * @param workDir
	 */
	public void setWorkDir(String workDir) {
		workDirectory = workDir;
	}
	
	/**
	 * work ���丮�� ��ȯ�Ѵ�.
	 * 
	 * @return
	 */
	public String getWorkDir() {
		return workDirectory;
	}	

	/**
	 * workingDirectory�� �θ� ���丮 ���ڿ��� ��ȯ�Ѵ�.
	 * 
	 * @param workingDirectory
	 * @return
	 */
	public String getParentDirectory(String workingDirectory) {
		if ( rootDirectory.equals(workingDirectory) )
			return rootDirectory;
		
		File workingDir = new File(workingDirectory);
		return workingDir.getParent().replace("\\", "/");
	}
	
	/**
	 * ������ �����Ѵ�.
	 * 
	 */
	public boolean deleteFile(String workingDirectory, String filename) {
		boolean isDelete = false;

		String name = getAbsoulteDirectoryName(workingDirectory) + File.separator + filename;
		File file = new File( name );

		if ( filename == null || filename.equals("") ||
				workingDirectory == null || workingDirectory.equals("") || !file.exists() )
			return false;

		try {
			FileUtils.forceDelete( file );
			logger.debug( "[DELETE] " +  file.getAbsolutePath() );
			isDelete = true;
		} catch( IOException ioe ) {
			logger.error("[���Ϻ��] ���ϻ��� �߿� ������ �߻��߽��ϴ�.");
			isDelete = false;
		}

		return isDelete;
	}
	
	/**
	 * TODO ��������
	 * workingDirectory�� rootDirectory�� ���� ���丮���� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param workingDirectory
	 * @return
	 */
	public boolean isChildForRootDirectory(String workingDirectory) {
		boolean isChild = false;
		
		if ( rootDirectory == null )
			return false;
		
		if ( getAbsoulteDirectoryName(workingDirectory).startsWith(getAbsoulteDirectoryName(rootDirectory)))
			isChild = true;
		else
			isChild = false;
		
		return isChild;
	}
}
