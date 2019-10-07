package com.ziaan.scorm2004.common;

public class StringUtil
{
    public static final int LEFT_PADDING  = 1;
    public static final int RIGHT_PADDING = 2;

    /**
     * source byte에서 begin index에서 end index까지 byte단위로 자르고자 할때 사용
     * @param src - 자르고자 하는 byte[]
     * @param beginIdx - begin index
     * @param endIdx   - end index
     * @return - 자른 byte[]
     */
    public static byte[] subByte(byte[] src, int beginIdx, int endIdx){
        if(src == null || src.length == 0){
            return src;
        }

        int differByteCnt = endIdx - beginIdx;
        if( differByteCnt < 0){
            throw new IllegalArgumentException ("end index 값 [" + endIdx + "] 이 begin index [" +  beginIdx + "] 보다 작습니다.");
        }

        byte[] resultBytes = new byte[differByteCnt];
        int resultIdx        = 0;
        for( int idx = beginIdx; idx < endIdx ; idx++ ){
            resultBytes[resultIdx++] = src[idx];
        }
        return resultBytes;
    }

    /**
     * source String에서 begin index에서 end index까지 byte단위로 자르고자 할때 사용
     * @param src - 자르고자 하는 String
     * @param beginIdx - begin index
     * @param endIdx   - end index
     * @return - byte 단위로 잘려진 String
     */
    public static String substringByte(String src, int beginIdx, int endIdx){
        return new String(subByte(src.getBytes(), beginIdx, endIdx));
    }

    /**
     * source String에서 findStr문자열을 찾고 대체문자열로 대체한다.
     * @param src         - 변경하고자 하는 String
     * @param findStr     - 찾고자 하는 문자열
     * @param replacement - 대체문자열
     * @return - 대체된 문자열
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

        //전문 문자열 앞에 filler를 추가함
        //한글 byte단위 처리로 수정
        byte[] strBytes = val.getBytes();
        int valLen = strBytes.length;

        if(valLen > totLen){
            //한글 byte단위로 처리하도록 수정
            val =  substringByte( val ,0, totLen);
        }

        if(direct == RIGHT_PADDING){
          cb.append(val);     // 실제값 추가
        }

        if(valLen < totLen){
            for(int i=0; i<(totLen - valLen); i++){
              cb.append(pad);  // PAD(예 공백, '0') 한개 추가
            }
        }

        if(direct == LEFT_PADDING){
            cb.append(val);     // 실제값 추가
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
     * 입력된 문자열이 null인경우 ""를 아니면 입력문자열을 리턴
     * @param inStr
     * @return
     */
    public static String null2Str(String inStr){
        return inStr==null?"":inStr;
    }

    /**
     * 입력된 문자열이 null인경우 ""를 아니면 입력문자열을 trim()하여 리턴
     * @param inStr
     * @return
     */
    public static String null2TrimStr(String inStr){
        return inStr==null?"":inStr.trim();
    }


    /**
     * 입력문자열을 double형으로 변환하여 리턴, 에러인경우 0을 리턴
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
     * 입력된 문자열을 long형으로 변환하여 리턴, 에러인경우 0을 리턴
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
     * 입력된 문자열을 int 형으로 변환하여 리턴, 에러인경우 0을 리턴
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
     * 입력된 문자열이 숫자형이면 trim하여 리턴, 아닌경우 "0"문자열을 리턴
     * @param val
     * @return
     */
    public static String valueOfNumStr(String val){
        return null2TrimStr(val); //TODO 후에 숫자형 체크후 "0" 또는 원숫자로..
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
