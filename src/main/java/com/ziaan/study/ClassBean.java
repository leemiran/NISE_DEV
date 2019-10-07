// **********************************************************
// 1. 제      목:
// 2. 프로그램명: ClassBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-07-29
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.study;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.FormatDate;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class ClassBean { 
    public static String SINGLE_CLASS = "1";
    public static String PLURAL_CLASS = "2";

    public static String CLASSNM = "클래스";
    public static String SINGLE_CLASS_CODE = "0001";

    public ClassBean() { }

    
    /**
     * 클래스 분반 자동 처리
     * @param box          receive from the form object and session
     * @return ArrayList   과목리스트
     */
    public int ClassAutoCut(RequestBox box) throws Exception
    {
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String  sql         = "";
        int     isOk01      = -1;

        String v_subj       = box.getString("p_subj");
        String v_year       = box.getString("p_year");
        String v_subjseq    = box.getString("p_subjseq01");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            
            sql  = "update                                                                          \n";
            sql += "        tz_student aa                                                           \n";
            sql += "   set                                                                          \n";
            sql += "        aa.class = (                                                            \n";
            sql += "                    select                                                      \n";
            sql += "                            class                                               \n";
            sql += "                      from                                                      \n";
            sql += "                            (                                                   \n";
            sql += "                            select                                              \n";
            sql += "                                    b.class,                                    \n";
            sql += "                                    a.subj,                                     \n";
            sql += "                                    a.year,                                     \n";
            sql += "                                    a.subjseq,                                  \n";
            sql += "                                    a.userid                                    \n";
            sql += "                              from                                              \n";
            sql += "                                    (                                           \n";
            sql += "                                    select                                      \n";
            sql += "                                            mod(rownum-1, ( select count(class) from tz_class where subj = ? and year=? and subjseq=? ) )+1 rnum1,            \n";
            sql += "                                            rownum rnum2,TZ_STUDENT.*           \n";
            sql += "                                      from                                      \n";
            sql += "                                            TZ_STUDENT                          \n";
            sql += "                                     where                                      \n";
            sql += "                                            subj = ?                            \n";
            sql += "                                       and  year = ?                            \n";
            sql += "                                       and  subjseq = ?                         \n";
            sql += "                                    ) A,                                        \n";
            sql += "                                    (                                           \n";
            sql += "                                    select                                      \n";
            sql += "                                            rownum rnum1,                       \n";
            sql += "                                            class                               \n";
            sql += "                                      from                                      \n";
            sql += "                                            (                                   \n";
            sql += "                                            select                              \n";
            sql += "                                                    class                       \n";
            sql += "                                              from                              \n";
            sql += "                                                    TZ_CLASS                    \n";
            sql += "                                             where                              \n";
            sql += "                                                    subj = ?                    \n";
            sql += "                                               and  year = ?                    \n";
            sql += "                                               and  subjseq = ?                 \n";
            sql += "                                             order by                           \n";
            sql += "                                                    class                       \n";
            sql += "                                            ) itable                            \n";
            sql += "                                    ) B                                         \n";
            sql += "                             where                                              \n";
            sql += "                                    A.rnum1=b.rnum1                             \n";
            sql += "                             order by                                           \n";
            sql += "                                    a.rnum2                                     \n";
            sql += "                            ) bb                                                \n";
            sql += "                     where                                                      \n";
            sql += "                            aa.subj=bb.subj                                     \n";
            sql += "                       and  aa.year=bb.year                                     \n";
            sql += "                       and  aa.subjseq=bb.subjseq                               \n";
            sql += "                       and  aa.userid=bb.userid                                 \n";
            sql += "                    )                                                           \n";
            sql += " where                                                                          \n";
            sql += "        aa.subj = ?                                                             \n";
            sql += "   and  aa.year = ?                                                             \n";
            sql += "   and  aa.subjseq = ?                                                          \n";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setString(4, v_subj);
            pstmt.setString(5, v_year);
            pstmt.setString(6, v_subjseq);
            pstmt.setString(7, v_subj);
            pstmt.setString(8, v_year);
            pstmt.setString(9, v_subjseq);
            pstmt.setString(10, v_subj);
            pstmt.setString(11, v_year);
            pstmt.setString(12, v_subjseq);
            isOk01 = pstmt.executeUpdate();
            
            

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk01 >= 0 ) { connMgr.commit(); }
            if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk01;
    }
    
    /**
    클래스리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectClassList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ClassListData data = null;
        Hashtable classinfo = null;

        String v_grcode   = box.getStringDefault("s_grcode","ALL");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getStringDefault("s_grseq","ALL");
        String v_uclass   = box.getStringDefault("s_upperclass","ALL");
        String v_mclass   = box.getStringDefault("s_middleclass","ALL");
        String v_lclass   = box.getStringDefault("s_lowerclass","ALL");
        String v_subj     = box.getStringDefault("s_subjcourse","ALL");
        String v_subjseq  = box.getStringDefault("s_subjseq","ALL");
        String v_comp     = box.getStringDefault("s_comp","ALL");

        String  v_orderColumn= box.getString("p_orderColumn");           // 정렬할 컬럼명
        String  v_orderType     = box.getString("p_orderType");          // 정렬할 순서

        try { 
            if ( box.getString("p_action").equals("go") ) { 
                sql  = "select                              \n" +
                       "        course,                     \n" +
                       "        cyear,                      \n" +
                       "        courseseq,                  \n" +
                       "        coursenm,                   \n" +
                       "        subj,                       \n" +
                       "        year,                       \n" +
                       "        subjseq,                    \n" +
                       "        subjseqgr,                  \n" +
                       "        subjnm,                     \n" +
                       "        edustart,                   \n" +
                       "        eduend                      \n" +
                       "  from                              \n" +
                       "        vz_scsubjseq                \n" +
                       "where 1 = 1                         \n";

                if ( !v_grcode.equals("ALL"))  sql += "   and grcode = "        + SQLString.Format(v_grcode)    + " \n";
                if ( !v_gyear.equals("ALL"))   sql += "   and gyear = "         + SQLString.Format(v_gyear)     + " \n";
                if ( !v_grseq.equals("ALL"))   sql += "   and grseq = "         + SQLString.Format(v_grseq)     + " \n";
                if ( !v_uclass.equals("ALL"))  sql += "   and scupperclass = "  + SQLString.Format(v_uclass)    + " \n";
                if ( !v_mclass.equals("ALL"))  sql += "   and scmiddleclass = " + SQLString.Format(v_mclass)    + " \n";
                if ( !v_lclass.equals("ALL"))  sql += "   and sclowerclass = "  + SQLString.Format(v_lclass)    + " \n";
                if ( !v_subj.equals("ALL"))    sql += "   and scsubj = "        + SQLString.Format(v_subj)      + " \n";
                if ( !v_subjseq.equals("ALL")) sql += "   and scsubjseq = "     + SQLString.Format(v_subjseq)   + " \n";

                if ( v_orderColumn.equals("subj"))    v_orderColumn = "subj";
                if ( v_orderColumn.equals("subjseq")) v_orderColumn = "subjseqgr";
                if ( v_orderColumn.equals("") ) { 
                    sql += " order by subj, year, subjseq";
                } else { 
                    sql += " order by " + v_orderColumn + v_orderType;
                }

                connMgr = new DBConnectionManager();
                
                //System.out.println("\n클래스리스트 조회\n" + sql + "\n\n\n");
                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    classinfo = getClassListInfo(connMgr, ls.getString("subj"), ls.getString("year"), ls.getString("subjseq") );
                    if ( Integer.parseInt((String)classinfo.get("studentcnt")) > 0 ) { 
                        data = new ClassListData();
                        data.setCoursenm(           ls.getString("coursenm")                                        );
                        data.setSubj(               ls.getString("subj")                                            );
                        data.setYear(               ls.getString("year")                                            );
                        data.setSubjseq(            ls.getString("subjseq")                                         );
                        data.setSubjseqgr(          ls.getString("subjseqgr")                                       );
                        data.setSubjnm(             ls.getString("subjnm")                                          );
                        data.setEdustart(           ls.getString("edustart")                                        );
                        data.setEduend(             ls.getString("eduend")                                          );
                        data.setStudentcnt(         Integer.parseInt((String)classinfo.get("studentcnt"))           );
                        data.setClasscnt(           Integer.parseInt((String)classinfo.get("classcnt"))             );
                        data.setNoassignstudentcnt( Integer.parseInt((String)classinfo.get("noassignstudent"))      );
                        data.setTutor(              (Vector)classinfo.get("tutor")                                  );
                        data.setAvailabletutorcnt(  Integer.parseInt((String)classinfo.get("availabletutorcnt"))    );
                        data.setSubtutor(           (String)classinfo.get("subtutor")                               );
                        list.add(data);
                    }
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    클래스리스트 정보
    @param  connMgr      DB Connection Manager
    @param  subj         과목코드
    @param  year         과목년도
    @param  subjseq      과목기수
    @param  userid       유저 아이디
    @return Hashtable
    */
    public Hashtable getClassListInfo(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq) throws Exception { 
        Hashtable classinfo = new Hashtable();
        TutorSelectData tutordata = null;
        Vector tutor = new Vector();
        String subtutor  = "";
        int v_classcnt   = 0;
        int v_studentcnt = 0;
        String v_mtutor  = "";

        ListSet             ls      = null;
        String sql  = "";
        try { 
            // 교육생수
            sql = "select count(*) studentcnt";
            sql += "  from tz_student ";
            sql += " where subj    = " + SQLString.Format(p_subj);
            sql += "   and year    = " + SQLString.Format(p_year);
            sql += "   and subjseq = " + SQLString.Format(p_subjseq);
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_studentcnt = ls.getInt("studentcnt");
                classinfo.put("studentcnt", String.valueOf(v_studentcnt));
            }
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            if ( v_studentcnt > 0 ) { 
                // 클래스수
                sql = "select count(*) classcnt";
                sql += "  from tz_class ";
                sql += " where subj    = " + SQLString.Format(p_subj);
                sql += "   and year    = " + SQLString.Format(p_year);
                sql += "   and subjseq = " + SQLString.Format(p_subjseq);
                ls = connMgr.executeQuery(sql);
                while ( ls.next() ) { 
                    v_classcnt = ls.getInt("classcnt");
                    classinfo.put("classcnt", String.valueOf(v_classcnt));
                }
                        if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

                // 클래스 미지정 교육생수
                sql = "select count(*) noassignstudent";
                sql += "  from tz_student ";
                sql += " where subj    = " + SQLString.Format(p_subj);
                sql += "   and year    = " + SQLString.Format(p_year);
                sql += "   and subjseq = " + SQLString.Format(p_subjseq);
                sql += "   and subjseq = " + SQLString.Format(p_subjseq);
                sql += "   and class is null ";
                ls = connMgr.executeQuery(sql);
                while ( ls.next() ) { 
                    classinfo.put("noassignstudent", String.valueOf( ls.getInt("noassignstudent")));
                }
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

                // 가용강사수
                sql = "select count(*) availabletutorcnt";
                sql += "  from tz_subjman  a, ";
                sql += "       tz_tutor    b ";
                sql += " where a.userid = b.userid ";
                sql += "   and a.gadmin like 'P%' ";
                sql += "   and a.subj   = " + SQLString.Format(p_subj);
                ls = connMgr.executeQuery(sql);
                while ( ls.next() ) { 
                    classinfo.put("availabletutorcnt", String.valueOf( ls.getInt("availabletutorcnt")));
                }
                if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

                // 단일클래스인 경우만 값을 가져온다.
                if ( v_classcnt < 2) { 
                    // 부강사
                    sql = "select a.tuserid, a.ttype, b.name";
                    sql += "  from tz_classtutor  a, ";
                    sql += "       tz_tutor       b  ";
                    sql += " where a.tuserid = b.userid ";
                    sql += "   and a.subj    = " + SQLString.Format(p_subj);
                    sql += "   and a.year    = " + SQLString.Format(p_year);
                    sql += "   and a.subjseq = " + SQLString.Format(p_subjseq);

                    ls = connMgr.executeQuery(sql);
                    while ( ls.next() ) { 
                        if ( ls.getString("ttype").equals("M") ) { 
                            v_mtutor = ls.getString("tuserid");
                        } else { 
                            subtutor += ls.getString("name") + ":";
                        }
                    }
                    ls.close();

                    // 보조강사를 가져온다.
                    sql = "select a.userid, b.name";
                    sql += "  from tz_student  a, ";
                    sql += "       tz_member       b  ";
                    sql += " where a.userid = b.userid ";
                    sql += "   and a.subj    = " + SQLString.Format(p_subj);
                    sql += "   and a.year    = " + SQLString.Format(p_year);
                    sql += "   and a.subjseq = " + SQLString.Format(p_subjseq);
                    sql += "   and a.issubtutor = 'Y'";

                    ls = connMgr.executeQuery(sql);
                    subtutor = "";
                    while ( ls.next() ) { 
                        subtutor += ls.getString("name") + ":";                        
                    }
                    
                    ls.close();
                    // 강사
                    sql = "select b.userid, b.name";
                    sql += "  from tz_subjman     a, ";
                    sql += "       tz_tutor       b  ";
                    sql += " where a.userid = b.userid ";
                    sql += "   and a.gadmin like 'P%'     ";
                    sql += "   and a.subj   = " + SQLString.Format(p_subj);

                    ls = connMgr.executeQuery(sql);
                    while ( ls.next() ) { 
                        tutordata = new TutorSelectData();
                        tutordata.setUserid( ls.getString("userid") );
                        tutordata.setName( ls.getString("name") );
                        tutordata.setSelected(v_mtutor.equals(tutordata.getUserid() ) ? true : false);
                        tutor.add(tutordata);
                    }
                    ls.close();

                    if ( tutor.size() < 1) { 
                        tutordata = new TutorSelectData();
                        tutordata.setUserid("notutor");
                        tutordata.setName("-선택-");
                        tutor.add(tutordata);
                    }
                }
                classinfo.put("tutor", tutor);
                classinfo.put("subtutor", subtutor);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return classinfo;
    }

    /**
    단일클래스 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int OneStopMakeClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = -1;

        String v_subj = "";
        String v_year = "";
        String v_subjseq = "";
        String v_tutorid = "";

        StringTokenizer v_token = null;
        String v_tempStr = "";
        int i = 0;
        Hashtable insertData = new Hashtable();

        try { 
            // p_tutors 넘어온 다수의 value를 처리하기 위해 vector로 구현
            Vector v_tutors = box.getVector("p_tutors");
            if ( v_tutors != null ) { 
                Enumeration em  = v_tutors.elements();

                connMgr = new DBConnectionManager();
                connMgr.setAutoCommit(false);

                while ( em.hasMoreElements() ) { 
                    i = 0;
                    v_token = new StringTokenizer((String)em.nextElement(), ";");
                    while ( v_token.hasMoreTokens() ) { 
                        v_tempStr = v_token.nextToken();
                        switch (i) { 
                            case 0:
                                v_subj = v_tempStr;
                                break;
                            case 1:
                                v_year = v_tempStr;
                                break;
                            case 2:
                                v_subjseq = v_tempStr;
                                break;
                            case 3:
                                v_tutorid = v_tempStr;
                                break;
                        }
                        i++;
                    }

                    insertData.clear();
                    insertData.put("connMgr",  connMgr);
                    insertData.put("subj",    v_subj);
                    insertData.put("year",    v_year);
                    insertData.put("subjseq", v_subjseq);
                    insertData.put("tutorid", v_tutorid);
                    insertData.put("ttype",   "M");
                    insertData.put("class",  ClassBean.SINGLE_CLASS_CODE);
                    insertData.put("classtype", ClassBean.SINGLE_CLASS);
                    insertData.put("luserid", box.getString("userid") );

                    isOk = InsertClass(insertData);
                    insertData.put("ttype",   "ALL");
                    isOk = DeleteClassTutor(insertData);
                    insertData.put("ttype",   "M");
                    isOk = InsertClassTutor(insertData);
                    isOk = UpdateStudentClass(insertData);
                }
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk >= 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    복수클래스 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int CreateClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = -1;

        String v_subj    = box.getString("p_subj");
        String v_year    = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq01");
        int v_classcnt   = box.getInt("p_" +v_subj +v_year +v_subjseq);

        String v_class   = "";
        String v_classnm = "";
        Hashtable insertData = new Hashtable();
        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);
            for ( int i = 0; i<v_classcnt; i++ ) { 
                insertData.clear();
                insertData.put("connMgr", connMgr);
                insertData.put("subj",    v_subj);
                insertData.put("year",    v_year);
                insertData.put("subjseq", v_subjseq);
                insertData.put("luserid", box.getString("userid") );

                if ( i == 0 ) { 
                    insertData.put("classtype", ClassBean.SINGLE_CLASS);
                    isOk = DeleteClass(insertData);
                }

                v_class   = new DecimalFormat("0000").format(i +1);
                v_classnm = ClassBean.CLASSNM + v_class;

                insertData.put("class",   v_class);
                insertData.put("classnm", v_classnm);
                insertData.put("classtype", ClassBean.PLURAL_CLASS);
                isOk = InsertClass(insertData);
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk >= 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    public int InsertClass(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String v_subj     = (String)data.get("subj");
        String v_year     = (String)data.get("year");
        String v_subjseq  = (String)data.get("subjseq");
        String v_classtype= (String)data.get("classtype");
        String v_class    = "";
        String v_classnm  = "";

        if ( v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
            DeleteClass(data);
            v_class   = ClassBean.SINGLE_CLASS_CODE;
            v_classnm = ClassBean.CLASSNM + v_class;
        } else { 
            v_class   = (String)data.get("class");
            v_classnm = (String)data.get("classnm");
        }
        String v_luserid = (String)data.get("luserid");
        String v_comp    = "";

        // insert TZ_CLASS table
        sql =  "insert into TZ_CLASS (  subj,    year,   subjseq,   class,   classnm, comp,   luserid,   ldate ) ";
        sql +=  "              values (  ?, ?, ?, ?,  ?, ?, ?, ? ) ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            pstmt.setString( 4, v_class);
            pstmt.setString( 5, v_classnm);
            pstmt.setString( 6, v_comp);
            pstmt.setString( 7, v_luserid);
            pstmt.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 

            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    public int DeleteClass(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;

        String              sql     = "";

        String v_subj     = (String)data.get("subj");
        String v_year     = (String)data.get("year");
        String v_subjseq  = (String)data.get("subjseq");

        String v_classtype= (String)data.get("classtype");
        String v_class    = (String)data.get("class");

        // insert TZ_CLASS table
        sql =  "delete from TZ_CLASS " ;
        sql +=  " where subj    = ? ";
        sql +=  "   and year    = ? ";
        sql +=  "   and subjseq = ? ";
        if ( !v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
            sql +=  "   and class   = ? ";
        }

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            if ( !v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
                pstmt.setString( 4, v_class);
            }
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    public int InsertClassTutor(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;

        String              sql     = "";
        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_classtype= (String)data.get("classtype");

        String v_class   = "";
        if ( v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
            v_class   = ClassBean.SINGLE_CLASS_CODE;
        } else { 
            v_class   = (String)data.get("class");
        }
        String v_tutor   = (String)data.get("tutorid");
        String v_ttype   = (String)data.get("ttype");

        String v_luserid = (String)data.get("luserid");

        // insert TZ_CLASSTUTOR table
        sql =  "insert into TZ_CLASSTUTOR ( " ;
        sql +=  " subj,    year,   subjseq,   class,  ";
        sql +=  " tuserid, ttype,  luserid,   ldate ) ";
        sql +=  " values ( ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ? ) ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            pstmt.setString( 4, v_class);
            pstmt.setString( 5, v_tutor);
            pstmt.setString( 6, v_ttype);
            pstmt.setString( 7, v_luserid);
            pstmt.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    public int DeleteClassTutor(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;

        String              sql     = "";
        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");

        String v_classtype= (String)data.get("classtype");
        String v_class   = (String)data.get("class");
        String v_tutor   = (String)data.get("tutorid");
        String v_ttype   = (String)data.get("ttype");

        // delete TZ_CLASSTUTOR table
        sql =  "delete from TZ_CLASSTUTOR  " ;
        sql +=  " where subj = ? ";
        sql +=  "   and year = ? ";
        sql +=  "   and subjseq =  ? ";
        if ( v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
            if ( v_ttype.equals("ALL") ) { 

            } else { 
                // sql +=  "   and class   = ? ";
                sql +=  "   and tuserid = ? ";
            }
        } else  { 
            sql +=  "   and class   = ? ";
            sql +=  "   and tuserid = ? ";
        }

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            if ( v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
                if ( !v_ttype.equals("ALL"))
                    pstmt.setString( 4, v_ttype);
            } else { 
                pstmt.setString( 4, v_class);
                pstmt.setString( 5, v_tutor);
            }

            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    public int UpdateStudentClass(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_class   = (String)data.get("class");;
        String v_classtype= (String)data.get("classtype");
        String v_userid  = (String)data.get("userid");
        String v_luserid = (String)data.get("luserid");

        // update TZ_STUDENT table
        sql =  "update TZ_STUDENT  " ;
        sql +=  "   set class   =  ?, ";
        sql +=  "       luserid =  ?, ";
        sql +=  "       ldate   =  ?,  ";
        sql +=  "       issubtutor   =  'N'  ";
        sql +=  " where subj    =  ?  ";
        sql +=  "   and year    =  ?  ";
        sql +=  "   and subjseq =  ?  ";
        if ( !v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
            sql +=  "   and userid   = ? ";
        }

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_class);
            pstmt.setString( 2, v_luserid);
            pstmt.setString( 3, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString( 4, v_subj);
            pstmt.setString( 5, v_year);
            pstmt.setString( 6, v_subjseq);
            if ( !v_classtype.equals(ClassBean.SINGLE_CLASS)) { 
                pstmt.setString( 7, v_userid);
            }
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    /**
    보조강사(반장) 대상자 LIST 2006.07.07 추가 김민수
    @param box          receive from the form object and session
    @return ArrayList   보조강사(반장) 대상자 LIST
    */
    public ArrayList SelectSubtutor(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        ListSet             ls      = null;
        String sql  = "";
        ArrayList list = new ArrayList();
        DataBox             dbox    = null;

        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq01");
        String v_class    = box.getString("p_class");


        try { 
            // 주강사 결정
            sql = "select a.userid, b.name, a.issubtutor";
            sql += "  from tz_student a, tz_member b  ";
            sql += " where a.userid = b.userid and a.subj    = " + SQLString.Format(v_subj);
            sql += "   and a.year    = " + SQLString.Format(v_year);
            sql += "   and a.subjseq = " + SQLString.Format(v_subjseq);
            sql += "   and a.class   = " + SQLString.Format(v_class);

            connMgr = new DBConnectionManager();
            System.out.println("보조강사 대상자 리스트 \n"+sql+"\n\n\n");
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 

                dbox = ls.getDataBox();
                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    
    /**
    보조강사(반장) 저장 2006.07.07 추가 김민수
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int SubTutorMapping(RequestBox box) throws Exception { 
        DBConnectionManager connMgr = null;
        PreparedStatement pstmt = null;
        String  sql         = "";
        int     isOk01      = -1;
        int     isOk02      = -1;

        String v_subj       = box.getString("p_subj");
        String v_year       = box.getString("p_year");
        String v_subjseq    = box.getString("p_subjseq01");
        String v_class      = box.getString("p_class");
        String v_tutorid    = box.getString("p_subtutorid");

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            sql =   "update                         " +
                    "        tz_student             " +
                    "   set                         " +
                    "        issubtutor = 'N'       " +
                    " where                         " +
                    "        subj = ?               " +
                    "   and  year = ?               " +
                    "   and  subjseq = ?            " +
                    "   and  class = ?              ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setString(4, v_class);
            isOk01 = pstmt.executeUpdate();
            
            if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            
            sql =   "update                         " +
                    "        tz_student             " +
                    "   set                         " +
                    "        issubtutor = 'Y'       " +
                    " where                         " +
                    "        subj = ?               " +
                    "   and  year = ?               " +
                    "   and  subjseq = ?            " +
                    "   and  class = ?              " +
                    "   and  userid = ?             ";
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString(1, v_subj);
            pstmt.setString(2, v_year);
            pstmt.setString(3, v_subjseq);
            pstmt.setString(4, v_class);
            pstmt.setString(5, v_tutorid);
            isOk02 = pstmt.executeUpdate();
            
            

        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk01 >= 0 ) { connMgr.commit(); }
            if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk01+isOk02;
    }

    /**
    보조강사 조회
    @param box          receive from the form object and session
    @return ArrayList   보조강리스트
    */
    public ArrayList SelectSubtutorList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ArrayList list = new ArrayList();
        DataBox             dbox    = null;

        String v_subj     = box.getString("p_subj");
        String v_year     = box.getString("p_year");
        String v_subjseq  = box.getString("p_subjseq01");
        String v_class    = box.getStringDefault("p_class", ClassBean.SINGLE_CLASS_CODE);

        String v_mtutor   = "";

        try { 
            // 주강사 결정
            sql = "select a.tuserid, a.ttype, b.name";
            sql += "  from tz_classtutor  a, ";
            sql += "       tz_tutor       b  ";
            sql += " where a.tuserid = b.userid ";
            sql += "   and a.subj    = " + SQLString.Format(v_subj);
            sql += "   and a.year    = " + SQLString.Format(v_year);
            sql += "   and a.subjseq = " + SQLString.Format(v_subjseq);
            sql += "   and a.class   = " + SQLString.Format(ClassBean.SINGLE_CLASS_CODE);

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( ls.getString("ttype").equals("M") ) { 
                    v_mtutor = ls.getString("tuserid");
                }
            }

            // 강사 리스트
            sql = "select b.userid, b.name";
            sql += "  from tz_subjman     a, ";
            sql += "       tz_tutor       b  ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.gadmin like 'P%' ";
            sql += "   and a.subj   = " + SQLString.Format(v_subj);

                        if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 

                dbox = ls.getDataBox();
                dbox.put("d_ttype", v_mtutor.equals(dbox.getString("d_userid")) ? "M" : "S");

                list.add(dbox);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    /**
    단일클래스 저장
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int SubTutorInsert(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = -1;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq01");
        String v_class = box.getStringDefault("p_class", ClassBean.SINGLE_CLASS_CODE);
        String v_tutorid = "";

        // p_tutors 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_tutors = box.getVector("p_checks");
        Enumeration em  = v_tutors.elements();
        int i = 0;
        Hashtable insertData = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            while ( em.hasMoreElements() ) { 
                v_tutorid = (String)em.nextElement();

                insertData.clear();
                insertData.put("connMgr",  connMgr);

                insertData.put("subj",    v_subj);
                insertData.put("year",    v_year);
                insertData.put("subjseq", v_subjseq);
                insertData.put("class",   v_class);
                insertData.put("tutorid", v_tutorid);

                insertData.put("ttype",   "S");
                insertData.put("classtype",  ClassBean.PLURAL_CLASS);
                insertData.put("luserid", box.getSession("userid") );

                isOk = DeleteClassTutor(insertData);
                isOk = InsertClassTutor(insertData);
                i++;
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk > 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    클래스입력리스트 조회 - 개인별
    @param box          receive from the form object and session
    @return ArrayList   개인별 리스트
    */
    public ArrayList SelectClassInsertList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ClassMemberData data = null;
        Hashtable tutorinfo  = null;

        try { 
            sql  = "select                                                                          \n" +
                   "    b.comp                      asgn,                                           \n" +
                   "    get_compnm(b.comp)      as  companynm,                                      \n" +
                   "    b.userid,                                                                   \n" +                   
                   "    b.name,                                                                     \n" +
                   "    a.class,                                                                    \n" +
                   "    c.classnm,                                                                  \n" +
                   "    a.subj,                                                                     \n" +
                   "    a.year,                                                                     \n" +
                   "    a.subjseq                                                                   \n";
            sql += "  from tz_student  a,                                                           \n";
            sql += "       tz_member   b,                                                           \n";
            sql += "       tz_class    c                                                            \n";
            sql += " where a.userid     = b.userid                                                  \n";
            sql += "   and a.subj       = c.subj( +)                                                \n";
            sql += "   and a.year       = c.year( +)                                                \n";
            sql += "   and a.subjseq    = c.subjseq( +)                                             \n";
            sql += "   and a.class      = c.class( +)                                               \n";
            sql += "   and a.subj       = " + SQLString.Format(box.getString("p_subj") ) + "        \n";
            sql += "   and a.year       = " + SQLString.Format(box.getString("p_year") ) + "        \n";
            sql += "   and a.subjseq    = " + SQLString.Format(box.getString("p_subjseq01") ) + "   \n";

            connMgr = new DBConnectionManager();
            
            System.out.println("\n 클래스입력리스트 개인별 리스트 \n" + sql + "\n\n\n");
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ClassMemberData();
                data.setCompanynm(  ls.getString("companynm") );
                data.setAsgn(       ls.getString("asgn") );
                data.setUserid(     ls.getString("userid") );
                data.setName(       ls.getString("name") );
                data.setClass1(     ls.getString("class") );
                data.setClassnm(    ls.getString("classnm") );
                data.setSubj(       ls.getString("subj") );
                data.setYear(       ls.getString("year") );
                data.setSubjseq(    ls.getString("subjseq") );

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    클래스입력리스트 조회 - 사업부별
    @param box          receive from the form object and session
    @return ArrayList   사업부별 리스트
    */
    public ArrayList SelectGroupStudentList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ClassInsertData data = null;
        String v_subpage = box.getStringDefault("p_subpage", "individual_page");
        try { 
            if ( v_subpage.equals("asgn_page") ) { 
                sql = "select b.comp code, get_compnm(b.comp,2,4) codenm, count(*) studentcnt, c.class,  min(c.classnm) classnm ";
            } else if ( v_subpage.equals("jikun_page") ) { 
                sql = "select b.jikun code, get_jikunnm(b.jikun, b.comp) codenm, count(*) studentcnt, c.class,  min(c.classnm) classnm ";
            } else if ( v_subpage.equals("jikup_page") ) { 
                sql = "select b.jikup code, get_jikupnm(b.jikup, b.comp) codenm, count(*) studentcnt, c.class,  min(c.classnm) classnm ";
            }
            sql += "  from tz_student  a, ";
            sql += "       tz_member   b, ";
            sql += "       tz_class    c ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.subj   = c.subj( +) ";
            sql += "   and a.year   = c.year( +) ";
            sql += "   and a.subjseq = c.subjseq( +) ";
            sql += "   and a.class   = c.class( +) ";
            sql += "   and a.subj = " + SQLString.Format(box.getString("p_subj") );
            sql += "   and a.year = " + SQLString.Format(box.getString("p_year") );
            sql += "   and a.subjseq = " + SQLString.Format(box.getString("p_subjseq01") );
            if ( v_subpage.equals("asgn_page") ) { 
                sql += " group by b.comp,  get_compnm(b.comp,2,4), c.class ";
            } else if ( v_subpage.equals("jikun_page") ) { 
                sql += " group by b.jikun, get_jikunnm(b.jikun, b.comp), c.class ";
            } else if ( v_subpage.equals("jikup_page") ) { 
                sql += " group by b.jikup, get_jikupnm(b.jikup, b.comp), c.class ";
            }

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ClassInsertData();
                data.setCode( ls.getString("code") );
                data.setCodenm( ls.getString("codenm") );
                data.setStudentcnt( ls.getInt("studentcnt") );
                data.setClass1( ls.getString("class") );
                data.setClassnm( ls.getString("classnm") );

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    클래스수정리스트 조회
    @param box          receive from the form object and session
    @return ArrayList   과목리스트
    */
    public ArrayList SelectClassUpdateList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ClassMemberData data = null;
        Hashtable tutorinfo  = null;

        try { 
            sql = "select b.comp asgn, get_compnm(b.comp,2,4) asgnnm, b.jikwi, get_jikwinm(b.jikwi, b.comp) jikwinm, b.userid, b.cono, b.name, a.class, c.classnm, a.subj, a.year, a.subjseq ";
            sql += "  from tz_student  a, ";
            sql += "       tz_member   b, ";
            sql += "       tz_class    c ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.subj   = c.subj( +) ";
            sql += "   and a.year   = c.year( +) ";
            sql += "   and a.subjseq = c.subjseq( +) ";
            sql += "   and a.class   = c.class( +) ";
            sql += "   and a.subj = " + SQLString.Format(box.getString("p_subj") );
            sql += "   and a.year = " + SQLString.Format(box.getString("p_year") );
            sql += "   and a.subjseq = " + SQLString.Format(box.getString("p_subjseq01") );

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ClassMemberData();
                data.setAsgn( ls.getString("asgn") );
                data.setAsgnnm( ls.getString("asgnnm") );
                data.setJikwi( ls.getString("jikwi") );
                data.setJikwinm( ls.getString("jikwinm") );
                data.setUserid( ls.getString("userid") );
                data.setCono( ls.getString("cono") );
                data.setName( ls.getString("name") );
                data.setClass1( ls.getString("class") );
                data.setClassnm( ls.getString("classnm") );
                data.setSubj( ls.getString("subj") );
                data.setYear( ls.getString("year") );
                data.setSubjseq( ls.getString("subjseq") );
                tutorinfo = getTutorInfo(connMgr, data.getSubj(), data.getYear(), data.getSubjseq(), data.getClass1() );
                data.setMtutor((String)tutorinfo.get("p_mtutor") );
                data.setStutor((String)tutorinfo.get("p_stutor") );

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /**
    클래스 전체삭제
    @param  box     receive from the form object and session
    @return isOk    1:delete success, 0:delete fail
    */
    public int DeleteAllClass(RequestBox box) throws Exception { 
        int isOk = -1;

        DBConnectionManager	connMgr	= null;
        String              sql     = "";
        Hashtable deleteData = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            deleteData.put("connMgr", connMgr);
            deleteData.put("subj",    box.getString("p_subj") );
            deleteData.put("year",    box.getString("p_year") );
            deleteData.put("subjseq", box.getString("p_subjseq01") );
            deleteData.put("classtype", ClassBean.SINGLE_CLASS);
            deleteData.put("ttype",  "ALL");
            deleteData.put("luserid", box.getSession("userid") );

            isOk = DeleteClass(deleteData);
            isOk = DeleteClassTutor(deleteData);
            isOk = UpdateStudentClass(deleteData);
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk >= 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    public Hashtable getSubjInfo(RequestBox box) throws Exception { 
        Hashtable subjinfo = new Hashtable();

        ListSet             ls      = null;
        String sql  = "";
        DBConnectionManager	connMgr	= null;

        try { 
            // 과목정보
            sql = "select a.grcode, c.grcodenm, a.gyear, a.grseq, d.grseqnm, b.upperclass, e.classname, ";
            sql += "       a.subjnm, a.subj, a.year, a.subjseq, a.subjseqgr, a.edustart, a.eduend        "; 
            sql += "  from tz_subjseq  a, ";
            sql += "       tz_subj     b, ";
            sql += "       tz_grcode   c, ";
            sql += "       tz_grseq    d, ";
            sql += "       tz_subjatt  e  ";
            sql += " where a.subj = b.subj ";
            sql += "   and a.grcode = c.grcode ";
            sql += "   and a.grcode = d.grcode ";
            sql += "   and a.gyear  = d.gyear  ";
            sql += "   and a.grseq  = d.grseq  ";
            sql += "   and b.upperclass  = e.upperclass ";
            sql += "   and e.middleclass = '000' ";
            sql += "   and e.lowerclass  = '000' ";
            sql += "   and a.subj   = " + SQLString.Format(box.getString("p_subj") );
            sql += "   and a.year   = " + SQLString.Format(box.getString("p_year") );
            sql += "   and a.subjseq= " + SQLString.Format(box.getString("p_subjseq01") );

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                subjinfo.put("p_grcode",    ls.getString("grcode") );
                subjinfo.put("p_grcodenm",  ls.getString("grcodenm") );
                subjinfo.put("p_gyear",     ls.getString("gyear") );
                subjinfo.put("p_grseq",     ls.getString("grseq") );
                subjinfo.put("p_grseqnm",   ls.getString("grseqnm") );
                subjinfo.put("p_uperclass", ls.getString("upperclass") );
                subjinfo.put("p_classname", ls.getString("classname") );
                subjinfo.put("p_subjnm",    ls.getString("subjnm") );
                subjinfo.put("p_subj",      ls.getString("subj") );
                subjinfo.put("p_year",      ls.getString("year") );
                subjinfo.put("p_subjseq",   ls.getString("subjseq") );
                subjinfo.put("p_subjseqgr", ls.getString("subjseqgr") );
                subjinfo.put("p_edustart",  ls.getString("edustart") );
                subjinfo.put("p_eduend",    ls.getString("eduend") );
            }
            ls.close();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return subjinfo;
    }

    /*
     * 클래스별 학생 명단 조회
     * */    
    public ArrayList SelectMemberList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        DBConnectionManager	connMgr	        = null;
        ListSet             ls              = null;
        String              sql             = "";
        ClassMemberData     data            = null;
        Hashtable           tutorinfo       = null;

        try { 
            sql   = "select                                                                         \n" +
                    "    b.comp                             asgn,                                   \n" +
                    "    nvl(get_compnm(b.comp), ' ')   companynm,                              	\n" +
                    "    nvl(get_compnm(b.comp), ' ')   asgnnm,                                 	\n" +
                    "    ' '                                    jikwi,                              \n" +
                    "    nvl(b.post_nm, ' ')  jikwinm,                                				\n" +
                    "    b.userid,                                                                  \n" +
                    "    ' ' cono,                                                                  \n" +
                    "    b.name,                                                                    \n" +
                    "    a.class,                                                                   \n" +
                    "    c.classnm,                                                                 \n" +
                    "    a.subj,                                                                    \n" +
                    "    a.year,                                                                    \n" +
                    "    a.subjseq                                                                  \n";
             sql += "  from tz_student  a,                                                          \n";
             sql += "       tz_member   b,                                                          \n";
             sql += "       tz_class    c                                                           \n";
             sql += " where                                                                         \n";
             sql += "       a.userid    = b.userid                                                  \n";
             sql += "   and a.subj      = c.subj( +)                                                \n";
             sql += "   and a.year      = c.year( +)                                                \n";
             sql += "   and a.subjseq   = c.subjseq( +)                                             \n";
             sql += "   and a.class     = c.class( +)                                               \n";
             sql += "   and a.subj      = " + SQLString.Format(box.getString("p_subj") ) + "        \n";
             sql += "   and a.year      = " + SQLString.Format(box.getString("p_year") ) + "        \n";
             sql += "   and a.subjseq   = " + SQLString.Format(box.getString("p_subjseq01") ) + "   \n";
             if ( !box.getStringDefault("p_class","ALL").equals("ALL") ) { 
                 sql += "   and a.class      = " + SQLString.Format(box.getString("p_class") ) + "       \n";
             }

            connMgr = new DBConnectionManager();
            
            System.out.println("\n\n\n"+sql+"\n\n\n");
            ls = connMgr.executeQuery(sql);

            
            while ( ls.next() ) { 
                data = new ClassMemberData();
                data.setCompanynm(  ls.getString("companynm") );
                data.setAsgn(       ls.getString("asgn") );
                data.setAsgnnm(     ls.getString("asgnnm") );
                data.setJikwi(      ls.getString("jikwi") );
                data.setJikwinm(    ls.getString("jikwinm") );
                data.setUserid(     ls.getString("userid") );
                data.setCono(       ls.getString("cono") );
                data.setName(       ls.getString("name") );
                data.setClass1(     ls.getString("class") );
                data.setClassnm(    ls.getString("classnm") );
                data.setSubj(       ls.getString("subj") );
                data.setYear(       ls.getString("year") );
                data.setSubjseq(    ls.getString("subjseq") );
                
                tutorinfo = getTutorInfo(connMgr, data.getSubj(), data.getYear(), data.getSubjseq(), data.getClass1() );                
                data.setMtutor((String)tutorinfo.get("p_mtutor") );
                data.setStutor((String)tutorinfo.get("p_stutor") );
                
                list.add(data);
            }
            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

    /*
     * 
     */
    public Hashtable getTutorInfo(DBConnectionManager connMgr, String p_subj, String p_year, String p_subjseq, String p_class) throws Exception { 
        Hashtable tutorinfo = new Hashtable();
        String v_stutor  = "";
        String v_mtutor  = "";
        String v_mtutorid = "";
        int   v_tutorcnt = 0;    
        
        ListSet             ls      = null;
        String sql  = "";
        try { 
            // 부강사
            sql = "select a.tuserid, a.ttype, b.name";
            sql += "  from tz_classtutor  a, ";
            sql += "       tz_tutor       b  ";
            sql += " where a.tuserid = b.userid ";
            sql += "   and a.subj    = " + SQLString.Format(p_subj);
            sql += "   and a.year    = " + SQLString.Format(p_year);
            sql += "   and a.subjseq = " + SQLString.Format(p_subjseq);
            sql += "   and a.class   = " + SQLString.Format(p_class);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( ls.getString("ttype").equals("M") ) { 
                    v_mtutor  = ls.getString("name");
                    v_mtutorid  = ls.getString("tuserid");
                } else if ( ls.getString("ttype").equals("S") ) { 
                    v_stutor += ls.getString("name") + "<br > ";
                }
            }
            ls.close();
            
            // 보조강사를 가져온다. 2006.07.08 김민수
            sql = "select a.userid, b.name";
            sql += "  from tz_student  a, ";
            sql += "       tz_member       b  ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.subj    = " + SQLString.Format(p_subj);
            sql += "   and a.year    = " + SQLString.Format(p_year);
            sql += "   and a.subjseq = " + SQLString.Format(p_subjseq);
            sql += "   and a.class   = " + SQLString.Format(p_class);
            sql += "   and a.issubtutor = 'Y'";

            ls = connMgr.executeQuery(sql);
            v_stutor = "";
            while ( ls.next() ) { 
                v_stutor += ls.getString("name") + "";                        
            }
            ls.close();            
            
            tutorinfo.put("p_mtutor", v_mtutor);
            tutorinfo.put("p_mtutorid", v_mtutorid);
            tutorinfo.put("p_stutor", v_stutor);

            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }

            // 강사숫자
            sql = "select count(*) tutorcnt ";
            sql += "  from tz_subjman  a, ";
            sql += "       tz_tutor    b  ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.gadmin like 'P%'     ";
            sql += "   and a.subj   = " + SQLString.Format(p_subj);
            
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_tutorcnt = ls.getInt("tutorcnt");
                tutorinfo.put("p_tutorcnt", String.valueOf(v_tutorcnt));
            }
            ls.close();
        } catch ( Exception ex ) {             
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally {
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return tutorinfo;
    }

    public Hashtable getTutorInfo(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        Hashtable tutorinfo  = null;
        try { 
            connMgr = new DBConnectionManager();
            tutorinfo  = getTutorInfo(connMgr, box.getString("p_subj"), box.getString("p_year"), box.getString("p_subjseq01"), box.getStringDefault("p_class", ClassBean.SINGLE_CLASS_CODE));
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, "");
            throw new Exception("sql = " + "\r\n" + ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return tutorinfo;
    }

    /**
    학습자 클래스 수정 - 개인별
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int UpdateStudentClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = -1;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq01");
        String v_class = box.getString("p_class");
        String v_userid = "";
        String v_tutorid = box.getString("p_mtutorid");

        // p_userids 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_userids = box.getVector("p_checks");
        Enumeration em  = v_userids.elements();
        Hashtable paramData = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            paramData.put("connMgr",  connMgr);
            paramData.put("subj",    v_subj);
            paramData.put("year",    v_year);
            paramData.put("subjseq", v_subjseq);
            paramData.put("class",   v_class);
            paramData.put("tutorid", v_tutorid);
            paramData.put("classtype", ClassBean.PLURAL_CLASS);
            paramData.put("ttype",   "M");
            paramData.put("luserid", box.getSession("userid") );

            isOk = DeleteClassTutor(paramData);
            isOk = InsertClassTutor(paramData);

            while ( em.hasMoreElements() ) { 
                v_userid = (String)em.nextElement();
                paramData.clear();
                paramData.put("connMgr",  connMgr);
                paramData.put("subj",    v_subj);
                paramData.put("year",    v_year);
                paramData.put("subjseq", v_subjseq);
                paramData.put("class",   v_class);
                paramData.put("userid",  v_userid);
                paramData.put("tutorid", v_tutorid);
                paramData.put("classtype", ClassBean.PLURAL_CLASS);
                paramData.put("luserid", box.getSession("userid") );

                isOk = UpdateStudentClass(paramData);
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk >= 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    /**
    학습자 클래스 수정 - 사업부,직군, 직급별
    @param box      receive from the form object and session
    @return isOk    1:insert success,0:insert fail
    */
     public int UpdateGroupClass(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;

        String              sql     = "";
        int isOk = -1;

        String v_subj = box.getString("p_subj");
        String v_year = box.getString("p_year");
        String v_subjseq = box.getString("p_subjseq01");
        String v_class   = box.getString("p_class");
        String v_code    = "";
        String v_tutorid = box.getString("p_mtutorid");
        String v_subpage = box.getString("p_subpage");

        // p_userids 넘어온 다수의 value를 처리하기 위해 vector로 구현
        Vector v_checks = box.getVector("p_checks");
        Enumeration em  = v_checks.elements();
        Hashtable paramData = new Hashtable();

        try { 
            connMgr = new DBConnectionManager();
            connMgr.setAutoCommit(false);

            paramData.put("connMgr",  connMgr);
            paramData.put("subj",    v_subj);
            paramData.put("year",    v_year);
            paramData.put("subjseq", v_subjseq);
            paramData.put("class",   v_class);
            paramData.put("tutorid", v_tutorid);
            paramData.put("classtype", ClassBean.PLURAL_CLASS);
            paramData.put("ttype",   "M");
            paramData.put("luserid", box.getSession("userid") );

            isOk = DeleteClassTutor(paramData);
            isOk = InsertClassTutor(paramData);

            while ( em.hasMoreElements() ) { 
                v_code = (String)em.nextElement();
                paramData.clear();
                paramData.put("connMgr",  connMgr);
                paramData.put("subj",    v_subj);
                paramData.put("year",    v_year);
                paramData.put("subjseq", v_subjseq);
                paramData.put("class",   v_class);
                paramData.put("code",    v_code);
                paramData.put("subpage", v_subpage);
                paramData.put("luserid", box.getSession("userid") );

                isOk = UpdateStudentClassGroup(paramData);
            }
        } catch ( Exception ex ) { 
            if ( connMgr != null ) { connMgr.rollback(); }
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( isOk >= 0 ) { connMgr.commit(); }
            if ( connMgr != null ) { try { connMgr.setAutoCommit(true); } catch ( Exception e10 ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return isOk;
    }

    public int UpdateStudentClassGroup(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_class   = (String)data.get("class");;
        String v_code    = (String)data.get("code");
        String v_subpage = (String)data.get("subpage");
        String v_luserid = (String)data.get("luserid");

        // update TZ_STUDENT table
        sql =  "update tz_student    ";
        sql +=  "    set class   = ?, ";
        sql +=  "        luserid = ?, ";
        sql +=  "        ldate   = ?  ";
        sql +=  "  where subj    = ?  ";
        sql +=  "    and year    = ?  ";
        sql +=  "    and subjseq = ?  ";
        sql +=  "    and userid in (select a.userid ";
        sql +=  "                     from tz_student a, ";
        sql +=  "                          tz_member  b  ";
        sql +=  "                    where a.userid  = b.userid ";
        sql +=  "                      and a.subj    = ?  ";
        sql +=  "                      and a.year    = ?  ";
        sql +=  "                      and a.subjseq = ?  ";
        if ( v_subpage.equals("asgn_page") ) { 
            sql +=  "                      and b.comp    = ?) ";
        } else if ( v_subpage.equals("jikun_page") ) { 
            sql +=  "                      and b.jikun   = ?) ";
        } else if ( v_subpage.equals("jikup_page") ) { 
            sql +=  "                      and b.jikup   = ?) ";
        }

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }
            pstmt = connMgr.prepareStatement(sql);
            pstmt.setString( 1, v_class);
            pstmt.setString( 2, v_luserid);
            pstmt.setString( 3, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString( 4, v_subj);
            pstmt.setString( 5, v_year);
            pstmt.setString( 6, v_subjseq);
            pstmt.setString( 7, v_subj);
            pstmt.setString( 8, v_year);
            pstmt.setString( 9, v_subjseq);
            pstmt.setString(10, v_code);
            isOk = pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    /**
    전체 클래스 삭제
    @param  Hashtable    input data
    @return Hashtable    delete data
    */
    public Hashtable DeleteAllClass(Hashtable inputdata)  { 
        Hashtable outputdata = new Hashtable();
        Hashtable deleteData = new Hashtable();

        outputdata.put("p_errorcode", "1");

        DBConnectionManager	connMgr	= null;
        String  v_subj      = "";
        String  v_year      = "";
        String  v_subjseq   = "";
        String  v_luserid   = "";

        try { 
            connMgr = (DBConnectionManager)inputdata.get("connMgr");

            v_subj    = (String)inputdata.get("subj");
            v_year    = (String)inputdata.get("year");
            v_subjseq = (String)inputdata.get("subjseq");
            v_luserid = (String)inputdata.get("luserid");

            deleteData.put("connMgr", connMgr);
            deleteData.put("subj",    v_subj);
            deleteData.put("year",    v_year);
            deleteData.put("subjseq", v_subjseq);
            deleteData.put("classtype", ClassBean.SINGLE_CLASS);
            deleteData.put("ttype",  "ALL");
            deleteData.put("luserid", v_luserid);

            DeleteClass(deleteData);
            DeleteClassTutor(deleteData);
            UpdateStudentClass(deleteData);
        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
        }

        return outputdata;
    }

    public Hashtable InsertClass2(Hashtable data) { 
        Hashtable outputdata = new Hashtable();
        outputdata.put("p_errorcode", "1");

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";
        String v_subj     = (String)data.get("subj");
        String v_year     = (String)data.get("year");
        String v_subjseq  = (String)data.get("subjseq");
        String v_class    = (String)data.get("class");
        String v_classnm  = (String)data.get("classnm");
        String v_luserid  = (String)data.get("luserid");
        String v_comp     = "";

        // insert TZ_CLASS table
        sql =  "insert into TZ_CLASS ( " ;
        sql +=  " subj,    year,   subjseq,   class,  ";
        sql +=  " classnm, comp,   luserid,   ldate ) ";
        sql +=  " values ( ";
        sql +=  " ?, ?, ?, ?, ";
        sql +=  " ?, ?, ?, ? ) ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_subj);
            pstmt.setString( 2, v_year);
            pstmt.setString( 3, v_subjseq);
            pstmt.setString( 4, v_class);
            pstmt.setString( 5, v_classnm);
            pstmt.setString( 6, v_comp);
            pstmt.setString( 7, v_luserid);
            pstmt.setString( 8, FormatDate.getDate("yyyyMMddHHmmss") ); // ldate
            pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return outputdata;
    }

    public Hashtable UpdateStudentClass2(Hashtable data)  { 
        Hashtable outputdata = new Hashtable();
        outputdata.put("p_errorcode", "1");

        DBConnectionManager	connMgr	= null;
        PreparedStatement   pstmt   = null;
        String              sql     = "";

        String v_subj    = (String)data.get("subj");
        String v_year    = (String)data.get("year");
        String v_subjseq = (String)data.get("subjseq");
        String v_class   = (String)data.get("class");;
        String v_classtype= (String)data.get("classtype");
        String v_userid  = (String)data.get("userid");
        String v_luserid = (String)data.get("luserid");

        // update TZ_STUDENT table
        sql =  "update TZ_STUDENT  " ;
        sql +=  "   set class   =  ?, ";
        sql +=  "       luserid =  ?, ";
        sql +=  "       ldate   =  ?  ";
        sql +=  " where subj    =  ?  ";
        sql +=  "   and year    =  ?  ";
        sql +=  "   and subjseq =  ?  ";
        sql +=  "   and userid   = ? ";

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");

            pstmt = connMgr.prepareStatement(sql);

            pstmt.setString( 1, v_class);
            pstmt.setString( 2, v_luserid);
            pstmt.setString( 3, FormatDate.getDate("yyyyMMddHHmmss") );
            pstmt.setString( 4, v_subj);
            pstmt.setString( 5, v_year);
            pstmt.setString( 6, v_subjseq);
            pstmt.setString( 7, v_userid);

            pstmt.executeUpdate();
        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
            if ( pstmt != null ) { try { pstmt.close(); } catch ( Exception e ) { } }
        }

        return outputdata;
    }

    /**
    클래스분반 FILE TO DB
    @param Hashtable    클래스 분반 정보
    @return Hashtable   에러내용
    */
    public Hashtable FileToDB(Hashtable data)  { 
        Hashtable outputdata = new Hashtable();
        outputdata.put("p_errorcode", "1");
        int isOk =0;

        try { 
            // tz_class  select 후 delete / insert   클래스 생성
            data.put("classtype", ClassBean.PLURAL_CLASS);

            isOk = DeleteClass(data);
            isOk = InsertClass(data);
            // tz_classtutor select 후 delete / insert 강사 배정
            data.put("ttype",   "M");
            isOk = DeleteClassTutor(data);
            isOk = InsertClassTutor(data);
            // tz_manager select 후 insert / update 강사권한 설정
            isOk = InsertManager(data);
            isOk = InsertSubjman(data);
            // tz_student update                     클래스 배정
            isOk = UpdateStudentClass(data);
        } catch ( Exception ex ) { 
            outputdata.put("p_errorcode", "0");
            outputdata.put("p_exception", ex);
        } finally { 
        }

        return outputdata;
    }


    /**
    강사 권한 등록
    @param Hashtable    클래스 분반 정보
    @return isOk    1:insert success, 0:insert fail
    */
    public int InsertManager(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String v_tutorid = (String)data.get("tutorid");
        String v_subj    = (String)data.get("subj");
        String v_fmon    = FormatDate.getDate("yyyyMMdd");                                      // 권한 시작일자 (오늘날자로 세팅)
        String v_tmon    = FormatDate.getDateAdd(StringManager.substring((String)data.get("eduend"),0,8), "yyyyMMdd", "month", 1) ;  // 권한 종료일자 (권한 학습종료일로부터 1개월)
        String v_compcd  = "";
        String v_luserid = (String)data.get("luserid");
        String v_gadmin  = "P1";// 강사 기본권한으로 셋팅
        
        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            // 강사권한 select
            sql1 = "select fmon, tmon, gadmin from TZ_MANAGER where userid = '" +v_tutorid + "' and gadmin like 'P%'";
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() )  { 
                if ( StringManager.toInt(v_tmon) < StringManager.toInt( ls1.getString("tmon"))) v_tmon = ls1.getString("tmon");          // 비교 후 큰 날자로
                sql2  = " update TZ_MANAGER set isdeleted = 'N' , fmon ='" +v_fmon + "', tmon='" +v_tmon + "', ";
                sql2 += "                       luserid = '" +v_luserid + "', ldate =to_char(sysdate, 'YYYYMMDDHH24MISS') ";
                sql2 += "  where userid = '" +v_tutorid + "' and gadmin = " + StringManager.makeSQL(ls1.getString("gadmin"));
                isOk = connMgr.executeUpdate(sql2);
            } else { 
                sql3 =  "insert into TZ_MANAGER(userid, gadmin, comp, isdeleted, fmon, tmon, luserid, ldate) ";
                sql3 +=  "values('" +v_tutorid + "','"+v_gadmin+"','" +v_compcd + "','N','" +v_fmon + "','" +v_tmon + "','" +v_luserid + "', to_char(sysdate, 'YYYYMMDDHH24MISS'))";
                isOk = connMgr.executeUpdate(sql3);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }


    /**
    강사 담당 과목 등록
    @param Hashtable    클래스 분반 정보
    @return isOk    1:insert success, 0:insert fail
    */
    public int InsertSubjman(Hashtable data) throws Exception { 
        int isOk = -1;
        boolean v_CreateConnManager = false;
        DBConnectionManager	connMgr	= null;
        ListSet ls1 = null;

        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        String v_tutorid = (String)data.get("tutorid");
        String v_subj    = (String)data.get("subj");
        String v_luserid = (String)data.get("luserid");
        String v_gadmin  = "P1"; // 강사 기본권한으로 셋팅

        try { 
            connMgr = (DBConnectionManager)data.get("connMgr");
            if ( connMgr == null ) { 
                connMgr = new DBConnectionManager();
                v_CreateConnManager = true;
            }

            // 강사권한 select
            sql1 = "select userid from tz_subjman where userid = '" +v_tutorid + "' and gadmin like 'P%' and subj = '" +  v_subj + "'";
            ls1 = connMgr.executeQuery(sql1);

            if ( ls1.next() )  { 

            } else { 
                sql3  =  "insert into tz_subjman(userid,gadmin,subj,luserid,ldate) ";
                sql3 +=  "                 values('" +v_tutorid + "','"+v_gadmin+"','" +v_subj + "','" +v_luserid + "', to_char(sysdate, 'YYYYMMDDHH24MISS'))";
                isOk = connMgr.executeUpdate(sql3);
            }

        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, null, sql1);
            throw new Exception("sql = " + sql1 + "\r\n" + ex.getMessage() );
        } finally { 
            if ( v_CreateConnManager) { 
                if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
            }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        }

        return isOk;
    }

    

    /**
    과목코드 및 학생 리스트
    @param box          receive from the form object and session
    @return ArrayList   과목코드 및 학생 리스트
    */
    public ArrayList ClassStudentList(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        DataBox             dbox    = null;

        String v_grcode   = box.getStringDefault("s_grcode","ALL");
        String v_gyear    = box.getString("s_gyear");
        String v_grseq    = box.getStringDefault("s_grseq","ALL");
        String v_subj     = box.getStringDefault("s_subjcourse","ALL");
        String v_subjseq  = box.getStringDefault("s_subjseq","ALL");
        String v_comp     = box.getStringDefault("s_comp","ALL");

        try { 
                connMgr = new DBConnectionManager();
                sql = "  select a.subj, a.year, a.subjseq, b.userid, a.subjseqgr  ";
                sql += "    from tz_subjseq a, tz_student b                        ";
                sql += "   where a.subj = b.subj and a.year=b.year and a.subjseq=b.subjseq ";
                if ( !v_grcode.equals("ALL"))  sql += "   and a.grcode = " + SQLString.Format(v_grcode);
                if ( !v_gyear.equals("ALL"))   sql += "   and a.gyear = " + SQLString.Format(v_gyear);
                if ( !v_grseq.equals("ALL"))   sql += "   and a.grseq = " + SQLString.Format(v_grseq);
                if ( !v_subj.equals("ALL"))    sql += "   and a.subj = " + SQLString.Format(v_subj);
                if ( !v_subjseq.equals("ALL")) sql += "   and a.subjseq = " + SQLString.Format(v_subjseq);
                sql += " order by subj, year, subjseq";
                // System.out.println(sql);

                ls = connMgr.executeQuery(sql);

                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    list.add(dbox);
                }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }
    


    /**
    복수클래스 엑셀보기 
    @param box          receive from the form object and session
    @return ArrayList   
    */
    public ArrayList SelectMemberListExcel(RequestBox box) throws Exception { 
        ArrayList list = new ArrayList();
        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String sql  = "";
        ClassMemberData data = null;
        Hashtable tutorinfo  = null;

				String v_grcode  = box.getString("s_grcode");
				String v_grseq   = box.getString("s_grseq");
				String v_subj    = box.getStringDefault("s_subjcourse", "ALL");
				String v_year    = box.getStringDefault("s_gyear", "ALL");
				String v_subjseq = box.getStringDefault("s_subjseq", "ALL");
				
				
        try { 

            sql = "select b.comp asgn "
            	+ "     , get_compnm(b.comp) companynm "
            	+ "     , get_compnm(b.comp) asgnnm "
            	+ "     , b.userid "
            	+ "     , b.name "
            	+ "     , a.class "
            	+ "     , c.classnm "
            	+ "     , a.subj "
            	+ "     , a.year "
            	+ "     , a.subjseq "; //b.jikwi, get_jikwinm(b.jikwi, b.comp) jikwinm, b.cono,  
            sql += "    , d.scsubjnm, d.subjseqgr  ";
            sql += "  from tz_student  a, ";
            sql += "       tz_member   b, ";
            sql += "       tz_class    c, ";
            sql += "       vz_scsubjseq d  ";
            sql += " where a.userid = b.userid ";
            sql += "   and a.subj   = c.subj(+) ";
            sql += "   and a.year   = c.year(+) ";
            sql += "   and a.subjseq = c.subjseq(+) ";
            sql += "   and a.class   = c.class(+) ";
            sql += "   and a.subj   = d.subj      ";   
            sql += "   and a.year   = d.year      ";   
            sql += "   and a.subjseq = d.subjseq  ";   
            sql += "   and d.grcode=" +SQLString.Format(v_grcode);
            if ( !v_grseq.equals("") ) { 
            	sql += "   and d.grseq= " + SQLString.Format(v_grseq);
          	}
          	if ( !v_subj.equals("ALL") ) { 
            	sql += "   and a.subj = " + SQLString.Format(v_subj);
            }
            if ( !v_year.equals("ALL") ) { 
            	sql += "   and a.year = " + SQLString.Format(v_year);
            }
            if ( !v_subjseq.equals("ALL") ) { 
            	sql += "   and a.subjseq = " + SQLString.Format(v_subjseq);       
            }
            sql += " order by subj, b.userid ";

            connMgr = new DBConnectionManager();
            ls = connMgr.executeQuery(sql);

            while ( ls.next() ) { 
                data = new ClassMemberData();
                data.setCompanynm( ls.getString("companynm") );
                data.setAsgn( ls.getString("asgn") );
                data.setAsgnnm( ls.getString("asgnnm") );
                //data.setJikwi( ls.getString("jikwi") );
                //data.setJikwinm( ls.getString("jikwinm") );
                data.setUserid( ls.getString("userid") );
                //data.setCono( ls.getString("cono") );
                data.setName( ls.getString("name") );
                data.setClass1( ls.getString("class") );
                data.setClassnm( ls.getString("classnm") );
                data.setSubj( ls.getString("subj") );
                data.setYear( ls.getString("year") );
                data.setSubjseq( ls.getString("subjseq") ); // 과목기수
                data.setSubjnm( ls.getString("scsubjnm") ); // 과목명

                tutorinfo = getTutorInfo(connMgr, data.getSubj(), data.getYear(), data.getSubjseq(), data.getClass1() );
                data.setMtutor((String)tutorinfo.get("p_mtutor") );
                data.setMtutorid((String)tutorinfo.get("p_mtutorid") );
                data.setStutor((String)tutorinfo.get("p_stutor") );

                list.add(data);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex, box, sql);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }    

}
