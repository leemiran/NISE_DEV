package com.ziaan.library;

import java.io.UnsupportedEncodingException;

public class CryptoUtil{
    /**
     * 파일암호화에 쓰이는 버퍼 크기 지정
     */
    public static final int kBufferSize = 8192;
	public static java.security.Key key = null;
	public static final String defaultkeyfileurl = "tripleDesKey.key";

    /**
     * 고정키 정보
     * @return
     */
    public static String key()
    {
    	// DES
        //return "ehd_oanfr_kqoren";
        // TripleDES
        return "ehdgo_anfrhkqore_tksdlak";
        // TripleDES 자동생성 (단일서버에 한함)
        //return "";
    }
    
    /**
     * 키값
     * 24바이트인 경우 TripleDES 16바이트인 경우 DES 아니면 TripleDES
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
    // 키 자동 생성하기 (단일서버에서만 사용가능/위치는 WAS_HOME 이 된다)
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
				throw new Exception("암호키객체를 생성할 수 없습니다.");
			}
		}
		return key;
	}

	
	/**
     * 비밀키 생성메소드
     * @return  void
     * @exception java.io.IOException,java.security.NoSuchAlgorithmException
     */
 
	public static java.io.File makekey() throws java.io.IOException,java.security.NoSuchAlgorithmException{
		return makekey(defaultkeyfileurl);
	}
	public static java.io.File makekey(String filename) throws java.io.IOException,java.security.NoSuchAlgorithmException{
		java.io.File tempfile = new java.io.File(".",filename);
		//javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("DES");
		// 일단 TripleDES 로 생성하도록 한다. (위치는 WAS_HOME 이 된다)
		javax.crypto.KeyGenerator generator = javax.crypto.KeyGenerator.getInstance("DESede");
		generator.init(new java.security.SecureRandom());
		java.security.Key key = generator.generateKey();
		java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(tempfile));
		out.writeObject(key);
		out.close();
		return tempfile;
	}
	
	
    /**
     * 비밀키 가져오기 (DES)
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
     * 비밀키 가져오기 (TripleDES)
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
     * 문자열 대칭 암호화
     * @param   ID  비밀키 암호화를 희망하는 문자열
     * @return  String  암호화된 ID
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
     * 문자열 대칭 복호화
     * @param   codedID  비밀키 복호화를 희망하는 문자열
     * @return  String  복호화된 ID
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
     * 파일 대칭 암호화
     * @param   infile 암호화을 희망하는 파일명
     * @param   outfile 암호화된 파일명
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
     * 파일 대칭 복호화
     * @param   infile 복호화을 희망하는 파일명
     * @param   outfile 복호화된 파일명
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
     * 값을 1Byte 씩 잘라서 HEX(2Byte) 로 변환
     * @param   str 원 문자열
     * @param   ret 변환된 문자열
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
     * 값을 2Byte 씩 잘라서 ASCII(1Byte) 로 변환
     * @param   str 원 문자열
     * @param   ret 변환된 문자열
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
     * Test 내보내기
     * @param   str 원 문자열
     * @param   ret 변환된 문자열
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
