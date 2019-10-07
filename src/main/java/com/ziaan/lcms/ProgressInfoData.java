package com.ziaan.lcms;

public class ProgressInfoData
{
    private String subj = null;
    private String year = null;
    private String subjseq = null;
    private String userid = null;
    
    private String module = null;
    private String lesson = null;
    private String object = null;
    private String oid = null;
    private int page = 0;

    private int totalPage = 0;
    private int currentPage = 0; 

    private boolean isCompletedObject = false;
    
    public String getLesson()
    {
        return lesson;
    }
    
    public void setLesson(String lesson)
    {
        this.lesson = lesson;
    }
    
    public String getModule()
    {
        return module;
    }
    
    public void setModule(String module)
    {
        this.module = module;
    }
    
    public String getObject()
    {
        return object;
    }
    
    public void setObject(String object)
    {
        this.object = object;
    }
    
    public String getOid()
    {
        return oid;
    }
    
    public void setOid(String oid)
    {
        this.oid = oid;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
    
    public String getSubj()
    {
        return subj;
    }
    
    public void setSubj(String subj)
    {
        this.subj = subj;
    }
    
    public String getSubjseq()
    {
        return subjseq;
    }
    
    public void setSubjseq(String subjseq)
    {
        this.subjseq = subjseq;
    }
    
    public String getUserid()
    {
        return userid;
    }
    
    public void setUserid(String userid)
    {
        this.userid = userid;
    }
    
    public String getYear()
    {
        return year;
    }
    
    public void setYear(String year)
    {
        this.year = year;
    }
    
    public int getTotalPage()
    {
        return totalPage;
    }
    
    public void setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
    }
    
    public int getCurrentPage()
    {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }
    
    public void setCompletedObject(boolean isCompletedObject )
    {
        this.isCompletedObject = isCompletedObject;
        
    }
    
    public boolean isCompletedObject()
    {
        return isCompletedObject;
        
    }
    
    public boolean isLastPage()
    {
        if ( this.page == this.totalPage )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append( "===================================== \r\n");
        sb.append( " ProgressInfoData \r\n");
        sb.append( "  subj : " + subj + "\r\n" );
        sb.append( "  year : " + year + "\r\n" );
        sb.append( "  subjseq : " + subjseq + "\r\n" );
        sb.append( "  userid : " + userid + "\r\n" );
        sb.append( "  module : " + module + "\r\n" );
        sb.append( "  lesson : " + lesson + "\r\n" );
        sb.append( "  object : " + object + "\r\n" );
        sb.append( "  oid : " + oid + "\r\n" );
        sb.append( "  page : " + page + "\r\n" );
        sb.append( "  totalPage : " + totalPage + "\r\n" );
        sb.append( "  currentPage : " + currentPage + "\r\n" );
        sb.append( "  isCompletedObject : " + isCompletedObject + "\r\n" );
        sb.append( "=====================================");
        
        return sb.toString();
    }
}
