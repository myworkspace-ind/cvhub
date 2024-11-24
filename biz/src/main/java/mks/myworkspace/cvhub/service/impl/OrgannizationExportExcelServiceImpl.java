package mks.myworkspace.cvhub.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.service.OrganizationExportExcelService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class OrgannizationExportExcelServiceImpl implements OrganizationExportExcelService{

	@Override
	public ByteArrayOutputStream createExcelFile(List<Organization> organizations) throws IOException {
		Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Organizations");
        
        CellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderRight(BorderStyle.THIN);
        
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Title", "Logo", "Website", "Summary", "Location", "Created Date"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
            cell.setCellStyle(borderStyle);
        }

        int rowIdx = 1;
        for (Organization org : organizations) {
            Row row = sheet.createRow(rowIdx);

            row.createCell(0).setCellValue(org.getId() != null ? org.getId().toString() : "");
            row.createCell(1).setCellValue(org.getTitle() != null ? org.getTitle() : "");
            row.createCell(3).setCellValue(org.getWebsite() != null ? org.getWebsite() : "");
            row.createCell(4).setCellValue(org.getSummary() != null ? org.getSummary() : "");
            row.createCell(5).setCellValue(org.getLocation() != null ? org.getLocation() : "");
            row.createCell(6).setCellValue(org.getCreatedDate() != null ? org.getCreatedDate().toString() : "");
            
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
                row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
            }

            rowIdx++;
        }

        for (int i = 0; i < headers.length; i++) {
            if (i == 2) {
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
