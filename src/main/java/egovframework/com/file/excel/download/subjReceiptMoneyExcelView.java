package egovframework.com.file.excel.download;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class subjReceiptMoneyExcelView extends AbstractExcelView{
	
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
		
		HSSFSheet sheet = wb.createSheet("subjReceiptMoney List");
		sheet.setDefaultColumnWidth((short)12);
		
		// put text in first cell
		cell = getCell(sheet, 0, 0);
		setText(cell, "subjReceiptMoney List");
		
		
		Map<String, Object> map = (Map)model.get("subjReceiptMoneyExcelMap");
		List<Object> list = (List)map.get("list");
		
		int index = 0;
		// set header information
		setText(getCell(sheet, 1, index++), "No"); 
		setText(getCell(sheet, 1, index++), "수험번호");
		setText(getCell(sheet, 1, index++), "과정명");
		setText(getCell(sheet, 1, index++), "교육구분");
		setText(getCell(sheet, 1, index++), "기수");      
		setText(getCell(sheet, 1, index++), "ID");
		setText(getCell(sheet, 1, index++), "성명");
		setText(getCell(sheet, 1, index++), "나이스개인번호");
		setText(getCell(sheet, 1, index++), "전화");
		setText(getCell(sheet, 1, index++), "핸드폰");
		setText(getCell(sheet, 1, index++), "근무지");
		setText(getCell(sheet, 1, index++), "진도율");
		
		setText(getCell(sheet, 1, index++), "참여도(일)");
		setText(getCell(sheet, 1, index++), "참여도평가");
		setText(getCell(sheet, 1, index++), "온라인시험");
		setText(getCell(sheet, 1, index++), "출석시험");
		setText(getCell(sheet, 1, index++), "온라인과제");
		setText(getCell(sheet, 1, index++), "총점");
		setText(getCell(sheet, 1, index++), "평가");
		setText(getCell(sheet, 1, index++), "리포트");
		setText(getCell(sheet, 1, index++), "설문");
		
		
		for( int i=0; i<list.size(); i++ ){
			Map mp = (Map)list.get(i);
			
			index = 0;
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, (i+1)+"");
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("examnum")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjnm")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("isonoffval")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("subjseq")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userid")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("name")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("nicepersonalnum")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("hometel")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("handphone")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("userPath")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("tstep")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("rect1")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("avetc2")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("ftest")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("mtest")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("report")));
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(mp.get("score")));
			
			System.out.println((2+i)+" : "+mp);
			
//			==========================================================================================	
//			평가
//			==========================================================================================
			int v_totexamcnt    = Integer.parseInt(this.nullValue(mp.get("totexamcnt")));
			int v_repexamcnt    = Integer.parseInt(this.nullValue(mp.get("repexamcnt")));
			
			cell = getCell(sheet, 2+i, index++);
			if(v_totexamcnt > 0){
				if((v_totexamcnt - v_repexamcnt) == 0){
					setText(cell, "완료");
				}else{
					setText(cell, (v_totexamcnt - v_repexamcnt) + " ("+v_totexamcnt+")");
				}
			}else{
				setText(cell, "-");
			}
//			==========================================================================================
			
			
			
			
//			==========================================================================================		
//			리포트
//			========================================================================================== 
			String v_reportstatus  = "";
			int v_totprojcnt    = Integer.parseInt(this.nullValue(mp.get("totprojcnt")));
			int v_repprojcnt    = Integer.parseInt(this.nullValue(mp.get("repprojcnt")));
			int v_isretcnt      = Integer.parseInt(this.nullValue(mp.get("isretcnt")));
			int v_resend        = v_totprojcnt - v_repprojcnt;
			
			if(v_totprojcnt > 0){
				if((v_totprojcnt - v_repprojcnt + v_isretcnt) == 0){
					v_reportstatus = "완료";
				}else{
					if(v_isretcnt > 0){ 
						v_reportstatus = v_isretcnt + " (" + v_totprojcnt + ")<font color='red'>모사</font>";
					}else{
						v_reportstatus = v_resend + " ("+v_totprojcnt + ")";
					}
				}
			}else{
				v_reportstatus ="-";
			}
			
			cell = getCell(sheet, 2+i, index++);
			setText(cell, this.nullValue(v_reportstatus));
//			==========================================================================================
			
			
			
			
//			==========================================================================================	
//			설문
//			==========================================================================================
			int v_totsulcnt     = Integer.parseInt(this.nullValue(mp.get("totsulcnt")));
			int v_repsulcnt     = Integer.parseInt(this.nullValue(mp.get("repsulcnt")));
			
			
			cell = getCell(sheet, 2+i, index++);
			if(v_totsulcnt > 0){
				if((v_totsulcnt - v_repsulcnt) == 0){
					setText(cell, "완료");
				}else{
					setText(cell, (v_totsulcnt - v_repsulcnt) + " ("+v_totsulcnt+")");
				}
			}else{
				setText(cell, "-");
			}
//			==========================================================================================	
			
			
		}
	}

}
