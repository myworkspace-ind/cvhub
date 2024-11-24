package mks.myworkspace.cvhub.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.service.ParsingCVService;
import mks.myworkspace.cvhub.service.UserService;

@Service
public class ParsingCVImpl implements ParsingCVService {
	@Autowired
	private UserService userService;
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final Set<String> SKILL_KEYWORDS = new HashSet<>(Arrays.asList(
        "skills", "technical skills", "programming languages", "technologies", "tools", "frameworks"
    ));
    private static final Set<String> EXPERIENCE_KEYWORDS = new HashSet<>(Arrays.asList(
        "work experience", "professional experience", "employment history", "work history"
    ));
    private static final Set<String> EDUCATION_KEYWORDS = new HashSet<>(Arrays.asList(
        "education", "academic background", "qualifications", "academic qualifications"
    ));
    private static final Set<String> CERTIFICATION_KEYWORDS = new HashSet<>(Arrays.asList(
        "certifications", "certificates", "professional certifications"
    ));

    @Override
    public String extractTextFromPdfOrWord(MultipartFile file) throws IOException {
        String content = "";

        try {
            if (file.getContentType().equals("application/pdf")) {
                content = extractTextFromPDF(file);
            } else if (file.getContentType()
                    .equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    || file.getContentType().equals("application/msword")) {
                content = extractTextFromWord(file);
            }

            logger.info("Extracted content: " + content);

        } catch (IOException e) {
            logger.error("Đã xảy ra lỗi: {}", e.getMessage(), e);
            throw e;
        }

        return content;
    }

    @Override
    public Map<String, String> parseContent(String content, User user) {
        Map<String, String> parsedInfo = new HashMap<>();
        // Lấy thông tin từ user được truyền vào
        parsedInfo.put("name", user.getFullName());
        parsedInfo.put("phone", user.getPhone());
        parsedInfo.put("email", user.getEmail());
        
        parsedInfo.put("skills", extractSection(content, SKILL_KEYWORDS));
        parsedInfo.put("experience", extractSection(content, EXPERIENCE_KEYWORDS));
        parsedInfo.put("education", extractSection(content, EDUCATION_KEYWORDS));
        parsedInfo.put("certifications", extractSection(content, CERTIFICATION_KEYWORDS));

        return parsedInfo;
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    private String extractTextFromWord(MultipartFile file) throws IOException {
        XWPFDocument document = new XWPFDocument(file.getInputStream());
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        document.close();
        return text;
    }

  

    private String extractSection(String content, Set<String> keywords) {
        String lowercaseContent = content.toLowerCase();
        int startIndex = -1;
        String startKeyword = "";
        for (String keyword : keywords) {
            int index = lowercaseContent.indexOf(keyword.toLowerCase());
            if (index != -1 && (startIndex == -1 || index < startIndex)) {
                startIndex = index;
                startKeyword = keyword;
            }
        }
        if (startIndex == -1) return "";

        int endIndex = content.length();
        for (Set<String> otherKeywords : Arrays.asList(SKILL_KEYWORDS, EXPERIENCE_KEYWORDS, EDUCATION_KEYWORDS, CERTIFICATION_KEYWORDS)) {
            if (otherKeywords != keywords) {
                for (String keyword : otherKeywords) {
                    int index = lowercaseContent.indexOf(keyword.toLowerCase(), startIndex + startKeyword.length());
                    if (index != -1 && index < endIndex) {
                        endIndex = index;
                    }
                }
            }
        }

        String extractedContent = content.substring(startIndex, endIndex).trim();
        
        // Loại bỏ tiêu đề phần chính
        extractedContent = extractedContent.replaceFirst("(?i)" + Pattern.quote(startKeyword) + "\\s*:?\\s*", "");
        
        // Loại bỏ tất cả các tiêu đề phụ
        extractedContent = extractedContent.replaceAll("(?m)^[A-Z][A-Za-z\\s]+:\\s*", "");

        // Loại bỏ các dòng trống
        extractedContent = extractedContent.replaceAll("(?m)^\\s*$\\n", "");

        return extractedContent.trim();
    }
}