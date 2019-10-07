package com.ziaan.library;

import java.io.UnsupportedEncodingException;

public class CryptoUtil{
    /**
     * ���Ͼ�ȣȭ�� ���̴� ���� ũ�� ����
     */
    public static final int kBufferSize = 8192;
	public static java.security.Key key = null;
	public static final String defaultkeyfileurl = "tripleDesKey.key";

    /**
     * ����Ű ����
     * @return
     */
    public static String key()
    {
    	// DES
        //return "ehd_oanfr_kqoren";
        // TripleDES
        return "ehdgo_anfrhkqore_tksdlak";
        // TripleDES �ڵ����� (���ϼ����� ����)
        //return "";
    }
    
    /**
     * Ű��
     * 24����Ʈ�� ��� TripleDES 16����Ʈ�� ��� DES �ƴϸ� TripleDES
     * @return
     * @throws Exception
     */
    public static java.security.Key getKey() throws Exception {
		if (key().length() == 24) {
    		return getKeyTriDes(key());
    	} else if (key().length() == 16) {
    		return getKeyDes(key());
    	} else {
    		return getKey(defaultkeyfileurl);
    	}
    }
    // Ű �ڵ� �����ϱ� (���ϼ��������� ��밡��/��ġ�� WAS_HOME �� �ȴ�)
	private static java.security.Key getKey(String fileurl) throws Exception{
		if(key == null){
			java.io.File file = new java.io.File(fileurl);
			if(!file.exists()){
				file = makekey();
			}
			if(file.exists()){
				java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileurl));
				key = (java.security.Key)in.readObject();
				in.close();
			} else {
				throw new Exception("��ȣŰ��ü�� ������ �� �����ϴ�.");
			}
		}
		return key;
	}

	
	/**
     * ���Ű �����޼ҵ�
     * @return  void
     * @exception java.io.IOException,java.security.NoSuchAlgorithmException
     */
 
	public static java.io.File makekey() throws java.io.IOException,java.security.NoSuchAlgorithmException{
		return makekey(defaultkeyfileurl);
	}
	public static java.io.File makekey(String filename) throws java.io.IOException,java.security.NoSuchAlgorithmException{
		java.io.File tempfile = new java.io.File(".",filename);
		//javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("DES");
		// �ϴ� TripleDES �� �����ϵ��� �Ѵ�. (��ġ�� WAS_HOME �� �ȴ�)
		javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("DESede");
		generator.init(new java.security.SecureRandom());
		java.security.Key key = generator.generateKey();
		java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(tempfile));
		out.writeObject(key);
		out.close();
		return tempfile;
	}
	
	
    /**
     * ���Ű �������� (DES)
     * @return Key 
     * @exception Exception
     */
    public static java.security.Key getKeyDes(String vkey) throws Exception {
    	javax.crypto.spec.DESKeySpec desKeySpec = new javax.crypto.spec.DESKeySpec(vkey.getBytes());
    	javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance("DES");
    	java.security.Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
    
    /**
     * ���Ű �������� (TripleDES)
     * @return Key 
     * @exception Exception
     */
    public static java.security.Key getKeyTriDes(String vkey) throws Exception {
    	javax.crypto.spec.DESedeKeySpec desKeySpec = new javax.crypto.spec.DESedeKeySpec(vkey.getBytes());
    	javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance("DESede");
    	java.security.Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
	
 
    /**
     * ���ڿ� ��Ī ��ȣȭ
     * @param   ID  ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return  String  ��ȣȭ�� ID
     * @exception Exception
     */
	public static String encrypt(String ID) throws Exception{
	   if ( ID == null || ID.length() == 0 ) return "";
       String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
       javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
	   cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());
	   String amalgam = ID;
	  
	   byte[] inputBytes1 = amalgam.getBytes("UTF8");
	   byte[] outputBytes1 = cipher.doFinal(inputBytes1);
	   sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();        
	   String outputStr1 = encoder.encode(outputBytes1);
	   return outputStr1;
	}
 
    /**
     * ���ڿ� ��Ī ��ȣȭ
     * @param   codedID  ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return  String  ��ȣȭ�� ID
     * @exception Exception
     */
	public static String decrypt(String codedID) throws Exception{
	   if ( codedID == null || codedID.length() == 0 ) return "";
       String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
       javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
	   cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());
	   sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	  
	   byte[] inputBytes1  = decoder.decodeBuffer(codedID);
	   byte[] outputBytes2 = cipher.doFinal(inputBytes1);
	  
	   String strResult = new String(outputBytes2,"UTF8");
	   return strResult;
	}
 
    /**
     * ���� ��Ī ��ȣȭ
     * @param   infile ��ȣȭ�� ����ϴ� ���ϸ�
     * @param   outfile ��ȣȭ�� ���ϸ�
     * @exception Exception
     */
    public static void encryptFile(String infile, String outfile) throws Exception{
    	String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
    	javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE,getKey());
		
		java.io.FileInputStream in = new java.io.FileInputStream(infile);
		java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outfile);
		
		javax.crypto.CipherOutputStream out = new javax.crypto.CipherOutputStream(fileOut, cipher);
		byte[] buffer = new byte[kBufferSize];
		int length;
		while((length = in.read(buffer)) != -1)
			out.write(buffer,0,length);
		in.close();
		out.close();
	}

    /**
     * ���� ��Ī ��ȣȭ
     * @param   infile ��ȣȭ�� ����ϴ� ���ϸ�
     * @param   outfile ��ȣȭ�� ���ϸ�
     * @exception Exception
     */
    public static void decryptFile(String infile, String outfile) throws Exception{
    	String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE,getKey());
 
        java.io.FileInputStream in = new java.io.FileInputStream(infile);
        java.io.FileOutputStream fileOut = new java.io.FileOutputStream(outfile);
 
        javax.crypto.CipherOutputStream out = new javax.crypto.CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[kBufferSize];
        int length;
        while((length = in.read(buffer)) != -1)
            out.write(buffer,0,length);
        in.close();
        out.close();
    }
    
    /**
     * ���� 1Byte �� �߶� HEX(2Byte) �� ��ȯ
     * @param   str �� ���ڿ�
     * @param   ret ��ȯ�� ���ڿ�
     * @exception Exception
     */
	public static String EncordeOnePass(String str) {
		String ret = "";
		if (str.length() > 0) {
			// byte array -> hex string
			ret = new java.math.BigInteger(str.getBytes()).toString(16);
		}
		return ret;
	}

    /**
     * ���� 2Byte �� �߶� ASCII(1Byte) �� ��ȯ
     * @param   str �� ���ڿ�
     * @param   ret ��ȯ�� ���ڿ�
     * @exception Exception
     */
    public static String DecordeOnePass(String str) {
    	String ret = "";
        try {
    		if (str.length() > 0) {    	
    	    	// hex string -> byte array
    	    	byte[] bytes = new java.math.BigInteger(str, 16).toByteArray();
    	    	ret = new String(bytes, "EUC-KR");
    		}
        } catch (UnsupportedEncodingException e) {

        } 
		return ret;		
	}

    
    /**
     * Test ��������
     * @param   str �� ���ڿ�
     * @param   ret ��ȯ�� ���ڿ�
     * @exception Exception
     */
    public static String test(String str) {
    	String ret = str;
    	try {
    	} catch (Exception e) {
    		return str;
    	} 
		return ret;		
	
	}    
}
