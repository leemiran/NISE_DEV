package egovframework.usr.lgn.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mng.dao.ManagerDAO;
import egovframework.adm.sta.log.dao.LoginLogDAO;
import egovframework.com.cmm.service.Globals;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.svt.valid.ValidService;
import egovframework.usr.lgn.dao.LoginDAO;
import egovframework.usr.lgn.service.LoginService;

@Service("loginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService{
	
	@Resource(name="loginDAO")
    private LoginDAO loginDAO;
	
	@Resource(name="loginLogDAO")
	private LoginLogDAO loginLogDAO;
	
	@Resource(name="managerDAO")
    private ManagerDAO managerDAO;
	
	@Autowired
	ValidService validService;
	
	/**
	 * 숫자변환때문에 만듬
	 * @param number
	 * @param def
	 * @return int
	 * @throws Exception
	 */
	public int parseWithDefault(String number, int def)
	{
		try {
			return Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return def;
		}
	}
	
	/**
     * 회사별로 포탈 로그인 가능여부에 따라 제한을 둔다.
     * @param commandMap
     * @return int
     * @throws Exception
     */
	public int checkPortalYn(Map<String, Object> commandMap) throws Exception{
		return loginDAO.checkPortalYn(commandMap);
	}
	
	public int login(HttpServletRequest request, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		
		if (commandMap.get("p_d_type") !=null)
		{
			commandMap.put("p_d_type", "T");
		}
		
		String p_d_type = commandMap.get("p_d_type").toString();
		String p_l_type = commandMap.get("p_l_type") != null ? commandMap.get("p_l_type").toString() : "";
		String pwd = (String)commandMap.get("pwd");
		String issso = (String)commandMap.get("p_issso");
		String lgexcept = Globals.LOGIN_EXCEPT_ISUSE;
		int lgexceptcnt = Integer.valueOf(Globals.LOGIN_EXCEPT_VALUE);		
		String lgip = request.getRemoteAddr();
		
		String p_userLoginType = commandMap.get("userLoginType")  != null ? commandMap.get("userLoginType").toString() : "";
		
		System.out.println("p_userLoginType =================> "+p_userLoginType);
		System.out.println("lgip =================> "+lgip);
		
		System.out.println("pwd(impl)=================> "+pwd);
		
		commandMap.put("p_userid", (String)commandMap.get("userId"));
		commandMap.put("p_gadmin", "A1");
		
		Map adminIs = managerDAO.selectManagerView(commandMap);
		String gadmin = "";
		if (adminIs != null)
		{
			gadmin = adminIs.get("gadmin").toString();
		}

		Map userInfo = loginDAO.login(commandMap);

//		java.math.BigDecimal tempValue = (java.math.BigDecimal)userInfo.get("lgfail");
//		Integer v_lgfail = Integer.valueOf(tempValue.toString());
		Integer v_lgfail = this.parseWithDefault(userInfo.get("lgfail") + "", 0);

		//비밀번호변경기간이 지난 학습자임(Y : 3개월이 지남 / N : 3개월안됨)
		String v_pwdchangeyn = (String)userInfo.get("pwdchangeyn");

		//나이스개인번호 
		String v_nicePersonalNum = (String)userInfo.get("nicePersonalNum");
		String v_niceNumAllowYn = (String)userInfo.get("niceNumAllowYn");
		String v_empGubun = (String)userInfo.get("empGubun");
		
		String v_isretire = (String)userInfo.get("isretire");
		String v_pwd = (String)userInfo.get("pwd");
		
		String v_o_pwd = (String)userInfo.get("oPwd");
		
		String v_base64sha256encryptorpwd = (String)userInfo.get("base64sha256encryptorpwd");
		
		String v_deptCd = (String)userInfo.get("deptCd");
		String v_deptCnt = (String)userInfo.get("deptCnt");
		String v_agencyCnt = (String)userInfo.get("agencyCnt");
		String v_curDate5min = (String)userInfo.get("curDate5min");
		
		String v_jobCd = userInfo.get("jobCd") !=null ? (String)userInfo.get("jobCd") : "" ;
		
		int v_duptCnt = Integer.parseInt(userInfo.get("duptCnt")+"");		//아이디 중복 
		int v_ziplen = Integer.parseInt(userInfo.get("ziplen")+"");		//자택 우편번호 자리수
		int v_ziplen1 = Integer.parseInt(userInfo.get("ziplen1")+"");		//근무지 우편번호 자리수
		
		System.out.println("v_jobCd===============> "+v_jobCd);
		System.out.println("p_d_type===============> "+p_d_type);
		System.out.println("v_pwd===============> "+v_pwd);
		System.out.println("v_o_pwd===============> "+v_o_pwd);
		System.out.println("v_base64sha256encryptorpwd===============> "+v_base64sha256encryptorpwd);
		
		System.out.println("v_nicePersonalNum===============> "+v_nicePersonalNum);
		System.out.println("v_empGubun===============> "+v_empGubun);
		System.out.println("v_deptCd===============> "+v_deptCd);
		System.out.println("v_ziplen===============> "+v_ziplen);
		System.out.println("v_curDate5min===============> "+v_curDate5min);
		
		String loginYN = "N";
		
		if ("O".equals(p_d_type) )
		{
			if (pwd.equals(v_pwd) || (issso != null && issso.equals("Y")) )
			{
				loginYN = "Y";
				System.out.println("O -----> "+loginYN);
			}
		}
		else
		{
			if ("A".equals(p_l_type))
			{
				if (v_o_pwd.equals(pwd))
				{
					loginYN = "Y";
					System.out.println("A T -----> "+loginYN);
				}
			}
			else
			{
				if (v_o_pwd.equals(v_base64sha256encryptorpwd))
				{
					loginYN = "Y";
					System.out.println("T -----> "+loginYN);
				}				
			}			
		}	
		//loginYN = "Y";
		
		System.out.println("gadmin ------> "+ gadmin);
		System.out.println("v_lgfail ------> "+ v_lgfail);
		System.out.println("v_curDate5min ------> "+ v_curDate5min);
		
		//관리자 5번 로그인 틀렸을 경우 5분 후에 로그인 가능하게
		if ("A1".equals(gadmin) && v_lgfail>=5 && "N".equals(v_curDate5min))
		{
			isOk = -8;
		}
		else if ("A1".equals(gadmin) && (
										 !"0:0:0:0:0:0:0:1".equals(lgip) && 	//로컬
										 !"127.0.0.1".equals(lgip) &&		//로컬
										 !"112.217.104.34".equals(lgip) && 		//사무실1
										 !"112.217.104.35".equals(lgip) && 		//사무실1
										 !"58.150.186.35".equals(lgip) &&		//사무실2
										 !"112.187.162.111".equals(lgip) &&		//사무실3
										 !"114.202.33.118".equals(lgip) &&		//사무실4
										 !"103.129.186.164".equals(lgip) &&		//사무실5
										 !"59.26.110.113".equals(lgip) &&		//국특원
										 !"27.101.217.151".equals(lgip)		//미디어 서버
										)
		){
			isOk = -9;
			System.out.println("isOk===============> -9 " + isOk);
			System.out.println("lgip===============> -9 " + lgip);
			
		}
		else if ("A1".equals(gadmin) && !"A".equals(p_userLoginType)){
			isOk = -10;
			System.out.println("isOk===============> -10 " + isOk);
			System.out.println("lgip===============> -10 " + lgip);
		
		}
		else
		{
			if ( userInfo != null )
			{
				if ( v_lgfail < lgexceptcnt || !lgexcept.equals("true") )
				{
					if ("Y".equals(loginYN))
					{
						
						if ( v_isretire.equals("Y") )
						{
							isOk = -6; //탈퇴회원
						}
						else
						{
							isOk = 1; //로그인성공
							
							if (v_nicePersonalNum == null)
							{
								v_nicePersonalNum = "";
							}
							if (v_deptCd == null)
							{
								v_deptCd = "";
							}
							
							if (v_duptCnt > 1)
							{
								isOk = 990;
							//교원이고 나이스개인번호를 입력하지 않은 경우 개인정보수정 페이지로 보냄
							}
							else if (v_empGubun.equals("T") && v_nicePersonalNum.equals("") && v_niceNumAllowYn.equals("N") && !v_jobCd.equals("00039"))
							{
								isOk = 998;
							}
							else if ((v_empGubun.equals("T") || v_empGubun.equals("E")) && (v_deptCd.equals("") || !v_deptCnt.equals("Y") || !v_agencyCnt.equals("Y")) )
							{
								/**
								 * 교원 : T, 보조인력 : E, 교육전문직 : R, 일반회원 : P
								 * 교원, 교육전문직인 경우는 depthCnt(이전 상위교육청코드 개수), agencyCnt(이전 하위교육청코드 개수) 가 0개가 아니라면 개인정보페이지로 보낸다.
								 */
								isOk = 997;
							}
							else
							{
								//비밀번호변경이 3개월이 지나서 변경페이지로 보내야 하는 사용자임
								if (v_pwdchangeyn != null)
								{
									if (v_pwdchangeyn.equals("Y"))
									{
										isOk = 999;
									}
								}
							}
							
							//우편번호 자리수
							if (!v_empGubun.equals("T") && v_ziplen >= 6) isOk = 1000;
							if(!v_empGubun.equals("T") && v_ziplen == 0) isOk = 1100;
							if (!v_empGubun.equals("P") && v_ziplen1 >= 6) isOk = 1200;
							if(!v_empGubun.equals("P") && v_ziplen1 == 0) isOk = 1300;
							
							System.out.println("@@@@@@@@@@@@@@@@@@@@@ is Ok " + isOk);
							//세션셋팅
							userInfo.put("lgip", lgip);
							this.setSession(request, userInfo);
							
							String v_gubun = (String)commandMap.get("tem_grcode");
							if (v_gubun == null || v_gubun.equals(""))   v_gubun = "N000001";
							String v_userid = (String)commandMap.get("userId");
					        String v_year  = new SimpleDateFormat("yyyy").format(new Date() );
					        String v_month = new SimpleDateFormat("MM").format(new Date() );
					        String v_day   = new SimpleDateFormat("dd").format(new Date() );
					        String v_time  = new SimpleDateFormat("HH").format(new Date() );
							String v_week  = "";
							Calendar calendar=Calendar.getInstance();
					        calendar.set(Integer.parseInt(v_year),Integer.parseInt(v_month)-1,Integer.parseInt(v_day));
					        v_week=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)); // 1(일),2(월),3(화),4(수),5(목),6(금),7(토)
					        
					        Map inputMap = new HashMap();
					        inputMap.put("gubun", v_gubun);
					        inputMap.put("userid", v_userid);
					        inputMap.put("year", v_year);
					        inputMap.put("month", v_month);
					        inputMap.put("day", v_day);
					        inputMap.put("time", v_time);
					        inputMap.put("week", v_week);
					        
					        // 접속정보 변경
							if( loginLogDAO.selectLoginCount(inputMap) > 0 ){
								loginLogDAO.updateLogCount(inputMap);
							}else{
								loginLogDAO.insertLogCount(inputMap);
							}
							commandMap.put("lgip", lgip);
							commandMap.put("userNm", userInfo.get("name"));
							commandMap.put("comp", userInfo.get("comp"));
							this.updateLoginData(commandMap);
							this.updateLoginFail(commandMap, "Y");
						}
					}else{
						isOk = -3; //비밀번호 오류
						commandMap.put("p_lgfailcnt", v_lgfail+1);
				        // 오류 회수 추가
						this.updateLoginFail(commandMap, "N");
					}
				}
				else
				{
					isOk = -4; //Login Exception 로그인 제한
				}
			}
			else
			{
				isOk = -1;//사용자정보 없음
			}
		}
		System.out.println("isOk ---> "+isOk);
		return isOk;
	}
	
	/**
	 * 세션 세팅
	 * @param request
	 * @param map
	 * @throws Exception
	 */
	public void setSession(HttpServletRequest request, Map map) throws Exception {
		HttpSession session = request.getSession();
		session.setAttribute("logoSaveFileName", map.get("logoSaveFileName"));
		session.setAttribute("S_ShowDetail", "F");
		session.setAttribute("gubun1"		,map.get("gubun"));
    	session.setAttribute("userid"		,map.get("userid")  );
        session.setAttribute("resno"		,map.get("resno")   );
        session.setAttribute("name"			,map.get("name")    );
        session.setAttribute("email"		,map.get("email")   );
        session.setAttribute("comp"			,map.get("comp")    );
        session.setAttribute("compnm"		,map.get("compnm")  );
        session.setAttribute("grcode"		,map.get("grcode")  );
        session.setAttribute("jobcd"		,map.get("jobCd")  	);                    
        session.setAttribute("hometel"		,map.get("hometel") );
        session.setAttribute("handphone"	,map.get("handphone"));
        session.setAttribute("userip"		,map.get("userip")	);
        session.setAttribute("buserip"		,map.get("lgip")	);
        session.setAttribute("bonadm"		,map.get("bonAdm")	);	//본부담당교육여부
        session.setAttribute("hqorgcd"		,map.get("hqorgcd")	);	//소속(본부코드)
        session.setAttribute("domain"		,map.get("domain")	);	//소속(본부코드)
        session.setAttribute("emp_id"		,map.get("userid")  );
        session.setAttribute("emp_nm"		,map.get("userid")  );
        session.setAttribute("emp_gubun"		,map.get("empGubun")  	);
        session.setAttribute("email_id"		,map.get("email")   );
        session.setAttribute("lastlg"		,map.get("today")   );
       // session.setAttribute("nicePersonalNum"		,map.get("nicePersonalNum")   );
        session.setAttribute("birthDate"		,map.get("birthDate")   );
        session.setAttribute("s_niceNumAllowYn"		,map.get("niceNumAllowYn")   );
        session.setAttribute("gadmin","ZZ");
        session.setAttribute("lglast", map.get("lglast"));
        session.setAttribute("userAreaCode", validService.validUserArea(String.valueOf(map.get("userid"))));
    } 
	
	/**
	 * Login 정보 변경 lgcnt:로그인횟수, lglast:최종로그인시간, lgip:로그인ip, lgfirst : 최초로그인
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateLoginData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			//최초 로그인 체크
			int cnt = loginDAO.selectLoginCount(commandMap);
			if( cnt > 0 ){
				commandMap.put("loginFirstYn", "Y");
				
				
				//로그인정보 재등록
				//loginDAO.deleteLoginId(commandMap);
				
				
				//로그인 대상자 기록
				loginDAO.insertLoginLog(commandMap);
			}			
			//로그인 정보 수정
			loginDAO.updateLoginInfo(commandMap);
			//로그인 로그 기록
			loginDAO.insertLoginId(commandMap);
			
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 비밀번호 오류 회수 증가
	 * @param commandMap
	 * @param issuccess
	 * @return
	 * @throws Exception
	 */
	public int updateLoginFail(Map<String, Object> commandMap, String issuccess) throws Exception{
		int isOk = 1;
		try{
			commandMap.put("issuccess", issuccess);
			loginDAO.updateLoginFailCount(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 권한이 강사일 경우 강사 로그아웃 정보를 입력
	 * @param commandMap
	 * @param issuccess
	 * @return
	 * @throws Exception
	 */
	public int tutorLogout(Map<String, Object> commandMap) throws Exception{
		return loginDAO.tutorLogout(commandMap);
	}

	/**
     * 기존 비밀번호와 일치하는지를 개수로 가져온다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int userOldPwdCount(Map<String, Object> commandMap) throws Exception{
    	return loginDAO.userOldPwdCount(commandMap);
    }
    
    /**
     * 비밀번호와 비밀번호 변경일자를 업데이트 한다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int updateUserPasswdChange(Map<String, Object> commandMap) throws Exception{
    	return loginDAO.updateUserPasswdChange(commandMap);
    }

	public int actionCrtfctLogin(HttpServletRequest request,
			Map<String, Object> commandMap) throws Exception {
		int isOk = -1;
		String lgip = request.getRemoteAddr();
		
		//회원테이블의 epki_dn 컬럼에 인증키가 있는지 확인
		Map epkiMap = loginDAO.epkiDnYn(commandMap);
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ emtpyEpki    "+epkiMap);
		
		if(epkiMap != null){
			if(epkiMap.get("epkiDn") == null){
				System.out.println("!!!!!!!");
				return 99;
			}
		}else{
			System.out.println("%%%%%%%");
			return -1;
		}
		
		Map userInfo = loginDAO.loginCert(commandMap);
		
		if( userInfo != null ){
			isOk = 1;
			
			//나이스개인번호 
			String v_nicePersonalNum = (String)userInfo.get("nicePersonalNum");
			String v_empGubun = (String)userInfo.get("empGubun");
			String v_deptCd = (String)userInfo.get("deptCd");
			
			if(v_nicePersonalNum == null){
				v_nicePersonalNum = "";
			}
			
			if(v_deptCd == null){
				v_deptCd = "";
			}
			
			if(v_empGubun.equals("T") && v_nicePersonalNum.equals("")){
				isOk = 98;
			}else if(v_empGubun.equals("E") && v_deptCd.equals("")){
				isOk = 97;
			}
			
			if(v_empGubun.equals("E") && v_deptCd.equals("")){
				isOk = 97;
			}
			
			//세션셋팅
			userInfo.put("lgip", lgip);
			this.setSession(request, userInfo);
			
			String v_gubun = (String)commandMap.get("tem_grcode");
			if (v_gubun == null || v_gubun.equals(""))   v_gubun = "N000001";
			String v_userid = (String)commandMap.get("userId");
	        String v_year  = new SimpleDateFormat("yyyy").format(new Date() );
	        String v_month = new SimpleDateFormat("MM").format(new Date() );
	        String v_day   = new SimpleDateFormat("dd").format(new Date() );
	        String v_time  = new SimpleDateFormat("HH").format(new Date() );
			String v_week  = "";
			Calendar calendar=Calendar.getInstance();
	        calendar.set(Integer.parseInt(v_year),Integer.parseInt(v_month)-1,Integer.parseInt(v_day));
	        v_week=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)); // 1(일),2(월),3(화),4(수),5(목),6(금),7(토)
	        
	        Map inputMap = new HashMap();
	        inputMap.put("gubun", v_gubun);
	        inputMap.put("userid", v_userid);
	        inputMap.put("year", v_year);
	        inputMap.put("month", v_month);
	        inputMap.put("day", v_day);
	        inputMap.put("time", v_time);
	        inputMap.put("week", v_week);
	        
	        // 접속정보 변경
			if( loginLogDAO.selectLoginCount(inputMap) > 0 ){
				loginLogDAO.updateLogCount(inputMap);
			}else{
				loginLogDAO.insertLogCount(inputMap);
			}
			commandMap.put("lgip", lgip);
			commandMap.put("userNm", userInfo.get("name"));
			commandMap.put("comp", userInfo.get("comp"));
			this.updateLoginData(commandMap);
		}
		
		return isOk;
	}

	public String selectUserIdNo(Map<String, Object> commandMap)
			throws Exception {
		// TODO Auto-generated method stub
		return loginDAO.selectUserIdNo(commandMap);
	}

	public int updateDn(Map<String, Object> commandMap) throws Exception {
		return loginDAO.updateDn(commandMap);
	}

	public int checkDormantYn(Map<String, Object> commandMap) throws Exception {
		return loginDAO.checkDormantYn(commandMap);
	}
	
	
	public int actionCrtfctLoginHtml5(HttpServletRequest request,Map<String, Object> commandMap) throws Exception {
		int isOk = -1;
		String lgip = request.getRemoteAddr();
		
		//회원테이블의 epki_dn 컬럼에 인증키가 있는지 확인
		Map epkiMap = loginDAO.epkiDnYn(commandMap);
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ emtpyEpki    "+epkiMap);
		
		if(epkiMap != null){
			if(epkiMap.get("epkiDn") == null){
				System.out.println("!!!!!!!");
				return 99;
			}
		}else{
			System.out.println("%%%%%%%");
			return -1;
		}
		
		Map userInfo = loginDAO.loginCert(commandMap);
		
		if( userInfo != null ){
			isOk = 1;
			
			//나이스개인번호 
			String v_nicePersonalNum = (String)userInfo.get("nicePersonalNum");
			String v_niceNumAllowYn = (String)userInfo.get("niceNumAllowYn");
			//비밀번호변경기간이 지난 학습자임(Y : 3개월이 지남 / N : 3개월안됨)
			String v_pwdchangeyn = (String)userInfo.get("pwdchangeyn");			
			String v_empGubun = (String)userInfo.get("empGubun");
			String v_isretire = (String)userInfo.get("isretire");
			String v_deptCd = (String)userInfo.get("deptCd");
			String v_deptCnt = (String)userInfo.get("deptCnt");
			String v_agencyCnt = (String)userInfo.get("agencyCnt");
			String v_curDate5min = (String)userInfo.get("curDate5min");
		
			String v_jobCd = userInfo.get("jobCd") !=null ? (String)userInfo.get("jobCd") : "" ;
		
			int v_duptCnt = Integer.parseInt(userInfo.get("duptCnt")+"");		//아이디 중복 
			int v_ziplen = Integer.parseInt(userInfo.get("ziplen")+"");		//우편번호 자리수

			if(v_nicePersonalNum == null){
				v_nicePersonalNum = "";
			}
			
			if(v_deptCd == null){
				v_deptCd = "";
			}
			
			if(v_empGubun.equals("T") && v_nicePersonalNum.equals("")){
				isOk = 98;
			}else if(v_empGubun.equals("E") && v_deptCd.equals("")){
				isOk = 97;
			}
			
			if(v_empGubun.equals("E") && v_deptCd.equals("")){
				isOk = 97;
			}
			if ( v_isretire.equals("Y") ){
				isOk = -6; //탈퇴회원
			}else{
				isOk = 1; //로그인성공
				
				if (v_nicePersonalNum == null)
				{
					v_nicePersonalNum = "";
				}
				if (v_deptCd == null)
				{
					v_deptCd = "";
				}
				
				if (v_duptCnt > 1)
				{
					isOk = 990;
				//교원이고 나이스개인번호를 입력하지 않은 경우 개인정보수정 페이지로 보냄
				}
				else if (v_empGubun.equals("T") && v_nicePersonalNum.equals("") && v_niceNumAllowYn.equals("N") && !v_jobCd.equals("00039"))
				{
					isOk = 998;
				}
				else if ((v_empGubun.equals("T") || v_empGubun.equals("E")) && (v_deptCd.equals("") || !v_deptCnt.equals("Y") || !v_agencyCnt.equals("Y")) )
				{
					/**
					 * 교원 : T, 보조인력 : E, 교육전문직 : R, 일반회원 : P
					 * 교원, 교육전문직인 경우는 depthCnt(이전 상위교육청코드 개수), agencyCnt(이전 하위교육청코드 개수) 가 0개가 아니라면 개인정보페이지로 보낸다.
					 */
					isOk = 997;
				}
				else
				{
					//비밀번호변경이 3개월이 지나서 변경페이지로 보내야 하는 사용자임
					if (v_pwdchangeyn != null)
					{
						if (v_pwdchangeyn.equals("Y"))
						{
							isOk = 999;
						}
					}
				}
				
				//우편번호 자리수
				if (v_ziplen >= 6) isOk = 1000;
				if(v_ziplen == 0) isOk = 1100;
				
				System.out.println("@@@@@@@@@@@@@@@@@@@@@ is Ok " + isOk);
				//세션셋팅
				userInfo.put("lgip", lgip);
				this.setSession(request, userInfo);
				
				String v_gubun = (String)commandMap.get("tem_grcode");
				if (v_gubun == null || v_gubun.equals(""))   v_gubun = "N000001";
				String v_userid = (String)commandMap.get("userId");
		        String v_year  = new SimpleDateFormat("yyyy").format(new Date() );
		        String v_month = new SimpleDateFormat("MM").format(new Date() );
		        String v_day   = new SimpleDateFormat("dd").format(new Date() );
		        String v_time  = new SimpleDateFormat("HH").format(new Date() );
				String v_week  = "";
				Calendar calendar=Calendar.getInstance();
		        calendar.set(Integer.parseInt(v_year),Integer.parseInt(v_month)-1,Integer.parseInt(v_day));
		        v_week=String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)); // 1(일),2(월),3(화),4(수),5(목),6(금),7(토)
		        
		        Map inputMap = new HashMap();
		        inputMap.put("gubun", v_gubun);
		        inputMap.put("userid", v_userid);
		        inputMap.put("year", v_year);
		        inputMap.put("month", v_month);
		        inputMap.put("day", v_day);
		        inputMap.put("time", v_time);
		        inputMap.put("week", v_week);
		        
		        // 접속정보 변경
				if( loginLogDAO.selectLoginCount(inputMap) > 0 ){
					loginLogDAO.updateLogCount(inputMap);
				}else{
					loginLogDAO.insertLogCount(inputMap);
				}
				commandMap.put("lgip", lgip);
				commandMap.put("userNm", userInfo.get("name"));
				commandMap.put("comp", userInfo.get("comp"));
				this.updateLoginData(commandMap);
				this.updateLoginFail(commandMap, "Y");
			}			
		}
		
		return isOk;
	}
	
    
}
