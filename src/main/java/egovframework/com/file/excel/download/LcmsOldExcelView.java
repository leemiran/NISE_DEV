package egovframework.com.file.excel.download;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class LcmsOldExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("Lcms List");
		sheet.setDefaultColumnWidth((short)12);
		
		Map<String, Object> map = (Map)model.get("lcmsMap");
			
			cell = getCell(sheet, 0, 0);
			setText(cell, "module");
			
			cell = getCell(sheet, 0, 1);
			setText(cell, "module명");
			
			cell = getCell(sheet, 0, 2);
			setText(cell, "모바일경로");
			
			cell = getCell(sheet, 0, 3);
			setText(cell, "lesson");
			
			cell = getCell(sheet, 0, 4);
			setText(cell, "lesson명");
			
			cell = getCell(sheet, 0, 5);
			setText(cell, "시작위치(STARTING)");
			
			cell = getCell(sheet, 0, 6);
			setText(cell, "모바일시작시간(초)");
			
			cell = getCell(sheet, 0, 7);
			setText(cell, "모바일종료시간(초)");
			
			cell = getCell(sheet, 0, 8);
			setText(cell, "lesson시간(초)");
			
			
			
			
		List<Object> list = (List)map.get("list");
		if( list != null && list.size() > 0 ){
			for( int i=0; i<list.size(); i++){
				Map lcms = (Map)list.get(i);
				cell = getCell(sheet, i+1, 0);
				setText(cell, (String)lcms.get("module"));
				
				cell = getCell(sheet, i+1, 1);
				setText(cell, (String)lcms.get("modulenm"));
				
				cell = getCell(sheet, i+1, 2);
				setText(cell, (String)lcms.get("mobileUrl"));
				
				cell = getCell(sheet, i+1, 3);
				setText(cell, (String)lcms.get("lesson"));
				
				cell = getCell(sheet, i+1, 4);
				setText(cell, (String)lcms.get("lessonnm"));
				
				cell = getCell(sheet, i+1, 5);
				setText(cell, (String)lcms.get("starting"));
				
				cell = getCell(sheet, i+1, 6);
				setText(cell, ((BigDecimal)lcms.get("mStart")+"").toString());

				cell = getCell(sheet, i+1, 7);
				setText(cell, ((BigDecimal)lcms.get("mEnd")+"").toString());
				
				cell = getCell(sheet, i+1, 8);
				setText(cell, ((BigDecimal)lcms.get("lessonTime")+"").toString());
			}
		}
	}

}
