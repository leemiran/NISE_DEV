package com.ziaan.library;

  /**
 * <p> 제목: jsp 리스트화면 하단에 출력되는 paging관련 라이브러리</p> 
 * <p> 설명: </p> 
 * <p> Copyright: Copyright (c) 2004</p> 
 * <p> Company:  </p> 
 *@author 이정한
 *@date 2003. 12
 *@version 1.0
 */
public class PageList { 

    private int m_pageCount=0;
    private int m_pageNum=0;
    private int m_pageBlockSize=0;
    
    private boolean m_previous = false;
    private boolean m_next = false;
    
    private int m_previousBlockStartNumber=0;
    private int m_nextBlockStartNumber=0;
    
    private int m_blockStartNumber=0;
    private int m_blockEndNumber=0;
    
    public PageList() { }
    
    /*
     * PageList(총페이지수, 현재페이지번호, 한페이지당 리스트 갯수)
     */
    public PageList(int pageCount, int pageNum, int pageBlockSize) { 
        // 3, 1, 10
        // 3, 2, 10
        // 3, 3, 10
        m_pageCount = pageCount;
        m_pageNum = pageNum;
        m_pageBlockSize = pageBlockSize;
        
        this.init();
    }
	
    private void init() {         
//        // 전체 block 갯수
//        int block_count = ((m_pageCount - 1) / m_pageBlockSize) + 1;    // 1/15+1
//        
//        // 현재 block 위치
//        int current_block = ((m_pageNum - 1) / m_pageBlockSize) + 1;    // 1
//        
//        // 이전 block 유뮤
//        m_previous = (current_block > 1) ? true : false;    // false
//        
//        // 이전 block의 시작 페이지
//        if ( m_previous) { 
//            m_previousBlockStartNumber = ((current_block - 2) * m_pageBlockSize) + 1;   //
//        }
//        
//        // 다음 block 유뮤        
//        m_next = (current_block <= block_count) ? true : false;  // true        
//        
//        // 다음 block의 시작 페이지
//        if ( m_next) { 
//            m_nextBlockStartNumber = (current_block * m_pageBlockSize) + 1;
//        }
//        
//        // 현재 block의 시작 페이지 및 끝 페이지
//        if ( current_block < block_count) { 
//            m_blockStartNumber = ((current_block - 1) * m_pageBlockSize) + 1;
//            m_blockEndNumber = current_block * m_pageBlockSize;
//        }else { 
//            m_blockStartNumber = ((current_block - 1) * m_pageBlockSize) + 1;
//            m_blockEndNumber = m_pageCount;
//        }
        
        // 전체 block 갯수
        int block_count = ((m_pageCount - 1) / m_pageBlockSize) + 1;    // 
        
        // 현재 block 위치
        int current_block = ((m_pageNum - 1) / m_pageBlockSize) + 1;    // 1
        
        // 이전 block 유뮤
        m_previous = (m_pageNum > 1) ? true : false;    // false
        
        // 이전 block의 시작 페이지
        if ( m_previous) { 
            m_previousBlockStartNumber = m_pageNum-1;   //
        }
        
        // 다음 block 유뮤        
        m_next = (m_pageNum < m_pageCount) ? true : false;  // true        
        
        // 다음 block의 시작 페이지
        if ( m_next) { 
            m_nextBlockStartNumber = m_pageNum + 1;
        }
        
      // 현재 block의 시작 페이지 및 끝 페이지
      if ( current_block < block_count) { 
          m_blockStartNumber = ((current_block - 1) * m_pageBlockSize) + 1;
          m_blockEndNumber = current_block * m_pageBlockSize;
      }else { 
          m_blockStartNumber = ((current_block - 1) * m_pageBlockSize) + 1;
          m_blockEndNumber = m_pageCount;
      }
        
    }

    public boolean previous() { 
        return m_previous;
    }
    
    public boolean next() {
        return m_next;
    }
    
    public int getStartPage() {
        return m_blockStartNumber;
    }
    
    public int getEndPage() {        
        return m_blockEndNumber;
    }
    
    public int getPreviousStartPage() { 
        return m_previousBlockStartNumber;
    }
    
    public int getNextStartPage() { 
        return m_nextBlockStartNumber;
    }	
}
