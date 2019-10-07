package kr.masking;

public class MaskingManager {

	public static String masking(String data, int sIdx, int eIdx) {
		
		if( sIdx < 0 ) {
			throw new ArrayIndexOutOfBoundsException("sIdx : " + sIdx);
		}
		
		if( (eIdx+1) > data.length() ) {
			throw new ArrayIndexOutOfBoundsException("eIdx : " + eIdx + ", data size : " + data.length());
		}
		
		if( sIdx > eIdx ) {
			throw new ArrayIndexOutOfBoundsException("sIdx : " + sIdx + ", eIdx : " + eIdx);
		}
		
		StringBuffer buff = new StringBuffer();
		buff.append(data.substring(0, sIdx));
		for( int i = sIdx; i <= eIdx; i++ ) {
			buff.append("*");
		}
		buff.append(data.substring(eIdx+1));
		
		return buff.toString();
	}
	
	public static String maskingCtzNum(String data) {
		
		if( data.indexOf("-") != -1 ) {
			return masking(data, 8, 13);
		} else {
			return masking(data, 7, 12);
		}
	}
	
	public static String maskingCardNum(String data) {
		
		if( data.indexOf("-") != -1 ) {
			return masking(data, 10, 13);
		} else {
			return masking(data, 8, 11);
		}
	}
	
	public static String maskingEmail(String data) {
		
		int idx = data.indexOf("@");
		if( idx != -1 ) {
			return masking(data, idx-2, idx-1);
		} else {
			return masking(data, 0, data.length()-1);
		}
	}
	
	public static String maskingName(String data) {
		
		if( data.length() > 1 ) {
			return masking(data, 1, 1);
		} else {
			return String.valueOf(data);
		}
	}
	
	public static String maskingBirthday(String data, String format) {
		
		String temp = String.valueOf(data);
		
		int idx = -1;
		
		// Year : y
		idx = format.indexOf("y");
		while( idx != -1 ) {
			temp = masking(temp, idx, idx);
			idx = format.indexOf("y", idx+1);
		}
				
		// Month : M
		idx = format.indexOf("M");
		while( idx != -1 ) {
			temp = masking(temp, idx, idx);
			idx = format.indexOf("M", idx+1);
		}
		
		// Day : d
		idx = format.indexOf("d");
		while( idx != -1 ) {
			temp = masking(temp, idx, idx);
			idx = format.indexOf("d", idx+1);
		}
		
		return temp;
	}
	
	public static void main(String args[]) {
		
		System.out.println( maskingBirthday("1111년 11월 11일", "yyyy년 MM월 dd일") );
	}
}
