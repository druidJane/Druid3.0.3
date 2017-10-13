package com.xuanwu.mos.file.handler;

import com.xuanwu.mos.exception.BusinessException;
import com.xuanwu.mos.file.FileHead;
import com.xuanwu.mos.file.FileType;
import com.xuanwu.mos.file.FileUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 林泽强 on 2016/8/22. Excel文件处理器
 */
public class ExcelHandler extends AbstractFileHandler {

	private static final Logger logger = LoggerFactory.getLogger(ExcelHandler.class);

	private Workbook workbook = null;
	private DataFormatter formatter = null;
	private FormulaEvaluator evaluator = null;

	@Override
	public FileHead readHead(String filePath, String delimiter) {
		this.openWorkbook(new File(filePath));
		try {
			Sheet sheet = this.workbook.getSheetAt(0);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				FileHead fileHead = new FileHead();
				// 获取代码里面的第一行是否拥有文件头
				String[] cells = acceptRow(sheet.getRow(0));

				if (cells == null) {
					return null;
				}

				for (int i = 0; i < cells.length; i++) {
					fileHead.putCell(i, cells[i]);
				}
				return fileHead;
			}
		} catch (Exception e) {
			logger.error("Read file head failed,cause by:{}", e);
		}
		return null;
	}

	@Override
	public int readLineCount(File file) {
		this.openWorkbook(file);
		try {
			Sheet sheet = this.workbook.getSheetAt(0);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				int lastRowNum = sheet.getLastRowNum();
				int totalRowNum = 0;
				while (lastRowNum > 0) {
					String[] cells = acceptRow(sheet.getRow(lastRowNum));
					if (cells != null) {
						totalRowNum++;
					}
					// 不计空行
					lastRowNum--;
				}
				return totalRowNum;
			}
		} catch (Exception e) {
			logger.error("Read file line count failed,cause by:{}", e);
		}
		return 0;
	}

	@Override
	public void readContent(File file, String delimiter, RowHandler rowHandler) {
		this.openWorkbook(file);
		String[] cells = null;
		Sheet sheet = null;
		boolean first = true; // 第一行为文件头
		try {
			sheet = this.workbook.getSheetAt(0);
			// 不为空表
			if (sheet.getPhysicalNumberOfRows() > 0) {
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					cells = acceptRow(sheet.getRow(i));
					if (cells == null)
						continue;
					if (first) {
						if (!rowHandler.handleHead(cells))
							break;
						first = false;
						continue;
					}
					if (!rowHandler.handleRow(cells)) {
						break;
					}
				}
			}
		} catch (BusinessException e) {
			//捕获业务异常，不处理，往上级抛，由上级处理
			throw e;
		} catch (Exception e) {
			logger.error("Read file content failed,cause by:{}", e);
		}
	}

	@Override
	public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList, Charset charset) {
		writeFile(file, delimiter, isNew, cellsList);
	}

	@Override
	public void writeFile(File file, String delimiter, boolean isNew, List<String[]> cellsList) {
		Workbook wb = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		Sheet sheet = null;
		int lastRowNum = 0;
		try {
			FileType type = FileUtil.getFileType(file.getName());
			switch (type) {
			case Excel:
				if (isNew) {
					wb = new HSSFWorkbook();
					sheet = wb.createSheet();
					fos = new FileOutputStream(file, true);
				} else {
					fis = new FileInputStream(file);
					wb = new HSSFWorkbook(fis);
					fos = new FileOutputStream(file);
					sheet = wb.getSheetAt(0);
					lastRowNum = sheet.getLastRowNum() + 1;
				}
				break;
			case ExcelX:
				if (isNew) {
					wb = new XSSFWorkbook();
					sheet = wb.createSheet();
					fos = new FileOutputStream(file, true);
				} else {
					fis = new FileInputStream(file);
					wb = new XSSFWorkbook(fis);
					fos = new FileOutputStream(file);
					sheet = wb.getSheetAt(0);
					lastRowNum = sheet.getLastRowNum() + 1;
				}
				break;
			default:
				throw new BusinessException("Unsupport excel version");
			}
			for (int i = 0; i < cellsList.size(); i++) {
				Row row = sheet.createRow(lastRowNum + i);
				String[] cells = cellsList.get(i);
				for (int j = 0; j < cells.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(cells[j] == null ? "" : cells[j]);
				}
			}
			wb.write(fos);
		} catch (Exception e) {
			logger.error("Write excel failed,cause by:{}", e);
		} finally {
			FileUtil.closeInputStrem(fis);
			FileUtil.closeOutputStrem(fos);
		}
	}

	@Override
	public void readAll(String filePath, String delimiter, RowHandler rowHandler) {
		FileHead fileHead = readHead(filePath, delimiter);
		if (null != fileHead) {
			File file = new File(filePath);
			readContent(file, delimiter, rowHandler);
		}
	}

	private void openWorkbook(File file) {
		FileInputStream fis = null;
		try {
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.error("Open work book file not found.", e);
			}
			try {
				workbook = WorkbookFactory.create(fis);
			} catch (InvalidFormatException e) {
				logger.error("Create work book invalid format exception occur.", e);
			} catch (IOException e) {
				logger.error("Create work book invalid IOException occur.", e);
			}
			evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			formatter = new DataFormatter(true);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					// TODO: Ignore it
				}
			}
		}
	}

	private String[] acceptRow(Row row) {
		if (row == null)
			return null;
		int lastCellNum = row.getLastCellNum();
		String[] cells = readCells(row, lastCellNum);
		if (cells == null || cells.length == 0)
			return null;
		for (String s : cells) {
			if (StringUtils.isNotBlank(s))
				return cells;
		}
		return null;
	}

	private String[] readCells(Row row, int lastCellNum) {
		Cell cell = null;

		List<String> cells = new ArrayList<String>();
		for (int i = 0; i < lastCellNum; i++) {
			cell = row.getCell(i);
			if (cell == null) {
				cells.add("");
				continue;
			}
			if (cell.getCellType() != Cell.CELL_TYPE_FORMULA) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
						String d = new SimpleDateFormat("yyyy-MM-dd").format(date);
						cells.add(d);
					} else {
						String temp = formatter.formatCellValue(cell);
						cells.add(temp == null ? "" : temp.trim());
					}
				} else {
					cells.add(formatter.formatCellValue(cell));
				}
			} else {
				cells.add(formatter.formatCellValue(cell, this.evaluator));
			}
		}
		return cells.toArray(new String[0]);
	}

	public static void main(String args[]) {
		ExcelHandler handler = new ExcelHandler();
		FileHead fileHead = handler.readHead("E:\\玄武-图书.xlsx", ",");
		System.out.println(fileHead.getHeadMap());
	}
}
