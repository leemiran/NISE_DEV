// **********************************************************
//  1. ��      ��: ����з��ڵ� OPERATION BEAN
//  2. ���α׷���: ClassifySubjectBean.java
//  3. ��      ��:
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 0.1
//  6. ��      ��: anonymous 2003. 6. 30
//  7. ��      ��:
// **********************************************************
package com.ziaan.course;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.StringManager;

public class ClassifySubjectBean { 

    public ClassifySubjectBean() { }

    /**
    ����з��ڵ� ��ȸ
    @param box          receive from the form object and session
    @return ArrayList   ����з��ڵ� ����Ʈ
    */      
    public ArrayList SelectList(RequestBox box) throws Exception {               
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        ArrayList           list1           = null;
        ArrayList           list2           = null;
        StringBuffer        sbSQL           = new StringBuffer("");
                                            
        ClassifySubjectData data            = null;            
        
        int                iSysAdd          = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 
        
        String              upperclass      = "";
        String              upperclass2     = ""; 
        String              middleclass     = ""; 
        
        try { 
            connMgr = new DBConnectionManager();
            
            list1   = new ArrayList();
            list2   = new ArrayList();
        
            sbSQL.append(" select  a.subjclass                                  \n")
                 .append("     ,   a.upperclass                                 \n")
                 .append("     ,   a.middleclass                                \n")
                 .append("     ,   a.lowerclass                                 \n")
                 .append("     ,   b.upperclassname                             \n")
                 .append("     ,   c.middleclassname                            \n")
                 .append("     ,   a.classname                                  \n")
                 .append(" from    tz_subjatt  a                                \n")
                 .append("     ,   (                                            \n")
                 .append("          select  upperclass                          \n")
                 .append("              ,   classname  upperclassname           \n")
                 .append("          from    tz_subjatt                          \n")
                 .append("          where   middleclass = '000'                 \n")
                 .append("          and     lowerclass  = '000'                 \n")
                 .append("         )            b                               \n")
                 .append("     ,   (                                            \n")
                 .append("          select  upperclass                          \n")
                 .append("              ,   middleclass                         \n")
                 .append("              ,   classname   middleclassname         \n")
                 .append("          from    tz_subjatt                          \n")
                 .append("          where   middleclass != '000'                \n")
                 .append("          and     lowerclass  = '000'                 \n")
                 .append("         )            c                               \n")
                 .append(" where   a.upperclass = b.upperclass                  \n")
                 .append(" and     a.upperclass = c.upperclass( +)              \n")
                 .append(" and     a.middleclass = c.middleclass( +)            \n")
                 .append(" order by a.subjclass, b.upperclassname               \n");
                 
            System.out.println(this.getClass().getName() + "." + "getMaxSubjcode() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");                 
                    
            ls          = connMgr.executeQuery(sbSQL.toString());
            
            while ( ls.next() ) { 
                data    = new ClassifySubjectData();                   
                
                data.setSubjclass       ( ls.getString("subjclass"      ) );
                data.setUpperclass      ( ls.getString("upperclass"     ) );
                data.setMiddleclass     ( ls.getString("middleclass"    ) );
                data.setLowerclass      ( ls.getString("lowerclass"     ) );
                data.setClassname       ( ls.getString("classname"      ) );
                data.setUpperclassname  ( ls.getString("upperclassname" ) );
                data.setMiddleclassname ( ls.getString("middleclassname") );
                data.setLowerclassname  ( ls.getString("classname"      ) );

                list1.add(data);
            }
            
            for ( int i = 0; i < list1.size(); i++ ) { 
                data = (ClassifySubjectData)list1.get(i);
                if ( data.isUpperclass() ) { 
                    if ( getUpperCodeCnt(list1, data.getUpperclass() ) < 2 ) {  
                        list2.add(data);
                    }
                } else if ( data.isMiddleclass() ) { 
                    if ( getMiddleCodeCnt(list1, data.getUpperclass(), data.getMiddleclass() ) < 2 ) {  
                        list2.add(data);
                    }
                } else if ( data.isLowerclass() ) { 
                    list2.add(data);
                }
            }
            
            for ( int i = 0; i < list2.size(); i++ ) { 
                data = (ClassifySubjectData)list2.get(i);
                if ( upperclass.equals(data.getUpperclass() )) { 
                    data.setUpperrowspannum(0);
                } else { 
                    data.setUpperrowspannum(getUpperCodeCnt(list2, data.getUpperclass() ));
                    upperclass = data.getUpperclass();
                }

                if ( upperclass2.equals(data.getUpperclass() ) && middleclass.equals(data.getMiddleclass() )) { 
                    data.setMiddlerowspannum(0);
                } else { 
                    data.setMiddlerowspannum(getMiddleCodeCnt(list2, data.getUpperclass(), data.getMiddleclass() ));
                    upperclass2 = data.getUpperclass();
                    middleclass = data.getMiddleclass();
                }
            }
        } catch ( Exception e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list2;
    }
    
    /**
    ����з��ڵ� ��Ͻ� ���ο� �ڵ�(�����ڵ� + 1)�� ��ȸ
    @param box          receive from the form object and session
    @return DataBox   ����з��ڵ� ����Ʈ
    */      
    public String getNewClassCode(RequestBox box) throws Exception {               
        DBConnectionManager connMgr         = null;
        ListSet             ls              = null;
        DataBox             databox         = null;
        StringBuffer        sbSQL           = new StringBuffer("");
        
        int                 iSysAdd         = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 

        String              sRtnValue       = "";
        
        String              sUpperClass     = "";
        String              sMiddleClass    = "";
        String              sClassType      = box.getString ("p_classtype");
        String              sClassCode      = box.getString ("p_classcode");
        
        try {
            connMgr = new DBConnectionManager();
            
            if ( sClassType.equals("upper") ) { 
                sUpperClass     = sClassCode;
                sMiddleClass    = "B00";
                
                sbSQL.append(" -- ��з��� ���ο� �ڵ� ����                                                                                                                     \n")
                     .append(" select decode(substr(classcode, 2, 2)                                            \n")
                     .append("        , '99'                                                                    \n")
                     .append("        ,     chr(ascii(substr(classcode, 1, 1)) + 1)                             \n")
                     .append("          ||  '01'                                                                \n")
                     .append("        ,     substr(classcode, 1, 1)                                             \n")
                     .append("          ||  lpad(to_number(substr(classcode, 2, 2)) + 1, 2, '0')                \n")
                     .append("        )                                                           classcode     \n")
                     .append(" from   (                                                                         \n")
                     .append("         select nvl(max(upperclass), 'A00') classcode                             \n")
                     .append("         from   tz_subjatt 									                    \n")
                     .append("         where substr(upperclass,1,1) ='A'					                    \n")
                     .append("         and substr(upperclass,1,1) !='0'					                    \n")
                     .append("       )                                                                          \n");
            } else if ( sClassType.equals("middle") ) { 
                sUpperClass     = box.getString("p_upperclass");
                sMiddleClass    = sClassCode;
                
                /*
                 * KT ���� �ߺз��ڵ尡 A00 ���·� �ٲ� ����...
                 * 
                 * sbSQL.append(" select lpad(nvl(max(middleclass) + 1, 'A01'), 3, '0') classcode       \n")
                     .append(" from   tz_subjatt                                                                \n")
                     .append(" where  upperclass  = " + StringManager.makeSQL(sUpperClass) + "                  \n");*/
                
                sbSQL.append(" -- �ߺз��� ���ο� �ڵ� ����                                                                                                                     \n")
                	 .append(" select decode(substr(classcode, 2, 2)                                            \n")
                	 .append("        , '99'                                    								\n")
                	 .append("        ,     chr(ascii(substr(classcode, 1, 1)) + 1)                             \n")
                	 .append("          ||  '01'							                                    \n")
                	 .append("        ,     substr(classcode, 1, 1)			                                    \n")
                	 .append("          ||  lpad(to_number(substr(classcode, 2, 2)) + 1, 2, '0')                \n")
                	 .append("        ) classcode							                                    \n")
                	 .append(" from   (                        		                                            \n")
                	 .append(" 		  select nvl(max(middleclass), 'B00') classcode                         	\n")
                	 .append("  	  from   tz_subjatt                      		                            \n")
                	 .append("        where substr(middleclass,1,1) !='A'					                    \n")
                	 .append("        and substr(middleclass,1,1) !='0'					                    	\n")
                	 .append(" 		  and upperclass  = " + StringManager.makeSQL(sUpperClass) + "              \n")
                	 .append("   	  )																			\n");
            } else if ( sClassType.equals("lower") ) { 
                sUpperClass     = box.getString("p_upperclass");
                sMiddleClass    = box.getString("p_middleclass");
                
                /*sbSQL.append(" select lpad(nvl(to_number(max(lowerclass)) + 1, '001'), 3, '0') classcode        \n")
                     .append(" from   tz_subjatt                                                                \n")
                     .append(" where  upperclass  = " + StringManager.makeSQL(sUpperClass  ) + "                \n")
                     .append(" and    middleclass = " + StringManager.makeSQL(sMiddleClass ) + "                \n");*/
                
                sbSQL.append(" -- �Һз��� ���ο� �ڵ� ����                                                                                                                     \n")
		           	 .append(" select decode(substr(classcode, 2, 2)                                            \n")
		           	 .append("        , '99'                                    								\n")
		           	 .append("        ,     chr(ascii(substr(classcode, 1, 1)) + 1)                             \n")
		           	 .append("          ||  '01'							                                    \n")
		           	 .append("        ,     substr(classcode, 1, 1)			                                    \n")
		           	 .append("          ||  lpad(to_number(substr(classcode, 2, 2)) + 1, 2, '0')                \n")
		           	 .append("        ) classcode							                                    \n")
		           	 .append(" from   (                        		                                            \n")
		           	 .append(" 		  select nvl(max(lowerclass), 'C00') classcode                         		\n")
		           	 .append("  	  from   tz_subjatt                      		                            \n")
		           	 .append("        where substr(lowerclass,1,1) !='A'					                    \n")
		           	 .append("        and substr(lowerclass,1,1) !='0'					                    	\n")
		           	 .append(" and    upperclass  = " + StringManager.makeSQL(sUpperClass  ) + "                \n")
                     .append(" and    middleclass = " + StringManager.makeSQL(sMiddleClass ) + "                \n")
		           	 .append("   	  )																			\n");
            }
            
            System.out.println(this.getClass().getName() + "." + "getNewClassCode() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            ls          = connMgr.executeQuery(sbSQL.toString());
            
            if ( ls.next() )
                databox = ls.getDataBox();
            
            sRtnValue   = databox.getString("d_classcode");
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( ls != null ) { 
                try { 
                    ls.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return sRtnValue;
    }
    
    /**
    ��з��ڵ� ���� ��ȸ
    @param ArrayList   ����з��ڵ� ����Ʈ 
    @param String      ��з��ڵ� 
    @return int         ��з��ڵ� ����
    */      
    public int getUpperCodeCnt(ArrayList list1, String uppercode) { 
        int ncnt = 0;
        ClassifySubjectData data = null;            

        for ( int i = 0; i<list1.size(); i++ ) { 
            data = (ClassifySubjectData)list1.get(i);
            
            if ( data.getUpperclass().equals(uppercode)) { 
                ncnt++;             
            }
        }
        return ncnt;
    }
    
    /**
    �ߺз��ڵ� ���� ��ȸ
    @param ArrayList   ����з��ڵ� ����Ʈ 
    @param String      ��з��ڵ� 
    @param String      �ߺз��ڵ� 
    @return int         �ߺз��ڵ� ����
    */      
    public int getMiddleCodeCnt(ArrayList list1, String uppercode, String middlecode) { 
        int ncnt = 0;
        ClassifySubjectData data = null;            

        for ( int i = 0; i<list1.size(); i++ ) { 
            data = (ClassifySubjectData)list1.get(i);
            
            if ( data.getUpperclass().equals(uppercode) && data.getMiddleclass().equals(middlecode)) { 
                ncnt++;             
            }
        }
        return ncnt;
    }

    
    /**
    ���ο� ����з��ڵ� ���
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */              
    public int InsertSubjectClassification(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;        
        PreparedStatement   pstmt           = null;        
        StringBuffer        sbSQL           = new StringBuffer("");
        int                 isOk            = 0;   
        
        int                 iSysAdd         = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 
        
        String              v_upperclass    = "";
        String              v_middleclass   = "";
        String              v_lowerclass    = "";
        String              v_classtype     = box.getString("p_classtype");
        String              v_classcode     = box.getString("p_classcode");
        String              v_classname     = box.getString("p_classname");
        
        String              v_subjclass     = "";
        String              v_luserid       = box.getSession("userid");
        
        try {
            if ( v_classtype.equals("upper") ) { 
                v_upperclass    = v_classcode;
                v_middleclass   = "000";
                v_lowerclass    = "000";
            } else if ( v_classtype.equals("middle") ) { 
                v_upperclass    = box.getString("p_upperclass");
                v_middleclass   = v_classcode;
                v_lowerclass    = "000";
            } else if ( v_classtype.equals("lower") ) { 
                v_upperclass    = box.getString("p_upperclass");
                v_middleclass   = box.getString("p_middleclass");
                v_lowerclass    = v_classcode;
            }
        
            v_subjclass         = v_upperclass + v_middleclass + v_lowerclass;
         
            connMgr             = new DBConnectionManager();                             
                        
            // insert TZ_SUBJATT table
            sbSQL.append(" insert into tz_subjatt                               \n")
                 .append(" (                                                    \n")
                 .append("         subjclass                                    \n")
                 .append("     ,   upperclass                                   \n")
                 .append("     ,   middleclass                                  \n")
                 .append("     ,   lowerclass                                   \n")
                 .append("     ,   classname                                    \n")
                 .append("     ,   luserid                                      \n")
                 .append("     ,   ldate                                        \n")
                 .append(" ) values (                                           \n")
                 .append("         ?                                            \n")
                 .append("     ,   ?                                            \n")
                 .append("     ,   ?                                            \n")
                 .append("     ,   ?                                            \n")
                 .append("     ,   ?                                            \n")
                 .append("     ,   ?                                            \n")
                 .append("     ,   to_char(sysdate, 'yyyymmddhh24miss')         \n")
                 .append(" )                                                    \n");
                 
            System.out.println(this.getClass().getName() + "." + "InsertSubjectClassification() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

            pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, v_subjclass      );
            pstmt.setString(2, v_upperclass     );
            pstmt.setString(3, v_middleclass    );
            pstmt.setString(4, v_lowerclass     );
            pstmt.setString(5, v_classname      );
            pstmt.setString(6, v_luserid        );
            
            isOk    = pstmt.executeUpdate();
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
            
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }   

    
    /**
    ���õ� ����з��ڵ� ����
    @param box      receive from the form object and session
    @return isOk    1:update success,0:update fail    
    */              
     public int UpdateSubjectClassification(RequestBox box) throws Exception { 
        DBConnectionManager connMgr     = null;        
        PreparedStatement   pstmt       = null;        
        StringBuffer        sbSQL       = new StringBuffer("");
        int                 isOk        = 0;
        
        int                 iSysAdd     = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 
        
        String              v_subjclass  = box.getString("p_upperclass") + box.getString("p_middleclass") + box.getString("p_lowerclass");
        String              v_classname  = box.getString("p_classname");
        String              v_luserid    = box.getSession("userid");
                
        try { 
            connMgr     = new DBConnectionManager();   
            
            // update TZ_SUBJATT table
            sbSQL.append(" update tz_subjatt set                                        \n")
                 .append("         classname   = ?                                      \n")
                 .append("     ,   luserid     = ?                                      \n")
                 .append("     ,   ldate       = to_char(sysdate,'yyyymmddhh24miss')    \n")
                 .append(" where   subjclass   = ?                                      \n");
            
            System.out.println(this.getClass().getName() + "." + "UpdateSubjectClassification() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");
            
            pstmt       = connMgr.prepareStatement(sbSQL.toString());
            
            pstmt.setString(1, v_classname  );
            pstmt.setString(2, v_luserid    );
            pstmt.setString(3, v_subjclass  );
            
            isOk        = pstmt.executeUpdate();
            
            if ( pstmt != null ) { pstmt.close(); }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }   

     
    /**
    ���õ� ����з��ڵ� ����
    @param box      receive from the form object and session
    @return isOk    1:delete success,0:delete fail    
    */              
    public int DeleteSubjectClassification(RequestBox box) throws Exception { 
        DBConnectionManager connMgr         = null;        
        PreparedStatement   pstmt           = null;        
        StringBuffer        sbSQL           = new StringBuffer("");
        ListSet             ls              = null;
        int                 isOk            = 1;   
        
        int                 iSysAdd         = 0; // System.out.println�� Mehtod���� ����ϴ� ������ ǥ���ϴ� ���� 
        
        String              v_upperclass    = box.getString("p_upperclass");
        String              v_middleclass   = box.getString("p_middleclass");
        String              v_subjclass     = box.getString("p_upperclass") + box.getString("p_middleclass") + box.getString("p_lowerclass");
                
        try { 
            connMgr     = new DBConnectionManager();                             
            
            // ��з��� ��� �ߺз� Ȯ��
            if ( v_middleclass.equals("000") ) {
                sbSQL.append(" select  upperclass                                                   \n")
                     .append(" from    tz_subjatt                                                   \n")
                     .append(" where   upperclass  = " + StringManager.makeSQL(v_upperclass) + "    \n")
                     .append(" and     middleclass != '000'                                         \n");
                
                System.out.println(this.getClass().getName() + "." + "DeleteSubjectClassification() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                ls = connMgr.executeQuery( sbSQL.toString() );
                
                if ( ls.next() ) { 
                    // �ߺз��� �־� ������ �� ����
                    isOk = -1;
                }
            } else { 
                if ( ls != null ) { 
                    try { 
                        ls.close(); 
                    } catch ( Exception e ) { } 
                }
                
                // �ߺз��� ��� ������ Ȯ��
                sbSQL.append(" SELECT  subjclass                                                    \n")
                     .append(" FROM    tz_subj                                                      \n")
                     .append(" WHERE   subjclass   = " + StringManager.makeSQL(v_subjclass) + "     \n");
                
                System.out.println(this.getClass().getName() + "." + "DeleteSubjectClassification() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");
                
                ls  = connMgr.executeQuery(sbSQL.toString());
                
                if ( ls.next() ) { 
                    // �ߺз��� �־� ������ �� ����
                    isOk = -2;
                }
            }
            
            System.out.println(this.getClass().getName() + "." + "DeleteSubjectClassification() Printing Order " + ++iSysAdd + ". ======[isOk] : " + " [\n" + isOk + "\n]");
            
            sbSQL.setLength(0);
            
            if ( isOk == 1) { 
                // delete TZ_SUBJATT table
                sbSQL.append("delete from tz_subjatt where subjclass = ? ");
                
                System.out.println(this.getClass().getName() + "." + "DeleteSubjectClassification() Printing Order " + ++iSysAdd + ". ======[SELECT SQL] : " + " [\n" + sbSQL.toString() + "\n]");

                pstmt   = connMgr.prepareStatement(sbSQL.toString());
            
                pstmt.setString(1, v_subjclass);
                
                isOk    = pstmt.executeUpdate();
                if ( pstmt != null ) { pstmt.close(); }
            }
        } catch ( SQLException e ) {
            ErrorManager.getErrorStackTrace(e, box, sbSQL.toString());
            throw new Exception("\n SQL : [\n" + sbSQL.toString() + "]\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } catch ( Exception e ) { 
            ErrorManager.getErrorStackTrace(e, box, "");
            throw new Exception("\n e.getMessage() : [\n" + e.getMessage() + "\n]");
        } finally { 
            if ( pstmt != null ) { 
                try { 
                    pstmt.close(); 
                } catch ( Exception e ) { } 
            }
                
            if ( connMgr != null ) { 
                try { 
                    connMgr.freeConnection(); 
                } catch ( Exception e ) { } 
            }
        }

        return isOk;
    }   
}
