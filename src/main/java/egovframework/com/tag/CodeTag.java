
package egovframework.com.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import egovframework.adm.cfg.cod.service.CodeManageService;

/**
 * <pre>
 * system      : 공통
 * menu        : 공통
 * source      : CodeTag.java
 * description : 첨부파일 태그
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2008-06-24 created by ?
 * 1.1	
 * </pre>
 */
@Component
@SuppressWarnings("unchecked")
public class CodeTag extends TagSupport {

    @Autowired(required=false)
	private CodeManageService codeManageService;
	
    /** CodeManageService */
//	@Resource(name = "codeManageService")
//    private CodeManageService codeManageService;

	private String gubun;
	private String selectItem;
	private String id;
	private String className;
	private String title;
	private String type;
	private String levels;
	private String condition;
	private String event;
	private String selectTitle;
	private String itemRowCount;
	private String disabled;
	
	private String codetype;
	private String upper;
	private String deleteItem;
	

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2269141048826089009L;

	public int doStartTag() throws JspException {
		if (gubun == null)
			gubun = "";
		if (selectItem == null)
			selectItem = "";
		if (id == null)
			id = "";
		if (className == null)
			className = "";
		if (title == null)
			title = "";
		if (type == null) 
			type = "select";
		if (levels == null) 
			levels = "";
		if (condition == null)
			condition = "";
		if (event == null)
			event = "";
		if (selectTitle == null)
			selectTitle = "";
		if (itemRowCount == null)
			itemRowCount = "";
		if (disabled == null)
			disabled = "";
		
		if (codetype == null)
			codetype = "";
		
		if (upper    == null)
			upper = "";
		if (deleteItem == null)
			deleteItem = "";

		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();

			String html = "";
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			if (type == "select")
				html += makeCodeSelect( request);
			else if (type == "radio")
				html += makeCodeRadio( request);
			else if (type == "check") 
				html += makeCodeCheck( request);
			else if (type == "text") 
				html += makeText( request);

			out.print(html);
		} catch (Exception e) {
			throw new JspTagException("I/O 예외 " + e.getMessage());
		}

		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		gubun = null;
		selectItem = null;
		id = null;
		className = null;
		title = null;
		type = null;
		condition = null;
		itemRowCount = null;
		levels = null;
		codetype = null;
		upper = null;
		deleteItem = null;
	}
	
	
	
	/**
	 * 종류별 가져올 코드의 리스트를 리턴한다.
	 * @param input
	 * @return
	 */
	public List<?> selectCodeList(Map input) throws Exception {

		List codeList = null;
		
		this.getCodeService();
		
		//Yes, No 기본 값
		if(gubun.equalsIgnoreCase("defaultYN"))
		{
			codeList =  new ArrayList();
			
			HashMap yesNo = new HashMap();
			yesNo.put("code", "Y");
			yesNo.put("codenm", "Yes");
			codeList.add(yesNo);
			
			yesNo = new HashMap();
			yesNo.put("code", "N");
			yesNo.put("codenm", "No");
			codeList.add(yesNo);
		}
		//설문정보 가져오기
		else if(gubun.equalsIgnoreCase("sulPaper"))
		{
			codeList = codeManageService.selectSulPaperList(input);
		}
		//출석고사장 학교정보 가져오기
		else if(gubun.equalsIgnoreCase("schoolRoom"))
		{
			//과정에서 선택한 출석고사장이 존재한다면 선택된 출석고사장만 보여주기 위하여 배열로 넣어준다.
			if(codetype != null && !"".equals(codetype))
			{
				String[] selectSchoolValue = codetype.split(",");
				input.put("selectSchoolValue", selectSchoolValue);
			}
			
			codeList = codeManageService.selectSchoolRoomList(input);
		}
		//과정분류정보 가져오기
		else if(gubun.equalsIgnoreCase("cursBunryu"))
		{
			codeList = codeManageService.selectCursBunryuList(null);
		}
		//교육쪽 코드정보 가져오기 tk_edu000t 테이블에서 가져온다.
		else if(gubun.equalsIgnoreCase("eduCode"))
		{
			codeList = codeManageService.selectEduListCode(input);
		}
		//교육쪽 기관코드코드정보 가져오기 tz_eduorg 테이블에서 가져온다.
		else if(gubun.equalsIgnoreCase("eduOrg"))
		{
			codeList = codeManageService.selectEduOrgList(input);
		}
		//설문쪽 척도 가져오기
		else if(gubun.equalsIgnoreCase("sulScale"))
		{
			codeList = codeManageService.selectSulScaleList(input);
		}
		//과정검색에서 평가종류 가져오기
		else if(gubun.equalsIgnoreCase("examPaper"))
		{
			HashMap cursMap = new HashMap();
			cursMap.put("ses_search_subj", input.get("p_gubun"));			//p_gubun = codetype은 과정코드로
			cursMap.put("ses_search_gyear", input.get("p_levels"));			//p_levels = levels는 년도로
			cursMap.put("ses_search_subjseq", input.get("condition"));		//condition은 기수코드로 사용한다.
			
			
			codeList = codeManageService.selectExamResultPaperNum(cursMap);
		}
		//사이트코드 상세정보가져오기
		else
		{
			codeList = codeManageService.selectSubListCode(input);
		}
		
		return codeList;
	}
	
	
	
	/**
	 * select box 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String makeCodeSelect(HttpServletRequest request) throws Exception {
		StringBuffer html = new StringBuffer();
		Map input = new HashMap();
		input.put("p_gubun", codetype);
		input.put("p_levels", levels);
		
		if(!"".equals(condition))
			input.put("condition", condition);
		
		
		List codeList = this.selectCodeList(input);
		
		//System.out.println(":::" + gubun.toString());
		
		
		if (disabled.equals("true")) {
			disabled = "disabled='disabled'";
		}
		
		if (!event.equals("")){
			if (event.indexOf('(')>-1){
				event = "onchange='"+ event +"'";
			}else{
				event = "onchange='"+ event +"(this)'";
			}
		}
		html.append( "<select id='"+ id +"' style=\"max-width:200px;\" name='"+ id +"' class='"+ className +"' title='"+ title +"' "+ event +" "+ disabled +">" + "\n");
		
		if (!selectTitle.equals("")) {
			html.append( "	<option value=''>"+ selectTitle +"</option>" + "\n");
		}

		for(int i = 0; i < codeList.size(); i++){
			Map output = (Map)codeList.get(i);
			
			String commonCode     = (String)output.get("code");
			String commonCodeName = (String)output.get("codenm");
			
			if (deleteItem.contains(commonCode)) continue;
			if (selectItem.equals(commonCode))
				html.append( "	<option value='"+ commonCode +"' selected >"+ commonCodeName +"</option>" + "\n");
			else 
				html.append( "	<option value='"+ commonCode +"'>"+ commonCodeName +"</option>" + "\n");
		}
		html.append( "</select>" + "\n");
		
		
		//System.out.println(html.toString());
		return html.toString();
	}
	
	
	/**
	 * radio button
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String makeCodeRadio(HttpServletRequest request) throws Exception {
		StringBuffer html = new StringBuffer();
		Map input = new HashMap();
		input.put("p_gubun", codetype);
		input.put("p_levels", levels);
		
		
		if(!"".equals(condition))
			input.put("condition", condition);
		
		List codeList = this.selectCodeList(input);
		
		
		/*
		for (int i = 0 ; i < codeList.size() ; i++) {
			Code code = (Code)codeList.get(i);
			String checked = "";
			
			
			if (selectItem.equals(code.getCode()))
				checked = "checked=checked";
			if (i != 0 && !itemRowCount.equals("") && !itemRowCount.equals("0") &&  (i % (Integer.parseInt(itemRowCount)) == 0)) {
				html.append(" <br>" + "\n");
			}
			html.append( "<input type='radio' id='"+ id +"' name='"+ id +"' value='"+ code.getCode() +"' "+ event +" "+ checked+" "+ disabled +">"+ code.getCodenm() + "\n");
		}
		*/
		
		for (int i = 0 ; i < codeList.size() ; i++) {
			Map output = (Map)codeList.get(i);
			
			String commonCode     = (String)output.get("code");
			String commonCodeName = (String)output.get("codenm");
			String checked = "";
			
			
			if (selectItem.equals(commonCode))
				checked = "checked=checked";
			if (i != 0 && !itemRowCount.equals("") && !itemRowCount.equals("0") &&  (i % (Integer.parseInt(itemRowCount)) == 0)) {
				html.append(" <br>" + "\n");
			}
			html.append( "<input type='radio' id='"+ id +"' name='"+ id +"' value='"+ commonCode +"' "+ event +" "+ checked+" "+ disabled +">"+ commonCodeName + "\n");
		}
		
		
        //System.out.println(html.toString());
		return html.toString();
	}
	
	/**
	 * checkbox 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String makeCodeCheck(HttpServletRequest request) throws Exception {
		StringBuffer html = new StringBuffer();
		Map input = new HashMap();
		input.put("p_gubun", codetype);
		input.put("p_levels", levels);
		
		
		if(!"".equals(condition))
			input.put("condition", condition);
		
		List codeList = this.selectCodeList(input);
		
		
		
		if (!event.equals(""))
			event = "onClick='"+ event +"(this)'";
		
		String[] checkValues = selectItem.split(",");
		if (disabled.equals("true")) {
			disabled = "disabled='disabled'";
		}

		for (int i = 0 ; i < codeList.size() ; i++) {
			Map output = (Map)codeList.get(i);
			String commonCode     = (String)output.get("code");
			String commonCodeName = (String)output.get("codenm");
			
			String checked = "";
			for (int j = 0 ; j < checkValues.length ; j++) {
				if (checkValues[j].equals(commonCode)) {
					checked = "checked=checked";
					break;
				}
			}
			if (i != 0 && !itemRowCount.equals("") && !itemRowCount.equals("0") &&  (i % (Integer.parseInt(itemRowCount)) == 0)) {
				html.append(" <br>" + "\n");
			}
			html.append( "<input type='checkbox' id='"+ id +"' name='"+ id +"' class='"+ className +"' title='"+ title +"' value='"+ commonCode +"' "+ event +" "+ checked+" "+ disabled +">"+ commonCodeName + "\n");
		}
//		System.out.println(html.toString());
		return html.toString();
	}
	
	/**
	 * text 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String makeText(HttpServletRequest request) throws Exception {
		StringBuffer html = new StringBuffer();
		Map input = new HashMap();
		input.put("p_gubun", codetype);
		input.put("p_levels", levels);
		
		
		if(!"".equals(condition))
			input.put("condition", condition);
		
		List codeList = this.selectCodeList(input);
		
		
		
		String[] checkValues = selectItem.split(",");
		int matchCount = 0;
		/**
		for (int i = 0 ; i < codeList.size() ; i++) {
			Code code = (Code)codeList.get(i);
			for (int j = 0 ; j < checkValues.length ; j++) {
				if (checkValues[j].equals(code.getCode())) {
					if (matchCount > 0) {
						html.append(",");
					}
					html.append(code.getCodenm());
					matchCount++;
					break;
				}
			}
		}
		**/
		for (int i = 0 ; i < codeList.size() ; i++) {
			Map output = (Map)codeList.get(i);
			String commonCode     = (String)output.get("code");
			String commonCodeName = (String)output.get("codenm");
			
			for (int j = 0 ; j < checkValues.length ; j++) {
				if (checkValues[j].equals(commonCode)) {
					if (matchCount > 0) {
						html.append(",");
					}
					html.append(commonCodeName);
					matchCount++;
					break;
				}
			}
		}
		return html.toString();
	}
	
/**
	public CodeService getCodeService() {
		if( codeService == null)
			codeService = (CodeService)SharedMethods.getWebApplicationBean( "adm.codeService");
		
		return codeService;
	}
**/
	
	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(String selectItem) {
		this.selectItem = selectItem;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getSelectTitle() {
		return selectTitle;
	}

	public void setSelectTitle(String selectTitle) {
		this.selectTitle = selectTitle;
	}

	public String getItemRowCount() {
		return itemRowCount;
	}

	public void setItemRowCount(String itemRowCount) {
		this.itemRowCount = itemRowCount;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getCodetype() {
		return codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}
	
	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}
	
	

	public String getDeleteItem() {
		return deleteItem;
	}

	public void setDeleteItem(String deleteItem) {
		this.deleteItem = deleteItem;
	}

	public CodeManageService getCodeService() {
		if( codeManageService == null){
			codeManageService = (CodeManageService)this.getWebApplicationBean("codeManageService");
		}
		
			
		return codeManageService;
	}
	
	public  WebApplicationContext getContext()
	{
	return ContextLoader.getCurrentWebApplicationContext();
	}
    public  Object getWebApplicationBean(String beanName)
    {
      return getContext().getBean(beanName);
    }
	
}
