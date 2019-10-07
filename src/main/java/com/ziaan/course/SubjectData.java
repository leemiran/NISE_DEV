// **********************************************************
//  1. ��      ��: Subject Data ����
//  2. ��  ��  ��: SubjectData.java
//  3. ��      ��: Subject Data ����
//  4. ȯ      ��: JDK 1.4
//  5. ��      ��: 1.0
//  6. ��      ��: ���� 2004. 11. 14
//  7. ��      ��: ����� 2008.10.10
// **********************************************************
package com.ziaan.course;

public class SubjectData { 
    private String  grcode          ;
    private String  subj            ;
    private String  subjnm          ;
    private String  isonoff         ;
    private String  subjclass       ;
    private String  upperclass      ;
    private String  upperclassnm    ;
    private String  middleclass     ;
    private String  middleclassnm   ;
    private String  lowerclass      ;
    private String  lowerclassnm    ;
    private String  specials        ;
    private String  muserid         ;
    private String  musertel        ;
    private String  cuserid         ;
    private String  isuse           ;
    private String  isapproval      ;
    private String 	isgoyong		;
    private String  ispropose       ;
    private String  mftype          ;
    private String  ismfmenuimg     ;
    private String  ismfbranch      ;
    private String  mforder         ;
    private String  mfdlist         ;
    private String  mfstart         ;
    private String  mfnmtype        ;
    private String  mfgrdate        ;
    private String  otbgcolor       ;
    private String  iscentered      ;
    private int     biyong          ;
    private int     edudays         ;
    private int     studentlimit    ;
    private String  usebook         ;
    private int     bookprice       ;
    private String  owner           ;
    private String  producer        ;
    private String  crdate          ;
    private String  language        ;
    private String  server          ;
    private String  dir             ;
    private String  eduurl          ;
    private String  vodurl          ;
    private String  preurl          ;
    private String  conturl         ;
    private int     ratewbt         ;
    private int     ratevod         ;
    private String  env             ;
    private String  tutor           ;
    private String  tutornm         ;
    private String  bookname        ;
    private String  sdesc           ;
    private int     warndays        ;
    private int     stopdays        ;
    private int     point           ;
    private int     edulimit        ;
    private int     gradscore       ;
    private int     gradstep        ;
    private double  wstep           ;
    private double  wmtest          ;
    private double  wftest          ;
    private double  wreport         ;
    private double  wact            ;
    private double  wetc1           ;
    private double  wetc2           ;
    private String  inuserid        ;
    private String  indate          ;
    private String  luserid         ;
    private String  ldate           ;
            
    private String  classname       ;
    private String  codenm          ;
    private String  name            ;
            
    private String  place           ;
    private String  placejh         ;
    private String  ispromotion     ;
    private String  isessential     ;
    private int     score           ;
    private String  subjtarget      ;
                    
    private String  museridnm       ;
    private String  cuseridnm       ;
    private String  producernm      ;
    private String  ownernm         ;
                    
    private String  proposetype     ;
    private int     edutimes        ;
    private String  edutype         ;
    private String  intro           ;
    private String  explain         ;
                    
    private String  contenttype     ;
    private String  contentgrade    ;
                    
    private int     gradreport              ;   // ������� - ����
    private int     gradexam                ;   // ������� - ����(�߰���)
    private int     gradftest               ;   // ������� - ����(������)
    private int     gradhtest               ;   // ������� - ����(������)
    private double  whtest                  ;   // ������
    private String  usesubjseqapproval      ;   // ���翩�� - ��������ְ�����
    private String  useproposeapproval      ;   // ���翩�� - ������û��������
    private String  usemanagerapproval      ;   // ���翩�� - �ְ������������
    private double  rndcreditreq            ;   // ��������(��������-�ʼ�)
    private double  rndcreditchoice         ;   // ��������(��������-����)
    private double  rndcreditadd            ;   // ��������(��������-��������)
    private double  rndcreditdeduct         ;   // ��������(��������-��������)
    private String  rndjijung               ;   // ��������-�������񿩺�
    private String  rndjikmu                ;   // ��������-�������񿩺�
    private String  isablereview            ;   // �������ɿ���
    private String  isoutsourcing           ;   // ��Ź��������
    private String  edumans                 ;   // ������ ���
    private String  bookfilenamereal        ;   // �������ΰ��� ÷������
    private String  bookfilenamenew         ;   // �������ΰ��� ÷������
                                            
    private String  isvisible               ;   // �н��ڿ��� �����ֱ�
    private String  isalledu                ;   // ���米������
            
    private int     subjseqcount            ;   // ������ ����
    private int     subjobjcount            ;   // ������������� ����
            
    private String  isintroduction          ;   // �н��Ұ� ��뿩��
    private int     eduperiod               ;   // �н��Ⱓ
    private int goyongpricemajor;            //��뺸�� ȯ�޾�(����)
    private int goyongpriceminor;            //��뺸�� ȯ�޾�(�߼ұ��)
    private String  introducefilenamereal   ;   // ����Ұ� �̹���
    private String  introducefilenamenew    ;   // ����Ұ� �̹���
    private String  informationfilenamereal ;   // ����(����)
    private String  informationfilenamenew  ;   // ����(����)
                    
    private String  memo                    ;   // ���
    private String  ischarge                ;   // ������ ��/���Ῡ��(Y/N)
    private String  isopenedu               ;   // ���� ���� ����
    private int     maleassignrate          ;   // ���� �Ҵ� ����
    
    private String  gradexam_flag           ;   // �߰��� flag
    private String  gradftest_flag          ;   // ������ flag
    private String  gradhtest_flag          ;   // ������ flag
    private String  gradreport_flag         ;   // ���� flag
    
    private int     requiredyscore          ;
    private int     requirednscore          ;
    
    private String class_a             = "";
    private String class_b             = "";
    private String class_c             = "";
    private String class_d             = "";
    private String level_a             = "";
    private String level_b             = "";
    private String level_c             = "";
    private String charge_a            = "";
    private String graduatednote       = "";
    private String taxcomp             = "";
    private String taxcompnm           = "";

    // s : �߰� ��� ==================================
    private String  subj_gu				;	// ��������(�Ϲ�, JIT : J, �������� : M)
    private String  content_cd				;	// �������ڵ�(Normal�����ϰ�� ����)
    private int  	study_count				;	// �������-����Ƚ��
    private int		reviewdays				;	// �������ɱⰣ
    private String  lev					;	// ��������
    private String	gubn					;	// �̼�����
    private String	duty					;	// �������
    private String	grade					;	// �������
    private String 	test					;	// ������
    private String 	org						;	// �������(������)
    private int   	study_time				;	// �������-�ð�

    private String 	sel_dept				;	// �μ� ��û����
    private String 	sel_post				;	// ���� ��û����
    private int   	cp_accrate				;	// CP������

    private int  	goyongpricestand	= 0; 	// ��뺸��ȯ�޾�-CP������
    private String 	getmethod			= ""; 	// ������Ȯ��-���
    private String 	cp					= ""; 	// ������Ȯ��-CP
    private String 	cpnm				= ""; 	// ������Ȯ��-CP��
    private String 	firstdate			= ""; 	// �����⵵-����Ȯ��
    private String 	judgedate			= ""; 	// �����⵵-�ɻ翬��
    private String 	getdate				= ""; 	// �����⵵-��������
    private String 	iscustomedu			= "";	// ��������������
    private int 	cp_account			= 0;	// CP ����ݾ�
    private int 	cp_vat				= 0;	// CP VAT
    private String  contentprogress		= "";
    private String  cpsubj		        = "";
    
    // e : �߰� ��� ==================================
    
    public SubjectData() { 
        grcode              = "";
        subj                = "";
        subjnm              = "";
        isonoff             = "";
        subjclass           = "";
        upperclass          = "";
        upperclassnm        = "";
        middleclass         = "";
        middleclassnm       = "";
        lowerclass          = "";
        lowerclassnm        = "";
        specials            = "";
        muserid             = "";
        musertel            = "";
        cuserid             = "";
        isuse               = "";
        isapproval          = "";
        isgoyong			= "";
        ispropose           = "";
        mftype              = "";
        ismfmenuimg         = "";
        ismfbranch          = "";
        mforder             = "";
        mfdlist             = "";
        mfstart             = "";
        mfnmtype            = "";
        mfgrdate            = "";
        otbgcolor           = "";
        iscentered          = "";
        biyong              = 0;
        edudays             = 0;
        studentlimit        = 0;
        usebook             = "";
        bookprice           = 0;
        owner               = "";
        producer            = "";
        crdate              = "";
        language            = "";
        server              = "";
        dir                 = "";
        eduurl              = "";
        vodurl              = "";
        preurl              = "";
        conturl             = "";
        ratewbt             = 0;
        ratevod             = 0;
        env                 = "";
        tutor               = "";
        tutornm             = "";
        bookname            = "";
        sdesc               = "";
        warndays            = 0;
        stopdays            = 0;
        point               = 0;
        edulimit            = 0;
        gradscore           = 0;
        gradstep            = 0;
        wstep               = 0;
        wmtest              = 0;
        wftest              = 0;
        wreport             = 0;
        wact                = 0;
        wetc1               = 0;
        wetc2               = 0;
        inuserid            = "";
        indate              = "";
        luserid             = "";
        ldate               = "";

        classname           = "";
        codenm              = "";
        name                = "";

        place               = "";
        placejh             = "";
        ispromotion         = "";
        isessential         = "";
        score               = 0;
        subjtarget          = "";

        museridnm           = "";
        cuseridnm           = "";
        producernm          = "";
        ownernm             = "";

        gradreport          = 0;   // ������� - ����
        gradexam            = 0;   // ������� - ����(�߰���)
        gradftest           = 0;   // ������� - ����(������)
        gradhtest           = 0;   // ������� - ����(������)
        gradreport_flag     = "";  // ������� - ����(�ʼ�/���� ����)
        gradexam_flag       = "";  // ������� - �߰���(�ʼ�/���� ����)
        gradftest_flag      = "";  // ������� - ������(�ʼ�/���� ����)
        gradhtest_flag      = "";  // ������� - ������(�ʼ�/���� ����)
        whtest              = 0;   // ������
        usesubjseqapproval  = "";
        useproposeapproval  = "";
        usemanagerapproval  = "";
        rndcreditreq        = 0;
        rndcreditchoice     = 0;
        rndcreditadd        = 0;
        rndcreditdeduct     = 0;
        rndjijung           = "";
        rndjikmu            = "";
        isablereview        = "";
        isoutsourcing       = "";
        edumans             = "";
        bookfilenamereal    = "";
        bookfilenamenew     = "";
        subjseqcount        = 0;

        isvisible           = "Y";
        isalledu            = "N";
        ischarge            = "N";
        isopenedu           = "N";
        maleassignrate      = 0;
        
        requiredyscore      = 0;
        requirednscore      = 0;
        class_a             = "";
        class_b             = "";
        class_c             = "";
        class_d             = "";
        level_a             = "";
        level_b             = "";
        level_c             = "";
        charge_a            = "";
        taxcomp             = "";
        taxcompnm           = "";
        
        subj_gu				= "";
        content_cd			= "";
        study_count			= 0;
        reviewdays			= 0;	// �������ɱⰣ
        lev					= "";	// ��������
        gubn				= "";	// �̼�����
        duty				= "";	// �������
        grade				= "";	// �������
        test				= "";	// ������
        sel_dept			= "";	// �μ� ��û����
        sel_post			= "";	// ���� ��û����
        org					= "";	// �������(������)
        study_time			= 0;	// �������-�ð�
        cp_accrate			= 0; 	// CP������

        goyongpricestand	= 0; 	// ��뺸��ȯ�޾�-CP������
        getmethod			= ""; 	// ������Ȯ��-���
        cp					= ""; 	// ������Ȯ��-CP
        cpnm				= ""; 	// ������Ȯ��-CP��
        firstdate			= ""; 	// �����⵵-����Ȯ��
        judgedate			= ""; 	// �����⵵-�ɻ翬��
        getdate				= ""; 	// �����⵵-��������
        cp_account			= 0;	// CP����ݾ�
        contentprogress		= "Y";	// ���� üũ (Y : ���� , N : ����)
        proposetype     	= "N";
        cpsubj              = "";
        
    }
    
	public String getContentprogress() {
		return contentprogress;
	}

	public void setContentprogress(String contentprogress) {
		this.contentprogress = contentprogress;
	}

	public int getCp_vat() {
		return cp_vat;
	}

	public void setCp_vat(int cp_vat) {
		this.cp_vat = cp_vat;
	}
	
	public String getCpsubj() {
		return cpsubj;
	}

	public void setCpsubj(String cpsubj) {
		this.cpsubj = cpsubj;
	}
	
	public int getCp_account() {
		return cp_account;
	}

	public void setCp_account(int cp_account) {
		this.cp_account = cp_account;
	}

	public int getGoyongpricestand() {
		return goyongpricestand;
	}

	public void setGoyongpricestand(int goyongpricestand) {
		this.goyongpricestand = goyongpricestand;
	}

	public String getGetmethod() {
		return getmethod;
	}

	public void setGetmethod(String getmethod) {
		this.getmethod = getmethod;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getCpnm() {
		return cpnm;
	}

	public void setCpnm(String cpnm) {
		this.cpnm = cpnm;
	}

	public String getFirstdate() {
		return firstdate;
	}

	public void setFirstdate(String firstdate) {
		this.firstdate = firstdate;
	}

	public String getJudgedate() {
		return judgedate;
	}

	public void setJudgedate(String judgedate) {
		this.judgedate = judgedate;
	}

	public String getGetdate() {
		return getdate;
	}

	public void setGetdate(String getdate) {
		this.getdate = getdate;
	}

	public int getCp_accrate() {
		return cp_accrate;
	}

	public void setCp_accrate(int cp_accrate) {
		this.cp_accrate = cp_accrate;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public int getStudy_time() {
		return study_time;
	}

	public void setStudy_time(int study_time) {
		this.study_time = study_time;
	}

	public String getSel_dept() {
		return sel_dept;
	}

	public void setSel_dept(String sel_dept) {
		this.sel_dept = sel_dept;
	}

	public String getSel_post() {
		return sel_post;
	}

	public void setSel_post(String sel_post) {
		this.sel_post = sel_post;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getGrcode() {
		return grcode;
	}

	public void setGrcode(String grcode) {
		this.grcode = grcode;
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

	public String getIsonoff() {
		return isonoff;
	}

	public void setIsonoff(String isonoff) {
		this.isonoff = isonoff;
	}

	public String getSubjclass() {
		return subjclass;
	}

	public void setSubjclass(String subjclass) {
		this.subjclass = subjclass;
	}

	public String getUpperclass() {
		return upperclass;
	}

	public void setUpperclass(String upperclass) {
		this.upperclass = upperclass;
	}

	public String getUpperclassnm() {
		return upperclassnm;
	}

	public void setUpperclassnm(String upperclassnm) {
		this.upperclassnm = upperclassnm;
	}

	public String getMiddleclass() {
		return middleclass;
	}

	public void setMiddleclass(String middleclass) {
		this.middleclass = middleclass;
	}

	public String getMiddleclassnm() {
		return middleclassnm;
	}

	public void setMiddleclassnm(String middleclassnm) {
		this.middleclassnm = middleclassnm;
	}

	public String getLowerclass() {
		return lowerclass;
	}

	public void setLowerclass(String lowerclass) {
		this.lowerclass = lowerclass;
	}

	public String getLowerclassnm() {
		return lowerclassnm;
	}

	public void setLowerclassnm(String lowerclassnm) {
		this.lowerclassnm = lowerclassnm;
	}

	public String getSpecials() {
		return specials;
	}

	public void setSpecials(String specials) {
		this.specials = specials;
	}

	public String getMuserid() {
		return muserid;
	}

	public void setMuserid(String muserid) {
		this.muserid = muserid;
	}

	public String getMusertel() {
		return musertel;
	}

	public void setMusertel(String musertel) {
		this.musertel = musertel;
	}

	public String getCuserid() {
		return cuserid;
	}

	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}

	public String getIsuse() {
		return isuse;
	}

	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}

	public String getIsapproval() {
		return isapproval;
	}

	public void setIsapproval(String isapproval) {
		this.isapproval = isapproval;
	}

	public String getIsgoyong() {
		return isgoyong;
	}

	public void setIsgoyong(String isgoyong) {
		this.isgoyong = isgoyong;
	}

	public String getIspropose() {
		return ispropose;
	}

	public void setIspropose(String ispropose) {
		this.ispropose = ispropose;
	}

	public String getMftype() {
		return mftype;
	}

	public void setMftype(String mftype) {
		this.mftype = mftype;
	}

	public String getIsmfmenuimg() {
		return ismfmenuimg;
	}

	public void setIsmfmenuimg(String ismfmenuimg) {
		this.ismfmenuimg = ismfmenuimg;
	}

	public String getIsmfbranch() {
		return ismfbranch;
	}

	public void setIsmfbranch(String ismfbranch) {
		this.ismfbranch = ismfbranch;
	}

	public String getMforder() {
		return mforder;
	}

	public void setMforder(String mforder) {
		this.mforder = mforder;
	}

	public String getMfdlist() {
		return mfdlist;
	}

	public void setMfdlist(String mfdlist) {
		this.mfdlist = mfdlist;
	}

	public String getMfstart() {
		return mfstart;
	}

	public void setMfstart(String mfstart) {
		this.mfstart = mfstart;
	}

	public String getMfnmtype() {
		return mfnmtype;
	}

	public void setMfnmtype(String mfnmtype) {
		this.mfnmtype = mfnmtype;
	}

	public String getMfgrdate() {
		return mfgrdate;
	}

	public void setMfgrdate(String mfgrdate) {
		this.mfgrdate = mfgrdate;
	}

	public String getOtbgcolor() {
		return otbgcolor;
	}

	public void setOtbgcolor(String otbgcolor) {
		this.otbgcolor = otbgcolor;
	}

	public String getIscentered() {
		return iscentered;
	}

	public void setIscentered(String iscentered) {
		this.iscentered = iscentered;
	}

	public int getBiyong() {
		return biyong;
	}

	public void setBiyong(int biyong) {
		this.biyong = biyong;
	}

	public int getEdudays() {
		return edudays;
	}

	public void setEdudays(int edudays) {
		this.edudays = edudays;
	}

	public int getStudentlimit() {
		return studentlimit;
	}

	public void setStudentlimit(int studentlimit) {
		this.studentlimit = studentlimit;
	}

	public String getUsebook() {
		return usebook;
	}

	public void setUsebook(String usebook) {
		this.usebook = usebook;
	}

	public int getBookprice() {
		return bookprice;
	}

	public void setBookprice(int bookprice) {
		this.bookprice = bookprice;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCrdate() {
		return crdate;
	}

	public void setCrdate(String crdate) {
		this.crdate = crdate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getEduurl() {
		return eduurl;
	}

	public void setEduurl(String eduurl) {
		this.eduurl = eduurl;
	}

	public String getVodurl() {
		return vodurl;
	}

	public void setVodurl(String vodurl) {
		this.vodurl = vodurl;
	}

	public String getPreurl() {
		return preurl;
	}

	public void setPreurl(String preurl) {
		this.preurl = preurl;
	}

	public String getConturl() {
		return conturl;
	}

	public void setConturl(String conturl) {
		this.conturl = conturl;
	}

	public int getRatewbt() {
		return ratewbt;
	}

	public void setRatewbt(int ratewbt) {
		this.ratewbt = ratewbt;
	}

	public int getRatevod() {
		return ratevod;
	}

	public void setRatevod(int ratevod) {
		this.ratevod = ratevod;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getTutornm() {
		return tutornm;
	}

	public void setTutornm(String tutornm) {
		this.tutornm = tutornm;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getSdesc() {
		return sdesc;
	}

	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
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

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getEdulimit() {
		return edulimit;
	}

	public void setEdulimit(int edulimit) {
		this.edulimit = edulimit;
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

	public String getInuserid() {
		return inuserid;
	}

	public void setInuserid(String inuserid) {
		this.inuserid = inuserid;
	}

	public String getIndate() {
		return indate;
	}

	public void setIndate(String indate) {
		this.indate = indate;
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

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getCodenm() {
		return codenm;
	}

	public void setCodenm(String codenm) {
		this.codenm = codenm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getIspromotion() {
		return ispromotion;
	}

	public void setIspromotion(String ispromotion) {
		this.ispromotion = ispromotion;
	}

	public String getIsessential() {
		return isessential;
	}

	public void setIsessential(String isessential) {
		this.isessential = isessential;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getSubjtarget() {
		return subjtarget;
	}

	public void setSubjtarget(String subjtarget) {
		this.subjtarget = subjtarget;
	}

	public String getMuseridnm() {
		return museridnm;
	}

	public void setMuseridnm(String museridnm) {
		this.museridnm = museridnm;
	}

	public String getCuseridnm() {
		return cuseridnm;
	}

	public void setCuseridnm(String cuseridnm) {
		this.cuseridnm = cuseridnm;
	}

	public String getProducernm() {
		return producernm;
	}

	public void setProducernm(String producernm) {
		this.producernm = producernm;
	}

	public String getOwnernm() {
		return ownernm;
	}

	public void setOwnernm(String ownernm) {
		this.ownernm = ownernm;
	}

	public String getProposetype() {
		return proposetype;
	}

	public void setProposetype(String proposetype) {
		this.proposetype = proposetype;
	}

	public int getEdutimes() {
		return edutimes;
	}

	public void setEdutimes(int edutimes) {
		this.edutimes = edutimes;
	}

	public String getEdutype() {
		return edutype;
	}

	public void setEdutype(String edutype) {
		this.edutype = edutype;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public String getContentgrade() {
		return contentgrade;
	}

	public void setContentgrade(String contentgrade) {
		this.contentgrade = contentgrade;
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

	public String getRndjikmu() {
		return rndjikmu;
	}

	public void setRndjikmu(String rndjikmu) {
		this.rndjikmu = rndjikmu;
	}

	public String getIsablereview() {
		return isablereview;
	}

	public void setIsablereview(String isablereview) {
		this.isablereview = isablereview;
	}

	public String getIsoutsourcing() {
		return isoutsourcing;
	}

	public void setIsoutsourcing(String isoutsourcing) {
		this.isoutsourcing = isoutsourcing;
	}

	public String getEdumans() {
		return edumans;
	}

	public void setEdumans(String edumans) {
		this.edumans = edumans;
	}

	public String getBookfilenamereal() {
		return bookfilenamereal;
	}

	public void setBookfilenamereal(String bookfilenamereal) {
		this.bookfilenamereal = bookfilenamereal;
	}

	public String getBookfilenamenew() {
		return bookfilenamenew;
	}

	public void setBookfilenamenew(String bookfilenamenew) {
		this.bookfilenamenew = bookfilenamenew;
	}

	public String getIsvisible() {
		return isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}

	public String getIsalledu() {
		return isalledu;
	}

	public void setIsalledu(String isalledu) {
		this.isalledu = isalledu;
	}

	public int getSubjseqcount() {
		return subjseqcount;
	}

	public void setSubjseqcount(int subjseqcount) {
		this.subjseqcount = subjseqcount;
	}

	public int getSubjobjcount() {
		return subjobjcount;
	}

	public void setSubjobjcount(int subjobjcount) {
		this.subjobjcount = subjobjcount;
	}

	public String getIsintroduction() {
		return isintroduction;
	}

	public void setIsintroduction(String isintroduction) {
		this.isintroduction = isintroduction;
	}

	public int getEduperiod() {
		return eduperiod;
	}

	public void setEduperiod(int eduperiod) {
		this.eduperiod = eduperiod;
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

	public String getIntroducefilenamereal() {
		return introducefilenamereal;
	}

	public void setIntroducefilenamereal(String introducefilenamereal) {
		this.introducefilenamereal = introducefilenamereal;
	}

	public String getIntroducefilenamenew() {
		return introducefilenamenew;
	}

	public void setIntroducefilenamenew(String introducefilenamenew) {
		this.introducefilenamenew = introducefilenamenew;
	}

	public String getInformationfilenamereal() {
		return informationfilenamereal;
	}

	public void setInformationfilenamereal(String informationfilenamereal) {
		this.informationfilenamereal = informationfilenamereal;
	}

	public String getInformationfilenamenew() {
		return informationfilenamenew;
	}

	public void setInformationfilenamenew(String informationfilenamenew) {
		this.informationfilenamenew = informationfilenamenew;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIscharge() {
		return ischarge;
	}

	public void setIscharge(String ischarge) {
		this.ischarge = ischarge;
	}

	public String getIsopenedu() {
		return isopenedu;
	}

	public void setIsopenedu(String isopenedu) {
		this.isopenedu = isopenedu;
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

	public int getRequiredyscore() {
		return requiredyscore;
	}

	public void setRequiredyscore(int requiredyscore) {
		this.requiredyscore = requiredyscore;
	}

	public int getRequirednscore() {
		return requirednscore;
	}

	public void setRequirednscore(int requirednscore) {
		this.requirednscore = requirednscore;
	}

	public String getClass_a() {
		return class_a;
	}

	public void setClass_a(String class_a) {
		this.class_a = class_a;
	}

	public String getClass_b() {
		return class_b;
	}

	public void setClass_b(String class_b) {
		this.class_b = class_b;
	}

	public String getClass_c() {
		return class_c;
	}

	public void setClass_c(String class_c) {
		this.class_c = class_c;
	}

	public String getClass_d() {
		return class_d;
	}

	public void setClass_d(String class_d) {
		this.class_d = class_d;
	}

	public String getLevel_a() {
		return level_a;
	}

	public void setLevel_a(String level_a) {
		this.level_a = level_a;
	}

	public String getLevel_b() {
		return level_b;
	}

	public void setLevel_b(String level_b) {
		this.level_b = level_b;
	}

	public String getLevel_c() {
		return level_c;
	}

	public void setLevel_c(String level_c) {
		this.level_c = level_c;
	}

	public String getCharge_a() {
		return charge_a;
	}

	public void setCharge_a(String charge_a) {
		this.charge_a = charge_a;
	}

	public String getGraduatednote() {
		return graduatednote;
	}

	public void setGraduatednote(String graduatednote) {
		this.graduatednote = graduatednote;
	}

	public String getTaxcomp() {
		return taxcomp;
	}

	public void setTaxcomp(String taxcomp) {
		this.taxcomp = taxcomp;
	}

	public String getTaxcompnm() {
		return taxcompnm;
	}

	public void setTaxcompnm(String taxcompnm) {
		this.taxcompnm = taxcompnm;
	}

	public String getSubj_gu() {
		return subj_gu;
	}

	public void setSubj_gu(String subj_gu) {
		this.subj_gu = subj_gu;
	}

	public String getContent_cd() {
		return content_cd;
	}

	public void setContent_cd(String content_cd) {
		this.content_cd = content_cd;
	}

	public int getStudy_count() {
		return study_count;
	}

	public void setStudy_count(int study_count) {
		this.study_count = study_count;
	}

	public int getReviewdays() {
		return reviewdays;
	}

	public void setReviewdays(int reviewdays) {
		this.reviewdays = reviewdays;
	}

	public String getLev() {
		return lev;
	}

	public void setLev(String lev) {
		this.lev = lev;
	}

	public String getGubn() {
		return gubn;
	}

	public void setGubn(String gubn) {
		this.gubn = gubn;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getIscustomedu() {
		return iscustomedu;
	}

	public void setIscustomedu(String iscustomedu) {
		this.iscustomedu = iscustomedu;
	}

}
