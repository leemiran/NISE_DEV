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
public class JLObj extends Object
{
	String m_sName;
	String m_sClassName;

	public JLObj()
	{
		m_sName = "";
		m_sClassName = "";
	}

	public String getClassName()
	{
		return m_sClassName;
	}

	public void setClassName(String sClass)
	{
		m_sClassName = sClass;
	}

	public String getName()
	{
		return m_sName;
	}

	public void setName(String sName)
	{
		m_sName = sName;
	}
} // JLObj
%>
