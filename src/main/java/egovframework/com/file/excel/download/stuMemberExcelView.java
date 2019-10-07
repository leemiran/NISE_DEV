package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class stuMemberExcelView extends AbstractExcelView{
	
	private String nullValue(Object val)
	{
		if(val == null)
            return "";
        else
            return val.toString();
		
	}
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("stuMember List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "stuMember List");
		
		
		Map<String, Object> map = (Map)model.get("stuMemberMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "수험번호"); 
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "지역");
		setText(getCell(sheet, 1, index++), "교육구분");
		setText(getCell(sheet, 1, index++), "기수");
		setText(getCell(sheet, 1, index++), "회원구분");
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "생년월일");
		setText(getCell(sheet, 1, index++), "핸드폰번호");
		setText(getCell(sheet, 1, index++), "학교명/소속근무처");
		setText(getCell(sheet, 1, index++), "출석고사장");
		setText(getCell(sheet, 1, index++), "연수지명번호");
		setText(getCell(sheet, 1, index++), "교재유무");
		setText(getCell(sheet, 1, index++), "교재명");
		setText(getCell(sheet, 1, index++), "우편번호");
		setText(getCell(sheet, 1, index++), "자택/학교(직장) 주소");
		setText(getCell(sheet, 1, index++), "교재수령지");
		setText(getCell(sheet, 1, index++), "결재수단");
		setText(getCell(sheet, 1, index++), "입금일");
		setText(getCell(sheet, 1, index++), "수강료");
		setText(getCell(sheet, 1, index++), "이메일수신여부");
		setText(getCell(sheet, 1, index++), "SMS수신여부");

		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("examnum")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("areaCodenm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isonoff")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseq")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("gb")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
						
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
						
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("birthDate")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("handphone")));
			
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("empGubun")).equals("R") ? this.nullValue(mp.get("positionNm")) : this.nullValue(mp.get("userPath")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("gosa")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("lecSelNo")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("usebook")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("bookname")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (this.nullValue(mp.get("hrdc")).equals("C") ? this.nullValue(mp.get("zipCd1")) : this.nullValue(mp.get("zipCd"))));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (this.nullValue(mp.get("hrdc")).equals("C") ? this.nullValue(mp.get("address1")) : this.nullValue(mp.get("address"))));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("hrdc_nm")));
			
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("pay")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("enterDt")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("biyong")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ismailling")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("issms")));
			
		}
	}

}
