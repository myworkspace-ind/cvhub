package mks.myworkspace.cvhub.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository_Khoi_22110357;
import mks.myworkspace.cvhub.service.OrganizationExportExcelService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class OrgannizationExportExcelServiceImpl implements OrganizationExportExcelService {

    @Autowired
    private JobRequestRepository_Khoi_22110357 jobRequestRepository;
    
	@Override
	public ByteArrayOutputStream createExcelFile(List<Organization> organizations) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Organizations");

		// Styles
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		CellStyle borderStyle = workbook.createCellStyle();
		borderStyle.setBorderTop(BorderStyle.THIN);
		borderStyle.setBorderBottom(BorderStyle.THIN);
		borderStyle.setBorderLeft(BorderStyle.THIN);
		borderStyle.setBorderRight(BorderStyle.THIN);
		borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		CellStyle dateStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
		dateStyle.setBorderTop(BorderStyle.THIN);
		dateStyle.setBorderBottom(BorderStyle.THIN);
		dateStyle.setBorderLeft(BorderStyle.THIN);
		dateStyle.setBorderRight(BorderStyle.THIN);
		dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Title Row
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Organization Report");
		CellStyle titleStyle = workbook.createCellStyle();
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCell.setCellStyle(titleStyle);

		// Merge title cell
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		titleRow.setHeightInPoints(30);

		// Header Row
		Row headerRow = sheet.createRow(1);
		String[] headers = { "ID", "Title", "Logo", "Website", "Summary", "Location", "Created Date","Job Postings"};
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}
		headerRow.setHeightInPoints(25);

		// Data Rows
		int rowIdx = 2;
		for (Organization org : organizations) {
			Row row = sheet.createRow(rowIdx);

			Cell idCell = row.createCell(0);
			idCell.setCellValue(org.getId() != null ? org.getId().toString() : "");
			idCell.setCellStyle(borderStyle);

			Cell titleCellData = row.createCell(1);
			titleCellData.setCellValue(org.getTitle() != null ? org.getTitle() : "");
			titleCellData.setCellStyle(borderStyle);

			if (org.getLogo() != null && org.getLogo().length > 0) {
				int pictureIdx = workbook.addPicture(org.getLogo(), Workbook.PICTURE_TYPE_PNG);
				Drawing<?> drawing = sheet.createDrawingPatriarch();
				ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
				anchor.setCol1(2);
				anchor.setRow1(rowIdx);
				anchor.setCol2(3);
				anchor.setRow2(rowIdx + 1);
				drawing.createPicture(anchor, pictureIdx);
				row.setHeightInPoints(60);
			} else {
				row.createCell(2).setCellValue("No Logo");
				row.getCell(2).setCellStyle(borderStyle);
				row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
			}

			Cell websiteCell = row.createCell(3);
			websiteCell.setCellValue(org.getWebsite() != null ? org.getWebsite() : "");
			websiteCell.setCellStyle(borderStyle);

			Cell summaryCell = row.createCell(4);
			summaryCell.setCellValue(org.getSummary() != null ? org.getSummary() : "");
			summaryCell.setCellStyle(borderStyle);

			Cell locationCell = row.createCell(5);
			locationCell.setCellValue(org.getLocation() != null ? org.getLocation() : "");
			locationCell.setCellStyle(borderStyle);

			Cell dateCell = row.createCell(6);
			if (org.getCreatedDate() != null) {
				dateCell.setCellValue(org.getCreatedDate());
				dateCell.setCellStyle(dateStyle);
			} else {
				dateCell.setCellValue("");
				dateCell.setCellStyle(borderStyle);
			}
			
            // Add Job Postings Count
            Cell jobPostingsCell = row.createCell(7);
            Long jobPostingsCount = jobRequestRepository.countJobPostingsByOrganizationId(org.getId());
            jobPostingsCell.setCellValue(jobPostingsCount != null ? jobPostingsCount : 0);
            jobPostingsCell.setCellStyle(borderStyle);

			rowIdx++;
		}

		// Adjust Column Width
		for (int i = 0; i < headers.length; i++) {
			if (i == 2) { // Logo column
				sheet.setColumnWidth(i, 20 * 256);
			} else {
				sheet.autoSizeColumn(i);
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}

}
