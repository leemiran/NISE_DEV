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
	public class JLButton
	{
		JLButton()
		{
		}

		void printButton(int nX,int nY,String sName,int nWidth,int nImgTopMargin,String sButtonText,String sAction,String sIcon,int nHeight)
		{
			print("<script language=\"javascript\"> JLButton_printObj("+nX+","+nY+",\""+sName+"\","+nWidth+","+nImgTopMargin+",\""+sButtonText+"\",\""+sAction+"\",\""+sIcon+"\","+nHeight+"); </script>");
		}
	}
%>