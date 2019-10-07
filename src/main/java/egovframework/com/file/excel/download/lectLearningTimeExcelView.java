package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class lectLearningTimeExcelView extends AbstractExcelView{
	
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
		
		HSSFSheet sheet = wb.createSheet("lectLearningTime List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "lectLearningTime List");
		
		
		Map<String, Object> map = (Map)model.get("lectLearningTimeMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "No"); 
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "지역");
		setText(getCell(sheet, 1, index++), "교육구분");
		setText(getCell(sheet, 1, index++), "기수");      
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "최초학습일");
		setText(getCell(sheet, 1, index++), "최근학습시작일");
		setText(getCell(sheet, 1, index++), "최근학습종료일");
		setText(getCell(sheet, 1, index++), "총학습시간");

		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (i+1)+"");
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("areaCodenm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isonoff")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseq")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("firstEdu")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ldateStart")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ldateEnd")));
			
			
			  String v_totaltime 		= this.nullValue(mp.get("totalTime"));
			  String v_totalminute 	= this.nullValue(mp.get("totalMinute"));
			  String v_totalsec 		= this.nullValue(mp.get("totalSec"));
			
			  if(!(v_totaltime.length() > 0) ) {
					v_totaltime = "0";
			  }
			  if( ! (v_totalminute.length() >0)){
					v_totalminute = "0";
			  }
			  if( ! (v_totalsec.length() >0)){
			  	v_totalsec = "0";
			  }
			
			  int v_edutotmin = (Integer.parseInt(v_totaltime)*60*60 + Integer.parseInt(v_totalminute)*60 + Integer.parseInt(v_totalsec))/60;

              
			cell = getCell(sheet, 2+i, index++);
			setText(cell, v_edutotmin + "");
			
		}
	}

}
