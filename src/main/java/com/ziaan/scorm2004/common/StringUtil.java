package com.ziaan.scorm2004.common;

public class StringUtil
{
    public static final int LEFT_PADDING  = 1;
    public static final int RIGHT_PADDING = 2;

    /**
     * source byte���� begin index���� end index���� byte������ �ڸ����� �Ҷ� ���
     * @param src - �ڸ����� �ϴ� byte[]
     * @param beginIdx - begin index
     * @param endIdx   - end index
     * @return - �ڸ� byte[]
     */
    public static byte[] subByte(byte[] src, int beginIdx, int endIdx){
        if(src == null || src.length == 0){
            return src;
        }

        int differByteCnt = endIdx - beginIdx;
        if( differByteCnt < 0){
            throw new IllegalArgumentException ("end index �� [" + endIdx + "] �� begin index [" +  beginIdx + "] ���� �۽��ϴ�.");
        }

        byte[] resultBytes = new byte[differByteCnt];
        int resultIdx        = 0;
        for( int idx = beginIdx; idx < endIdx ; idx++ ){
            resultBytes[resultIdx++] = src[idx];
        }
        return resultBytes;
    }

    /**
     * source String���� begin index���� end index���� byte������ �ڸ����� �Ҷ� ���
     * @param src - �ڸ����� �ϴ� String
     * @param beginIdx - begin index
     * @param endIdx   - end index
     * @return - byte ������ �߷��� String
     */
    public static String substringByte(String src, int beginIdx, int endIdx){
        return new String(subByte(src.getBytes(), beginIdx, endIdx));
    }

    /**
     * source String���� findStr���ڿ��� ã�� ��ü���ڿ��� ��ü�Ѵ�.
     * @param src         - �����ϰ��� �ϴ� String
     * @param findStr     - ã���� �ϴ� ���ڿ�
     * @param replacement - ��ü���ڿ�
     * @return - ��ü�� ���ڿ�
     */
    public static String replaceStr(String src, String findStr, String replacement)
    {
        int findIdx  = src.indexOf(findStr);

        int          startIdx = 0;
        String       tmp      = src;
        StringBuffer sb       = null;

        while( findIdx >= 0 )
        {
            sb = new StringBuffer();
            sb.append( tmp.substring(startIdx, findIdx) )
                .append( replacement )
                .append( tmp.substring(findIdx + findStr.length() ,  tmp.length() ) );
            tmp = sb.toString();
            findIdx = tmp.indexOf(findStr);

            startIdx = 0 ;
        }

        return sb.toString();
    }

    public static String fillPadding(String val, char pad, int totLen, int direct)
    {
        StringBuffer cb= new StringBuffer();

        //���� ���ڿ� �տ� filler�� �߰���
        //�ѱ� byte���� ó���� ����
        byte[] strBytes = val.getBytes();
        int valLen = strBytes.length;

        if(valLen > totLen){
            //�ѱ� byte������ ó���ϵ��� ����
            val =  substringByte( val ,0, totLen);
        }

        if(direct == RIGHT_PADDING){
          cb.append(val);     // ������ �߰�
        }

        if(valLen < totLen){
            for(int i=0; i<(totLen - valLen); i++){
              cb.append(pad);  // PAD(�� ����, '0') �Ѱ� �߰�
            }
        }

        if(direct == LEFT_PADDING){
            cb.append(val);     // ������ �߰�
        }
        return cb.toString();
    }

    public static String rtrim(String source)
    {
        int orgLen = source.getBytes().length;
        int len = orgLen;
        int st   = 0;
        int off  = 0;      /* avoid getfield opcode */
        char[] val = new char[len];

        source.getChars(0, len, val, 0);

        while ((st < len) && (val[off + len - 1] <= ' ')) {
            len--;
        }

        return ( (st > 0) || (len < orgLen) ) ? source.substring(st, len) : source;
    }

    public static String ltrim(String source)
    {
        int orgLen = source.getBytes().length;
        int len = orgLen;
        int st   = 0;
        int off  = 0;      /* avoid getfield opcode */
        char[] val = new char[len];

        source.getChars(0, len, val, 0);

        while ((st < len) && (val[off + st] <= ' ')) {
            st++;
        }

        return ( (st > 0) || (len < orgLen) ) ? source.substring(st, len) : source;
    }

    /**
     * �Էµ� ���ڿ��� null�ΰ�� ""�� �ƴϸ� �Է¹��ڿ��� ����
     * @param inStr
     * @return
     */
    public static String null2Str(String inStr){
        return inStr==null?"":inStr;
    }

    /**
     * �Էµ� ���ڿ��� null�ΰ�� ""�� �ƴϸ� �Է¹��ڿ��� trim()�Ͽ� ����
     * @param inStr
     * @return
     */
    public static String null2TrimStr(String inStr){
        return inStr==null?"":inStr.trim();
    }


    /**
     * �Է¹��ڿ��� double������ ��ȯ�Ͽ� ����, �����ΰ�� 0�� ����
     * @param value
     * @return
     */
    public static double str2Double(String value)
    {
      double result = 0;
      try {
        if (value != null && !value.trim().equals("")) {
          result = Double.parseDouble(value);
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return result;
    }

    /**
     * �Էµ� ���ڿ��� long������ ��ȯ�Ͽ� ����, �����ΰ�� 0�� ����
     * @param value
     * @return
     */
    public static long str2Long(String value)
    {
      long result = 0;
      try {
        if (value != null && !value.trim().equals("")) {
          result = Long.parseLong(value);
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return result;
    }

    /**
     * �Էµ� ���ڿ��� int ������ ��ȯ�Ͽ� ����, �����ΰ�� 0�� ����
     * @param value
     * @return
     */
    public static int str2Int(String value)
    {
      int result = 0;
      try {
        if (value != null && !value.trim().equals("")) {
          result = Integer.parseInt(value);
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return result;
  }

    /**
     * �Էµ� ���ڿ��� �������̸� trim�Ͽ� ����, �ƴѰ�� "0"���ڿ��� ����
     * @param val
     * @return
     */
    public static String valueOfNumStr(String val){
        return null2TrimStr(val); //TODO �Ŀ� ������ üũ�� "0" �Ǵ� �����ڷ�..
    }

    public static String replace(String src, char chr, String sReplace)
    {
            try {
                    int idx, len = src.length();
                    StringBuffer buffer = new StringBuffer(len);
                    while ((idx = src.indexOf(chr)) != -1)
                    {
                            buffer.append(src.substring(0, idx));
                            buffer.append(sReplace);
                            if (len == idx+1)
                                    break;
                            src = src.substring(idx + 1);
                    }
                    if (len != idx+1)
                            buffer.append(src);

                    return buffer.toString();
            } catch (NullPointerException e) {
                    return null;
            } catch (Exception e) {
                    return null;
            }
    }

    public static String toCodeStr(String src)
    {
            if (src == null)
                    return "";

            src = replace(src, '\n', "\\n");
            src = replace(src, '\t', "\\t");
            src = replace(src, '\r', "\\r");
            src = replace(src, '\\', "\\\\");
            src = replace(src, '\"', "\\\"");
            src = replace(src, '\'', "\\\'");
            src = replace(src, '>', "\\>");
            src = replace(src, '<', "\\<");
            src = replace(src, ';', "\\;");
            return src;
    }
}
