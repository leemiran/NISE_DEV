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
 * 파일뷰어 Bean
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
	 * 기본설정을 한다
	 */
	private void setConfiguation() {
		try {
			ConfigSet conf = new ConfigSet();
			
			String absoultePath = conf.getProperty("fileviewer.absoulte.path");
			String allowedDirectory = conf.getProperty("fileviewer.allow.dir");
			
			setAbsoultePath(absoultePath);
			setAllowedDirectory(allowedDirectory);
		} catch( Exception e ) {
			String errorMsg = "설정파일(cresys.properties)을 불러오는 도중에 에러가 발생했습니다.  \n" +
							"fileviewer.absoulte.path 및 fileviewer.allow.dir 을 확인 해 주세요.";
			logger.error(errorMsg);
		}
	}

	public static FileViewerBean getInstance() {
		if ( instance == null )
			instance = new FileViewerBean();

		return instance;
	}

	/**
	 * File 리스트를 반환한다.
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
	 * 절대경로 디렉토리명을 반환한다.
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
	 * 디렉토리가 접근허용 디렉토리 목록에 있는지 여부를 반환한다.
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
	 * 디렉토리가 접근허용 디렉토리인지 여부를 반환한다.
	 * *(asterisk) 표현식 사용시, 하위 디렉토리 접근을 허용한다.
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

		logger.debug( "허용 디렉토리 : " + allowedDirectory + ", 대상 디렉토리 : " + absouluteDirectory );
		
		return isAllow;
	}

	/**
	 * 디렉토리 매칭 여부를 반환한다.
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
	 * 허용 디렉토리 목록을 저장한다.
	 * 
	 * @param allowedDirectoryList
	 */
	public void setAllowedDirectory(List allowedDirectoryList) {
		this.allowedDirectoryList = allowedDirectoryList; 
	}

	/**
	 * 허용 디렉토리를 등록한다. (기본 구분자로)
	 * 
	 * @param allowedDirectory
	 */
	public void setAllowedDirectory(String allowedDirectory) {
		setAllowedDirectory(allowedDirectory, FileViewerBean.DEFAULT_DELIMETER );
	}

	/**
	 * 허용 디렉토리를 등록한다.
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
	 * 기본(절대)경로를 저장한다.
	 * 
	 * @param absoultePath
	 */
	public void setAbsoultePath(String absoultePath) {
		this.absoultePath = absoultePath;
	
	}
	
	/**
	 * root 디렉토리를 저장한다.
	 * 
	 * @param rootDir
	 */
	public void setRootDir(String rootDir) {
		rootDirectory = rootDir;
	}

	/**
	 * root 디렉토리를 반환한다.
	 * 
	 * @return
	 */
	public String getRootDir() {
		return rootDirectory;
	}

	/**
	 * work 디렉토리를 저장한다.
	 * 
	 * @param workDir
	 */
	public void setWorkDir(String workDir) {
		workDirectory = workDir;
	}
	
	/**
	 * work 디렉토리를 반환한다.
	 * 
	 * @return
	 */
	public String getWorkDir() {
		return workDirectory;
	}	

	/**
	 * workingDirectory의 부모 디렉토리 문자열을 반환한다.
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
	 * 파일을 삭제한다.
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
			logger.error("[파일뷰어] 파일삭제 중에 문제가 발생했습니다.");
			isDelete = false;
		}

		return isDelete;
	}
	
	/**
	 * TODO 삭제예정
	 * workingDirectory가 rootDirectory의 하위 디렉토리인지 여부를 반환한다.
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
