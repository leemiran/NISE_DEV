package common;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class NameCheck
{
  public int ErrCode;
  private String ChkName;
  private String Jumin;
  private String SiteCode;
  private String EncJumin;
  private int TimeOut;
  final short PROC_OK = 0;
  final short DATA_ERR = 21;

  public NameCheck()
  {
    this.ErrCode = 0;
    this.ChkName = "";
    this.Jumin = "";
    this.SiteCode = "";
    this.EncJumin = "";
    this.TimeOut = 0;
  }

  public String getEnc()
  {
    return this.EncJumin;
  }

  public void setChkName(String paramString)
  {
    String[] arrayOfString;
    int i;
    this.ChkName = paramString;
    try
    {
      arrayOfString = null;
      arrayOfString = new String[] { "KS_C_5601-1987", "EUC-KR", "ISO_8859-1", "ISO_8859-2", "ISO-10646-UCS-2", "IBM037", "IBM273", "IBM277", "IBM278", "IBM280", "IBM284", "IBM285", "IBM297", "IBM420", "IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852", "IBM855", "IBM857", "IBM860", "IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868", "IBM869", "IBM870", "IBM871", "IBM918", "IBM1026", "Big5-HKSCS", "UNICODE-1-1", "UTF-16BE", "UTF-16LE", "UTF-16", "UTF-8", "ISO-8859-13", "ISO-8859-15", "GBK", "GB18030", "JIS_Encoding", "Shift_JIS", "Big5", "TIS-620", "us-ascii", "iso-8859-1", "iso-8859-2", "iso-8859-3", "iso-8859-4", "iso-8859-5", "iso-8859-6", "iso-8859-7", "iso-8859-8", "iso-8859-9", "koi8-r", "euc-cn", "euc-tw", "big5", "euc-jp", "shift_jis", "euc-kr" };

      for (i = 0; i < arrayOfString.length; ++i)
      {
        for (int j = 0; j < arrayOfString.length; ++j)
          if (i != j)
          {
            String str = new String(paramString.getBytes(arrayOfString[i]), arrayOfString[j]);
          }
      }
    }
    catch (Exception localException)
    {
    }
  }

  public String setJumin(String paramString)
  {
    this.Jumin = paramString.trim();
    return String.valueOf(getEncJumin(this.Jumin, 21));
  }

  public void setSiteCode(String paramString)
  {
    this.SiteCode = paramString;
  }

  public void setTimeOut(int paramInt)
  {
    this.TimeOut = paramInt;
  }

  public String getRtn()
  {
    return getNameCheck();
  }

  private int getRandom()
  {
    return Math.abs(new Long(System.currentTimeMillis()).intValue());
  }

  private String getNameCheck()
  {
    String str1 = "";
    Socket localSocket = null;
    InputStream localInputStream = null;
    PrintWriter localPrintWriter = null;
    try
    {
      int i = getRandom();
      String str2 = "http://203.234.219.72/cnm.asp";
      URL localURL = new URL(str2);
      String str3 = localURL.getHost();
      int j = 81 + i % 5;
      String str4 = localURL.getFile();
      localSocket = new Socket(str3, j);
      localSocket.setSoTimeout(this.TimeOut);
      localPrintWriter = new PrintWriter(localSocket.getOutputStream(), false);
      localInputStream = localSocket.getInputStream();
      StringBuffer localStringBuffer1 = new StringBuffer();
      localStringBuffer1.append(URLEncoder.encode("a3", "euc-kr") + "=" + URLEncoder.encode(this.ChkName, "euc-kr") + "&");
      localStringBuffer1.append(URLEncoder.encode("a2", "euc-kr") + "=" + URLEncoder.encode(this.EncJumin, "euc-kr") + "&");
      localStringBuffer1.append(URLEncoder.encode("a1", "euc-kr") + "=" + URLEncoder.encode(this.SiteCode, "euc-kr"));
      int k = localStringBuffer1.toString().length();
      StringBuffer localStringBuffer2 = new StringBuffer();
      localStringBuffer2.append("POST " + str4 + " HTTP/1.1\n");
      localStringBuffer2.append("Accept: */*\n");
      localStringBuffer2.append("Connection: close\n");
      localStringBuffer2.append("Host: wtname.creditbank.co.kr\n");
      localStringBuffer2.append("Content-Type: application/x-www-form-urlencoded\n");
      localStringBuffer2.append("Content-Length: " + k + "\r\n");
      localStringBuffer2.append("\r\n");
      localStringBuffer2.append(localStringBuffer1.toString());
      localPrintWriter.print(localStringBuffer2.toString());
      localPrintWriter.flush();
      localStringBuffer2.setLength(0);
      String str5 = "";
      int l = 0;
      for (int i1 = 1; (i1 != 0) && (l != -1); i1 = ((l = localInputStream.read()) != 61) ? 1 : ((l = localInputStream.read()) != 116) ? 1 : ((l = localInputStream.read()) != 108) ? 1 : ((l = localInputStream.read()) != 117) ? 1 : ((l = localInputStream.read()) != 115) ? 1 : ((l = localInputStream.read()) != 101) ? 1 : ((l = localInputStream.read()) != 114) ? 1 : 0);
      byte[] arrayOfByte = new byte[2];
      localInputStream.read(arrayOfByte);
      localPrintWriter.close();
      localInputStream.close();
      localSocket.close();
      localSocket = null;
      localInputStream = null;
      localPrintWriter = null;
      str1 = new String(arrayOfByte, "KSC5601").toString();
    }
    catch (MalformedURLException localMalformedURLException)
    {
      if (localPrintWriter != null)
        try
        {
          localPrintWriter.close();
          localPrintWriter = null;
        } catch (Exception localException2) {
        }
      if (localInputStream != null)
        try
        {
          localInputStream.close();
          localInputStream = null;
        } catch (Exception localException3) {
        }
      if (localSocket != null)
        try
        {
          localSocket.close();
          localSocket = null;
        } catch (Exception localException4) {
        }
      str1 = "62";
    }
    catch (NoRouteToHostException localNoRouteToHostException)
    {
      if (localPrintWriter != null)
        try
        {
          localPrintWriter.close();
          localPrintWriter = null;
        } catch (Exception localException5) {
        }
      if (localInputStream != null)
        try
        {
          localInputStream.close();
          localInputStream = null;
        } catch (Exception localException6) {
        }
      if (localSocket != null)
        try
        {
          localSocket.close();
          localSocket = null;
        } catch (Exception localException7) {
        }
      str1 = "61";
    }
    catch (Exception localException1)
    {
      if (localPrintWriter != null)
        try
        {
          localPrintWriter.close();
          localPrintWriter = null;
        } catch (Exception localException8) {
        }
      if (localInputStream != null)
        try
        {
          localInputStream.close();
          localInputStream = null;
        } catch (Exception localException9) {
        }
      if (localSocket != null)
        try
        {
          localSocket.close();
          localSocket = null;
        } catch (Exception localException10) {
        }
      str1 = "63";
    }
    finally
    {
      if (localPrintWriter != null)
        try
        {
          localPrintWriter.close();
          localPrintWriter = null;
        } catch (Exception localException11) {
        }
      if (localInputStream != null)
        try
        {
          localInputStream.close();
          localInputStream = null;
        } catch (Exception localException12) {
        }
      if (localSocket != null)
        try
        {
          localSocket.close();
          localSocket = null;
        } catch (Exception localException13) {
        }
    }
    return str1;
  }

  private int getEncJumin(String paramString, int paramInt)
  {
    String str9;
    String str10;
    String str1 = "13814175622071120141181061768611993108841416921423107181672510714175411266712670119411737212225184002090820525212741182820947153241426022005196831631213938161862274312787181662007815703134602165910388182131264812368213511080911151159881253313998114131777122809215401592122930118692301418370102821668712210148061538513870120181727420355200961795413534192821169714960142231124510693129551063218404145651690617787165521359419983207241159515423148221137115237203671177021155195251257514999190251531020044";
    int i = 0;
    int j = 0;
    int k = 0;
    int l = 0;
    int i1 = 0;
    String str2 = "";
    String str3 = "";
    String str4 = paramString;
    String str5 = "00";
    String str6 = "";
    String str7 = "";
    String str8 = "";
    str4.trim();
    if (paramInt != str4.length())
      return 21;
    i = paramInt - (paramInt / 3 * 3);
    if (i == 2) {
      str4 = str4 + "00";
    }
    else if (i == 1)
      str4 = str4 + "0";
    j = (int)(Math.random() * 100.0D);
    str8 = str1.substring(j * 5, j * 5 + 5);
    k = Integer.valueOf(str8).intValue();
    l = str4.length() / 3 / 2;
    DecimalFormat localDecimalFormat = null;
    localDecimalFormat = new DecimalFormat("00");
    str6 = localDecimalFormat.format(j);
    localDecimalFormat = new DecimalFormat("00000");
    for (i1 = 0; i1 < l; ++i1)
    {
      str9 = str4.substring(i1 * 2 * 3, i1 * 2 * 3 + 3);
      str10 = localDecimalFormat.format(new Integer(str9).intValue() + k);
      str9 = str4.substring((i1 * 2 + 1) * 3, (i1 * 2 + 1) * 3 + 3);
      String str11 = localDecimalFormat.format(new Integer(str9).intValue() + k);
      str6 = str6 + str11;
      str6 = str6 + str10;
    }

    if (l * 2 < str4.length() / 3)
    {
      str9 = str4.substring(i1 * 2 * 3, i1 * 2 * 3 + 3);
      str10 = localDecimalFormat.format(new Integer(str9).intValue() + k);
      str6 = str6 + str10;
    }
    this.EncJumin = str6;
    return 0;
  }
}