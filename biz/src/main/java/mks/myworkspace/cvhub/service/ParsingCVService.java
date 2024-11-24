package mks.myworkspace.cvhub.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mks.myworkspace.cvhub.entity.User;

@Service
public interface ParsingCVService {
	String extractTextFromPdfOrWord(MultipartFile file) throws IOException;
	 Map<String, String> parseContent(String content, User user);
}