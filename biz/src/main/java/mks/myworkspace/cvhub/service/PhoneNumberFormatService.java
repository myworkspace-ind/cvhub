package mks.myworkspace.cvhub.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberFormatService {
	private static final String AREA_CODE = "084"; // Mã vùng muốn thêm vào

    // Hàm để xử lý số điện thoại
    public String formatPhoneNumber(String phoneNumber) {
        // Kiểm tra xem số điện thoại đã có mã vùng (+ddd) ở đầu chưa
        if (phoneNumber.startsWith("(+") && phoneNumber.contains(")") && phoneNumber.length() > 5) {
            // Nếu đã có mã vùng, chỉ cần định dạng lại số điện thoại
            return formatPhoneNumberWithoutAreaCode(phoneNumber);
        }

        // Nếu không có mã vùng, thêm mã vùng (+084) vào và định dạng lại
        return addAreaCodeAndFormat(phoneNumber);
    }

    // Hàm để thêm mã vùng và định dạng số điện thoại
    private String addAreaCodeAndFormat(String phoneNumber) {
        // Thêm mã vùng (+084) vào đầu số điện thoại
        String withAreaCode = "(+" + AREA_CODE + ") " + phoneNumber;

        // Định dạng số điện thoại theo kiểu 0123-4567-89
        // Dùng biểu thức chính quy để thêm dấu "-" sau mỗi 4 chữ số
        return formatPhoneNumberWithoutAreaCode(withAreaCode);
    }

    // Hàm định dạng số điện thoại không có mã vùng
    private String formatPhoneNumberWithoutAreaCode(String phoneNumber) {
    	
    	String areaCode = phoneNumber.substring(0, 7);

    	String numberAfterAreaCode = phoneNumber.substring(7);
    	// Định dạng số điện thoại theo kiểu (084) 0123-4567-89
    	Pattern pattern = Pattern.compile("(\\d{4})(\\d{4})(\\d{2})");
    	Matcher matcher = pattern.matcher(numberAfterAreaCode);

    	if (matcher.find()) {
    	    // Trả về số điện thoại đã được định dạng với dấu "-"
    	    return areaCode + matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
    	}
    	

    	// Trả về số điện thoại ban đầu nếu không khớp với định dạng
    	return phoneNumber;
    }
}
