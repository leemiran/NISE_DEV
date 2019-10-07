// **********************************************************
// 1. 제      목: 
// 2. 프로그램명: SubjExamPaperData.java
// 3. 개      요:
// 4. 환      경: JDK 1.3
// 5. 버      젼: 0.1
// 6. 작      성: Administrator 2003-09-04
// 7. 수      정: 
//                 
// ********************************************************** 
 
package com.ziaan.exam;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class SubjExamPaperData { 
    private String          subj                = null;
    private String          year                = null;
    private String          subjseq             = null;
    private String          subjnm              = null;
    
    private Hashtable       ExamPaperDataList   = null;
    
    public SubjExamPaperData() { 
        subj                = "";
        year                = "";
        subjseq             = "";
        subjnm              = "";
        
        ExamPaperDataList   = new Hashtable();      
    }
    
    /**
     * @return
     */
    public String getSubj   ()                  { return subj;          }
    
    /**
     * @param string
     */
    public void setSubj     ( String string )   { subj    = string;     }
    
    /**
     * @return
     */
    public String getSubjnm ()                  { return subjnm;        }
    
    /**
     * @param string
     */
    public void setSubjnm   ( String string )   { subjnm  = string;     }
    

    /**
     * @return
     */
    public String getSubjseq()                  { return subjseq;       }

    
    /**
     * @param string
     */
    public void setSubjseq  ( String string )   { subjseq = string;     }
    

    /**
     * @return
     */
    public String getYear   ()                  { return year;          }

    /**
     * @param string
     */
    public void setYear     ( String string )   { year    = string;     }


    public ExamPaperData    get         (int index)                     { return (ExamPaperData)ExamPaperDataList.get(String.valueOf(index)); }
    
    public void             add         (ExamPaperData exampaperdata)   { ExamPaperDataList.put(String.valueOf(ExamPaperDataList.size() ), exampaperdata); }
                            
    public void             remove      (int index)                     { ExamPaperDataList.remove(String.valueOf(index)); }
                            
    public void             clear       ()                              { ExamPaperDataList.clear();            }
                                                                        
    public int              size        ()                              { return ExamPaperDataList.size();      }
                                                                        
    public Enumeration      elements    ()                              { return ExamPaperDataList.elements();  }
}
