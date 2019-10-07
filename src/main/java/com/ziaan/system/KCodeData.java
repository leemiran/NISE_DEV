// **********************************************************
//  1. 제      목: CODE DATA
//  2. 프로그램명 : CodeData.java
//  3. 개      요: 코드 data bean
//  4. 환      경: JDK 1.3
//  5. 버      젼: 1.0
//  6. 작      성:  2003. 5. 28
//  7. 수      정: 
// **********************************************************
package com.ziaan.system;

public class KCodeData
{ 
    private int seq;	            // 코드구분
    private int upper;           // 코드상위
    private String name;            // 코드명
    private String luserid;         // 코드작성자 ID
    private String ldate;           // 코드생성일
    private String type;            // 코드타입...K-지식노트코드    

    private int eorder;             // 코드순서
    private int level;              // 코드레벨
    
    public KCodeData() { }

    public void   setSeq(int seq)		{ this.seq	= seq;		}
    public int	  getSeq()			{ return seq;			}
    public void   setUpper(int upper)	{ this.upper	= upper;	}
    public int    getUpper()			{ return upper;			}
    public void   setName(String name)		{ this.name	= name;		}
    public String getName()			{ return name;			}
    
    public void   setLuserid(String luserid)	{ this.luserid	= luserid;	}
    public String getLuserid()                  { return luserid;		}
    public void   setLdate(String ldate)	{ this.ldate	= ldate;	}
    public String getLdate()			{ return ldate;			}
    public void   setType(String type)		{ this.type	= type;		}
    public String getType()			{ return type;			}
    

    public void   setEorder(int eorder)		{ this.eorder	= eorder;	}
    public int    getEorder()			{ return eorder;		}
    public void   setLevel(int level)		{ this.level	= level;	}
    public int    getLevel()			{ return level;			}    
}
