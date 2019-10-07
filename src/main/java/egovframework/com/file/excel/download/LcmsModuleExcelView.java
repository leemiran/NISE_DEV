package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ziaan.scorm.DateUtil;

public class LcmsModuleExcelView extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = (Map)model.get("lcmsMap");
		
		String fileName = DateUtil.toString("yyyyMMddhhmmss") + "_" + String.valueOf(map.get("subj")) + "_Module.xls";
		fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");	
		
		HSSFCell cell = null;
		
		HSSFSheet sheet = wb.createSheet("Lcms List");
		sheet.setDefaultColumnWidth((short)12);
		
			
			cell = getCell(sheet, 0, 0);
			setText(cell, "MODULE");
			
			cell = getCell(sheet, 0, 1);
			setText(cell, "MODULE명");
			
			cell = getCell(sheet, 0, 2);
			setText(cell, "모바일경로");
			
		List<Object> list = (List)map.get("list");
		if( list != null && list.size() > 0 ){
			for( int i=0; i<list.size(); i++){
				Map lcms = (Map)list.get(i);
				cell = getCell(sheet, i+1, 0);
				setText(cell, (String)lcms.get("module"));
				
				cell = getCell(sheet, i+1, 1);
				setText(cell, (String)lcms.get("sdesc"));
				
				cell = getCell(sheet, i+1, 2);
				setText(cell, (String)lcms.get("mobileUrl"));
			}
		}
	}

}
