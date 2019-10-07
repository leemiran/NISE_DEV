// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 2010-05-07 ¿ÀÈÄ 7:45:41
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DesEncrypter.java

package com.ziaan.library;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import sun.misc.*;

public class DesEncrypter
{

    public DesEncrypter(SecretKey key)
    {
        try
        {
            ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            ecipher.init(1, key);
            dcipher.init(2, key);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public String encrypt(String str)
    {
        try
        {
            byte utf8[] = str.getBytes("UTF8");
            byte enc[] = ecipher.doFinal(utf8);
            return (new BASE64Encoder()).encode(enc);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String str)
    {
        try
        {
            byte dec[] = (new BASE64Decoder()).decodeBuffer(str);
            byte utf8[] = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    Cipher ecipher;
    Cipher dcipher;
}