package egovframework.com.job.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.com.snd.controller.SendSmsMailManageController;
import egovframework.com.snd.service.SendSmsMailManageService;

public class SchedulerTask
{
	
	/** sendMailManageService */
	@Resource(name = "sendSmsMailManageService")
	SendSmsMailManageService sendSmsMailManageService;
	
	
	/** memberSearchService */
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	

	public void printMessage() {
		//System.out.println("Struts + Spring + Quartz integration example ~");

		
		/*
		 * 
		 * 탈퇴 회원이 아니면서 1년 이상 로그인 하지 않은 회원 조회
		1년이 지난 사람 메일 발송
		
		
		1년 3개월 지난 계정 휴면 처리
		
		2년3개월 지난 계쩡 삭제 처리
		*/
		
		
		
		Map<String, Object> commandMap = new HashMap();
		commandMap.put("p_title", "[국립특수교육원부설원격교육연수원] [$name$] 선생님. 국립특수교육원 원격교육연수원입니다.선생님 ID가 휴면상태로 전환 될 예정입니다. ");
		String str="";
		str +="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
		str +="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		str +="		<head> ";
		str +="<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
		str +="<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">";
		str +="<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0\">";
		str +="<title>휴면계정 안내 메일</title>";
		str +="</head>";
		str +="	<body width=\"700\" height=\"990\" style=\"margin:0; padding:0;\">";
		str +="		<table width=\"700\" height=\"990\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; font-family:'dotum'; border:1px solid #315192; background:url(http://iedu.nise.go.kr/images/mail/mail_bg.jpg) bottom no-repeat;\">";
		str +="			<tr>";
		str +="				<td width=\"700\" height=\"472\" colspan=\"3\" style=\"background-color:#315192;\">";
		str +="					<img width=\"700\" height=\"472\" src=\"http://iedu.nise.go.kr/images/mail/mail_top.jpg\" alt=\"휴면계정 안내 메일, 국립특수교육원 부설원격교육연수원 CI, 국립특수교육원부설원격교육연수원의 선생님 ID가 휴면상태로 전환 될 예정입니다. 안녕하세요. 국립특수교육원부설원격교육연수원입니다. 정보통신망 이용촉진 및 정보보호 등에 관한 법률 제29조 2항 동법 시행령 제 16조에 의거하여 원격교육연수원 서비스(웹 또는 앱)의 이용 내영이 1년 이상 없는 회원 계정은 휴면상태로 전환되어 개인정보를 별도 보관처리함을 알려드립니다. 이를 원치 않으실 경우 국립특수교육원부설원격교육연수원 홈페이지(http://iedu.nise.go.kr)에 접속하여 로그인해주시기 바랍니다. ※ 3개월 동안 로그인 하지 않을시, 휴면계정으로 전환됩니다. 휴면계정으로 전환 시 본인확인 절차가 필요합니다.\" border=\"0\" usemap=\"#Map2\" />";
		str +="					<map name=\"Map2\" id=\"Map2\">";
		str +="					 <area shape=\"rect\" coords=\"146,377,310,397\" href=\"http://iedu.nise.go.kr\" target=\"_blank\" alt=\"국립특수교육원부설원격교육연수원 홈페이지\" />";
		str +="					</map>";
		str +="				</td>";
		str +="			</tr>";
		str +="			<tr>";
		str +="				<td width=\"48\" height=\"129\" style=\"background-color:#315192; padding:0; margin:0;\"></td>";
		str +="				<td width=\"604\" height=\"89\" style=\"background-color:#315192; letter-spacing:-0.05em;\">";
		str +="					<table width=\"604\" height=\"89\" style=\"font-family:'dotum'; font-weight:700;\">";
		str +="						<tr height=\"35\">";
		str +="							<td height=\"35\" style=\"color:#c9d9fb; font-size:20px; vertical-align:bottom;\">휴면 전환(분리 보관) 대상 계정 : [$userid$]</td>";
		str +="						</tr>";
		str +="						<tr height=\"42\">";
		str +="							<td height=\"42\" style=\"color:#fff; font-size:16px;\">";
		str +="								<ul height=\"42\" style=\"list-style-type:disc;\">";
		str +="									<li height=\"21\">휴면전환일 : [$rdate$]</li>";
		str +="									<li height=\"21\">분리보관 개인정보 : 해당 계정 연수이력</li>";
		str +="								</ul>";
		str +="							</td>";
		str +="						</tr>";
		str +="					</table>";
		str +="				</td>";
		str +="				<td width=\"48\" height=\"129\" style=\"background-color:#315192; padding:0; margin:0;\"></td>";
		str +="			</tr>";
		str +="			<tr>";
		str +="				<td width=\"700\" height=\"256\" colspan=\"3\">";
		str +="					<img width=\"700\" height=\"256\" src=\"http://iedu.nise.go.kr/images/mail/mail_bottom.jpg\" alt=\"분리보관 후 이용 불가 서비스 안내 분리보관과 동시에 홈페이지 회원 전용 서비스 메뉴를 이용하실 수 없습니다. 1. 원격연수 수강신청 2. 성적 확인 및 연수내역확인 3. 수강생 서비스(이수증·영수증 출력 등) 4. 아이디, 비밀번호 찾기 - 별도 보관 처리를 원하지 않으실 경우 휴면 전환 예정일 이전까지 국립특수교육원부설원격교육연수원 홈페이지(http://iedu.nise.go.kr)에 1회 이상 로그인 하시면 개인정보 별도  보관처리 대상에서 제외됩니다. - 휴면 아이디는 전환된 일자로부터 로그인 후 본인인증을 통해 바로 재사용 가능합니다.\" />";
		str +="				</td>";
		str +="			</tr>";
		str +="			<tr>";
		str +="				<td width=\"700\" height=\"54\" colspan=\"3\">";
		str +="					<img width=\"700\" height=\"54\" src=\"http://iedu.nise.go.kr/images/mail/mail_footer.jpg\" alt=\"본 이메일은 휴면계 전환 대상인 회원님께만 발송되는 발신전용 메일입니다. 문의사항은 연수관련 문의 게시판을 이용해 주시기 바랍니다.\" border=\"0\" usemap=\"#Map\" />";
		str +="					<map name=\"Map\" id=\"Map\">";
		str +="					  <area shape=\"rect\" coords=\"261,28,369,43\" href=\"http://iedu.nise.go.kr\" target=\"_blank\" alt=\"연수원관련 문의 게시판\" />";
		str +="					</map>";
		str +="				</td>";
		str +="			</tr>";
		str +="			<tr>";
		str +="				<td width=\"700\" height=\"79\" colspan=\"3\">";
		str +="					<img width=\"700\" height=\"79\" src=\"http://iedu.nise.go.kr/images/mail/mail_address.jpg\" alt=\"국립특수교육원 부설원격교육연주원 주소 : 충남 아산시 배방읍 공원로 40 Tel : 041 537 1475 Fax : 041 537 1473 고유번호 : 134-83-01004 (국립특수교육원 부설 원격교육연수원) 대표자 : 우이구 COPYRIGHT(C)2010 국립특수교육원 All rights reserved\" border=\"0\" />";
		str +="				</td>";
		str +="			</tr>";
		str +="	    </table>";
		str +="	</body>";
		str +="	</html>";

		
		commandMap.put("p_content", str);
		commandMap.put("pGubun", "dormant");

		int DormantCnt = 0;
		
		try {
			
			
			//탈퇴 회원이 아니면서 1년 이상 로그인 하지 않은 회원 조회 1년이 지난 사람 메일 발송
			DormantCnt  = memberSearchService.selectDormantCnt(commandMap);
			
			if(DormantCnt > 0 )
			{
				// Dormant N(1년 안되면) -> Y(1년되고 메일 발송) -> E(1년 3개월)
				// 메일 발송
				sendSmsMailManageService.insertCloseUserSendMail(commandMap);
				
				// 메일 발송 대상 메일 발송 send
				memberSearchService.updateDormantYn(commandMap);
			}
			// 1년 3개월 된 휴면 계정 전환으로 E로 update
			// Y N 일때는 업무 이용가능
			// Y 이면서 메일 발송한지 3개월 된 경우 last login이 바뀔수 있음
			memberSearchService.updateDormantYnE(commandMap);
			
			// 휴면 계정이 된지 1년된 경우 탈퇴 기능 현재 ISRETIRE 만 업데이트 쳐 준다. 
			//memberSearchService.updateUserDelYn(commandMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}