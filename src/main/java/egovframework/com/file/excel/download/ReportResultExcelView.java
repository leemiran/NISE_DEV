package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ReportResultExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("report List");
		sheet.setDefaultColumnWidth((short)12);
		
		Map<String, Object> map = (Map)model.get("reportMap");
		List<Object> list = (List)map.get("list");
		int idx = 0;
		cell = getCell(sheet, 0, idx++);
		setText(cell, "NO");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "교육그룹코드");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "과목코드");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "년도");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "기수");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "제출상태");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "아이디");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "이름");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "휴대폰");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "제출일");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "추가제출일");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "제출과제물");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "평가점수");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "감점");
		
		cell = getCell(sheet, 0, idx++);
		setText(cell, "취득점수");
		
		
		if( list != null && list.size() > 0 ){
			for( int i=1; i<=list.size(); i++ ){
				Map m = (Map)list.get(i-1);
				
				idx = 0;

				cell = getCell(sheet, i, idx++);
				setText(cell, i+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, "N000001");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("subj")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("year")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("subjseq")+"");
				
				String date = "";
				if( m.get("indate").toString().equals("IN") ) date = "기한내";
				else if( m.get("indate").toString().equals("RE") ) date = "추가기한";
				else if( m.get("indate").toString().equals("OUT") ) date = "기간 외";
				else date = "미제출";
				
				cell = getCell(sheet, i, idx++);
				setText(cell, date);
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("projid")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("name")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("mobile")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("ldate2")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("adddate")+"");  
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("realfile") == null ? "-" : m.get("realfile")+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("getscore") == null ? "0.0" : m.get("getscore")+"");			
				
				cell = getCell(sheet, i, idx++);
				
				float score = 0;
				float minusday = Float.parseFloat(m.get("minusday") == null ? "0" : m.get("minusday")+"");
				if(minusday < 0)
				{
					if(minusday >= 6.0)
						score = (float) 15;
					else
						score = (float) (minusday * 7.5);
				}
				else
				{
					score = 0;
				}
				setText(cell, score+"");
				
				cell = getCell(sheet, i, idx++);
				setText(cell, m.get("scoreexists").equals("N") ? "0" : m.get("finalscore")+"");				
				
			}
		}
	}

}
