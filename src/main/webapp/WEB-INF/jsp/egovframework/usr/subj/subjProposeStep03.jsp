<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page errorPage = "/code500.jsp" %>
<%@ page import="javax.crypto.*, org.apache.xerces.impl.dv.util.HexBin,javax.crypto.spec.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap" %>
<%@ page import = "com.ziaan.library.*" %>

<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonPopUpHead.jsp" %>

<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->



<% 
	//실거래 상점아이디
	//String mid = "believe2024";	//pwd : zerodea70
	//String mertKey = "a126743d464c0e9bdcfa3f8066054106"; //상점암호키
	
	
	String mid = "kniseiedu";	//pwd : knise1477
	String mertKey = "072c17c58fb48418b2c296ec630ab203"; //상점암호키

	//개발서버인지 체크하기 위한 변수
	boolean isDevChecker = false;
	
	String reqHost = request.getServerName();
	//String reqHost = request.getLocalAddr().toLowerCase();
	//String serverPort = request.getLocalPort() + "";

	//국립특수교육원이 아니면 테스트 서버로 결제한다.
	if(reqHost.indexOf("iedu.nise.go.kr") == -1)
	{
		isDevChecker = true;
	}
	
	 isDevChecker = false;
	 
	//결제아이디 생성
	EgovMap orderinfo = (EgovMap)request.getAttribute("orderinfo");
	//과정정보
	List subjinfo = (List)request.getAttribute("subjinfo");
	//회원정보
	EgovMap meminfo = (EgovMap)request.getAttribute("meminfo");
	
	System.out.println("##################################            "+reqHost.indexOf("iedu.nise.go.kr"));
	System.out.println("##################################            "+isDevChecker);
	
	//주문번호
	String oid = orderinfo.get("orderId") + "";
	
	//가격
	String amount = "0";
	
	int v_amount = 0 ;
	
	//과정명
	String v_subjnm = "";
	
	for(int i=0; i<subjinfo.size(); i++)
	{
		Map mm = (Map)subjinfo.get(i);
		
		v_amount += Integer.parseInt(mm.get("biyong") + "");
		
		if( i == 0 ) {
			if(mm.get("course") != null && !mm.get("course").equals("000000"))
			{
				v_subjnm = "(패키지) " + mm.get("coursenm") + "";
			}
			else
			{
				v_subjnm =  mm.get("sujseqname")+ " " + mm.get("subjnm") + "";
			}
		}
	}
	
	
	amount = v_amount+"";
	
	
	//주민번호
	String resno = meminfo.get("resnoall") + "";
	//out.println(resno);
	
	String p_subj = request.getAttribute("p_subj") + "";
	String p_year = request.getAttribute("p_year") + "";
	String p_subjseq = request.getAttribute("p_subjseq") + "";
	
	String [] arr_lec_sel_no = (String [])request.getParameterValues("p_lec_sel_no");
	
	String p_hrdc2 = request.getAttribute("p_hrdc2") + "";
	String p_post1 = request.getAttribute("p_post1") + "";
	String p_post2 = request.getAttribute("p_post2") + "";
	String p_pay_sel = request.getAttribute("p_pay_sel") + "";
	String p_enterance_dt = request.getAttribute("p_enterance_dt") + "";
	String p_jik = request.getAttribute("p_jik") + "";
	String p_comp = request.getAttribute("p_comp") + "";
	String p_is_attend = request.getAttribute("p_is_attend") + "";
	
	
	String p_lec_sel_no = "";
	
	if(arr_lec_sel_no != null)
	{
		for(int i=0; i<arr_lec_sel_no.length; i++)
		{
			p_lec_sel_no += arr_lec_sel_no[i] + "Ω";
		}
	}
	
	//out.println(p_lec_sel_no);
	
	String url = "" 
	+ "?p_subj=" + p_subj 
	+ "&p_year=" + p_year
	+ "&p_subjseq=" + p_subjseq 
	+ "&p_lec_sel_no=" + java.net.URLEncoder.encode(p_lec_sel_no, "UTF-8") 
	+ "&p_hrdc2=" + p_hrdc2
	+ "&p_post1=" + p_post1 
	+ "&p_post2=" + p_post2 
	+ "&p_pay_sel=" + p_pay_sel 
	+ "&p_enterance_dt=" + p_enterance_dt 
	+ "&p_comp=" + p_comp 
	+ "&p_is_attend=" + p_is_attend 
	//+ "&p_pay_sel=" + p_pay_sel 
	;
	
	
	
	//String note_url = "";
	String note_url = "http://iedu.nise.go.kr/learn/pg/note_url.jsp" + url;//결제결과 데이타처리URL(웹전송연동방식)
	
	//"http://iedu.nise.go.kr/servlet/controller.propose.ProposeCourseServlet?p_process=SubjectEduPropose"+url;//결제결과 데이타처리URL(웹전송연동방식)
	String ret_url = "";
		
	
	//개발모드에서는 테스트 서버로 보낸다.
	if (isDevChecker) {
		mid = "tkniseiedu"; //상점아이디 - 테스트상점 : pwd : tbelieve2024
		
		//note_url = "http://localhost:8083/learn/pg/note_url.jsp" + url;//결제결과 데이타처리URL(웹전송연동방식)
	}	
	
	
	
	System.out.println(oid + "<br/>");
	System.out.println(amount + "<br/>");
	System.out.println(resno + "<br/>");
	System.out.println(reqHost.indexOf("iedu.nise.go.kr") + " / " + isDevChecker);
	//System.out.close();
	

	
	System.out.println("****************************************************************************");
	System.out.println("* note_url::" + note_url);
	System.out.println("****************************************************************************");

	
	
	
	
	
	
	byte[] key = null;
	DESKeySpec spec = null;
	SecretKeyFactory factory = null;
	SecretKey secret = null;

	key = HexBin.decode(mertKey);
	spec = new DESKeySpec(key);
	factory = SecretKeyFactory.getInstance("DES");
	secret = factory.generateSecret(spec);

	DesEncrypter encrypter = new DesEncrypter(secret);

	//결제테스트용 개발자로 로그인시...
	if(session.getAttribute("userid").equals("admin")) 
	{
		amount = "200";
	}

	//암호화
	String encrypted_amount = encrypter.encrypt(amount);
	String encrypted_note_url = encrypter.encrypt(note_url);
	String encrypted_buyerssn = encrypter.encrypt(resno);
	
	//////////////////////////////md5///////////////////////////////////////////////
	StringBuffer sb = new StringBuffer();

	sb.append(mid);
	sb.append(oid);
	sb.append(amount);
	sb.append(mertKey);

	byte[] bNoti = sb.toString().getBytes();

	MessageDigest md = MessageDigest.getInstance("MD5");
	byte[] digest = md.digest(bNoti);

	StringBuffer strBuf = new StringBuffer();

	for (int i = 0; i < digest.length; i++) {
		int c = digest[i] & 0xff;
		if (c <= 15) {
			strBuf.append("0");
		}
		strBuf.append(Integer.toHexString(c));
	}

	String hashdata = strBuf.toString();
	//////////////////////////////md5//////////////////////////////////////////////
	
	if(oid == null || oid.equals("") || resno == null || resno.equals("") )
	{
		out.println("<script>");
		out.println("window.onload = function () { alert('죄송합니다. 정상적으로 주문번호(주민번호)가 생성되지 않았습니다. 다시 시도하시기 바랍니다.'); window.close();}");
		out.println("</script>");
		out.close();
	}
	
	
%>


</head>
	

	
<body style="background:none;">

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
		<input type="hidden"  name="p_subj" value="${p_subj}" />
		<input type="hidden"  name="p_year" value="${p_year}" />
		<input type="hidden"  name="p_subjseq" value="${p_subjseq}" />
		<c:forEach var="sel_no" items="${p_lec_sel_no}">
 				<input type="hidden"  name="p_lec_sel_no" value="${sel_no}" />
        </c:forEach>
		
<!--		<input type="hidden"  name="p_lec_sel_no" value="${p_lec_sel_no}" />-->
		<input type="hidden"  name="p_hrdc2" value="${p_hrdc2}" />
		<input type="hidden"  name="p_post1" value="${p_post1}" />
		<input type="hidden"  name="p_post2" value="${p_post2}" />
		<input type="hidden"  name="p_address1" value="${p_address1}" />
		<input type="hidden"  name="p_pay_sel" value="${p_pay_sel}" />
		<input type="hidden"  name="p_enterance_dt" value="${p_enterance_dt}" />
		
		<input type="hidden"  name="p_order_id" value="${orderinfo.orderId}" />
		<input type="hidden"  name="p_amount" value="<%=amount %>" />
		<input type="hidden"  name="p_jik" value="${meminfo.jik}" />
		<input type="hidden"  name="p_comp" value="${meminfo.comp}" />
		<input type="hidden"  name="p_is_attend" value="${p_is_attend}" />
		<input type="hidden"  name="p_addrFlag" value="${p_addrFlag}" />
		
		<input type="hidden"  name="p_usebook_yn" value="${p_usebook_yn}" />
		
<div id="money"><!-- 팝업 사이즈 800*650 -->
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
			<h2>수강신청</h2>
       		</div>
			<!-- //header -->








<!-- contents -->
			<div class="mycon">				
            	
              <!-- 과정타이틀 -->
              <div class="bbsList3">
				<ul>
					<li>
						<dl>
							<dt class="subject">
                            	<span class="head">강의명</span>
								<span class="txt" style="font-weight:bold;color:blue;"><%=v_subjnm %></span>	                              
                          </dt>
                            <dd class="info">
								<span class="head">연수비</span>
								<span class="txt">
									<c:choose>
										<c:when test="${p_ischarge eq 'S'}">
						           			교육청일괄납부						             
						           		</c:when>
						           		<c:otherwise>
						           			<fmt:formatNumber value="<%=amount %>" type="number"/>원												
						           		</c:otherwise>
					           		</c:choose>
				           		</span>
							</dd> 
                            <dd class="info">
								<span class="head">결제방식</span>
								<span class="txt">
								
								<c:choose>
					           	<c:when test="${p_ischarge eq 'C'}">
					           		<input type="radio" name="p_pay_sel" value="PB"  <c:if test="${p_pay_sel eq 'PB'}">checked</c:if> class="vrM" disabled/>무통장
					               	<input type="radio" name="p_pay_sel" value="OB"  <c:if test="${p_pay_sel eq 'OB'}">checked</c:if> class="vrM" disabled/>교육청일괄납부  
					               	<input type="radio" name="p_pay_sel" value="SC0030"  <c:if test="${p_pay_sel eq 'SC0030'}">checked</c:if> class="vrM" disabled/>실시간 계좌이체 
					               	<input type="radio" name="p_pay_sel" value="SC0010"  <c:if test="${p_pay_sel eq 'SC0010'}">checked</c:if> class="vrM" disabled/>카드결제
					           	</c:when>
					           	<c:when test="${p_ischarge eq 'S'}">
					               	<input type="radio" name="p_pay_sel" value="OB"  checked disabled/>교육청일괄납부  
					           	</c:when>
					           	<c:otherwise>
					           		무료
					           		<input type="radio" name="p_pay_sel" value="FE" checked disabled style="display:none;"/>
					           	</c:otherwise>
					           </c:choose>
									
                                </span>
							</dd>                             
					  </dl>
                        
				  </li>					
				</ul>
			</div>
            <!-- //과정타이틀 -->  
             
            	<c:if test="${p_ischarge ne 'S'}">
            		<c:if test="${p_pay_sel eq 'PB' || p_pay_sel eq 'SC0030'}">
		              <div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
		                    <h4>무통장 입금 계좌안내</h4>
		              </div> 
		              <ul class="course_1">
		                	<li>은행명 : 우체국</li>                	
		                    <li>계좌번호 : 301622-01-000041</li>
		                    <li>예금주 : 국립특수교육원</li>                    
		                    <li class="last_1">※ <!-- 무통장 입금하신 후에 고객센터<strong> (입금확인 요청) 게시판</strong>에 글을 남겨 주시면 빠른 <strong>수강승인</strong>이 가능합니다. -->
		                    무통장입금은 <strong>수강신청인과  입금인의 성함이 다를 경우</strong>, <br/>
		                    연수행정코너 입금확인요청 게시판에 글을 남겨주시면 빠른 수강승인 가능합니다.
		                    </li>  
		                    <li class="last_1">
		                    	※ <strong>무통장입금, 가상계좌</strong>의 경우 수강신청 기한 내 입금하셔야 합니다.<br />
		                        ※ <strong>수강승인</strong> 후 강의 시작일에 맞추어서 강의를 수강하십시오.<br />
		                        ※ <span class="font_red">시도교육청의 요청에 의한 연수는 교육청에서 연수비를 일괄납부</span>하므로 입금하실 필요 없습니다.
		                    </li>                   
		              </ul>  
	            	</c:if>
              	</c:if>
              
              <div class="sub_text" style="border-bottom:1px solid #e2e2e2;">
                    <h4>결제방법에 따른 환불방법</h4>
              </div> 

                	 <table summary="사이버 일반연수, 사이버 직무연수, 반환금액으로 구성" cellspacing="0" width="95%" border="1">
                        	<caption>결제방법에 따른 환불방법</caption>
                            <colgroup>
                                <col width="25%" />
                                <col width="75%" />                                
                            </colgroup>
                            <thead>
                                <tr bgcolor="#EFEFEF">
                                    <th scope="col" height="30">입금방식</th>
                                    <th scope="col" class="last">환불방법</th>                                    
                                </tr>
                            </thead>
                            <tbody>					
                                <tr>
                                    <td scope="col" style="text-align:center;">무통장</td>						
                                    <td class="left last">
                                    	<!-- 환불 요청 시 남겨주신 계좌번호로 환불해드립니다. 
										환불은 일괄지급이어서 3주 이상 걸리오니 이점 양해부탁드립니다. -->
										매월 1일 ~ 15일 입금 건은 그 달 말일 경,<br/>
  매월 16일 ~말일까지 입금 건은 다음 달 말일 경<br/>
  환불 요청시 남겨주신 계좌로 환불해 드립니다.<br/>
									</td>                                    
                                </tr> 
                                <tr>
                                    <td scope="col" style="text-align:center;">신용카드</td>						
                                    <td class="left last">
                                    결제 대금이 청구되지 않도록 신용카드 승인을 취소합니다. 카드결제 승인 취소 확인은 취소 요청 7일 후 카드사에 문의해주세요.
                                    </td>                                    
                                </tr>
                                <tr>
                                    <td scope="col" style="text-align:center;">실시간 계좌이체</td>						
                                    <td class="left last">
                                    결제 대금이 자동입금 될 수 있도록 실시간 계좌이체 취소합니다. 취소 후 계좌로 입금은 은행영업일 기준 3~4일 소요 됩니다.
                                    </td>                                    
                                </tr>                                               	
                            </tbody>
                        </table>
                	
              <ul class="course_1">
                    <li class="last_1">
                    	※  반환기준은 실 입금액에서 기본경비, 수수료 등을 제외한 범위로 한다.
                    </li>                   
              </ul>  
              
              
                       
           
              
              <!-- button -->
              <ul class="btnCen mrt20">
<!--              무통장/일괄납부시-->
              <c:if test="${p_pay_sel eq 'PB' || p_pay_sel eq 'OB' || p_pay_sel eq 'FE'}">
                <li><a href="#" class="pop_btn01" onclick="whenPBOBSubmit()"><span>수강신청하기</span></a></li>    
              </c:if>
<!--              카드/계좌이체-->
              <c:if test="${p_pay_sel eq 'SC0010' || p_pay_sel eq 'SC0030' || p_pay_sel eq 'SC0040'}">
                <li><a href="#" class="pop_btn01" onclick="openWindow()"><span>결제하기</span></a></li>
                 <li><a href="#" class="pop_btn01" onclick="window.close()"><span>닫 기</span></a></li>    
              </c:if>
                          
              </ul>
              <!-- // button -->
                
                
		  </div>
			<!-- //contents -->
			
			
			
			
			
			
			
		</div>
	</div>
	
<!-- 페이지 정보 -->
	<%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
	<!-- 페이지 정보 -->
</div>
<!-- // wrapper -->

</form>
<!--아래는 팝업과 뷰를 따로 스크립트를 프로그램한다.-->
<script type="text/javascript">
<!--
var frm = eval('document.<c:out value="${gsMainForm}"/>');

//무통장이나 교육청 일괄일때만 적용됨.(카드등은 다른 로직탐)
function whenPBOBSubmit()
{
	frm.action = "/usr/subj/subjProposeStepAction.do";
	frm.target = "_self";
	frm.submit();
}
//-->
</script>





<!-- 결제관련 히든값 -->
<form id="pgMainForm" name="pgMainForm" method="post" action="" onsubmit="return false;" accept-charset="euc-kr">
	
<!--	 결제를 위한 필수 hidden정보 -->
	<input type="hidden" name="hashdata" value="<%=hashdata%>" /> 
	
<!--	 결제요청 검증(무결성) 필드-->
	<input type="hidden" name="mid" value="<%=mid%>" /> 
	
<!--	 상점ID http://elearn.kise.go.kr -->
	<input type="hidden" name="oid" value="<%=oid%>" /> 
	
<!--	 가격 -->
	<input type="hidden" name="encrypted_amount" value="<%=encrypted_amount%>" />
	 
<!--	 팝업창 사용: 리턴URL  -->
	<input type="hidden" name="note_url" value="<%=note_url%>" /> 
	<input type="hidden" name="ret_url" value="<%=ret_url%>" /> 
	

	<input type="hidden" name="buyer" value="${meminfo.name}" /> 
	
	<c:set var="productinfo"><%=v_subjnm%></c:set>
<!--	 구매자  -->
	<input type="hidden" name="productinfo" value="${fn2:getFixTitle(productinfo, 30)}" /> 
<!--	 상품명 -->
	<input type="hidden" name="encrypted_note_url" value="<%=encrypted_note_url%>" /> 
<!--	 결제결과 데이타처리URL(웹전송연동방식) -->
	

<!--	 결제결과 데이타처리URL(가상계좌용)  -->
	<input type="hidden" name="encrypted_fail_url" value="<%=encrypted_note_url%>" /> 
	
<!--	 결제결과 데이타처리URL(가상계좌용) -->
<!--	 주민번호  -->
	<input type="hidden" name="encrypted_buyerssn" value="<%=encrypted_buyerssn%>" /> 
	
<!--	 주민번호  -->
	<input type="hidden" name="encrypted_pid" value="<%=encrypted_buyerssn%>" />
	
<!--	 주문번호  -->
	<input type="hidden" name="encrypted_oid" value="<%=oid%>" />
	
<!--	 주문번호 - 가상계좌용  -->
<!--	 통계서비스를 위한 선택적인 hidden정보  -->
	<input type="hidden" name="producttype" value="온라인교육" /> 
	<input type="hidden" name="productcode" value="${p_subj}" /> 
	<input type="hidden" name="buyerid" value="${sessionScope.userid}" /> 
	<input type="hidden" name="buyerphone" value="${meminfo.handphone}" /> 
	<input type="hidden" name="buyeremail" value="${meminfo.email}" /> 
	<input type="hidden" name="buyeraddress" value="${p_address1}" /> 
	
	<input type="hidden" name="deliveryinfo" value="국립특수교육원" /> 
	<input type="hidden" name="receiver" value="${meminfo.name}" /> 
	<input type="hidden" name="receiverphone" value="${meminfo.handphone}" /> 
	
<!--	 할부개월 선택창 제어를 위한 선택적인 hidden정보 -->
	<input type="hidden" name="install_range" value="" /> 
	
<!--	 할부개월 범위-->
	<input type="hidden" name="install_fr" value="" /> 
	
<!--	 할부개월범위 시작-->
	<input type="hidden" name="install_to" value="" /> 
<!--	 할부개월범위 끝-->
	
<!--	 무이자 할부(수수료 상점부담) 여부를 선택하는 hidden정보  -->
	<input type="hidden" name="" value="선택무이자" /> 
	<input type="hidden" name="nointerest" value="0" /> 
	
<!--	 사용자 전송 hidden정보  -->
	<input type="hidden" name="formflag" value="N" /> 
		
</form>
<!-- 결제관련 히든값 -->


<script language="JavaScript" type="text/JavaScript">
<!--

function openWindow(){

		//alert("결제를 완료하셔야 수강신청이 완료됩니다.");
		//alert("${sessionScope.userid} ");
		var win = window.open("","Window","width=330, height=430, status=yes, scrollbars=no,resizable=yes, menubar=no");
		//카드결제
		<c:if test="${p_pay_sel eq 'SC0010'}">
			//테스트용 결제창 URL
			//document.mainForm.action="http://pg.dacom.net:7080/card/cardAuthAppInfo.jsp";
			//서비스용 결제창 URL
			<%if (isDevChecker) {%>
				document.pgMainForm.action="http://pg.dacom.net:7080/card/cardAuthAppInfo.jsp";
			<%} else {%>
				document.pgMainForm.action="http://pg.dacom.net/card/cardAuthAppInfo.jsp";
			<%}%>
		</c:if>	
		
		//계좌이체
		<c:if test="${p_pay_sel eq 'SC0030'}">
			//테스트용 결제창 URL
			//document.mainForm.action="http://pg.dacom.net:7080/transfer/transferSelectBank.jsp";				 
		    //서비스용 결제창 URL	
		    <%if (isDevChecker) {%>
				document.pgMainForm.action="http://pg.dacom.net:7080/transfer/transferSelectBank.jsp";
			<%} else {%>
				document.pgMainForm.action="http://pg.dacom.net/transfer/transferSelectBank.jsp";
			<%}%>
		</c:if>	
		
		//가상결제
		<c:if test="${p_pay_sel eq 'SC0040'}">
			//테스트용 결제창 URL	
			//document.mainForm.action="http://pg.dacom.net:7080/cas/casRequestSA.jsp";
			//서비스용 결제창 URL
			document.pgMainForm.action="http://pg.dacom.net/cas/casRequestSA.jsp";
		</c:if>	
	
	
		document.charset = 'euc-kr'; 
		document.pgMainForm.target = "Window";
		document.pgMainForm.submit();
	    document.charset = 'utf-8';

	    if(win != undefined)
	    {
		    window.self.close();
	    }
	    else
	    {
		    alert("팝업창을 활성화 해주세요!");
	    }
	
} 
document.title="수강신청 3단계("+"결제)"+" : 개설교육과정/신청";


//-->
</script>


</body>
</html>