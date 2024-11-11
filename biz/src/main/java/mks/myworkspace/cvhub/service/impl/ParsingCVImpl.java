package mks.myworkspace.cvhub.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import mks.myworkspace.cvhub.service.ParsingCVService;


@Service
public class ParsingCVImpl implements ParsingCVService {
	private final StanfordCoreNLP pipeline;
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;
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
        public ParsingCVImpl() {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
            props.setProperty("ner.useSUTime", "false");
            this.pipeline = new StanfordCoreNLP(props);
        }

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
	         throw e; // Re-throw the exception to be handled by the caller
	     }

	     return content;
	 }

	@Override
	public Map<String, String> parseContent(String content) {
		 Map<String, String> parsedInfo = new HashMap<>();
	        Annotation document = new Annotation(content);
	        pipeline.annotate(document);

	        parsedInfo.put("name", extractName(document, content));
	        parsedInfo.put("email", extractEmail(content));
	        parsedInfo.put("phone", extractPhoneNumber(content));
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
	 private String extractName(Annotation document, String content) {
	        // First, try to find the name using NER
	        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
	            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
	                if ("PERSON".equals(token.get(CoreAnnotations.NamedEntityTagAnnotation.class))) {
	                    return token.get(CoreAnnotations.TextAnnotation.class);
	                }
	            }
	        }
	        
	        // If NER fails, try to find the name at the beginning of the document
	        Pattern namePattern = Pattern.compile("^([A-Z][a-z]+(\\s[A-Z][a-z]+)+)");
	        Matcher nameMatcher = namePattern.matcher(content);
	        if (nameMatcher.find()) {
	            return nameMatcher.group(1);
	        }
	        
	        return "";
	    }

	    private String extractEmail(String content) {
	        Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
	        Matcher matcher = pattern.matcher(content);
	        return matcher.find() ? matcher.group() : "";
	    }

	    private String extractPhoneNumber(String content) {
	        Pattern pattern = Pattern.compile("\\d+");
	        Matcher matcher = pattern.matcher(content);
	        String longestNumericSequence = "";

	        while (matcher.find()) {
	            String match = matcher.group();
	            if (match.length() > longestNumericSequence.length()) {
	                longestNumericSequence = match;
	            }
	        }

	        // Kiểm tra xem chuỗi số có đủ dài để là số điện thoại không
	        if (longestNumericSequence.length() >= 7 && longestNumericSequence.length() <= 15) {
	            return longestNumericSequence;
	        }

	        return "";
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