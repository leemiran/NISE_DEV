package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class FinishStudentExcelView extends AbstractExcelView{ 
	
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
		
		HSSFSheet sheet = wb.createSheet("FinishStudent List");
		sheet.setDefaultColumnWidth((short)12);
		
		Map<String, Object> map = (Map)model.get("fnMap");
		List<Object> list = (List)map.get("list");
		
		Map eMap = (Map)list.get(0);
		
		int edutimes = Integer.parseInt(eMap.get("edutimes").toString());
		String subj = eMap.get("subj").toString();
		
		cell = getCell(sheet, 0, 0);  setText(cell, "번호");
		cell = getCell(sheet, 0, 1);  setText(cell, "ID");
		cell = getCell(sheet, 0, 2);  setText(cell, "성명");
		cell = getCell(sheet, 0, 3);  setText(cell, "근무지");
		cell = getCell(sheet, 0, 4);  setText(cell, "진도율(%)");
		cell = getCell(sheet, 0, 5);  setText(cell, "참여도(일)");
		cell = getCell(sheet, 0, 6);  setText(cell, "참여도 평가");
		cell = getCell(sheet, 0, 7);  setText(cell, "온라인 시험");
		cell = getCell(sheet, 0, 8); setText(cell,  "출석 시험");
		cell = getCell(sheet, 0, 9); setText(cell, "온라인 과제");
		cell = getCell(sheet, 0, 10); setText(cell, "총점");
		cell = getCell(sheet, 0, 11); setText(cell, "등수");
		if(edutimes >= 60 && subj.substring(0,3).equals("PAR")){
			cell = getCell(sheet, 0, 12); setText(cell, "조정 점수");
			cell = getCell(sheet, 0, 13); setText(cell, "이수여부");
			cell = getCell(sheet, 0, 14); setText(cell, "이수번호");
		}else{
			cell = getCell(sheet, 0, 12); setText(cell, "이수여부");
			cell = getCell(sheet, 0, 13); setText(cell, "이수번호");
		}
		
		
		for( int i=0; i<list.size(); i++ ){
			Map fMap = (Map)list.get(i);
			
			cell = getCell(sheet, i+1, 0);
			setText(cell, String.valueOf(i+1));
			
			cell = getCell(sheet, i+1, 1);
			setText(cell, this.nullValue(fMap.get("userid")));
			
			cell = getCell(sheet, i+1, 2);
			setText(cell, this.nullValue(fMap.get("name")));
						
			cell = getCell(sheet, i+1, 3);
			setText(cell, this.nullValue(fMap.get("userPath")));
			
			cell = getCell(sheet, i+1, 4);
			setText(cell, this.nullValue(fMap.get("tstep"))+"%");
			
			cell = getCell(sheet, i+1, 5);
			setText(cell, this.nullValue(fMap.get("etc1")));
			
			cell = getCell(sheet, i+1, 6);
			setText(cell, this.nullValue(fMap.get("avetc2")));
			
			cell = getCell(sheet, i+1, 7);
			setText(cell, this.nullValue(fMap.get("avftest")));
			
			cell = getCell(sheet, i+1, 8);
			setText(cell, this.nullValue(fMap.get("avmtest")));
			
			cell = getCell(sheet, i+1, 9);
			setText(cell, this.nullValue(fMap.get("avreport")));
			
			cell = getCell(sheet, i+1, 10);
			setText(cell, this.nullValue(fMap.get("score")));
			
			cell = getCell(sheet, i+1, 11);
			setText(cell, this.nullValue(fMap.get("ranking")));
			
			if(edutimes >= 60 && subj.substring(0,3).equals("PAR")){
				cell = getCell(sheet, i+1, 12);
				setText(cell, this.nullValue(fMap.get("editscore")));
				
				cell = getCell(sheet, i+1, 13);
				setText(cell, this.nullValue(fMap.get("isgraduateddesc")));
				
				cell = getCell(sheet, i+1, 14);
				setText(cell, this.nullValue(fMap.get("serno")));
			}else{
				cell = getCell(sheet, i+1, 12);
				setText(cell, this.nullValue(fMap.get("isgraduateddesc")));
			
				cell = getCell(sheet, i+1, 13);
				setText(cell, this.nullValue(fMap.get("serno")));
			}
			
		}
	}

}
