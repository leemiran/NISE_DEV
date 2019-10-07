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

import com.ziaan.scorm.DateUtil;

public class LcmsLessonExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = (Map)model.get("lcmsMap");
		
		String fileName = DateUtil.toString("yyyyMMddhhmmss") + "_" + String.valueOf(map.get("subj")) + "_Lesson.xls";
		fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");	
		
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("Lcms List");
		sheet.setDefaultColumnWidth((short)12);
		
			
			cell = getCell(sheet, 0, 0);
			setText(cell, "MODULE");
			
			cell = getCell(sheet, 0, 1);
			setText(cell, "LESSON");
			
			cell = getCell(sheet, 0, 2);
			setText(cell, "LESSON명");
			
			cell = getCell(sheet, 0, 3);
			setText(cell, "STARTING(파일경로)");
			
			cell = getCell(sheet, 0, 4);
			setText(cell, "모바일시작시간(초)");
			
			cell = getCell(sheet, 0, 5);
			setText(cell, "모바일종료시간(초)");
			
			cell = getCell(sheet, 0, 6);
			setText(cell, "웹 lesson 시간(초)");
			
			
			
			
		List<Object> list = (List)map.get("list");
		if( list != null && list.size() > 0 ){
			for( int i=0; i<list.size(); i++){
				Map lcms = (Map)list.get(i);
				cell = getCell(sheet, i+1, 0);
				setText(cell, (String)lcms.get("module"));
				
				cell = getCell(sheet, i+1, 1);
				setText(cell, (String)lcms.get("lesson"));
				
				cell = getCell(sheet, i+1, 2);
				setText(cell, (String)lcms.get("sdesc"));
				
				cell = getCell(sheet, i+1, 3);
				setText(cell, (String)lcms.get("starting"));
				
				cell = getCell(sheet, i+1, 4);
				setText(cell, ((BigDecimal)lcms.get("mStart")+"").toString());

				cell = getCell(sheet, i+1, 5);
				setText(cell, ((BigDecimal)lcms.get("mEnd")+"").toString());
				
				cell = getCell(sheet, i+1, 6);
				setText(cell, ((BigDecimal)lcms.get("lessonTime")+"").toString());
			}
		}
	}

}
