package com.ziaan.library.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.ziaan.library.Log;

import com.ziaan.library.DataBox;

public class JxlRead {
	
	private Workbook workBook  = null;
	private Sheet    sheet     = null;
	private File     excelFile = null;

	private int sheetCnt    = 0;
	private int startRowNum = 0;
	
	public JxlRead(String pathName, int startRowNum) throws Exception {
		this.excelFile   = new File(pathName);
		this.startRowNum = startRowNum;
		if (!this.excelFile.exists()) {
			Log.info.println("Excel File Not Found. (" + pathName + ")");
			throw new FileNotFoundException();
		} else {
			try {
				this.workBook = Workbook.getWorkbook(this.excelFile);
				this.sheetCnt = this.workBook.getSheetNames().length;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public JxlRead(String pathName) throws Exception {
		this(pathName, 0);
	}
	
	/**
	 * Excel ?åå?ùº?ùÑ ?ã´?äî?ã§.
	 */
	public void closeExcel() {
		this.workBook.close();
		this.excelFile = null;
	}
	
	/**
	 * ?óë??? ?ãú?ä∏ Î¶¨Ïä§?ä∏?ùÑ Í∞??†∏?ò®?ã§.
	 * @return
	 */
	public ArrayList getSheetInfoList() {
		ArrayList arrList = null;

		try {
			arrList = new ArrayList(3);
			
			for (int i = 0; i < this.sheetCnt; i++) {
				this.sheet = this.workBook.getSheet(i);
				arrList.add(this.setSheetInfo(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrList;
	}
	
	/**
	 * Excel Sheet ?ùò ?†ïÎ≥¥Î?? Í∞??†∏?ò®?ã§.
	 * @param sheetName Sheet Name
	 * @return
	 */
	public DataBox getSheetInfo(String sheetName) {
		String[] sheetNames = this.workBook.getSheetNames();

		int sheetIdx = 0;
		
		for (int i = 0; i < this.sheetCnt; i++) {
			if (sheetName.equals(sheetNames[i])) {
				sheetIdx = i;
				break;
			}
		}

		return this.getSheetInfo(sheetIdx);
	}
	
	/**
	 * Excel Sheet ?ùò ?†ïÎ≥¥Î?? Í∞??†∏?ò®?ã§.
	 * @param Sheet Index
	 * @return
	 */
	public DataBox getSheetInfo(int sheetIdx) {
		DataBox   dataMap  = null;

		try {
			if (sheetIdx >= 0 && this.sheetCnt > sheetIdx) {
				this.sheet = this.workBook.getSheet(sheetIdx);
				dataMap = this.setSheetInfo(sheetIdx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataMap;
	}
	
	/**
	 * Excel Sheet Î™ÖÏúºÎ°? ?ç∞?ù¥?Ñ∞Î•? ?ùΩ?ñ¥?ò®?ã§.
	 * @param sheetName
	 */
	public ArrayList readExcelData(String sheetName) {
		ArrayList arrList = null;
		
		if (this.sheetCnt > 0) {
			arrList = this.readExcelSheetData(this.workBook.getSheet(sheetName));
		}

		return arrList;
	}

	/**
	 * Excel Sheet Index Í∞íÏúºÎ°? ?ç∞?ù¥?Ñ∞Î•? ?ùΩ?ñ¥?ò®?ã§.
	 * @param sheetIdx
	 */
	public ArrayList readExcelData(int sheetIdx) {
		ArrayList arrList = null;

		if (sheetIdx >= 0 && this.sheetCnt > sheetIdx) {
			arrList = this.readExcelSheetData(this.workBook.getSheet(sheetIdx));
		}
		return arrList;
	}
	
	/**
	 * Excel Sheet ?ùò ?ç∞?ù¥?Ñ∞Î•? ?ùΩ?äî?ã§.
	 * @param sheet
	 * @return
	 */
	private ArrayList readExcelSheetData(Sheet sheet) {
		ArrayList arrList = null;
		DataBox   dataMap = null;
		
		Cell[] rows = null;

		int colCnt = 0;
		int rowCnt = 0;
		int cellLen = 0;

		try {
			arrList = new ArrayList(5);

			colCnt = sheet.getColumns();
			rowCnt = sheet.getRows();

			for (int x = this.startRowNum; x < rowCnt; x++) {
				rows = sheet.getRow(x);
				cellLen = rows.length;
				
				dataMap = new DataBox("EXCEL_DATA");
				for (int y = 0; y < colCnt; y++) {
					if (y < cellLen) {
						dataMap.put(Constants._EXCEL_COLUMN_NAME + "_" +  + y, rows[y].getContents());
					} else {
						dataMap.put(Constants._EXCEL_COLUMN_NAME + "_" +  + y, "");
					}
				}
				arrList.add(dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rows = null;
		}
		
		return arrList;
	}
	
	/**
	 * ????úÑÏπòÏùò ?ç∞?ù¥?Ñ∞Î•? ?ùΩ?ñ¥?ò®?ã§.
	 * @param col
	 * @param row
	 * @return
	 */
	public String getCell(int col, int row ) {
		return this.sheet.getCell(col, row).getContents(); 
	}
	
	/**
	 * ????ùò ?ç∞?ù¥?Ñ∞Î•? int ?òï?úºÎ°? Î∞òÌôò?ïú?ã§.
	 * @param col
	 * @param row
	 * @return
	 */
	public int getIntCell(int col, int row) {
		String cellVal = this.getCell(col, row);
		int cellData = 0;

		try {
			cellData = Integer.parseInt(cellVal);
		} catch (Exception e) {
			cellData = 0;
		}
		
		return cellData;
	}
	
	/**
	 * Sheet ?ùò ?ç∞?ù¥?Ñ∞ ?†ïÎ≥¥Î?? ?Ñ∏?åÖ?ïú?ã§.
	 * @param sheetIdx Sheet Index
	 * @param sheet
	 */
	private DataBox setSheetInfo(int sheetIdx) {
		DataBox dataMap = new DataBox("EXCEL_DATA");

		dataMap.put(Constants._EXCEL_SHEET_INDEX, String.valueOf(sheetIdx));
		dataMap.put(Constants._EXCEL_SHEET_NAME, this.sheet.getName());
		dataMap.put(Constants._EXCEL_COLUMN_COUNT_NAME, String.valueOf(this.sheet.getColumns()));
		dataMap.put(Constants._EXCEL_ROW_COUNT_NAME, String.valueOf(this.sheet.getRows()));
		
		return dataMap;
	}

	public static void main(String[] args) {
		ArrayList dataList  = null;

		DataBox dataMap = null;
		JxlRead jxl = null;

		try {
			jxl = new JxlRead("C:/20073_EF100202A.xls", 0);

			dataMap = jxl.getSheetInfo("Í∏∞Ï°¥ÏΩîÎìúÎπÑÍµê");
			System.out.println("#####################################################");
			System.out.println("Sheet Index  : " + dataMap.getString(Constants._EXCEL_SHEET_INDEX));
			System.out.println("Sheet Name   : " + dataMap.getString(Constants._EXCEL_SHEET_NAME));
			System.out.println("Row Count    : " + dataMap.getString(Constants._EXCEL_ROW_COUNT_NAME));
			System.out.println("Column Count : " + dataMap.getString(Constants._EXCEL_COLUMN_COUNT_NAME));
			System.out.println("#####################################################");

			System.out.println(jxl.getCell(3, 2) + ":::::::::::::::::::::");

			/*
		sheetList = jxl.getSheetInfoList();

		for (int i = 0; i < sheetList.size(); i++) {
			dataMap = (DataMap)sheetList.get(i);

			System.out.println("#####################################################");
			System.out.println("Sheet Index  : " + dataMap.getString(Constants._EXCEL_SHEET_INDEX));
			System.out.println("Sheet Name   : " + dataMap.getString(Constants._EXCEL_SHEET_NAME));
			System.out.println("Row Count    : " + dataMap.getString(Constants._EXCEL_ROW_COUNT_NAME));
			System.out.println("Column Count : " + dataMap.getString(Constants._EXCEL_COLUMN_COUNT_NAME));
			System.out.println("#####################################################");
		}
			 */


			int columnCount = dataMap.getInt(Constants._EXCEL_COLUMN_COUNT_NAME);
			dataList = jxl.readExcelData(0);

			for (int j = 0; j < dataList.size(); j++) {
				dataMap = (DataBox)dataList.get(j);

				for (int k = 0; k < columnCount; k++) {
					//System.out.print(new String(dataMap.getValue(Constants._EXCEL_COLUMN_NAME + "_" + k).getBytes("EUC-KR"), "UTF-8") + " ::: ");
					System.out.print(dataMap.getString(Constants._EXCEL_COLUMN_NAME + "_" + k) + " ::: ");
				}

				System.out.println("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jxl.closeExcel();
		}

		/*
	int colCnt = 0;
	try {
		sheetList = jxl.readExcelSheet("C:/Í≥ºÎ™©?†ïÎ¶?.xls");

		for (int i = 0; i < sheetList.size(); i++) {
			dataMap = (DataMap)sheetList.get(i);
			dataList = jxl.readExcelData("C:/Í≥ºÎ™©?†ïÎ¶?.xls", dataMap.getString(Constants._EXCEL_SHEET_NAME + i));

			System.out.println("#####################################################");
			System.out.println("Sheet Name   : " + dataMap.get(Constants._EXCEL_SHEET_NAME + i));
			System.out.println("Row Count    : " + dataMap.get(Constants._EXCEL_ROW_COUNT_NAME + i));
			System.out.println("Column Count : " + dataMap.get(Constants._EXCEL_COLUMN_COUNT_NAME + i));
			System.out.println("#####################################################");

			colCnt = ((Integer)dataMap.get(Constants._EXCEL_COLUMN_COUNT_NAME + i)).intValue();

			for (int j = 0; j < dataList.size(); j++) {
				dataMap = (DataMap)dataList.get(j);

				for (int k = 0; k < colCnt; k++) {
					System.out.print(dataMap.get(Constants._EXCEL_COLUMN_NAME + k) + " ::: ");
				}

				System.out.println("");
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		 */
	}
}
