/**
*베타테스트시스템의 FAQCatogory빈
*<p> 제목:FAQCatogoryData.java</p> 
*<p> 설명:FAQCatogory빈</p> 
*<p> Copyright: Copright(c)2004</p> 
*<p> Company: VLC</p> 
*@author 박준현
*@version 1.0
*/
package com.ziaan.beta;

public class FaqCategoryData { 
    private String faqcategory  ;   // 코드 ID
    private String faqcategorynm;   // 코드명
    private String luserid      ;   // 최종수정자
    private String ldate        ;   // 최종수정일

    public FaqCategoryData() { }

    public void   setFaqCategory(String faqcategory)        { this.faqcategory = faqcategory;       }
    public String getFaqCategory()                          { return faqcategory;                   }
    public void   setFaqCategorynm(String faqcategorynm)    { this.faqcategorynm = faqcategorynm;   }
    public String getFaqCategorynm()                        { return faqcategorynm;                 }
    public void   setLuserid(String luserid)                { this.luserid = luserid;               }
    public String getLuserid()                              { return luserid;                       }
    public void   setLdate(String ldate)                    { this.ldate = ldate;                   }
    public String getLdate()                                { return ldate;                         }
}
