package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class MemberSearchExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("Member List");
		sheet.setDefaultColumnWidth((short)12);
		
		cell = getCell(sheet, 0, 0);  setText(cell, "번호");
		cell = getCell(sheet, 0, 1);  setText(cell, "성명");
		cell = getCell(sheet, 0, 2);  setText(cell, "아이디");
		cell = getCell(sheet, 0, 3);  setText(cell, "비밀번호");
		cell = getCell(sheet, 0, 4);  setText(cell, "이메일");
		cell = getCell(sheet, 0, 5);  setText(cell, "구분");
		cell = getCell(sheet, 0, 6);  setText(cell, "자격");
		cell = getCell(sheet, 0, 7);  setText(cell, "학교명");
		cell = getCell(sheet, 0, 8);  setText(cell, "학교연락처");
		cell = getCell(sheet, 0, 9); setText(cell, "학교우편번호");
		cell = getCell(sheet, 0, 10); setText(cell, "학교주소");
		cell = getCell(sheet, 0, 11); setText(cell, "자택연락처");
		cell = getCell(sheet, 0, 12); setText(cell, "자택우편번호");
		cell = getCell(sheet, 0, 13); setText(cell, "자택주소");
		cell = getCell(sheet, 0, 14); setText(cell, "교재수령지");
		cell = getCell(sheet, 0, 15); setText(cell, "SMS수신여부");
		cell = getCell(sheet, 0, 16); setText(cell, "이메일수신여부");
		
		Map<String, Object> map = (Map)model.get("memberMap");
		List<Object> list = (List)map.get("list");
		for( int i=0; i<list.size(); i++ ){
			Map member = (Map)list.get(i);
			
			cell = getCell(sheet, i+1, 0);
			setText(cell, String.valueOf(i+1));
			
			cell = getCell(sheet, i+1, 1);
			setText(cell, member.get("name") == null ? "" : member.get("name").toString());
			
			cell = getCell(sheet, i+1, 2);
			setText(cell, member.get("userid") == null ? "" : member.get("userid").toString());
			
			cell = getCell(sheet, i+1, 3);
			setText(cell, "");
			
			cell = getCell(sheet, i+1, 4);
			setText(cell, member.get("email") == null ? "" : member.get("email").toString());
			
			cell = getCell(sheet, i+1, 5);
			setText(cell, member.get("gb") == null ? "" : member.get("gb").toString());
			
			cell = getCell(sheet, i+1, 6);
			setText(cell, member.get("lic") == null ? "" : member.get("lic").toString());
			
			cell = getCell(sheet, i+1, 7);
			setText(cell, member.get("snm") == null ? "" : member.get("snm").toString());
			
			cell = getCell(sheet, i+1, 8);
			setText(cell, member.get("handphoneNo") == null ? "" : member.get("handphoneNo").toString());
			
			cell = getCell(sheet, i+1, 9);
			setText(cell, member.get("zipCd1") == null ? "" : member.get("zipCd1").toString());
			
			cell = getCell(sheet, i+1, 10);
			setText(cell, member.get("address1") == null ? "" : member.get("address1").toString());
			
			cell = getCell(sheet, i+1, 11);
			setText(cell, member.get("handphone") == null ? "" : member.get("handphone").toString());
			
			cell = getCell(sheet, i+1, 12);
			setText(cell, member.get("zipCd") == null ? "" : member.get("zipCd").toString());
			
			cell = getCell(sheet, i+1, 13);
			setText(cell, member.get("address") == null ? "" : member.get("address").toString());
			
			cell = getCell(sheet, i+1, 14);
			setText(cell, member.get("rv") == null ? "" : member.get("rv").toString());
			
			cell = getCell(sheet, i+1, 15);
			setText(cell, member.get("issms") == null ? "" : member.get("issms").toString());
			
			cell = getCell(sheet, i+1, 16);
			setText(cell, member.get("ismailling") == null ? "" : member.get("ismailling").toString());
			
		}
	}

}
