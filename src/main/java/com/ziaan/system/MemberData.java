// **********************************************************
//  1. ��      ��: ȸ�� Data
//  2. ���α׷��� : MemberData.java
//  3. ��      ��: ȸ�� data bean
//  4. ȯ      ��: JDK 1.3
//  5. ��      ��: 1.0
//  6. ��      ��:  2003. 7.  2
//  7. ��      ��:
// **********************************************************

package com.ziaan.system;

/**
 * file name : MemberData.java
 * date      : 2003/5/28
 * programmer:  (puggjsj@hanmail.net)
 * function  : MEMBER DATA
 */

public class MemberData
{ 
    private String  userid;          // ID
    private String  birth_date;           // �ֹι�ȣ
    private String  pwd;             // ��ȣȭ��ι�ȣ
    private String  name;            // �̸�
    private String  email;           // email
    private String  birthday;        // ����
    private String  cono;            // ���
    private String  authority;       // ����
    private String  usergubun;       // ����ڱ���(0022)
    private String  post1;           // ����1
    private String  post2;           // ����2
    private String  addr;            // �ּ�
    private String  addr2;           // ���ּ�
    private String  hometel;         // ����ȭ��ȣ
    private String  handphone;       // �޴���ȭ��ȣ
    private String  comptel;         // ȸ����ȭ��ȣ
    private String  tel_line;        // ������ȣ
    private String  comp;            // ȸ��ID
    private String  interest;        // ���ɻ���
    private String  recomid;         // ��õ��ID
    private String  ismailing;       // ���ϼ��ſ���
    private String  indate;          // ������
    private String  lglast;          // �����α��νð�
    private String  lgip;            // �α���IP
    private String  pwd_date;        // �н���������
    private String  old_pwd;         // �� �н�����
    private String  asgn;            // �Ҽ��ڵ�
    private String  asgnnm;          // �ҼӸ�
    private String  jikun;           // ����
    private String  jikunnm;         // ������
    private String  jikup;           // ����
    private String  jikupnm;         // ���޸�
    private String  jikwi;           // ����
    private String  jikwinm;         // ������
    private String  jikmu1;          // ����1
    private String  jikmu2;          // ����2
    private String  jikmu3;          // ����3
    private String  jikmu4;          // ����4
    private String  jikmunm;         // ������
    private String  jikchek;         // ��å
    private String  jikcheknm;       // ��å��
    private String  jikho;           // ��ȣ
    private String  jikhonm;         // ȣ����
    private String  ent_date;        // �Ի���
    private String  grp_ent_date;    // �׷��Ի���
    private String  pmt_date;        // ������
    private String  old_cono;        // �� ���
    private String  cono_chg_date;   // ���������
    private String  office_gbn;      // ��������
    private String  office_gbnnm;    // �������θ�
    private String  retire_date;     // �������
    private String  work_plc;        // �ٹ����ڵ�
    private String  work_plcnm;      // �ٹ�����
    private String  sex;             // ����
    private String  photo;
    private String  fix_gubun;       // ��� ����
    private String  promotion;       // ������󿩺�
    private String  gubb_prmt;       // ������󿩺�

    private String  gadmin;          // ����
    private String  companynm;       // ȸ���
    private String  gpmnm;           // ����θ�
    private String  deptnm;          // �μ���
    private String  compnm;          // ȸ��� + / + ����θ� + / + �μ���
    private String  isdeptmnger;     // �μ��� ����
    private String  compgubun;       // ȸ�� ������

    private int lgcnt;              // �α���Ƚ��
    private int point;              // ���ϸ��� ����Ʈ ����
    private int dispnum;            // �ѰԽù���
    private int total_page_count;   // �Խù�����������
    
    private String  gubun;          // ���� ����
    
    private String type;            // ȸ������
    private String useuserid;       // ���� ����
    
    private String branch;          // ����
    private String isretire;        // Ż�𿩺�
    private String retire_reason;   // Ż������
    private String retire_type;     // Ż�� ����
    private String bon_adm;		 // ���δ�米������
    private String lvl_nm;			//���޸�

	private String position_nm;		//�μ���
	private String lic;		//�����ڰ�
	private String gb;		//����
	private String snm;		//�б���
	private String rv;		//�������	
	private String dept_cd;	// ��������û
	private String agency_cd;	//��������û
	
    public MemberData() { }

    public String getDept_cd        ()                       { return dept_cd;    }
    public void setDept_cd          (String dept_cd)          { this.dept_cd = dept_cd;   }
    public String getAgency_cd   ()                       { return agency_cd;    }
    public void setAgency_cd     (String agency_cd)     { this.agency_cd = agency_cd;    }
    
    public String getLvl_nm        ()                       { return lvl_nm;    }
    public void setLvl_nm          (String lvl_nm)          { this.lvl_nm = lvl_nm;   }
    public String getPosition_nm   ()                       { return position_nm;    }
    public void setPosition_nm     (String position_nm)     { this.position_nm = position_nm;    }
    public void   setUserid        (String userid      )    { this.userid          = userid        ; }
    public String getUserid        ()                       { return userid                        ; }
    public void   setbirth_date         (String birth_date       )    { this.birth_date           = birth_date         ; }
    public String getbirth_date         ()                       { return birth_date                         ; }
    public void   setPwd           (String pwd         )    { this.pwd             = pwd           ; }
    public String getPwd           ()                       { return pwd                           ; }
    public void   setName          (String name        )    { this.name            = name          ; }
    public String getName          ()                       { return name                          ; }
    public void   setEmail         (String email       )    { this.email           = email         ; }
    public String getEmail         ()                       { return email                         ; }
    public void   setBirthday      (String birthday    )    { this.birthday        = birthday      ; }
    public String getBirthday      ()                       { return birthday                      ; }
    public void   setCono          (String cono        )    { this.cono            = cono          ; }
    public String getCono          ()                       { return cono                          ; }
    public void   setAuthority     (String authority   )    { this.authority       = authority     ; }
    public String getAuthority     ()                       { return authority                     ; }
    public void   setUsergubun     (String usergubun   )    { this.usergubun       = usergubun     ; }
    public String getUsergubun     ()                       { return usergubun                     ; }
    public void   setPost1         (String post1       )    { this.post1           = post1         ; }
    public String getPost1         ()                       { return post1                         ; }
    public void   setPost2         (String post2       )    { this.post2           = post2         ; }
    public String getPost2         ()                       { return post2                         ; }
    public void   setAddr          (String addr        )    { this.addr            = addr          ; }
    public String getAddr          ()                       { return addr                          ; }
    public void   setAddr2         (String addr2       )    { this.addr2           = addr2         ; }
    public String getAddr2         ()                       { return addr2                         ; }
    public void   setHometel       (String hometel     )    { this.hometel         = hometel       ; }
    public String getHometel       ()                       { return hometel                       ; }
    public void   setHandphone     (String handphone   )    { this.handphone       = handphone     ; }
    public String getHandphone     ()                       { return handphone                     ; }
    public void   setComptel       (String comptel     )    { this.comptel         = comptel       ; }
    public String getComptel       ()                       { return comptel                       ; }
    public void   setTel_line      (String tel_line    )    { this.tel_line        = tel_line      ; }
    public String getTel_line      ()                       { return tel_line                      ; }
    public void   setComp          (String comp        )    { this.comp            = comp          ; }
    public String getComp          ()                       { return comp                          ; }
    public void   setInterest      (String interest    )    { this.interest        = interest      ; }
    public String getInterest      ()                       { return interest                      ; }
    public void   setRecomid       (String recomid     )    { this.recomid         = recomid       ; }
    public String getRecomid       ()                       { return recomid                       ; }
    public void   setIsmailing     (String ismailing   )    { this.ismailing       = ismailing     ; }
    public String getIsmailing     ()                       { return ismailing                     ; }
    public void   setIndate        (String indate      )    { this.indate          = indate        ; }
    public String getIndate        ()                       { return indate                        ; }
    public void   setLglast        (String lglast      )    { this.lglast          = lglast        ; }
    public String getLglast        ()                       { return lglast                        ; }
    public void   setLgip          (String lgip        )    { this.lgip            = lgip          ; }
    public String getLgip          ()                       { return lgip                          ; }
    public void   setPwd_date      (String pwd_date    )    { this.pwd_date        = pwd_date      ; }
    public String getPwd_date      ()                       { return pwd_date                      ; }
    public void   setOld_pwd       (String old_pwd     )    { this.old_pwd         = old_pwd       ; }
    public String getOld_pwd       ()                       { return old_pwd                       ; }
    public void   setAsgn          (String asgn        )    { this.asgn            = asgn          ; }
    public String getAsgn          ()                       { return asgn                          ; }
    public void   setAsgnnm        (String asgnnm      )    { this.asgnnm          = asgnnm        ; }
    public String getAsgnnm        ()                       { return asgnnm                        ; }
    public void   setJikun         (String jikun       )    { this.jikun           = jikun         ; }
    public String getJikun         ()                       { return jikun                         ; }
    public void   setJikunnm       (String jikunnm     )    { this.jikunnm         = jikunnm       ; }
    public String getJikunnm       ()                       { return jikunnm                       ; }
    public void   setJikup         (String jikup       )    { this.jikup           = jikup         ; }
    public String getJikup         ()                       { return jikup                         ; }
    public void   setJikupnm       (String jikupnm     )    { this.jikupnm         = jikupnm       ; }
    public String getJikupnm       ()                       { return jikupnm                       ; }
    public void   setJikwi         (String jikwi       )    { this.jikwi           = jikwi         ; }
    public String getJikwi         ()                       { return jikwi                         ; }
    public void   setJikwinm       (String jikwinm     )    { this.jikwinm         = jikwinm       ; }
    public String getJikwinm       ()                       { return jikwinm                       ; }
    public void   setJikmu1        (String jikmu1      )    { this.jikmu1          = jikmu1        ; }
    public String getJikmu1        ()                       { return jikmu1                        ; }
    public void   setJikmu2        (String jikmu2      )    { this.jikmu2          = jikmu2        ; }
    public String getJikmu2        ()                       { return jikmu2                        ; }
    public void   setJikmu3        (String jikmu3      )    { this.jikmu3          = jikmu3        ; }
    public String getJikmu3        ()                       { return jikmu3                        ; }
    public void   setJikmu4        (String jikmu4      )    { this.jikmu4          = jikmu4        ; }
    public String getJikmu4        ()                       { return jikmu4                        ; }
    public void   setJikmunm       (String jikmunm     )    { this.jikmunm         = jikmunm       ; }
    public String getJikmunm       ()                       { return jikmunm                       ; }
    public void   setJikchek       (String jikchek     )    { this.jikchek         = jikchek       ; }
    public String getJikchek       ()                       { return jikchek                       ; }
    public void   setJikcheknm     (String jikcheknm   )    { this.jikcheknm       = jikcheknm     ; }
    public String getJikcheknm     ()                       { return jikcheknm                     ; }
    public void   setJikho         (String jikho       )    { this.jikho           = jikho         ; }
    public String getJikho         ()                       { return jikho                         ; }
    public void   setJikhonm       (String jikhonm     )    { this.jikhonm         = jikhonm       ; }
    public String getJikhonm       ()                       { return jikhonm                       ; }
    public void   setEnt_date      (String ent_date    )    { this.ent_date        = ent_date      ; }
    public String getEnt_date      ()                       { return ent_date                      ; }
    public void   setGrp_ent_date  (String grp_ent_date)    { this.grp_ent_date    = grp_ent_date  ; }
    public String getGrp_ent_date  ()                       { return grp_ent_date                  ; }
    public void   setPmt_date      (String pmt_date    )    { this.pmt_date        = pmt_date      ; }
    public String getPmt_date      ()                       { return pmt_date                      ; }
    public void   setOld_cono      (String old_cono    )    { this.old_cono        = old_cono      ; }
    public String getOld_cono      ()                       { return old_cono                      ; }
    public void   setCono_chg_date (String cono_chg_date )  { this.cono_chg_date   = cono_chg_date ; }
    public String getCono_chg_date ()                       { return cono_chg_date                 ; }
    public void   setOffice_gbn    (String office_gbn  )    { this.office_gbn      = office_gbn    ; }
    public String getOffice_gbn    ()                       { return office_gbn                    ; }
    public void   setOffice_gbnnm  (String office_gbnnm  )  { this.office_gbnnm    = office_gbnnm  ; }
    public String getOffice_gbnnm  ()                       { return office_gbnnm                  ; }
    public void   setRetire_date   (String retire_date  )   { this.retire_date     = retire_date   ; }
    public String getRetire_date   ()                       { return retire_date                   ; }
    public void   setWork_plc      (String work_plc  )      { this.work_plc        = work_plc      ; }
    public String getWork_plc      ()                       { return work_plc                      ; }
    public void   setWork_plcnm    (String work_plcnm  )    { this.work_plcnm      = work_plcnm    ; }
    public String getWork_plcnm    ()                       { return work_plcnm                    ; }
    public void   setSex           (String sex         )    { this.sex             = sex           ; }
    public String getSex           ()                       { return sex                           ; }

    public void   setCompanynm     (String companynm   )    { this.companynm       = companynm     ; }
    public String getCompanynm     ()                       { return companynm                     ; }
    public void   setGpmnm         (String gpmnm       )    { this.gpmnm           = gpmnm         ; }
    public String getGpmnm         ()                       { return gpmnm                         ; }
    public void   setDeptnm        (String deptnm      )    { this.deptnm          = deptnm        ; }
    public String getDeptnm        ()                       { return deptnm                        ; }
    public void   setCompnm        (String compnm      )    { this.compnm          = compnm        ; }
    public String getCompnm        ()                       { return compnm                        ; }
    public void   setPhoto         (String photo)           { this.photo           = photo         ; }
    public String getPhoto         ()                       { return photo                         ; }
    public void   setFix_gubun     (String fix_gubun   )    { this.fix_gubun = fix_gubun           ; }
    public String getFix_gubun     ()                       { return fix_gubun                     ; }
    public void   setPromotion     (String promotion   )    { this.promotion = promotion           ; }
    public String getPromotion     ()                       { return promotion                     ; }
    public void   setIsdeptmnger   (String isdeptmnger )    { this.isdeptmnger = isdeptmnger       ; }
    public String getIsdeptmnger   ()                       { return isdeptmnger                   ; }
    public void   setGubb_prmt     (String gubb_prmt )      { this.gubb_prmt = gubb_prmt           ; }
    public String getGubb_prmt     ()                       { return gubb_prmt                     ; }
    public void   setCompgubun     (String compgubun )      { this.compgubun = compgubun           ; }
    public String getCompgubun     ()                       { return compgubun                     ; }
	  
    public void   setLic     (String vlic )      { this.lic = vlic           ; }
    public String getLic     ()                       { return lic                     ; }
    public void   setGb     (String vgb )      { this.gb = vgb           ; }
    public String getGb     ()                       { return gb                     ; }
    public void   setSnm     (String vsnm )      { this.snm = vsnm           ; }
    public String getSnm     ()                       { return snm                     ; }
    public void   setRv     (String vrv )      { this.rv = vrv           ; }
    public String getRv     ()                       { return rv                     ; }
    
    
    public void   setLgcnt(int lgcnt) { this.lgcnt = lgcnt;  }
    public int    getLgcnt()          { return lgcnt;        }
    public void   setPoint(int point) { this.point = point;  }
    public int    getPoint()          { return point;        }
    public void   setDispnum(int dispnum) { this.dispnum = dispnum; }
    public int    getDispnum()            { return dispnum;         }
    public void   setTotalPageCount(int total_page_count) { this.total_page_count = total_page_count; }
    public int    getTotalpagecount()                     { return total_page_count;                  }

    /**
     * @return
     */
    public String getGadmin() { 
        return gadmin;
    }

    /**
     * @param string
     */
    public void setGadmin(String string) { 
        gadmin = string;
    }
    
    
    /**
     * @return
     */
    public String getGubun() { 
        return gubun;
    }

    /**
     * @param string
     */
    public void setGubun(String string) { 
        gubun = string;
    }
    
    /**
     * @return
     */
    public String getType() { 
        return type;
    }

    /**
     * @param string
     */
    public void setType(String string) { 
        type = string;
    }
    
    /**
     * @return
     */
    public String getUseuserid() { 
        return useuserid;
    }

    /**
     * @param string
     */
    public void setUseuserid(String string) { 
        useuserid = string;
    }
    
    /**
     * @return
     */
    public String getBranch() { 
        return branch;
    }

    /**
     * @param string
     */
    public void setBranch(String string) { 
        branch = string;
    }
    
    /**
     * @return
     */
    public String getIsretire() { 
        return isretire;
    }

    /**
     * @param string
     */
    public void setIsretire(String string) { 
        isretire = string;
    }
    
    /**
     * @return
     */
    public String getRetireReason() { 
        return retire_reason;
    }

    /**
     * @param string
     */
    public void setRetireReason(String string) { 
        retire_reason = string;
    }
    
    /**
     * @return
     */
    public String getRetireType() { 
        return retire_type;
    }

    /**
     * @param string
     */
    public void setRetireType(String string) { 
        retire_type = string;
    }

	public String getBon_adm() {
		return bon_adm;
	}

	public void setBon_adm(String bon_adm) {
		this.bon_adm = bon_adm;
	}
}