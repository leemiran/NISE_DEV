// **********************************************************
// 1. ��      ��: �����������ȭ��� DATA
// 2. ��  �� ��: SubjseqData.java
// 3. ��      ��:
// 4. ȯ      ��: JDK 1.4
// 5. ��      ��: 0.1
// 6. ��      ��: LeeSuMin 2003. 07. 22
// 7. ��      ��: ����� 2008.10.15
// **********************************************************
package com.ziaan.course;


public class SubjseqData { 
    private     String  grcode          ;  
    private     String  grcodenm        ;
    private     String  grseq           ;
    private     String  gyear           ;
    private     String  grseqnm         ;
    private     String  subj            ;
    private     String  subjnm          ;
    private     String  year            ;
    private     String  subjseq         ;
    private     String  subjseqgr       ;
    private     String  course          ;
    private     String  cyear           ;
    private     String  courseseq       ;
    private     String  coursenm        ;
    private     String  place           ;
    private     String  placejh         ;
    private     String  isbelongcourse  ;
    private     String  propstart       ;
    private     String  propend         ;
    private     String  edustart        ;
    private     String  eduend          ;
    private     String  endfirst        ;
    private     String  endfinal        ;
    private     String  isclosed        ;
    private     String  isopenedu       ;
    private     String  ismultipaper    ;
    private     String  luserid         ;
    private     String  ldate           ;
    private     String  isblended       ;
    private     int     studentlimit    ;
    private     int     point           ;
    private     int     biyong          ;
    private     int     edulimit        ;
    private     int     warndays        ;
    private     int     stopdays        ;
    private     int     gradscore       ;
    private     int     gradstep        ;
    private     double  wstep           ;
    private     double  wmtest          ;
    private     double  wftest          ;
    private     double  wreport         ;
    private     double  wact            ;
    private     double  wetc1           ;
    private     double  wetc2           ;
    private     String  proposetype     ;
    private     String  isonoff         ;
    private     String  subjtypenm      ;
    private     String  examnum      ;
    private     int     cnt_propose = 0;
    private     int     cnt_student = 0;
    private     int     cnt_stold   = 0;
    private     boolean canDelete   = false;

    // add
    private     int     score               ; // ��������
    private     int     gradreport          ; // ������� - ����
    private     int     gradexam            ; // ������� -  �߰���
    private     int     gradftest           ; // ������� -  ������
    private     int     gradhtest           ; // ������� -  ������
    private     double  whtest              ; // ������
    private     String  usesubjseqapproval  ; // ���翩�� - ��������ְ�����
    private     String  useproposeapproval  ; // ���翩�� - ������û��������
    private     String  usemanagerapproval  ; // ���翩�� - �ְ������������
    private     double  rndcreditreq        ; // ��������(��������-�ʼ�)
    private     double  rndcreditchoice     ; // ��������(��������-����)
    private     double  rndcreditadd        ; // ��������(��������-��������)
    private     double  rndcreditdeduct     ; // ��������(��������-��������)
    private     String  rndjijung           ; // ��������-�������񿩺�
    private     String  isablereview        ; // �������ɿ���
    private     int     tsubjbudget         ; // ���񿹻�
    private     String  isusebudget         ; // ���񿹻� �ݾ� ���� ��뿩��
    private     String  isessential     = "";
    
    private     String  isvisible           ; // �н��ڿ��� �����ֱ�
    private     String  usebook             ; // ���� ����
    private     String  bookname            ; // ����
    private     int     bookprice           ; // �����
    private     int     sulpapernum         ; // ������ȣ
    private     int     presulpapernum      ; // ���� ���� ��ȣ
    private     int     aftersulpapernum    ; // ���� ���� ��ȣ
    private     int     canceldays          ; // ������û��ұⰣ
    private     String  ischarge            ; // ������ ��/���� ����
    private     String  presulsdate         ; // ���� ���� ������ 
    private     String  presuledate         ; // ���� ���� ������
    private     String  aftersdate          ; // ���� ���� ������
    private     String  afteredate          ; // ���� ���� ������
    private     int     maleassignrate      ; // ���� �Ҵ� ����
    
    private     String  gradexam_flag       = "";   // �߰��� flag
    private     String  gradftest_flag      = "";   // ������ flag
    private     String  gradhtest_flag      = "";   // ������ flag
    private     String  gradreport_flag     = "";   // ���� flag
    private     String  isattendchk         = "";   // �¶����⼮üũ ����
    private     String  attstime            = "";   // �⼮���۽ð�
    private     String  attetime            = "";   // �⼮����ð�
    private 	String  isgoyong;					// ��� ���� ����
    private int goyongpricemajor;            		// ��뺸�� ȯ�޾�-ȯ�޾�(KT)
    private int goyongpriceminor;            		// ��뺸�� ȯ�޾�-KT������
    private int goyongpricestand;            		// ��뺸�� ȯ�޾�-CP������
    private String isoutsourcing = "";
    
    /* KT���簳�߿����� ���  */
    private	int		reviewdays			= 0;	// �������ɱⰣ
    private	int		study_count			= 0;	// �������-����Ƚ��
    private	int		study_time			= 0;	// �������- �н��ð�
    
    private	String muserid				= "";	// ����ھ��̵�
    private	String musernm				= "";	// ����ڸ�
    private 	String musertel				= "";	// ����ڿ���ó
    
    private	String mtest_start			= "";	// ������ ������
    private	String mtest_end			= "";	// ������ ������
    private	String ftest_start			= "";	// ������ ������
    private	String ftest_end			= "";	// ������ ������
    private	String mreport_start		= "";	// �߰��� ������
    private	String mreport_end			= "";	// �߰��� ������
    private	String freport_start		= "";	// �⸻�� ������
    private	String freport_end			= "";	// �⸻�� ������
    
    private String cpsubj  				= "";	//cp �����ڵ� (link����)		
    private String eduurl  				= "";	//�н�URL(link����)
    
    private String eroom                ="";    //�⼮����� ����
    
    
    public String getEroom() {
		return eroom;
	}


	public void setEroom(String eroom) {
		this.eroom = eroom;
	}


	public String getEduurl() {
		return eduurl;
	}


	public void setEduurl(String eduurl) {
		this.eduurl = eduurl;
	}


	public String getCpsubj() {
		return cpsubj;
	}


	public void setCpsubj(String cpsubj) {
		this.cpsubj = cpsubj;
	}

    public int getGoyongpricestand() {
		return goyongpricestand;
	}


	public void setGoyongpricestand(int goyongpricestand) {
		this.goyongpricestand = goyongpricestand;
	}


	public String getMtest_start() {
		return mtest_start;
	}


	public void setMtest_start(String mtest_start) {
		this.mtest_start = mtest_start;
	}


	public String getMtest_end() {
		return mtest_end;
	}


	public void setMtest_end(String mtest_end) {
		this.mtest_end = mtest_end;
	}


	public String getFtest_start() {
		return ftest_start;
	}


	public void setFtest_start(String ftest_start) {
		this.ftest_start = ftest_start;
	}


	public String getFtest_end() {
		return ftest_end;
	}


	public void setFtest_end(String ftest_end) {
		this.ftest_end = ftest_end;
	}


	public String getMreport_start() {
		return mreport_start;
	}


	public void setMreport_start(String mreport_start) {
		this.mreport_start = mreport_start;
	}


	public String getMreport_end() {
		return mreport_end;
	}


	public void setMreport_end(String mreport_end) {
		this.mreport_end = mreport_end;
	}


	public String getFreport_start() {
		return freport_start;
	}


	public void setFreport_start(String freport_start) {
		this.freport_start = freport_start;
	}


	public String getFreport_end() {
		return freport_end;
	}


	public void setFreport_end(String freport_end) {
		this.freport_end = freport_end;
	}


	public  SubjseqData() { }


	public String getMuserid() {
		return muserid;
	}


	public void setMuserid(String muserid) {
		this.muserid = muserid;
	}


	public String getMusernm() {
		return musernm;
	}


	public void setMusernm(String musernm) {
		this.musernm = musernm;
	}


	public String getMusertel() {
		return musertel;
	}


	public void setMusertel(String musertel) {
		this.musertel = musertel;
	}


	public int getStudy_time() {
		return study_time;
	}


	public void setStudy_time(int study_time) {
		this.study_time = study_time;
	}


	public String getSubjtypenm() { 
        
        if ( isonoff == null || isonoff.equals("")) {  
            return "aaa";
        }
        if ( isonoff.equals("OFF")) { 
            return "����";
        } else if (isonoff.equals("RC")) {
            return "��������";
        } else {
        	return "�̷���";
        }
    }


	public String getGrcode() {
		return grcode;
	}


	public void setGrcode(String grcode) {
		this.grcode = grcode;
	}
	public String getExamnum() {
		return examnum;
	}
	
	
	public void setExamnum(String examnum) {
		this.examnum = examnum;
	}


	public String getGrcodenm() {
		return grcodenm;
	}


	public void setGrcodenm(String grcodenm) {
		this.grcodenm = grcodenm;
	}


	public String getGrseq() {
		return grseq;
	}


	public void setGrseq(String grseq) {
		this.grseq = grseq;
	}


	public String getGyear() {
		return gyear;
	}


	public void setGyear(String gyear) {
		this.gyear = gyear;
	}


	public String getGrseqnm() {
		return grseqnm;
	}


	public void setGrseqnm(String grseqnm) {
		this.grseqnm = grseqnm;
	}


	public String getSubj() {
		return subj;
	}


	public void setSubj(String subj) {
		this.subj = subj;
	}


	public String getSubjnm() {
		return subjnm;
	}


	public void setSubjnm(String subjnm) {
		this.subjnm = subjnm;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public String getSubjseq() {
		return subjseq;
	}


	public void setSubjseq(String subjseq) {
		this.subjseq = subjseq;
	}


	public String getSubjseqgr() {
		return subjseqgr;
	}


	public void setSubjseqgr(String subjseqgr) {
		this.subjseqgr = subjseqgr;
	}


	public String getCourse() {
		return course;
	}


	public void setCourse(String course) {
		this.course = course;
	}


	public String getCyear() {
		return cyear;
	}


	public void setCyear(String cyear) {
		this.cyear = cyear;
	}


	public String getCourseseq() {
		return courseseq;
	}


	public void setCourseseq(String courseseq) {
		this.courseseq = courseseq;
	}


	public String getCoursenm() {
		return coursenm;
	}


	public void setCoursenm(String coursenm) {
		this.coursenm = coursenm;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public String getPlacejh() {
		return placejh;
	}


	public void setPlacejh(String placejh) {
		this.placejh = placejh;
	}


	public String getIsbelongcourse() {
		return isbelongcourse;
	}


	public void setIsbelongcourse(String isbelongcourse) {
		this.isbelongcourse = isbelongcourse;
	}


	public String getPropstart() {
		return propstart;
	}


	public void setPropstart(String propstart) {
		this.propstart = propstart;
	}


	public String getPropend() {
		return propend;
	}


	public void setPropend(String propend) {
		this.propend = propend;
	}


	public String getEdustart() {
		return edustart;
	}


	public void setEdustart(String edustart) {
		this.edustart = edustart;
	}


	public String getEduend() {
		return eduend;
	}


	public void setEduend(String eduend) {
		this.eduend = eduend;
	}


	public String getEndfirst() {
		return endfirst;
	}


	public void setEndfirst(String endfirst) {
		this.endfirst = endfirst;
	}


	public String getEndfinal() {
		return endfinal;
	}


	public void setEndfinal(String endfinal) {
		this.endfinal = endfinal;
	}


	public String getIsclosed() {
		return isclosed;
	}


	public void setIsclosed(String isclosed) {
		this.isclosed = isclosed;
	}


	public String getIsopenedu() {
		return isopenedu;
	}


	public void setIsopenedu(String isopenedu) {
		this.isopenedu = isopenedu;
	}


	public String getIsmultipaper() {
		return ismultipaper;
	}


	public void setIsmultipaper(String ismultipaper) {
		this.ismultipaper = ismultipaper;
	}


	public String getLuserid() {
		return luserid;
	}


	public void setLuserid(String luserid) {
		this.luserid = luserid;
	}


	public String getLdate() {
		return ldate;
	}


	public void setLdate(String ldate) {
		this.ldate = ldate;
	}


	public String getIsblended() {
		return isblended;
	}


	public void setIsblended(String isblended) {
		this.isblended = isblended;
	}


	public int getStudentlimit() {
		return studentlimit;
	}


	public void setStudentlimit(int studentlimit) {
		this.studentlimit = studentlimit;
	}


	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}


	public int getBiyong() {
		return biyong;
	}


	public void setBiyong(int biyong) {
		this.biyong = biyong;
	}


	public int getEdulimit() {
		return edulimit;
	}


	public void setEdulimit(int edulimit) {
		this.edulimit = edulimit;
	}


	public int getWarndays() {
		return warndays;
	}


	public void setWarndays(int warndays) {
		this.warndays = warndays;
	}


	public int getStopdays() {
		return stopdays;
	}


	public void setStopdays(int stopdays) {
		this.stopdays = stopdays;
	}


	public int getGradscore() {
		return gradscore;
	}


	public void setGradscore(int gradscore) {
		this.gradscore = gradscore;
	}


	public int getGradstep() {
		return gradstep;
	}


	public void setGradstep(int gradstep) {
		this.gradstep = gradstep;
	}


	public double getWstep() {
		return wstep;
	}


	public void setWstep(double wstep) {
		this.wstep = wstep;
	}


	public double getWmtest() {
		return wmtest;
	}


	public void setWmtest(double wmtest) {
		this.wmtest = wmtest;
	}


	public double getWftest() {
		return wftest;
	}


	public void setWftest(double wftest) {
		this.wftest = wftest;
	}


	public double getWreport() {
		return wreport;
	}


	public void setWreport(double wreport) {
		this.wreport = wreport;
	}


	public double getWact() {
		return wact;
	}


	public void setWact(double wact) {
		this.wact = wact;
	}


	public double getWetc1() {
		return wetc1;
	}


	public void setWetc1(double wetc1) {
		this.wetc1 = wetc1;
	}


	public double getWetc2() {
		return wetc2;
	}


	public void setWetc2(double wetc2) {
		this.wetc2 = wetc2;
	}


	public String getProposetype() {
		return proposetype;
	}


	public void setProposetype(String proposetype) {
		this.proposetype = proposetype;
	}


	public String getIsonoff() {
		return isonoff;
	}


	public void setIsonoff(String isonoff) {
		this.isonoff = isonoff;
	}


	public int getCnt_propose() {
		return cnt_propose;
	}


	public void setCnt_propose(int cnt_propose) {
		this.cnt_propose = cnt_propose;
	}


	public int getCnt_student() {
		return cnt_student;
	}


	public void setCnt_student(int cnt_student) {
		this.cnt_student = cnt_student;
	}


	public int getCnt_stold() {
		return cnt_stold;
	}


	public void setCnt_stold(int cnt_stold) {
		this.cnt_stold = cnt_stold;
	}


	public boolean isCanDelete() {
		return canDelete;
	}


	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public int getGradreport() {
		return gradreport;
	}


	public void setGradreport(int gradreport) {
		this.gradreport = gradreport;
	}


	public int getGradexam() {
		return gradexam;
	}


	public void setGradexam(int gradexam) {
		this.gradexam = gradexam;
	}


	public int getGradftest() {
		return gradftest;
	}


	public void setGradftest(int gradftest) {
		this.gradftest = gradftest;
	}


	public int getGradhtest() {
		return gradhtest;
	}


	public void setGradhtest(int gradhtest) {
		this.gradhtest = gradhtest;
	}


	public double getWhtest() {
		return whtest;
	}


	public void setWhtest(double whtest) {
		this.whtest = whtest;
	}


	public String getUsesubjseqapproval() {
		return usesubjseqapproval;
	}


	public void setUsesubjseqapproval(String usesubjseqapproval) {
		this.usesubjseqapproval = usesubjseqapproval;
	}


	public String getUseproposeapproval() {
		return useproposeapproval;
	}


	public void setUseproposeapproval(String useproposeapproval) {
		this.useproposeapproval = useproposeapproval;
	}


	public String getUsemanagerapproval() {
		return usemanagerapproval;
	}


	public void setUsemanagerapproval(String usemanagerapproval) {
		this.usemanagerapproval = usemanagerapproval;
	}


	public double getRndcreditreq() {
		return rndcreditreq;
	}


	public void setRndcreditreq(double rndcreditreq) {
		this.rndcreditreq = rndcreditreq;
	}


	public double getRndcreditchoice() {
		return rndcreditchoice;
	}


	public void setRndcreditchoice(double rndcreditchoice) {
		this.rndcreditchoice = rndcreditchoice;
	}


	public double getRndcreditadd() {
		return rndcreditadd;
	}


	public void setRndcreditadd(double rndcreditadd) {
		this.rndcreditadd = rndcreditadd;
	}


	public double getRndcreditdeduct() {
		return rndcreditdeduct;
	}


	public void setRndcreditdeduct(double rndcreditdeduct) {
		this.rndcreditdeduct = rndcreditdeduct;
	}


	public String getRndjijung() {
		return rndjijung;
	}


	public void setRndjijung(String rndjijung) {
		this.rndjijung = rndjijung;
	}


	public String getIsablereview() {
		return isablereview;
	}


	public void setIsablereview(String isablereview) {
		this.isablereview = isablereview;
	}


	public int getTsubjbudget() {
		return tsubjbudget;
	}


	public void setTsubjbudget(int tsubjbudget) {
		this.tsubjbudget = tsubjbudget;
	}


	public String getIsusebudget() {
		return isusebudget;
	}


	public void setIsusebudget(String isusebudget) {
		this.isusebudget = isusebudget;
	}


	public String getIsessential() {
		return isessential;
	}


	public void setIsessential(String isessential) {
		this.isessential = isessential;
	}


	public String getIsvisible() {
		return isvisible;
	}


	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}


	public String getBookname() {
		return bookname;
	}


	public void setBookname(String bookname) {
		this.bookname = bookname;
	}


	public int getBookprice() {
		return bookprice;
	}


	public void setBookprice(int bookprice) {
		this.bookprice = bookprice;
	}


	public int getSulpapernum() {
		return sulpapernum;
	}


	public void setSulpapernum(int sulpapernum) {
		this.sulpapernum = sulpapernum;
	}


	public int getCanceldays() {
		return canceldays;
	}


	public void setCanceldays(int canceldays) {
		this.canceldays = canceldays;
	}


	public String getIscharge() {
		return ischarge;
	}


	public void setIscharge(String ischarge) {
		this.ischarge = ischarge;
	}


	public int getMaleassignrate() {
		return maleassignrate;
	}


	public void setMaleassignrate(int maleassignrate) {
		this.maleassignrate = maleassignrate;
	}


	public String getGradexam_flag() {
		return gradexam_flag;
	}


	public void setGradexam_flag(String gradexam_flag) {
		this.gradexam_flag = gradexam_flag;
	}


	public String getGradftest_flag() {
		return gradftest_flag;
	}


	public void setGradftest_flag(String gradftest_flag) {
		this.gradftest_flag = gradftest_flag;
	}


	public String getGradhtest_flag() {
		return gradhtest_flag;
	}


	public void setGradhtest_flag(String gradhtest_flag) {
		this.gradhtest_flag = gradhtest_flag;
	}


	public String getGradreport_flag() {
		return gradreport_flag;
	}


	public void setGradreport_flag(String gradreport_flag) {
		this.gradreport_flag = gradreport_flag;
	}


	public String getIsattendchk() {
		return isattendchk;
	}


	public void setIsattendchk(String isattendchk) {
		this.isattendchk = isattendchk;
	}


	public String getAttstime() {
		return attstime;
	}


	public void setAttstime(String attstime) {
		this.attstime = attstime;
	}


	public String getAttetime() {
		return attetime;
	}


	public void setAttetime(String attetime) {
		this.attetime = attetime;
	}


	public int getReviewdays() {
		return reviewdays;
	}


	public void setReviewdays(int reviewdays) {
		this.reviewdays = reviewdays;
	}


	public void setSubjtypenm(String subjtypenm) {
		this.subjtypenm = subjtypenm;
	}


	public String getUsebook() {
		return usebook;
	}


	public void setUsebook(String usebook) {
		this.usebook = usebook;
	}


	public String getIsgoyong() {
		return isgoyong;
	}


	public void setIsgoyong(String isgoyong) {
		this.isgoyong = isgoyong;
	}


	public int getGoyongpricemajor() {
		return goyongpricemajor;
	}


	public void setGoyongpricemajor(int goyongpricemajor) {
		this.goyongpricemajor = goyongpricemajor;
	}


	public int getGoyongpriceminor() {
		return goyongpriceminor;
	}


	public void setGoyongpriceminor(int goyongpriceminor) {
		this.goyongpriceminor = goyongpriceminor;
	}


	public String getIsoutsourcing() {
		return isoutsourcing;
	}


	public void setIsoutsourcing(String isoutsourcing) {
		this.isoutsourcing = isoutsourcing;
	}


	public int getStudy_count() {
		return study_count;
	}


	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}


	public int getPresulpapernum() {
		return presulpapernum;
	}


	public void setPresulpapernum(int presulpapernum) {
		this.presulpapernum = presulpapernum;
	}


	public int getAftersulpapernum() {
		return aftersulpapernum;
	}


	public void setAftersulpapernum(int aftersulpapernum) {
		this.aftersulpapernum = aftersulpapernum;
	}


	public String getPresulsdate() {
		return presulsdate;
	}


	public void setPresulsdate(String presulsdate) {
		this.presulsdate = presulsdate;
	}


	public String getPresuledate() {
		return presuledate;
	}


	public void setPresuledate(String presuledate) {
		this.presuledate = presuledate;
	}
	


	public String getAftersdate() {
		return aftersdate;
	}


	public void setAftersdate(String aftersdate) {
		this.aftersdate = aftersdate;
	}


	public String getAfteredate() {
		return afteredate;
	}


	public void setAfteredate(String afteredate) {
		this.afteredate = afteredate;
	}

}