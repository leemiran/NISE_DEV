// **********************************************************
// 1. ��      ��: 
// 2. ���α׷���: StoldData.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.3
// 5. ��      ��: 0.1
// 6. ��      ��: Administrator 2003-08-12
// 7. ��      ��: 
//                 
// ********************************************************** 
 
package com.ziaan.complete;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window > Preferences > Java > Code Generation > Code and Comments
 */
public class StoldData { 
	private String	subj            ;
	private String  year            ;
	private String	subjseq         ;
	private String	userid          ;
	private String	name            ;
    private String  comp            ;
    private String	stdgubun;
                                    
	private int     gradscore       ;
	private int     gradstep        ;
	private int     gradexam        ;
	private int     gradftest       ;
	private int     gradhtest       ;
	private int     gradreport      ;
    private int     gradexamcnt     ;
    private int     gradftestcnt    ;
    private int     gradhtestcnt    ;
    private int     gradreportcnt   ;
                                    
	private double  score           ;
	private double  tstep           ;
                                    
	private double  mtest           ;   // ����ġ ������           
	private double  ftest           ;                
	private double  htest           ;                
	private double  test1;	   // �̷���2
	private double  test2; 	   // �ǽ���1
	private double  test3;     // �ǽ���2
	private double  test4;	   // �ǽ���3
	private double  report          ;                
	private double  act             ;                
	private double  etc1            ;                
	private double  etc2            ;                
                                    
	private double  avtstep         ;   // ����ġ ����
	private double  avmtest         ;   
	private double  avftest         ;
	private double  avhtest         ;
	private double  avtest1;
	private double  avtest2;
	private double  avtest3;
	private double  avtest4;
	private double  avreport        ;
	private double  avact           ;
	private double  avetc1          ;
	private double  avetc2          ;
                                    
	private double  wstep           ;   // ����ġ ����
	private double  wmtest          ;  
	private double  wftest          ; 
	private double  whtest          ; 
	private double  wtest1; 
	private double  wtest2; 
	private double  wtest3; 
	private double  wtest4;
	private double  wreport         ; 
	private double  wact            ;    
	private double  wetc1           ;   
	private double  wetc2           ;   
	private double  samtotal        ;   // �����ƿ�  
	private String  isgraduated     ;
	private String  isb2c           ;
	private String	edustart        ;
	private String	eduend          ;
	private String	serno           ;
	private String	isrestudy       ;
	private String	notgraducd      ;	// �̼�������ڵ�
	private String	notgraducddesc  ;	// �̼�������ڵ� ����
	private String	notgraduetc     ;   // �̼�������ڵ��Ÿ
	private String	notgraduetcdesc ;   // �̼�������ڵ��Ÿ ����
	
	private String  compnm          ;
	private String  examnum          ;

	private int study_count;	// �������-����Ƚ��
	private int study_time;	// �������-�н��ð�
	
	private String post;		// �����ڵ�
	private String dept_cd;	// ���ڵ�
	private String job_cd;		// �����ڵ�
	
	private double mreport;
	private double freport;
	
	
	private double editlink;
	private double editscore;
	
	
	public double getEditlink() {
		return editlink;
	}


	public void setEditlink(double editlink) {
		this.editlink = editlink;
	}


	public double getEditscore() {
		return editscore;
	}


	public void setEditscore(double editscore) {
		this.editscore = editscore;
	}


	public double getMreport() {
		return mreport;
	}


	public void setMreport(double mreport) {
		this.mreport = mreport;
	}


	public double getFreport() {
		return freport;
	}


	public void setFreport(double freport) {
		this.freport = freport;
	}


	public StoldData() { }	

	
	public String getPost() {
		return post;
	}


	public void setPost(String post) {
		this.post = post;
	}


	public String getDept_cd() {
		return dept_cd;
	}


	public void setDept_cd(String dept_cd) {
		this.dept_cd = dept_cd;
	}


	public String getJob_cd() {
		return job_cd;
	}


	public void setJob_cd(String job_cd) {
		this.job_cd = job_cd;
	}


	public int getStudy_count() {
		return study_count;
	}


	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}


	public int getStudy_time() {
		return study_time;
	}


	public void setStudy_time(int study_time) {
		this.study_time = study_time;
	}


	/**
	 * @return
	 */
	public double getAvact() { 
		return avact;
	}

	/**
	 * @return
	 */
	public double getAvetc1() { 
		return avetc1;
	}

	/**
	 * @return
	 */
	public double getAvetc2() { 
		return avetc2;
	}

	/**
	 * @return
	 */
	public double getAvftest() { 
		return avftest;
	}

	/**
	 * @return
	 */
	public double getAvmtest() { 
		return avmtest;
	}
	
	/**
	 * @return
	 */
	public double getAvhtest() { 
		return avhtest;
	}
	
	/**
	 * @return
	 */
	public double getAvtest1() {
		return avtest1;
	}
	
	/**
	 * @return
	 */
	public double getAvtest2() {
		return avtest2;
	}
	
	/**
	 * @return
	 */
	public double getAvtest3() {
		return avtest3;
	}
	
	/**
	 * @return
	 */
	public double getAvtest4() {
		return avtest4;
	}


	/**
	 * @return
	 */
	public double getAvreport() { 
		return avreport;
	}

	/**
	 * @return
	 */
	public double getAvtstep() { 
		return avtstep;
	}

	/**
	 * @return
	 */
	public String getComp() { 
		return comp;
	}
	
	/**
	 * @return
	 */
	public String getStdgubun() {
		return stdgubun;
	}

	/**
	 * @return
	 */
	public String getEduend() { 
		return eduend;
	}

	/**
	 * @return
	 */
	public String getEdustart() { 
		return edustart;
	}

	/**
	 * @return
	 */
	public String getIsb2c() { 
		return isb2c;
	}

	/**
	 * @return
	 */
	public String getIsgraduated() { 
		return isgraduated;
	}

	/**
	 * @return
	 */
	public String getIsrestudy() { 
		return isrestudy;
	}
	
	/**
	 * @return
	 */
	public String getNotgraducd() { 
		return notgraducd;
	}
	
	/**
	 * @return
	 */
	public String getNotgraducddesc() { 
		return notgraducddesc;
	}

	/**
	 * @return
	 */
	public String getNotgraduetc() { 
		return notgraduetc;
	}
	
	/**
	 * @return
	 */
	public String getNotgraduetcdesc() { 
		return notgraduetcdesc;
	}
	
	/**
	 * @return
	 */
	public String getName() { 
		return name;
	}

	/**
	 * @return
	 */
	public double getScore() { 
		return score;
	}

	/**
	 * @return
	 */
	public String getSerno() { 
		return serno;
	}

	/**
	 * @return
	 */
	public String getSubj() { 
		return subj;
	}

	/**
	 * @return
	 */
	public String getSubjseq() { 
		return subjseq;
	}

	/**
	 * @return
	 */
	public double getTstep() { 
		return tstep;
	}

	/**
	 * @return
	 */
	public String getUserid() { 
		return userid;
	}

	/**
	 * @return
	 */
	public String getYear() { 
		return year;
	}

	/**
	 * @param d
	 */
	public void setAvact(double d) { 
		avact = d;
	}

	/**
	 * @param d
	 */
	public void setAvetc1(double d) { 
		avetc1 = d;
	}

	/**
	 * @param d
	 */
	public void setAvetc2(double d) { 
		avetc2 = d;
	}

	/**
	 * @param d
	 */
	public void setAvftest(double d) { 
		avftest = d;
	}

	/**
	 * @param d
	 */
	public void setAvmtest(double d) { 
		avmtest = d;
	}
	
	/**
	 * @param d
	 */
	public void setAvhtest(double d) { 
		avhtest = d;
	}
	
	/**
	 * @param d
	 */
	public void setAvtest1(double d) {
		avtest1 = d;
	}
	
	/**
	 * @param d
	 */
	public void setAvtest2(double d) {
		avtest2 = d;
	}
	
	/**
	 * @param d
	 */
	public void setAvtest3(double d) {
		avtest3 = d;
	}
	
	/**
	 * @param d
	 */
	public void setAvtest4(double d) {
		avtest4 = d;
	}

	/**
	 * @param d
	 */
	public void setAvreport(double d) { 
		avreport = d;
	}

	/**
	 * @param d
	 */
	public void setAvtstep(double d) { 
		avtstep = d;
	}

	/**
	 * @param string
	 */
	public void setComp(String string) { 
		comp = string;
	}
	
	
	/**
	 * @param string
	 */
	public void setStdgubun(String string) {
		stdgubun = string;
	}

	/**
	 * @param string
	 */
	public void setEduend(String string) { 
		eduend = string;
	}

	/**
	 * @param string
	 */
	public void setEdustart(String string) { 
		edustart = string;
	}

	/**
	 * @param string
	 */
	public void setIsb2c(String string) { 
		isb2c = string;
	}

	/**
	 * @param string
	 */
	public void setIsgraduated(String string) { 
		isgraduated = string;
	}

	/**
	 * @param string
	 */
	public void setIsrestudy(String string) { 
		isrestudy = string;
	}
	
	/**
	 * @param string
	 */
	public void setNotgraducd(String string) { 
		notgraducd = string;
	}
	
	/**
	 * @param string
	 */
	public void setNotgraducddesc(String string) { 
		notgraducddesc = string;
	}
	
	/**
	 * @param string
	 */
	public void setNotgraduetc(String string) { 
		notgraduetc = string;
	}
	
	/**
	 * @param string
	 */
	public void setNotgraduetcdesc(String string) { 
		notgraduetcdesc = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) { 
		name = string;
	}

	/**
	 * @param d
	 */
	public void setScore(double d) { 
		score = d;
	}

	/**
	 * @param string
	 */
	public void setSerno(String string) { 
		serno = string;
	}

	/**
	 * @param string
	 */
	public void setSubj(String string) { 
		subj = string;
	}

	/**
	 * @param string
	 */
	public void setSubjseq(String string) { 
		subjseq = string;
	}

	/**
	 * @param d
	 */
	public void setTstep(double d) { 
		tstep = d;
	}

	/**
	 * @param string
	 */
	public void setUserid(String string) { 
		userid = string;
	}

	/**
	 * @param string
	 */
	public void setYear(String string) { 
		year = string;
	}

	/**
	 * @return
	 */
	public double getAct() { 
		return act;
	}

	/**
	 * @return
	 */
	public double getEtc1() { 
		return etc1;
	}

	/**
	 * @return
	 */
	public double getEtc2() { 
		return etc2;
	}

	/**
	 * @return
	 */
	public double getFtest() { 
		return ftest;
	}
	
	/**
	 * @return
	 */
	public double getHtest() { 
		return htest;
	}

	/**
	 * @return
	 */
	public double getMtest() { 
		return mtest;
	}
	
	
	/**
	 * @return
	 */
	public double getTest1() {
		return test1;
	}
	
	/**
	 * @return
	 */
	public double getTest2() {
		return test2;
	}
	
	/**
	 * @return
	 */
	public double getTest3() {
		return test3;
	}
	
	/**
	 * @return
	 */
	public double getTest4() {
		return test4;
	}

	/**
	 * @return
	 */
	public double getReport() { 
		return report;
	}

	/**
	 * @param d
	 */
	public void setAct(double d) { 
		act = d;
	}

	/**
	 * @param d
	 */
	public void setEtc1(double d) { 
		etc1 = d;
	}

	/**
	 * @param d
	 */
	public void setEtc2(double d) { 
		etc2 = d;
	}

	/**
	 * @param d
	 */
	public void setFtest(double d) { 
		ftest = d;
	}
		
	/**
	 * @param d
	 */
	public void setMtest(double d) { 
		mtest = d;
	}
	
	/**
	 * @param d
	 */
	public void setHtest(double d) { 
		htest = d;
	}
	
	/**
	 * @param d
	 */
	public void setTest1(double d) {
		test1 = d;
	}
	
	/**
	 * @param d
	 */
	public void setTest2(double d) {
		test2 = d;
	}
	
	/**
	 * @param d
	 */
	public void setTest3(double d) {
		test3 = d;
	}
	
	/**
	 * @param d
	 */
	public void setTest4(double d) {
		test4 = d;
	}


	/**
	 * @param d
	 */
	public void setReport(double d) { 
		report = d;
	}

	/**
	 * @return
	 */
	public double getWact() { 
		return wact;
	}

	/**
	 * @return
	 */
	public double getWetc1() { 
		return wetc1;
	}

	/**
	 * @return
	 */
	public double getWetc2() { 
		return wetc2;
	}

	/**
	 * @return
	 */
	public double getWftest() { 
		return wftest;
	}
	
	/**
	 * @return
	 */
	public double getWhtest() { 
		return whtest;
	}

	/**
	 * @return
	 */
	public double getWmtest() { 
		return wmtest;
	}
	
	
	/**
	 * @return
	 */
	public double getWtest1() {
		return wtest1;
	}
	
	/**
	 * @return
	 */
	public double getWtest2() {
		return wtest2;
	}
	
	/**
	 * @return
	 */
	public double getWtest3() {
		return wtest3;
	}
	
	/**
	 * @return
	 */
	public double getWtest4() {
		return wtest4;
	}

	/**
	 * @return
	 */
	public double getWreport() { 
		return wreport;
	}

	/**
	 * @return
	 */
	public double getWstep() { 
		return wstep;
	}

	/**
	 * @param d
	 */
	public void setWact(double d) { 
		wact = d;
	}

	/**
	 * @param d
	 */
	public void setWetc1(double d) { 
		wetc1 = d;
	}

	/**
	 * @param d
	 */
	public void setWetc2(double d) { 
		wetc2 = d;
	}

	/**
	 * @param d
	 */
	public void setWftest(double d) { 
		wftest = d;
	}
	
	/**
	 * @param d
	 */
	public void setWhtest(double d) { 
		whtest = d;
	}

	/**
	 * @param d
	 */
	public void setWmtest(double d) { 
		wmtest = d;
	}
	
	/**
	 * @param d
	 */
	public void setWtest1(double d) {
		wtest1 = d;
	}
	
	/**
	 * @param d
	 */
	public void setWtest2(double d) {
		wtest2 = d;
	}
	
	/**
	 * @param d
	 */
	public void setWtest3(double d) {
		wtest3 = d;
	}
	
	/**
	 * @param d
	 */
	public void setWtest4(double d) {
		wtest4 = d;
	}

	/**
	 * @param d
	 */
	public void setWreport(double d) { 
		wreport = d;
	}

	/**
	 * @param d
	 */
	public void setWstep(double d) { 
		wstep = d;
	}

	/**
	 * @return
	 */
	public int getGradscore() { 
		return gradscore;
	}

	/**
	 * @return
	 */
	public int getGradstep() { 
		return gradstep;
	}
	
	/**
	 * @return
	 */
	public int getGradexam() { 
		return gradexam;
	}
	
	/**
	 * @return
	 */
	public int getGradftest() { 
		return gradftest;
	}
	
	/**
	 * @return
	 */
	public int getGradhtest() { 
		return gradhtest;
	}

	/**
	 * @return
	 */
	public int getGradreport() { 
		return gradreport;
	}
    
    /**
     * @return
     */
    public int getGradexamcnt() { 
        return gradexamcnt;
    }
    
    /**
     * @return
     */
    public int getGradftestcnt() { 
        return gradftestcnt;
    }
    
    /**
     * @return
     */
    public int getGradhtestcnt() { 
        return gradhtestcnt;
    }

    /**
     * @return
     */
    public int getGradreportcnt() { 
        return gradreportcnt;
    }
	
	/**
	 * @param i
	 */
	public void setGradscore(int i) { 
		gradscore = i;
	}

	/**
	 * @param i
	 */
	public void setGradstep(int i) { 
		gradstep = i;
	}
	
	/**
	 * @param i
	 */
	public void setGradexam(int i) { 
		gradexam = i;
	}
	
	/**
	 * @param i
	 */
	public void setGradftest(int i) { 
		gradftest = i;
	}
	
	/**
	 * @param i
	 */
	public void setGradhtest(int i) { 
		gradhtest = i;
	}


	/**
	 * @param i
	 */
	public void setGradreport(int i) { 
		gradreport = i;
	}
    
    /**
     * @param i
     */
    public void setGradexamcnt(int i) { 
        gradexamcnt = i;
    }
    
    /**
     * @param i
     */
    public void setGradftestcnt(int i) { 
        gradftestcnt = i;
    }
    
    /**
     * @param i
     */
    public void setGradhtestcnt(int i) { 
        gradhtestcnt = i;
    }


    /**
     * @param i
     */
    public void setGradreportcnt(int i) { 
        gradreportcnt = i;
    }
	
	/**
	 * @return
	 */
	public String getCompnm() { 
		return compnm;
	}

	/**
	 * @param string
	 */
	public void setCompnm(String string) { 
		compnm = string;
	}
	/**
	 * @return
	 */
	public String getExamnum() { 
		return examnum;
	}
	
	/**
	 * @param string
	 */
	public void setExamnum(String string) { 
		examnum = string;
	}

	/**
	 * @return
	 */
	public double getSamtotal() { 
		return samtotal;
	}

	/**
	 * @param double
	 */
	public void setSamtotal(double d) { 
		samtotal = d;
	}	
}
