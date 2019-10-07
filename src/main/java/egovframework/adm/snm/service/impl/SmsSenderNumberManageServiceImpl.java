package egovframework.adm.snm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import com.ziaan.library.Mailing;

import egovframework.adm.snm.dao.SmsSenderNumberManageDAO;
import egovframework.adm.snm.service.SmsSenderNumberManageService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.snd.dao.SendSmsMailManageDAO;
import egovframework.com.snd.service.SendSmsMailManageService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("smsSenderNumberManageService")
public class SmsSenderNumberManageServiceImpl extends EgovAbstractServiceImpl implements SmsSenderNumberManageService{
	
	/** log */
	protected static final Log log = LogFactory.getLog(SmsSenderNumberManageServiceImpl.class);
	
	@Resource(name="smsSenderNumberManageDAO")
    private SmsSenderNumberManageDAO smsSenderNumberManageDAO;

	/*
	 * 문자 발신자 전호 번호 수정
	 * */
	public int updateSendFromHandPhon(Map<String, Object> map) throws Exception {
		return smsSenderNumberManageDAO.updateSendFromHandPhon(map);
	}
	
	
	
	

}