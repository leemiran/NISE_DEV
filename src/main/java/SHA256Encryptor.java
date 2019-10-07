public class SHA256Encryptor {


	public static String SHA256Encryptor(String str) {
		String SHA = null;
		try {
			java.security.MessageDigest sh = java.security.MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				//sb.append(Integer.toString((byteData[i] & 0xff) + 256, 16).substring(1));

				/*
				2^4  = 16B   = b0001 0000 = 0x10
				2^5  = 32B   = b0010 0000 = 0x20
				2^6  = 64B   = b0100 0000 = 0x40
				2^7  = 128B  = 0x80
				2^8  = 256B  = b0001 0000 0000 = 0x100
				2^10 = 1KB   = b0100 0000 0000 = 0x400
				2^20 = 1MB   = b0001 0000 0000 0000 0000 0000 = 0x100000
				2^30 = 1GB   = 0x40000000
				2^40 = 1TB   = 0X10000000000
				4KB  = 0x1000 
				64KB = 0x10000 
				1MB  = 0x100000
				*/				
			}
			SHA = sb.toString();
			//System.out.println("SHA Key Value : " + SHA + " , Length : " + SHA.length());
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
			SHA = "Exception : " + e.getMessage();
		}
		return SHA;
	}


	public static void main(String args[]){

		String rtrnValue = SHA256Encryptor("kang70417!@#$%");
//		String rtrnValue = SHA256Encryptor(args[0]);
		System.out.println(rtrnValue);
	}
}


