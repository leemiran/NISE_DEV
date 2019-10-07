package com.ziaan.library;


  /**
 * <p> 제목: HTML tag 표현과 직접적 관련이 있는 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class PageUtil { 
    
    // 리스트 화면 하단의 페이지 정보를 표시한다
    public static String getPageList(int totalPage, int currentPage, int blockSize) throws Exception {  
        PageList pagelist = new PageList(totalPage, currentPage, blockSize);
        String str = "";
        
        // 이전 10개
        if ( pagelist.previous() ) { 
            str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <b > <<이전10개</b > </a > &nbsp;&nbsp;";
        } 
        
        for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
            if ( i == currentPage) { 
                str += i + "&nbsp";
            }else { 
                str += "<a href=\"javascript:goPage('" + i + "')\" > " + i + "</a > &nbsp";
            }
        }
        
        // 다음 10개
        if ( pagelist.next() ) { 
            str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > &nbsp;<b > 다음10개 >> </b > </a > ";
        } 
        
        if ( str.equals("") ) { 
            str += "자료가 없습니다.";
        }       
        return str;
    }
    

    // 위의 넘과 하는일은 같으나, 보여주는 모양새가 다르다. 우측에 리스트박스가 생겨 페이지 바로가기가 가능함.
    public static String printPageList(int totalPage, int currPage, int blockSize) throws Exception { 
        
        currPage = (currPage == 0) ? 1 : currPage;     
        String str= "";  
        if ( totalPage > 0 ) { 
            PageList pagelist = new PageList(totalPage,currPage,blockSize);
                   
            
            str += "<table border='0' width='100%' align='center' > ";
            str += "<tr > ";
            str += "    <td width='100%' align='center' > ";
            
            // 이전 10개
            if ( pagelist.previous() ) { 
                // str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <b > <<이전10개</b > </a > &nbsp;&nbsp;";
                  str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <IMG src='/images/admin/common/prev.gif' border='0' align='absmiddle' > </a > ";
            } else { 
        		str += "<IMG src='/images/admin/common/prev.gif' border='0' align='absmiddle' > 	";
        	}
        
            for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
                if ( i == currPage) { 
                    str += "<b > " + i + "</b > &nbsp;";
                } else { 
                    str += "<a href=\"javascript:goPage('" + i + "')\" > " + i + "</a > &nbsp;";
                }
            }
            
            // 다음 10개
            if ( pagelist.next() ) { 
                // str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > &nbsp;<b > 다음10개 >> </b > </a > ";
                str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > <IMG src='/images/admin/common/next.gif' border='0' align='absmiddle' > </a > ";
            } else { 
        		str += "<IMG src='/images/admin/common/next.gif' border='0' align='absmiddle' > 	";
        	}
            
            if ( str.equals("") ) { 
                str += "자료가 없습니다.";
            }
        
            str += "    </td > ";
            str += "    <td width='15%' align='center' > ";
            
            
            // if ( totalPage > 0 ) { 
            //    str += "<select  onChange='go(this.selectedIndex +1)' > ";       
            //    for ( int k = 1; k <= totalPage; k++ ) { 
            //        if ( k == currPage) { 
            //            str += "<option selected > " + k;
            //        }
            //        else { 
            //            str += "<option > " + k;
            //        }
            //    }
            //    str += "</select > ";
            // }
        
            str += "    </td > ";
            str += "</tr > ";
            str += "</table > ";
        }

        return str;
    }
    
    // 위의 넘과 하는일은 같으나, 보여주는 모양새가 다르다. 우측에 리스트박스가 생겨 페이지 바로가기가 가능함. (HKMC 강사소개에서 사용)
    public static String printPageList1(int totalPage, int currPage, int blockSize) throws Exception { // 사용자쪽화면 페이지나누기
        
        currPage = (currPage == 0) ? 1 : currPage;     
        String str= "";  
        if ( totalPage > 0 ) { 
            PageList pagelist = new PageList(totalPage,currPage,blockSize);
                   
            
            str += "<table border='0' width='100%' align='center' > ";
            str += "<tr > ";
            str += "    <td width='100%' align='center' > ";
            
            if ( pagelist.previous() ) { 
                str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" > <b > <<이전10개</b > </a > &nbsp;&nbsp;";
            }
        
            for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
                if ( i == currPage) { 
                    str += i + "&nbsp;";
                } else { 
                    str += "<a href=\"javascript:goPage('" + i + "')\" > [" + i + "]</a > &nbsp;";
                }
            }
            
            if ( pagelist.next() ) { 
                str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" > &nbsp;<b > 다음10개 >> </b > </a > ";
            }
            
            if ( str.equals("") ) { 
                str += "자료가 없습니다.";
            }
        
            str += "    </td > ";
            str += "    <td width='15%' align='center' > ";
            
            if ( totalPage > 0 ) { 
                str += "<select  onChange='go(this.selectedIndex +1)' style='border-style:solid;border-width: 1px 1px 1px 1px;border-color:cccccc;color:333333;font-size:9pt;background-color:none;width:40px;height:19px;font-size:9pt;' > ";       
                for ( int k = 1; k <= totalPage; k++ ) { 
                    if ( k == currPage) { 
                        str += "<option selected > " + k;
                    }
                    else { 
                        str += "<option > " + k;
                    }
                }
                str += "</select > ";
            }
        
            str += "    </td > ";
            str += "</tr > ";
            str += "</table > ";
        }

        return str;
    }
    
    // 사용자 화면을 위한 페이징
    public static String getPageListByUser(int totalPage, int currentPage, int blockSize) throws Exception {  
        // 3, 1, 15
        // 3, 2, 15
        // 3, 3, 15
        
        PageList pagelist = new PageList(totalPage, currentPage, blockSize);
        
        String str = "";
        if(totalPage > 0) {
        str += "<div class=\"pageNavi\">";
        str += "<ul>";

        // 10page 뒤로 가기
        if(pagelist.getStartPage()-blockSize >= 1) {
            str += "<li><a href=\"#none\" onclick=\"javascript:goPage('" + (pagelist.getStartPage() - blockSize) + "')\"><img src=\"/images/user/d/btn_pre.gif\" alt=\"이전10페이지보기\" /></a></li>";
        } else {
            str += "<li><img src=\"/images/user/d/btn_pre.gif\" alt=\"이전10페이지보기\" /></li>";
        }
        for(int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++) {
            if(i == currentPage) {
                str += "<li class=\"nowPg\">" + i + "</li>";
            }else {
                str += "<li><a href=\"#none\" onclick=\"javascript:goPage('" + i + "')\">" + i + "</a></li>";
            }
        }
        //10page 앞으로 가기
        if(pagelist.getStartPage()+blockSize <= totalPage) {
            str += "<li><a href=\"#\" onclick=\"javascript:goPage('" + (pagelist.getStartPage() + blockSize) + "')\"><img src=\"/images/user/d/btn_next.gif\" alt=\"다음10페이지보기\" /></a></li>";
        } else {
            str += "<li><img src=\"/images/user/d/btn_next.gif\" alt=\"다음10페이지보기\" /></li>";
        }
        str +="</ul></div>";
        }
        return str;
        
    }
    // 사용자 화면을 위한 페이징
    public static String getPageListByUser2(int totalPage, int currentPage, int blockSize) throws Exception {  
    	// 3, 1, 15
    	// 3, 2, 15
    	// 3, 3, 15
    	
    	PageList pagelist = new PageList(totalPage, currentPage, blockSize);
    	
    	String str = "";
    	if(totalPage > 0) {
    		/*
        str += "<table  border='0' cellspacing='0' cellpadding='0'>";
        str += "<tr><td width='24'>";

        // 10page 뒤로 가기
        if(pagelist.getStartPage()-blockSize >= 1) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()-blockSize) + "')\"><img src='/images/user/button/bPrev10.gif' alt='이전10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bPrev10.gif' alt='이전10페이지보기' border='0'>";
        }

        str +="</td><td width='22'>";
        
        // 이전 페이지
        if(pagelist.previous()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\"><img src='/images/user/button/bPrev.gif' alt='이전페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bPrev.gif' alt='이전페이지 보기' border='0'>";
        }

        str +="</td><td>";
        
        for(int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++) {
            if(i == currentPage) {
                str +=  i ;
            }else {
                str += "<a href=\"javascript:goPage('" + i + "')\"><b>" + i + "</b></a>";
            }
            if(i!=pagelist.getEndPage())
                str +=" | ";
        }

        str +="</td><td width='22' align='right'>";
        
        // 다음페이지
        if (pagelist.next()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\"><img src='/images/user/button/bNext.gif' alt='다음페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bNext.gif' alt='다음페이지 보기' border='0'>";
        }

        str +="</td><td width='24' align='right'>";
        //10page 앞으로 가기
        if(pagelist.getStartPage()+blockSize <= totalPage) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()+blockSize) + "')\"><img src='/images/user/button/bNext10.gif' alt='다음10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bNext10.gif' alt='다음10페이지보기' border='0'>";
        }


        str +="</td></tr></table>";
    		 */
    		str += "<div class=\"pageNavi2\">";
    		str += "<ul>";
    		
    		// 10page 뒤로 가기
    		if(pagelist.getStartPage()-blockSize >= 1) {
    			str += "<li><a href=\"#none\" onclick=\"javascript:goPage('" + (pagelist.getStartPage() - blockSize) + "')\"><img src=\"/images/user/d/btn_pre.gif\" alt=\"이전10페이지보기\" /></a></li>";
    		} else {
    			str += "<li><img src=\"/images/user/d/btn_pre.gif\" alt=\"이전10페이지보기\" /></li>";
    		}
    		for(int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++) {
    			if(i == currentPage) {
    				str += "<li class=\"nowPg\">" + i + "</li>";
    			}else {
    				str += "<li><a href=\"#none\" onclick=\"javascript:goPage('" + i + "')\">" + i + "</a></li>";
    			}
    		}
    		//10page 앞으로 가기
    		if(pagelist.getStartPage()+blockSize <= totalPage) {
    			str += "<li><a href=\"#\" onclick=\"javascript:goPage('" + (pagelist.getStartPage() + blockSize) + "')\"><img src=\"/images/user/d/btn_next.gif\" alt=\"다음10페이지보기\" /></a></li>";
    		} else {
    			str += "<li><img src=\"/images/user/d/btn_next.gif\" alt=\"다음10페이지보기\" /></li>";
    		}
    		str +="</ul></div>";
    	}
    	return str;
    }
    
    // 사용자 화면을 위한 페이징
    public static String getPageListByUser1(int totalPage, int currentPage, int blockSize) throws Exception {  
        // 3, 1, 15
        // 3, 2, 15
        // 3, 3, 15
        
        PageList pagelist = new PageList(totalPage, currentPage, blockSize);
        
        String str = "";
        
        str += "<table  border='0' cellspacing='0' cellpadding='0'>";
        str += "<tr><td width='24'>";

        // 10page 뒤로 가기
        if(pagelist.getStartPage()-blockSize >= 1) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()-blockSize) + "')\"><img src='/images/user/button/bPrev10.gif' alt='이전10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bPrev10.gif' alt='이전10페이지보기' border='0'>";
        }

        str +="</td><td width='22'>";
        
        // 이전 페이지
        if(pagelist.previous()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\"><img src='/images/user/button/bPrev.gif' alt='이전페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bPrev.gif' alt='이전페이지 보기' border='0'>";
        }

        str +="</td><td>";
        
        for(int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++) {
            if(i == currentPage) {
                str +=  "<b>" + i + "</b>" ;
            }else {
                str += "<a href=\"javascript:goPage('" + i + "')\">" + i + "</a>";
            }
            if(i!=pagelist.getEndPage())
                str +=" | ";
        }

        str +="</td><td width='22' align='right'>";
        
        // 다음페이지
        if (pagelist.next()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\"><img src='/images/user/button/bNext.gif' alt='다음페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bNext.gif' alt='다음페이지 보기' border='0'>";
        }

        str +="</td><td width='24' align='right'>";
        //10page 앞으로 가기
        if(pagelist.getStartPage()+blockSize <= totalPage) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()+blockSize) + "')\"><img src='/images/user/button/bNext10.gif' alt='다음10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/button/bNext10.gif' alt='다음10페이지보기' border='0'>";
        }


        str +="</td></tr></table>";
    
        return str;
    }
    
//  사용자 화면을 위한 페이징3(멘토 시스템)
    public static String getPageListByUser3(int totalPage, int currentPage, int blockSize) throws Exception {  
        // 3, 1, 15
        // 3, 2, 15
        // 3, 3, 15
        
        PageList pagelist = new PageList(totalPage, currentPage, blockSize);
        
        String str = "";
        
        str += "<table cellspacing='0' cellpadding='0'>";
        str += "<tr><td>";

        // 10page 뒤로 가기
        if(pagelist.getStartPage()-blockSize >= 1) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()-blockSize) + "')\"><img src='/images/user/mentor/page01.gif' alt='이전10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/mentor/page01.gif' alt='이전10페이지보기' border='0'>";
        }

        str +="</td><td>";
        
        // 이전 페이지
        if(pagelist.previous()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\"><img src='/images/user/mentor/page02.gif' alt='이전페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/mentor/page02.gif' alt='이전페이지 보기' border='0'>";
        }

        str +="</td><td width='4'></td>";
        
        for(int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++) {
            if(i == currentPage) {
                str +=  "<td class='page_on'>" + i + "</td>" ;
            }else {
                str += "<td class='page'><a href=\"javascript:goPage('" + i + "')\">" + i + "</a></td>";
            }
        }

        str +="<td width='4'></td><td>";
        
        // 다음페이지
        if (pagelist.next()) {
            str += "<a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\"><img src='/images/user/mentor/page03.gif' alt='다음페이지 보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/mentor/page03.gif' alt='다음페이지 보기' border='0'>";
        }

        str +="</td><td>";
        //10page 앞으로 가기
        if(pagelist.getStartPage()+blockSize <= totalPage) {
            str += "<a href=\"javascript:goPage('" + (pagelist.getStartPage()+blockSize) + "')\"><img src='/images/user/mentor/page04.gif' alt='다음10페이지보기' border='0'></a>";
        } else {
            str += "<img src='/images/user/mentor/page04.gif' alt='다음10페이지보기' border='0'>";
        }


        str +="</td></tr></table>";
    
        return str;
    }
    
    
    //  쪽지 관련 페이징
    public static String printPageListMemo(int totalPage, int currPage, int blockSize) throws Exception { 
        
        currPage = (currPage == 0) ? 1 : currPage;     
        String str= "";  
        if ( totalPage > 0 ) { 
            PageList pagelist = new PageList(totalPage,currPage,blockSize);
                   	
            str += "<ul>";
            
            // 이전 10개
            if ( pagelist.previous() ) { 
            	str += "<li><a href=\"javascript:goPage('" + pagelist.getPreviousStartPage() + "')\" ><IMG src='/images/user/ap/btn_pre.gif' /></a></li>";
            } else { 
        		str += "<li><IMG src='/images/user/ap/btn_pre.gif' /></li>";
        	}
        
            for ( int i=pagelist.getStartPage(); i <= pagelist.getEndPage(); i++ ) { 
                if ( i == currPage) { 
                    str += "<li class='nowPg'><b>" + i + "</b></li>";
                } else { 
                    str += "<li><a href=\"javascript:goPage('" + i + "')\">" + i + "</a></li>";
                }
            }
            
            // 다음 10개
            if ( pagelist.next() ) { 
                str += "<li><a href=\"javascript:goPage('" + pagelist.getNextStartPage() + "')\" ><IMG src='/images/user/ap/btn_next.gif' /></a></li>";
            } else { 
        		str += "<li><IMG src='/images/user/ap/btn_next.gif' /></li>";
        	}
            
            if ( str.equals("") ) { 
                str += "자료가 없습니다.";
            }
        
            str += "</ul>";
        }

        return str;
    }
}