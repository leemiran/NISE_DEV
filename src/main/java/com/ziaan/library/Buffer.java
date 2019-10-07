/*
 * Created on 2005. 4. 19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ziaan.library;

import java.io.UnsupportedEncodingException;


public class Buffer {
	private byte[] buffer;
	private static byte[] zero;
	

	
	public Buffer()
	{
		buffer = null;
	}

	public Buffer(byte[] buffer)
	{
		this.buffer = buffer;
	}

	public byte[] getBuffer()
	{
		return buffer;
	}

	public void setBuffer(byte[] buffer)
	{
		this.buffer = buffer;
	}
	
	/* null ���� ���� ��ŭ�� zero ����Ʈ ���� */
	private void initZero(int size)
	{
		zero = new byte[size];
		for(int i = 0; i < size; i ++)
		{
			zero[i] = 0;
		}
	}

	public int getLength()
	{
		if (buffer==null) {
			return 0;
		} else {
			return buffer.length;
		}
	}
	
	/******************* setting bytebuffer ************************/
	
	/* �Է¹��� ��Ʈ���� ����Ʈ�� �ְ� �������̸�ŭ ���ι���Ʈ �ִ´�. */
	public void appendCString(String string, int maxLeng)
	{
		int null_size = 0;
		int buffleng = 0;
		if(string.length() > maxLeng)
			string = string.substring(0,maxLeng);
		
		if (string.length() > 0) {
			byte[] stringBuf = null;
			try {
				stringBuf = string.getBytes("KSC5601");
				buffleng = stringBuf.length;
			} catch (UnsupportedEncodingException e) {
				System.out.println("Error Occured...in appendCString");
				e.printStackTrace();
				// this can't happen as we use ASCII encoding
			} catch (Exception e1){
				System.out.println("Error Occured...in appendCString");
				e1.printStackTrace();
			}
			

			if ((stringBuf != null) && (stringBuf.length > 0)) {
				addBytes(stringBuf, stringBuf.length);
			}
		}
		null_size = maxLeng - buffleng;
		initZero(null_size);
		addBytes(zero,null_size); // always append terminating zero
	}
	
	/* ����Ʈ �����͸� �ִ´�. */
	public void appendBytes(byte[] bytes)
	{
		if (bytes != null) {
			addBytes(bytes, bytes.length);
		}
	}

	/* byte buffer�� �߰��ؼ� ����Ʈ�� �ִ´�. */
	private void addBytes(byte[] bytes, int count)
	{
		int len = getLength();
		byte[] newBuf = new byte[len + count];
		if (len > 0) {
			System.arraycopy(buffer, 0, newBuf, 0, len);
		}
		System.arraycopy(bytes, 0, newBuf, len, count);
		setBuffer(newBuf);
	}
	
	/******************* read bytebuffer ************************/
	

	/* ����Ʈ���� ������ ������ ����Ʈ�� String������ �о���δ�. */
	public String readCString(int leng)
	{
		int len = getLength();
		int zeroPos = 0;
		String result = "NULL";

		if (leng <= len) {
			byte[] temp = readBytes(0, leng, true);
			
			while(temp[zeroPos] != 0){
				zeroPos++;
				if(zeroPos == leng)
					break;
			}
	
			try {
				result = new String(temp, 0, zeroPos, "KSC5601");
			} catch (UnsupportedEncodingException e) {
				// this can't happen as we use ASCII encoding
			}
			return result;
		}else{
			System.err.println(
				"ERR:ByteBuffer.readCString => Request=" + leng + " Have=" + len);
			return result;
		}
	}
	

	/* ������ ���̸�ŭ ����Ʈ������ �о���δ� */
	public byte[] readBytes(int start, int leng, boolean del)
	{
		int len = getLength();
		byte[] result = null;
		if (leng > 0) {
			if ((start + leng) <= len) {
				result = new byte[leng];
				System.arraycopy(buffer, start, result, 0, leng);
				if(del)
					delBytes(leng);
				
				return result;
			} else {
				System.err.println(
					"ERR:ByteBuffer.readBytes => Request="+start+"~"+(start+leng)+ " Have=" + len);
				return result;
			}
		} else {
			return result;
		}
	}
	
	/* ������ ���̸�ŭ�� ����Ʈ�� �����Ѵ�. */
	private void delBytes(int count)
	{
		int len = getLength();
		int lefts = len - count;
		if (lefts > 0) {
			byte[] newBuf = new byte[lefts];
			System.arraycopy(buffer, count, newBuf, 0, lefts);
			setBuffer(newBuf);
		} else {
			setBuffer(null);
		}
	}
}