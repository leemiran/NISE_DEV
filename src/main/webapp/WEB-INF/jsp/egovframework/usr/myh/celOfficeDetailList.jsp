<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="javax.crypto.*, org.apache.xerces.impl.dv.util.HexBin,javax.crypto.spec.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="egovframework.rte.psl.dataaccess.util.EgovMap" %>
<%@ page import = "com.ziaan.library.*" %>


<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--login check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>
<!--login check-->

<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>



<% 
	//결제정보
	EgovMap mm = (EgovMap)request.getAttribute("payView");

	
	String v_transactionid = "";
	//out.print(" <br/> v_transactionid : " + v_transactionid);
	//v_transactionid = "belie2010042613202427493";
	
	if(mm != null)
	{
		v_transactionid = (String)mm.get("transactionId");
	}
	
	String mid = "believe2024";     // 상점아이디
	String tid = v_transactionid;     // 거래번호   PA_PAYMENT 테이블의 TRANSACTION_ID 값을 넣어주어야 함
	String mertKey = "a126743d464c0e9bdcfa3f8066054106";     //데이콤에서 상점아이디별 발급 키값


	byte[] key = null;
	DESKeySpec spec = null;
	SecretKeyFactory factory = null;
	SecretKey secret = null;

////////////////////////////// 전송필드 암화화(DES) //////////////////////////////
	key = HexBin.decode(mertKey);
	spec = new DESKeySpec(key);
	factory = SecretKeyFactory.getInstance("DES");
	secret = factory.generateSecret(spec);
	
	DesEncrypter encrypter		= new DesEncrypter(secret);
////////////////////////////// 전송필드 암화화(DES) //////////////////////////////


//////////////////////////////md5///////////////////////////////////////////////
    StringBuffer sb = new StringBuffer();
    
    sb.append(mid);
    sb.append(tid);
	sb.append(mertKey);
	//sb.append("00");

    byte[] bNoti = sb.toString().getBytes();

    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();

    for (int i5=0 ; i5 < digest.length ; i5++) {
        int c = digest[i5] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String authdata = strBuf.toString();
//////////////////////////////md5//////////////////////////////////////////////


%>


<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>


<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
<fieldset>
<legend>학습 지도</legend>
<%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
	<input type="hidden" id="pageIndex" name="pageIndex"/>
	<input type = "hidden" name="p_userid"     value = "${sessionScope.userid}" />
	<input type = "hidden" name="p_subj" value = "${p_subj}" />
	<input type = "hidden" name="p_year" value = "${p_year}" />
	<input type = "hidden" name="p_subjseq" value = "${p_subjseq}" />






 <!-- search wrap-->
		<div class="sub_title">
			<ul>
            	<li>이수증은 강의 종료 후 수료인 경우만 이수증 출력 버튼이 보입니다.</li>
                <li>연수성적 증명서는 인터넷 발급이 되지 않습니다. 고객센터에 전화로 문의해 주십시요.</li>                
                <li>신용카드 결제인 경우 신용카드 영수증을 출력하실 수 있습니다.</li>
                <li>무통장입금, 가상계좌로 결재하신 경우 현금영수증이 발급됩니다. </li>
                <li>은행계좌로 직접 무통장 입급하신 경우 강의시작 14일 경과후에 현금영수증이 출력됩니다.(환불마감 후 발급)</li>
                <li>교육청 위탁과정, 특별과정 등 무료수강 하신경우 수강료 영수증 발급이 되지 않습니다. </li>
                <li>수강료를 직접 납부하셨는데 영수증이 출력안될 경우 고객센터-연수관련상담 게시판에 문의하시면 확인해 드립니다.</li>
            </ul>
		</div>
		<!-- // search wrap -->	
        <!-- text -->
        <div class="sub_text">            
             <h4>${view.subjnm} (${fn2:getFormatDate(view.edustart, 'yyyy.MM.dd')} ~ ${fn2:getFormatDate(view.eduend, 'yyyy.MM.dd')})</h4>
        </div>
        <!-- text -->	
		
<!-- list table-->
		<div class="studyList">
			<table summary="수료여부, 수강료, 연수지명번호, 출석고사장으로 구분" cellspacing="0" width="100%">
				<caption>학습지도</caption>
                <colgroup>
					<col width="15%" />
					<col width="25%" />
					<col width="20%" />					
					<col width="40%" />                    
				</colgroup>
				<thead>
					<tr>
						<th scope="col">수료여부</th>
						<th scope="col">수강료</th>
						<th scope="col">연수지명번호</th>						
						<th scope="col" class="last">출석고사장</th>                        
					</tr>
				</thead>
				<tbody>		
                    <tr>
						<td>
						<c:if test="${view.endyn eq 'Y'}">${view.isgraduatedValue}</c:if>
						<c:if test="${view.endyn ne 'Y'}">
							수강중
							<br/>
							<a href="#none" onclick="confirmPrint()">
							<img alt="확인증" src="/images/user/btn_identify.gif"/>
							</a>
						</c:if>
						</td>						
						<td><fmt:formatNumber value="${view.biyong}" type="number"/>원</td>
                        <td rowspan="2">
                        <input name="p_lec_sel_no" type="text" size="20" value="${view.lecselno}" title="연수지명번호"/>
                        </td>
						<td rowspan="2" class="last">
<!--							${view.newroom}/${view.isattend}-->
<!--							codeType은 과정에서 고사장에서는 선택되어진 학교의 리스트이다. 만약 없다면 전체를 가져온다. -->
                        	<ui:code id="p_is_attend" selectItem="${view.isattend}" gubun="schoolRoom" codetype="${view.newroom}"  upper="" title="출석고사장" className="" type="select" selectTitle="::선택::" event="" itemRowCount="1"/>
                        </td>                        
					</tr>  
                    <tr>
						<td>
<!--						학부모가 아니고 수료일시 출력-->
						<c:if test="${view.isgraduated eq 'Y'}">
							<a href="javascript:suRoyJeung()"><img src="/images/user/btn_1_042.gif" alt="수료증" /></a>
						</c:if>
<!--						${payView.type}-->
<!--						학부모가 아닌경우 영수증 출력-->
						<c:if test="${payView.type ne 'OB' && payView.type ne 'RE'}">
							<a href="#none" onclick="whenCashPrint()"><img src="/images/user/btn_y_print.gif" alt="영수증출력" /></a>
						</c:if>
						
						
						</td>						
					  <td>
					  <c:choose>
					  	<c:when test="${payView.type eq 'SC0010'}">
					  		<!-- 신용카드 -->
							<!-- authdata = md5(아이디+주문번호+martkey)  로 암호화를 위에서 실행함...주의) 암호화 전에 주문번호는 DB에서 불러와서 암호화 해야 함-->
					  		<a href="javascript:showReceiptByTID('believe2024', '<%=v_transactionid%>', '<%=authdata%>')" class="pop_btn01"><span>신용카드 영수증 출력</span></a>
					  	</c:when>
					  	<c:when test="${payView.type eq 'SC0020' or payView.type eq 'PB'}">
							<!-- 무통장 입급  -->
					  		<a href="javascript:showCashReceipts('believe2024','${payView.orderId}','','CR','service')" class="pop_btn01"><span>무통장입금 현금영수증 출력</span></a>
					  	</c:when>
					  	<c:when test="${payView.type eq 'SC0030'}">
							<!--계좌이체 -->
					  		<a href="javascript:showCashReceipts('believe2024','${payView.orderId}','','BANK','service')" class="pop_btn01"><span>계좌이체 현금영수증 출력</span></a>
					  	</c:when>
					  	<c:when test="${payView.type eq 'SC0040'}">
							<!--가상계좌 --> 
					  		<a href="javascript:showCashReceipts('believe2024','${payView.orderId}','','CAS','service')" class="pop_btn01"><span>가상계좌 현금영수증 출력</span></a>
					  	</c:when>
					  	<c:when test="${payView.type eq 'OB'}">
					  		<img src="/images/user/btn_s_1.gif">
					  	</c:when>
					  	<c:when test="${payView.type eq 'PB'}">
					  		<img src="/images/user/btn_s_2.gif">
					  	</c:when>
					  	<c:otherwise>
					  		<!--구사용자들중  구분,order_id값이 없는 사용자 영수증출력가능 / 무통장입금처리2010-05-19-->
					  		<img src="/images/user/btn_s_3.gif">
					  	</c:otherwise>
					  </c:choose>
					  
					  
					  </td>
                    </tr>   
                                                    	
				</tbody>
			</table>
		</div>        
		<!-- list table--> 
        <div class="sub_check">            
             <p>연수지명번호, 출석고사장을 선택하신 후 [저장]버튼을 꼭 클릭해주십시요.</p>
             <p>연수지명번호 입력형식 : 16개 광역시도명-학교명-당해연도 두자리-교내 연수번호 (예: 경기-경기초-12-055)</p>
        </div>     
        
        
        <!-- button -->
		<ul class="btnR">
        	<li><a href="#none" onclick="whenSave()"><img src="/images/user/btn_save.gif" alt="저장" /></a></li>
		</ul>      
		<!-- // button -->

	</fieldset>
</form>



        
<!--eCredit 영수증 연결 영역 시작  --> 
<script language="JavaScript" src="http://pgweb.dacom.net/WEB_SERVER/js/receipt_link.js" type="text/javascript"></script>     
<!--eCredit 영수증 연결 영역 종료 -->

<script type="text/javascript">
<!--

var thisForm = eval('document.<c:out value="${gsMainForm}"/>');


//수료증출력
function suRoyJeung(){
	window.open('', 'suRoyJeungPop', 'left=100,top=100,width=667,height=600,scrollbars=yes');
	thisForm.action = "/usr/myh/suRoyJeungPrint.do";
	thisForm.target = "suRoyJeungPop";
	thisForm.submit();
}



//확인증
function confirmPrint()
{
	window.open('', 'confirmPrintWiindowPop', 'left=100,top=100,width=688,height=600,scrollbars=yes');
	thisForm.action = "/usr/myh/selectUserReqPrint.do";
	thisForm.target = "confirmPrintWiindowPop";
	thisForm.submit();
}

//영수증
function whenCashPrint()
{
	window.open('', 'whenCashPrintWindowPop', 'left=100,top=100,width=467,height=600, menubar=no,directories=no,resizable=yes,status=no,scrollbars=yes');
	thisForm.action = "/usr/myh/selectUserCashPrint.do";
	thisForm.target = "whenCashPrintWindowPop";
	thisForm.submit();
}

//상태저장
function whenSave(){

	if(confirm('수정하시겠습니까?')){
	 	thisForm.action='/usr/myh/celOfficeDetailAction.do';
	  	thisForm.target = "_self";
	  	thisForm.submit();
	}
 }
//-->
</script>
        
        
        
        
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->
