//**********************************************************
//1. ��      ��:
//2. ���α׷���: SulmunBean.java
//3. ��      ��:
//4. ȯ      ��: JDK 1.3
//5. ��      ��: 0.1
//6. ��      ��: Administrator 2003-08-18
//7. ��      ��:
//
//**********************************************************

package com.ziaan.sulmun;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.ziaan.library.*;
import com.ziaan.system.*;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SulmunBean {
    public final static String SPLIT_COMMA = ",";
    public final static String SPLIT_COLON = ":";

    public final static String DEFAULT_GRCODE = "N000001";
    public final static String DEFAULT_SUBJ   = "COMMON";

    //public final static String DIST_CODE        = "0010";
    //�����з�
    public final static String DIST_CODE        = "0008";
    //�����з�
    public final static String SUL_TYPE         = "0009";
    

    public final static String OBJECT_QUESTION  = "M1";
    public final static String MULTI_QUESTION   = "M2";
	public final static String COMPLEX_QUESTION = "";
	public final static String SUBJECT_QUESTION = "S1";
    public final static String FSCALE_QUESTION = "T1";
    public final static String SSCALE_QUESTION = "T2";
    
    
    public final static String F_GUBUN        = "";
    public final static String SUBJECT_SULMUN  = "S";
    public final static String TARGET_SULMUN = "T";
    public final static String COMMON_SULMUN  = "C";
    public final static String CONTENTS_SULMUN   = "O";

    public SulmunBean() {}


}