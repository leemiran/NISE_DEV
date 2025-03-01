// **********************************************************
// 1. 제      목:
// 2. 프로그램명: SulmunCommonResultBean.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-08-18
// 7. 수      정:
// 
// **********************************************************

package com.ziaan.research;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ziaan.library.DBConnectionManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.ListSet;
import com.ziaan.library.RequestBox;
import com.ziaan.library.SQLString;
import com.ziaan.library.StringManager;
import com.ziaan.system.SelectionUtil;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SulmunCommonResultBean { 


    public SulmunCommonResultBean() { }

//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
    /**
    일반설문 결과분석 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList SelectObectResultList(RequestBox box) throws Exception 
    { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        String v_action = box.getString("p_action");

        try {
                connMgr = new DBConnectionManager();
                list = getObjectSulmunResult(connMgr, box);            
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }

//// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// ///
    /**
    일반설문 결과분석 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getObjectSulmunResult(DBConnectionManager connMgr, RequestBox box) throws Exception { 
        Vector    v_sulnums = null;
        ArrayList QuestionExampleDataList  = null;
        Vector    v_answers = null;

        String v_subj     = box.getStringDefault("p_subj",SulmunCommonBean.DEFAULT_SUBJ);
        String v_subjseq  = box.getStringDefault("p_subjseq","0001");
        int v_sulpapernum = box.getInt("p_sulpapernum");

        String v_grseq    = box.getStringDefault("p_grseq","0001");

        // 설문번호 선택조건
        String v_grcode   = box.getString("p_grcode");
        String v_gyear    = box.getString("p_gyear");


        // 응답자 선택조건
        String v_company  = box.getString("p_company");
        String v_jikwi    = box.getString("p_jikwi");
        String v_jikun    = box.getString("p_jikun");
        String v_workplc  = box.getString("p_workplc");

        int v_studentcount= 0;

        try { 

            // 설문지번호에 해당하는 설문번호를 vector로 받아온다. 벡터(설문번호1,3,5 ....)
            v_sulnums = getSulnums(connMgr, v_grcode, v_subj, v_sulpapernum);

            // 설문번호에 해당하는 문제리스트를 만든다. 리스트((설문번호1, 보기1,2,3..))
            QuestionExampleDataList = getSelnums(connMgr, v_grcode, v_subj, v_sulnums);

            // 설문지 답변모음을 읽어온다. 벡터(답변1,3,2,3..)
             v_answers = getSulmunAnswers(connMgr, v_grcode, v_gyear, v_grseq, v_subj, v_sulpapernum, v_subjseq, v_company, v_jikwi, v_jikun, v_workplc);
    
            // 리스트((설문번호1, 보기1,2,3..)) + 벡터(답변1,3,2,3..) 이용해서 응답자수 및 비율을 계산한다.
            ComputeCount(QuestionExampleDataList, v_answers);

            // 응답자수
            box.put("p_replycount", String.valueOf(v_answers.size() ));

            // 수강자수
             v_studentcount = getStudentCount(connMgr, v_grcode, v_gyear, v_grseq, v_subj, v_subjseq, v_company, v_jikwi, v_jikun, v_workplc);

			box.put("p_studentcount", String.valueOf(v_studentcount));
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }

        return QuestionExampleDataList;
    }


    /**
    일반설문 설문번호 구하기 
    @param box          receive from the form object and session
    @return Vector
    */
	public Vector getSulnums(DBConnectionManager connMgr, String p_grcode, String p_subj, int p_sulpapernum) throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        Vector v_sulnums = new Vector();
        String v_tokens  = "";
        StringTokenizer st = null;

        try { 
            sql = "select sulnums  ";
            sql += "  from tz_sulpaper ";
//          sql += " where grcode      = " + SQLString.Format(p_grcode);
            sql += " where grcode      = " + SQLString.Format(SulmunCommonBean.DEFAULT_GRCODE);
            sql += "   and subj        = " + SQLString.Format(p_subj);
            sql += "   and sulpapernum = " + SQLString.Format(p_sulpapernum);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_tokens = ls.getString("sulnums");
            }

            st = new StringTokenizer(v_tokens,SulmunCommonBean.SPLIT_COMMA);
            while ( st.hasMoreElements() ) { 
                v_sulnums.add((String)st.nextToken() );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_sulnums;
    }


    /**
    일반설문 설문번호 구하기 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public ArrayList getSelnums(DBConnectionManager connMgr, String p_grcode, String p_subj, Vector p_sulnums) throws Exception { 
        Hashtable hash = new Hashtable();
        ArrayList list = new ArrayList();

        ListSet ls  = null;
        String sql  = "";
        SulmunQuestionExampleData data = null;
        SulmunExampleData exampledata  = null;
        int v_bef_sulnum = 0;

        String v_sulnums = "";
        for ( int i = 0; i < p_sulnums.size(); i++ ) { 
            v_sulnums += (String)p_sulnums.get(i);
            if ( i<p_sulnums.size()-1) { 
                v_sulnums += ", ";
            }
        }
        if ( v_sulnums.equals("")) v_sulnums = "-1";

        try { 
            sql = "select a.subj,     a.sulnum, ";
            sql +="        a.distcode, c.codenm distcodenm, ";
            sql += "       a.sultype,  d.codenm sultypenm, ";
            sql += "       a.sultext,  b.selnum, b.seltext, b.selpoint ";
            sql += "  from tz_sul     a, ";
            sql += "       tz_sulsel  b, ";
            sql += "       tz_code    c, ";
            sql += "       tz_code    d  ";
            sql += " where a.subj     = b.subj( +)    ";
            sql += "   and a.sulnum   = b.sulnum( +)  ";
            sql += "   and a.grcode  = b.grcode( +) ";
			sql += "   and a.distcode = c.code ";
            sql += "   and a.sultype  = d.code ";
            sql += "   and a.subj     = " + SQLString.Format(p_subj);
            sql += "   and a.grcode     = " + SQLString.Format(p_grcode);
            sql += "   and a.sulnum in (" + v_sulnums + ")";
            sql += "   and c.gubun    = " + SQLString.Format(SulmunCommonBean.DIST_CODE);
            sql += "   and d.gubun    = " + SQLString.Format(SulmunCommonBean.SUL_TYPE);
            sql += " order by a.subj, a.sulnum, b.selnum ";

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                if ( v_bef_sulnum != ls.getInt("sulnum") ) { 
                    data = new SulmunQuestionExampleData();
                    data.setSubj( ls.getString("subj") );
                    data.setSulnum( ls.getInt("sulnum") );
                    data.setSultext( ls.getString("sultext") );
                    data.setSultype( ls.getString("sultype") );
                    data.setSultypenm( ls.getString("sultypenm") );
                    data.setDistcode( ls.getString("distcode") );
                    data.setDistcodenm( ls.getString("distcodenm") );
                }
                exampledata = new SulmunExampleData();
                exampledata.setSubj(data.getSubj() );
                exampledata.setSulnum(data.getSulnum() );
                exampledata.setSelnum( ls.getInt("selnum") );
                exampledata.setSelpoint( ls.getInt("selpoint") );
                exampledata.setSeltext( ls.getString("seltext") );
                data.add(exampledata);
                if ( v_bef_sulnum != data.getSulnum() )  { 
                    hash.put(String.valueOf(data.getSulnum() ), data);
                    v_bef_sulnum = data.getSulnum();
                }
            }
            data = null;
            for ( int i = 0; i < p_sulnums.size(); i++ ) { 
                data = (SulmunQuestionExampleData)hash.get((String)p_sulnums.get(i));
                if ( data != null ) { 
                    list.add(data);
                }
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return list;
    }


    /**
    일반설문 결과 응답비율 계산 
    @param box          receive from the form object and session
    @return void
    */
    public void ComputeCount(ArrayList p_list, Vector p_answers) throws Exception { 
        StringTokenizer st1 = null;
        StringTokenizer st2 = null;

        SulmunQuestionExampleData data = null;
		Vector subject = new Vector();
		Vector complex = new Vector();

        String v_answers = "";
        String v_answer  = "";
        int index=0;

        try { 
            for ( int i = 0; i < p_answers.size(); i++ ) { 
                v_answers = (String)p_answers.get(i);
                st1 = new StringTokenizer(v_answers,SulmunCommonBean.SPLIT_COMMA);
                index = 0;
                while ( st1.hasMoreElements() && index < p_list.size() ) { 
                    v_answer = (String)st1.nextToken();
                    data = (SulmunQuestionExampleData)p_list.get(index);

                    if ( data.getSultype().equals(SulmunCommonBean.OBJECT_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                    } else if ( data.getSultype().equals(SulmunCommonBean.MULTI_QUESTION)) { 
                        st2 = new StringTokenizer(v_answer,SulmunCommonBean.SPLIT_COLON);
                        while ( st2.hasMoreElements() ) { 
                            v_answer = (String)st2.nextToken();
                            data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                        }
                    } else if ( data.getSultype().equals(SulmunCommonBean.SUBJECT_QUESTION)) { 
                        subject.add(v_answer);
                    } else if ( data.getSultype().equals(SulmunCommonBean.COMPLEX_QUESTION)) { 
                        st2 = new StringTokenizer(v_answer,SulmunCommonBean.SPLIT_COLON);
                        v_answer = (String)st2.nextToken();
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                        String v_sanswer = "";
                        if ( st2.hasMoreElements() ) { 
                            v_sanswer = (String)st2.nextToken();
                        }
                        complex.add(v_sanswer);
                    } else if ( data.getSultype().equals(SulmunCommonBean.FSCALE_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                    } else if ( data.getSultype().equals(SulmunCommonBean.SSCALE_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                    } 
					// System.out.println(data.getNewComplexAnswer() );
                    index++;
                }
            }
            // 응답비율을 계산한다. 리스트((설문번호1, 보기1,2,3..))
            for ( int i = 0; i < p_list.size(); i++ ) { 
                data = (SulmunQuestionExampleData)p_list.get(i);
	   				data.ComputeRate();
	   				data.setComplexAnswer(complex);
					data.setSubjectAnswer(subject);
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        }
    }


    /**
    일반설문 결과 정답 리스트 구하기 
    @param box          receive from the form object and session
    @return Vector
    */
    public Vector getSulmunAnswers(DBConnectionManager connMgr, String p_grcode, String p_gyear,
                                    String p_grseq,    String p_subj, int p_sulpapernum, String p_subjseq,
                                    String p_company,  String p_jikwi,     String p_jikun, String p_workplc)  throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        Vector v_answers = new Vector();
        String v_comp = "";

        try { 
                                            sql  = "select c.answers                \n";
                                            sql += "  from tz_suleach   c,          \n";
                                            sql += "       tz_member    d           \n";
                                            sql += " where c.userid      = d.userid \n";
                                            sql += "   and c.grcode      = " + SQLString.Format(p_grcode)       + "   \n";
            if ( !p_gyear.equals("ALL"))    sql += "   and c.year        = " + SQLString.Format(p_gyear)        + "   \n";
                                            sql += "   and c.subj        = " + SQLString.Format(p_subj)         + "   \n";
            if ( !p_subjseq.equals("ALL"))  sql += "   and c.subjseq     = " + SQLString.Format(p_subjseq)      + "   \n";
                                            sql += "   and c.sulpapernum = " + SQLString.Format(p_sulpapernum)  + "   \n";
            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_answers.add( ls.getString("answers") );
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_answers;
    }


    /**
    일반설문 결과 학생수 구하기 
    @param box          receive from the form object and session
    @return int
    */
    public int  getStudentCount(DBConnectionManager connMgr, String p_grcode, String p_gyear, String p_grseq, String p_subj, String p_subjseq, 
                                String p_company,  String p_jikwi,     String p_jikun, String p_workplc)  throws Exception { 
        ListSet             ls      = null;
        String sql  = "";
        String v_comp = "";
        int v_count = 0;

        try { 
            sql = "  select count(*) cnt ";
            sql += "    from tz_subjseq    a, ";
            sql += "         tz_student    b, ";
            sql += "         tz_member     c  ";
            sql += "   where a.subj    = b.subj  ";
            sql += "     and a.year    = b.year  ";
            sql += "     and a.subjseq = b.subjseq  ";
            sql += "     and b.userid  = c.userid   ";
            sql += "     and a.subj  = " + SQLString.Format(p_subj);
            if ( !p_grcode.equals("ALL"))
                sql += " and a.grcode  = " + SQLString.Format(p_grcode);
            if ( !p_gyear.equals("ALL"))
                sql += " and a.gyear   = " + SQLString.Format(p_gyear);
            if ( !p_gyear.equals("ALL"))
                sql += " and a.subjseq   = " + SQLString.Format(p_subjseq);
            if ( !p_grseq.equals("ALL"))
//                sql += " and a.grseq   = " + SQLString.Format(p_grseq);

            // if ( !p_company.equals("ALL"))
            // sql += "     and c.comp = " + SQLString.Format(p_company);
            // if ( !p_jikwi.equals("ALL"))
            //    sql += " and c.jikwi   = " + SQLString.Format(p_jikwi);
            // if ( !p_jikun.equals("ALL"))
            //    sql += " and c.jikun   = " + SQLString.Format(p_jikun);
            // if ( !p_workplc.equals("ALL"))
            //    sql += " and c.work_plc   = " + SQLString.Format(p_workplc);

            ls = connMgr.executeQuery(sql);
            while ( ls.next() ) { 
                v_count = ls.getInt("cnt");
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        }

        return v_count;
    }


    /**
    일련버호 구하기 
    @param box          receive from the form object and session
    @return int
    */
    public int getPapernumSeq(String p_subj, String p_grcode) throws Exception { 
        Hashtable maxdata = new Hashtable();
        maxdata.put("seqcolumn","sulpapernum");
        maxdata.put("seqtable","tz_sulpaper");
        maxdata.put("paramcnt","2");
        maxdata.put("param0","subj");
        maxdata.put("param1","grcode");
        maxdata.put("subj",   SQLString.Format(p_subj));
        maxdata.put("grcode",   SQLString.Format(p_grcode));

        return SelectionUtil.getSeq(maxdata);
    }

    /**
    *  대상자 설문지 셀렉트박스 (RequestBox, 셀렉트박스명,선택값,이벤트명)
	  *  TZ_SULPAPER 이용
    */
    public static String getSulpaperSelect (String p_grcode, String p_gyear, String p_subj, String name, int selected, String event) throws Exception { 

        DBConnectionManager	connMgr	= null;
        ListSet             ls      = null;
        String result = null;
        String              sql     = "";
        DataBox     dbox = null;

        result = "  <SELECT name=" + name + " " + event + " > \n";

        result += " <option value='0' > 설문지를 선택하세요.</option > \n";

        try { 
            connMgr = new DBConnectionManager();

			sql = "select grcode,       subj,         ";
            sql += "       sulpapernum,  sulpapernm, year, ";
            sql += "       totcnt,       sulnums, sulmailing, sulstart, sulend, ";
            sql += "       'COMMON'      subjnm ";
            sql += "  from tz_sulpaper ";
            sql += " where grcode = " + StringManager.makeSQL(p_grcode);
            sql += "   and subj   = " +StringManager.makeSQL(p_subj);
            sql += "   and year   = " + StringManager.makeSQL(p_gyear);
            sql += " order by subj, sulpapernum asc";

            ls = connMgr.executeQuery(sql);

                String v_null_test = "";
                String v_subj_bef = "";

            while ( ls.next() ) { 

                    dbox = ls.getDataBox();

                result += " <option value=" + dbox.getInt("d_sulpapernum");
                if ( selected == dbox.getInt("d_sulpapernum") ) { 
                    result += " selected ";
                }
                
                result += " > " + dbox.getString("d_sulpapernm") + "</option > \n";
            }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception("sql = " + sql + "\r\n" + ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        result += "  </SELECT > \n";
        return result;
    } 
    
    
    /**
    과목설문 결과 엑셀리스트_상세
    @param box          receive from the form object and session
    @return ArrayList   과목설문 결과 엑셀리스트
    */
    public ArrayList selectSulmunDetailResultExcelList(RequestBox box) throws Exception { 
        DBConnectionManager	connMgr	= null;
        ArrayList           list    = null;
        ArrayList list2 = null;
        ListSet             ls      = null;
        ListSet ls2 = null;
        DataBox             dbox    = null;
   //     DataBox dbox2 = null;
        String              sql     = "";  
        String sql2 = "";  	
        
        try {      
            String v_grcode    	= box.getStringDefault("s_grcode", "N000001");
            String v_year    		= box.getString("s_gyear");
            String v_subj     = box.getStringDefault("p_subj", "COMMON");
            String v_subjseq     = box.getStringDefault("p_subjseq","0001");
            int v_sulpapernum = box.getInt("p_sulpapernum");    

            String v_action  		= box.getString("p_action");

            String v_subjusernm = "";
         
            list = new ArrayList();
            
            if ( v_action.equals("go") ) { 
                connMgr = new DBConnectionManager();
                
                sql = "select a.userid, b.comp  asgn,  get_compnm(b.comp,2,4) asgnnm, b.divinam,";
                sql += "	   	  b.jikwi,  get_jikwinm(b.jikwi, b.comp) jikwinm, ";
                sql += "	   	  b.cono,   b.name, a.subj, a.grcode, a.year, a.subjseq, a.sulpapernum, a.sulnums, a.answers";
                sql += "	   	  from ";
                sql += " ( select userid, subj, grcode, year, subjseq, sulpapernum, sulnums, answers ";
                sql += "  from tz_suleach  ";
                sql += "   where subj    = 'COMMON'";
                sql += "   and grcode    = " + SQLString.Format(v_grcode);
                sql += "   and year    = " + SQLString.Format(v_year);
                sql += "   and subjseq    = " + SQLString.Format(v_subjseq);
		sql += "   and sulpapernum    = " + SQLString.Format(v_sulpapernum) + " ) a, ";
                sql += "         tz_member  b  ";
                sql += "         where a.userid = b.userid  ";
                sql += " order by a.userid ";

                ls = connMgr.executeQuery(sql);// System.out.println(sql);
                
                Vector sulnum = new Vector();               
                
                while ( ls.next() ) { 
                    dbox = ls.getDataBox();
                    
                    v_subjusernm = dbox.getString("d_name");
                    dbox.put("d_subjusernm", v_subjusernm);
                    dbox.put("d_subjasgmnm", dbox.getString("d_asgnnm") );

	   /*         sql2 = "select userid, sulnums, answers";
                    sql2 += "  from tz_suleach";
                    sql2 += "   where subj    = 'TARGET'";
                    sql2 += "   and grcode    = " + SQLString.Format(v_grcode);
                    sql2 += "   and year    = " + SQLString.Format(v_year);
                    sql2 += "   and subjseq    = " + SQLString.Format(v_subjseq);
                    sql2 += "   and sulpapernum    = " + SQLString.Format(v_sulpapernum);
                    sql2 += "   and userid    = " + SQLString.Format(dbox.getString("d_userid") );*/
                    
             //       ls2 = connMgr.executeQuery(sql2);System.out.println("sql2" + sql2);
                                        

             //       if ( ls2.next() ) { 
                     //   dbox2 = ls2.getDataBox();

                        dbox.put("d_anwsers", ls.getString("answers") );System.out.println("sulnums" + ls.getString("sulnums") );
                        
                        StringTokenizer st = new StringTokenizer( ls.getString("sulnums"), ",");
                                            
                        while ( st.hasMoreElements() ) {                            
                            sulnum.addElement(st.nextToken() );
                        }                  
                        
                        String selpoint = "";          
                                                                    
                        StringTokenizer f_st = new StringTokenizer( ls.getString("answers"), ",");System.out.println("answers" + ls.getString("answers") );
                        for ( int i = 0; f_st.hasMoreElements(); i++ ) {                            
        		    String token = f_st.nextToken();
        		    if ( token.equals("")) break;
        		           		    
        		    selpoint = selpoint + this.getSelpoints(connMgr, "COMMON", v_grcode, (String)(sulnum.elementAt(i)), token) + ",";
                        }                System.out.println(selpoint);
                        dbox.put("d_selpoint", selpoint);
               //     }                 
               //     ls2.close();

                    list.add(dbox);
                }
            }   
        } catch ( Exception ex ) { ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls != null ) { try { ls.close(); } catch ( Exception e ) { } }
        //    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
            if ( connMgr != null ) { try { connMgr.freeConnection(); } catch ( Exception e10 ) { } }
        }

        return list;
    }


    /**
    설문문제 분류별 척도 점수 
    @param box          receive from the form object and session
    @return String
    */    
    public String getSelpoints(DBConnectionManager connMgr, String p_subj, String p_grcode, String p_sulnum, String p_sulselnum) throws Exception { 
        ListSet ls1  = null;
        ListSet ls2  = null;
        String sql1  = "";
        String sql2  = "";
        String v_result = "0";
        try { 
            sql1 = "select sultype from tz_sul where sulnum = " + p_sulnum;
            sql1 += "   and subj    = 'COMMON'";
            sql1 += "   and grcode    = " + SQLString.Format(p_grcode);
            
            ls1 = connMgr.executeQuery(sql1);
            
            while ( ls1.next() ) { 
                sql2 = "select selpoint";
                sql2 += "  from tz_sulsel  ";
                sql2 += "   where subj    = 'COMMON'";
                sql2 += "   and grcode    = " + SQLString.Format(p_grcode);
                sql2 += "   and sulnum    = " + p_sulnum;
                sql2 += "   and selnum    = " + p_sulselnum;
                
           //     System.out.println(( ls1.getString("sultype")));
           //     System.out.println(p_sulselnum);
                if ( ls1.getString("sultype").equals("5") || ls1.getString("sultype").equals("6") ) {                    
                    
                    ls2 = connMgr.executeQuery(sql2);
                    while ( ls2.next() ) { 
                        v_result = ls2.getInt(1) + "";
                    }
                    if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
                }
            }
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
        } catch ( Exception ex ) { 
            ErrorManager.getErrorStackTrace(ex);
            throw new Exception(ex.getMessage() );
        } finally { 
            if ( ls1 != null ) { try { ls1.close(); } catch ( Exception e ) { } }
            if ( ls2 != null ) { try { ls2.close(); } catch ( Exception e ) { } }
        }

        return v_result;
    }
}