<%
/*
*  JDC (Java DHTML Control) by Hank Moon (Han-Goo Moon in Korean)
*  Copyright (C) 2003 Hank Moon
*
*  This program is distributed in the hope that it will be useful,
*  You can use it and/or modify it, If you do not use it for commercial purpose
*  If you want it to use for commercial purpose, 
*  You have to buy or receive a copy of the License to use this software from 
*  Hank Moon or HMSOFT Inc., Ltd. (www.hmsoft.org)
*
*  Hank Moon , HMSOFT Inc., Ltd. <hankmoon@hmsoft.org>
*
*/
%>

<%!
public class JLRuntimeClass extends JLObj
{
	String m_sFileName;
	FileOutputStream m_fos = null;
	OutputStreamWriter m_osr = null;
	BufferedWriter m_bw = null;
	String m_sUrl;
	String m_sClassName;
	String m_sClassExt;
	String m_sInstName;
	String m_sInstExt;

	String m_sClassHome;	// library의 disk상의 path
	String m_sClassPath;	// library의 web path
	String m_sHome = "";
	String m_sPath = "";

	JLRuntimeClass()
	{
		m_sInstName = "";
		m_sFileName = "";
		m_sUrl = "";
		m_sClassName = "";
		m_sClassExt = "java";
		m_sInstExt = "jsp";
		m_sClassHome = "";
		m_sClassPath = "";
		m_sHome = m_sClassHome;
		m_sPath = m_sClassPath;

		JLJsp jsp = new JLJsp();
		setClassHome(jsp.getValue("classhome"));
		setClassPath(jsp.getValue("classpath"));
		setHome(jsp.getValue("home"));
		setPath(jsp.getValue("path"));
	}

	public void setHome(String sHome)
	{
		m_sHome = sHome;
	}
	
	public void setPath(String sPath)
	{
		m_sPath = sPath;
	}
	
	public void setClassHome(String sHome)
	{
		m_sClassHome = sHome;
	}

	public void setClassPath(String sPath)
	{
		m_sClassPath = sPath;
	}

	public void setClassName(String sName)
	{
		m_sClassName = sName;
	}
	
	public String getCode()
	{
		return "";
	}

	public String getClassPath()
	{
		return m_sClassPath;
	}

	public String getClassHome()
	{
		return m_sClassHome;
	}

	public String createInstance(String sHome,String sPath,String sClassName)
	{
		m_sClassName = sClassName;
		m_sInstName = m_sClassName;
		
		String sNameExt = m_sInstName+"."+m_sInstExt;
		m_sUrl = JLString.BuildFilePath(sPath,sNameExt,"/");
		String sClassNameExt = m_sClassName+"."+m_sClassExt;
		{
			String sInstFileName = JLString.BuildFilePath(sHome,m_sInstName+"."+m_sInstExt,"/");
			String sClassFileName = JLString.BuildFilePath(sHome,sClassNameExt,"/");
			File fClass = new File(sClassFileName);
			File fInst = new File(sInstFileName);
			if (fClass.lastModified() < fInst.lastModified())
				return m_sUrl;
			//debugPrint(" create file : " + sInstFileName);
			fInst.delete();
			fInst = null;
		}
		String sHtml = getCode();
		m_sFileName = JLString.BuildFilePath(sHome,sNameExt,"/");
		//debugPrint(m_sFileName);
		openFile(m_sFileName);
		printEx(sHtml);
		closeFile();
		return m_sUrl;
	}

	public void openFile(String sFile)
	{
		if (JLString.IsEmpty(sFile))
			return;
		try 
		{
			m_fos = new FileOutputStream(sFile,true);
			m_osr = new OutputStreamWriter(m_fos);
			m_bw = new BufferedWriter(m_osr);
		}
		catch (FileNotFoundException fe)
		{
			debugPrint("fail to open file : " + sFile);
			// debugPrint(fe.getMessage());
		}
		catch (IOException e)
		{
			debugPrint("fail to open file : " + sFile);
			// debugPrint(e.getMessage());
		}
	}

	public void closeFile()
	{
		if (m_bw == null)
			return;
		try 
		{
			m_bw.flush();
			m_bw.close();
			m_bw = null;
		}
		catch (FileNotFoundException fe)
		{
		}
		catch (IOException e)
		{
		}
	}

	void printEx(String sStr)
	{
		if (m_bw == null)
		{
			return;
		}
		try 
		{
			m_bw.write(sStr);
		}
		catch (IOException e)
		{
			debugPrint("fail to write : " + m_sFileName);
			// debugPrint(e.getMessage());
		}
	}
}
%>
