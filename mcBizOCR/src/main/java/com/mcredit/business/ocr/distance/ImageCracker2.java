package com.mcredit.business.ocr.distance;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageCracker2 {

	// Mang cac ky tu goc co dau
	private static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù',
			'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ',
			'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ',
			'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề',
			'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ',
			'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ',
			'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', 'ỳ'};

	// Mang cac ky tu thay the khong dau
	private static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O',
			'U', 'U', 'Y', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a',
			'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
			'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
			'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
			'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u',
			'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'y'};
	
	private static String DAI_LY_CHI_HO_ERR = "VIETTELIVNPOST";
	private static String DAI_LY_CHI_HO = "VIETTEL/VNPOST";

	public static String getSHD(List<String> input) {
		String shd = null;
		for (int i = 0; i < 5; i++) {
			shd = getIndex(input, i);
			shd = removeAccent(shd).toLowerCase();
			shd = replaceSpecialCharators(shd);
			if (shd.contains("so")) {
				shd = getTextBetween(shd, "(?<=so)(.*)(?=)");
				shd = replaceLetters(shd);
				if (isNumeric(shd)) {
					if (!shd.startsWith("1"))
						shd = "1" + shd;
					break;
				}
			} else if (shd.contains("se")) {
				shd = getTextBetween(shd, "(?<=se)(.*)(?=)");
				shd = replaceLetters(shd);
				if (isNumeric(shd)) {
					if (!shd.startsWith("1"))
						shd = "1" + shd;
					break;
				}
			}
			shd = "";
		}

		return shd;
	}

	public static String getngayKyHopDong(List<String> input) {
		String output = "";
		for (int i = 1; i < 11; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = output.replaceAll("]", "1");
			output = replaceSpecialCharatorsExcept(output, " ");

			if ((output.contains("ngay") || output.contains("thang") || output.contains("nam"))
					&& !output.contains("vay")) {
				output = getNgayKyHopDong(output);
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getNgayKyHopDong(String input) {
		try {
			if (input == null)
				return "";

			String day = getTextBetween(input, "ngay", "thang");
			String month = getTextBetween(input, "thang", "nam") != "" ? getTextBetween(input, "thang", "nam")
					: getTextBetween(input, "thang", "nan");
			String year = getTextBetween(input, "am ", "tai") != "" ? getTextBetween(input, "am ", "tai")
					: getTextBetween(input, "an ", "tai");

			return day + "/" + month + "/" + year;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getNgayCap(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("ay:");
		int end = input.indexOf("tai:");

		return subString(input, start + 3, end);
	}

	public static String getTenCongTyBaoHiem(String input) {

		if (input == null || "".equals(input.trim()))
			return "";

		input = subString(input, 50, input.length());

		int start = input.indexOf("m:");

		return subString(input, start + 2, input.length());
	}

	public static String getDaiLyChiHo(String input) {

		if (input == null || "".equals(input.trim()))
			return "";

		input = subString(input, input.length() / 2, input.length());

		int start = input.indexOf(":");

		return subString(input, start + 1, input.length());
	}

	public static String getNoiCap(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("ại:") > 0 ? input.indexOf("ại:") : input.indexOf("ai:");
		String result = subString(input, start + 3, input.length());

		return result;
	}

	public static String getSoTienVay(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("tienvay:");

		return subString(input, start + 8, input.indexOf("vn"));
	}

	public static String getThoiHanVay(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("anvay:");

		return subString(input, start + 7, start + 9);
	}

	public static String getLaiSuatVay(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("tronghan:") > 0 ? input.indexOf("tronghan:") : input.indexOf("trongban:");
		int end = input.indexOf("%");

		return subString(input, start + 9, end);
	}

	public static String getSCM(String input) {

		if (input == null || "".equals(input.trim()))
			return "";
		int start = input.indexOf("cccd:");
		int end = input.indexOf("capngay");

		return subString(input, start + 5, end);
	}

	public static String getHoTenKhachHang(String input) {
		String output = "";
		if (input == null || "".equals(input.trim()))
			return "";

		int start = input.indexOf("BÀ") > 0 ? input.indexOf("BÀ") : input.indexOf("BA");

		String name = subString(input, start + 2, input.length());
		char[] chars = name.toCharArray();

		// iterate over char[] array using enhanced for loop
		for (char ch : chars) {
			if (Character.isUpperCase(ch) || Character.isSpace(ch) || Character.isDigit(ch))
				output += Character.toString(ch);
			else
				break;

		}
		output = removeAccent(output);
		return output.trim();
	}

	public static String getSoKhung(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 4);
				break;
			}
		}
		return output;
	}
	
	public static List<String> getChuoiSoKhung(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 4);
				break;
			}
		}
		return new ArrayList<>();
	}

	public static String getPos(List<String> input) {
		String output = "";
		for (int i = 20; i < 40; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			output = output.replace(";", ":");
			if (output.contains("banhang:")) {
				output = getIndex(input, i);
				output = output.replace(";", ":");
				int index = output.lastIndexOf(":");
				output = subString(output, index + 1, output.length());
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getSoTienVay(List<String> input) {
		String output = "";
		for (int i = 10; i < 40; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("tienvay")) {
				output = replaceLetters(getSoTienVay(output));
				if (output != null)
					output = output.replaceAll("i", "1");
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getThoiHanVay(List<String> input) {
		String output = "";
		for (int i = 15; i < 50; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("anvay")) {
				int startIndex = output.indexOf("anvay");
				int endIndexNew = output.indexOf("ky");
				int endIndexOld = output.indexOf("thang");
				if(endIndexOld > 0) {
					output = output.substring(startIndex+6, endIndexOld).trim();
				} else if(endIndexNew > 0) {
					output = output.substring(startIndex+6, endIndexNew).trim();
				} else {
					output = "";
				}
//				output = getTextBetween(output, "(?<=anvay:)(.*)(?=kytrano.trong)");
				if (output != null) {
					output = output.replaceAll("i", "1");
					output = output.replaceAll("l", "1");
				}
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getLaiSuatVay(List<String> input) {
		String output = "";
		for (int i = 15; i < 50; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			output = replaceSpecialCharatorsExcept(output, ",.:%");
			if (output.contains("tronghan") || output.contains("trongban")) {
				output = getTextBetween(output, "(?<=tronghan:)(.*)(?=%)") != ""
						? getTextBetween(output, "(?<=tronghan:)(.*)(?=%)")
						: getTextBetween(output, "(?<=trongban:)(.*)(?=%)");
				if (output != null)
					output = output.replaceAll("i", "1");
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getLaiSuatVayTG(List<String> input) {
		String output = "";
		for (int i = 15; i < 50; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			output = replaceSpecialCharatorsExcept(output, ",.:;%");
			output = output.replaceAll(";", ":");
			if (output.contains("thuong")) {
				output = getTextBetween(output, "(?<=thuong)(.*)(?=)") != ""
						? getTextBetween(output, "(?<=thuong)(.*)(?=)")
						: getTextBetween(output, "(?<=thuong)(.*)(?=)");
				output = replaceSpecialCharators(output, "[^.;0-9]+");
				output = output.replaceAll(":", "");
				if (output != null && output.endsWith("."))
					output = output.substring(0, output.length() - 1);
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getLaiSuatVayBH(List<String> input) {
		String output = "";
		for (int i = 15; i < 50; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			output = replaceSpecialCharatorsExcept(output, ",.:;%");
			output = output.replaceAll(";", ":");
			if (output.contains("han") && output.contains("vay") && output.contains("lai")) {
				output = getTextBetween(output, "(?<=onghan)(.*)(?=)") != ""
						? getTextBetween(output, "(?<=nghan)(.*)(?=)")
						: getTextBetween(output, "(?<=onghan)(.*)(?=)");
				output = replaceSpecialCharators(output, "[^.;:0-9]+");
				if (output.contains(":")) {
					output = output.substring(output.indexOf(":"), output.length());
				}
				output = output.replaceAll(":", "");
				output = output.replaceAll("i", "1");
				if (output != null && output.endsWith("."))
					output = output.substring(0, output.length() - 1);
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getPhiBaoHiem(List<String> input) {
		String output = "";
		for (int i = 18; i < 50; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("phibaohiem:")) {
				output = replaceLetters(output);
				if (output != null)
					output = output.replaceAll("i", "1");
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getTenCongTyBaoHiem(List<String> input) {
		String output = "";
		for (int i = 18; i < 35; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("ybaohiem:")) {
				output = getTenCongTyBaoHiem(getIndex(input, i));
				String nextLine = getIndex(input, i + 1);
				String outLine = nextLine;
				nextLine = removeAccent(nextLine).toLowerCase();
				nextLine = removeAllSpace(nextLine);
				if (!nextLine.contains("-")) {
					output = output + " " + outLine;
				}
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getDaiLyChiHo(List<String> input) {
		String output = "";
		for (int i = 20; i < 40; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("hoac") && output.indexOf("redi") > 0
					&& output.indexOf("redi") < output.lastIndexOf(":")) {
				output = getDaiLyChiHo(getIndex(input, i));
				if(output != null && output.equals(DAI_LY_CHI_HO_ERR)) {
					output = DAI_LY_CHI_HO;
				}
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getNoiCap(List<String> input) {
		String output = "";
		for (int i = 8; i < 30; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("tai:") || output.contains("tại:")) {
				output = getNoiCap(getIndex(input, i));
				String nextLine = getIndex(input, i + 1);
				String outLine = nextLine;
				nextLine = removeAccent(nextLine).toLowerCase();
				nextLine = removeAllSpace(nextLine);
				if (!nextLine.contains("diachi") && !nextLine.contains("thuongtru")) {
					output = output + " " + outLine;
				}
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getNgayCap(List<String> input) {
		String output = "";
		for (int i = 8; i < 30; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("ngay:")) {
				output = getTextBetween(output, "(?<=ngay:)(.*?)(?=tai)");
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getSoCMT(List<String> input) {
		String output = "";
		for (int i = 8; i < 30; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);
			if (output.contains("cccd")) {
				output = getTextBetween(output, "(?<=cd)(.*?)(?=c)");
				if (output != null)
					output = output.replaceAll("i", "1");
				output = replaceLetters(output);
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getHoTenKhachHang(List<String> input) {
		String output = "";
		for (int i = 2; i < 20; i++) {
			output = getIndex(input, i);
			output = removeAccent(output).toLowerCase();
			output = removeAllSpace(output);

			if (output.contains("ng/ba")) {
				output = getIndex(input, i);
				output = getHoTenKhachHang(output);
				break;
			}
			output = "";
		}

		return output;
	}

	public static String getSoTaiKHoanNguoiNhan(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			if (output.contains("Khoản:")) {
				output = output.substring(output.indexOf("Khoản:")+6, output.length());
				break;
			}
		}
		output = replaceSpecialCharators(output).trim();
		return output;
	}

	public static String getNganHangNhan(List<String> input) {
		String output = "";

		for (int i = 20; i < 35; i++) {
			output = getIndex(input, i);
			if (output.contains("PGD")) {
				output = output.substring(output.indexOf("PGD") + 3, output.length());
				break;
			}
		}
		output = output.replace(":", "").trim();
		return output;
	}
	
	public static List<String> getListProduct(List<String> array) {
		List<String> qrs = new ArrayList<>();
		
		for (int i = 14; i < 35; i++) {
			String output = getIndex(array, i).toLowerCase();
			output = removeSpecialCharacter(output);
			output = removeAllSpace(output);
			if (output.contains("stt") && output.contains("hànghóa")) {
				int num = 1;
				String res = getIndex(array, i + 1);
				while (!res.contains("hóa đơn") && !res.contains("iá bán")) {
					res = removeSpecialCharacter(res);
					qrs.add(res);
					num++;
					res = getIndex(array, i + num);
				}
			}
		}
		
		return qrs;
	}
	
	public static List<String> getDanhSachTenHangHoa(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("hàng hóa")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 1);
				break;
			}
		}
		return new ArrayList<>();
	}

	public static String getTenHangHoa(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("hàng hóa")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 1);
				break;
			}
		}
		return output;
	}

	public static String getNhanHieu(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 2);
				break;
			}
		}
		return output;
	}
	
	public static List<String> getChuoiNhanHieu(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 2);
				break;
			}
		}
		return new ArrayList<>();
	}

	public static String getMauMa(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 3);
				break;
			}
		}
		return output;
	}
	
	public static List<String> getChuoiMauMa(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 3);
				break;
			}
		}
		return new ArrayList<>();
	}

	public static String getSoMay(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 5);
				break;
			}
		}
		return output;
	}
	
	public static List<String> getChuoiSoMay(List<String> input) {
		String output = "";
		for (int i = 15; i < 35; i++) {
			output = getIndex(input, i);
			output = output.toLowerCase();
			if (output.contains("stt") && output.contains("nhãn hiệu")) {
				String res = getIndex(input, i + 1);
				res = replaceSpecialCharatorsToCharactor(res, "|");
				output = getThongTinBang(res, 5);
				break;
			}
		}
		return new ArrayList<>();
	}

	public static String getIndex(List<String> input, int index) {
		try {
			return input.get(index).trim();
		} catch (Exception e) {
			return "";
		}
	}

	public static char removeAccent(char ch) {
		int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
		if (index >= 0) {
			ch = DESTINATION_CHARACTERS[index];
		}
		return ch;
	}

	public static String removeAccent(String s) {
		try {
			StringBuilder sb = new StringBuilder(s);
			for (int i = 0; i < sb.length(); i++) {
				sb.setCharAt(i, removeAccent(sb.charAt(i)));
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	private static String replaceSpecialCharators(String input) {
		if (input == null || "".equals(input.trim()))
			return "";
		return input.replaceAll("[^a-zA-Z0-9]+", "");
	}

	private static String replaceSpecialCharatorsToCharactor(String input, String output) {
		input = input.trim();
		if (input == null || "".equals(input.trim()))
			return "";
		return input.replaceAll("[ ¡|;.]", output);
	}

	private static String replaceSpecialCharatorsExcept(String input, String except) {
		if (input == null || "".equals(input.trim()))
			return "";
		return input.replaceAll("[^a-zA-Z0-9" + except + "]+", "");
	}

	private static String replaceSpecialCharators(String input, String patten) {
		if (input == null || "".equals(input.trim()))
			return "";
		return input.replaceAll(patten, "");
	}

	private static String getTextBetween(String data, String patten) {
		try {
			Pattern pattern = Pattern.compile(patten);
			Matcher matcher = pattern.matcher(data);
			if (matcher.find())
				return matcher.group(0).trim();

			return "";
		} catch (Exception e) {
			return "";
		}
	}

	private static String getTextBetween(String data, String start, String end) {
		try {
			Pattern pattern = Pattern.compile("(?<=" + start + ")(.*?)(?=" + end + ")");
			Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
				String result = matcher.group(0).trim();
				return result;
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	private static String replaceLetters(String input) {
		if (input == null || "".equals(input.trim()))
			return "";
		// convert String to char[] array
		char[] chars = input.toCharArray();
		String output = "";

		// iterate over char[] array using enhanced for loop
		for (char ch : chars) {
			if (Character.isDigit(ch))
				output += Character.toString(ch);
		}

		return output;
	}

	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
	}

	private static String subString(String input, int beginIndex, int endIndex) {
		try {
			return input.trim().substring(beginIndex, endIndex).trim();
		} catch (Exception e) {
			return "";
		}
	}

	private static String removeAllSpace(String str) {
		try {
			return str.replaceAll("\\s+", "");
		} catch (Exception e) {
			return "";
		} // match a number with optional '-' and decimal.
	}

	private static String getThongTinBang(String res, int index) {
		String[] output = res.split("\\|");
		if (index < output.length) {
			return output[index];
		}
		return "";
	}

	private static String removeSpecialCharacter(String input) {
		if (input == null || "".equals(input.trim()))
			return "";
		// convert String to char[] array
		char[] chars = input.toCharArray();
		String output = "";
		
		List<Character> lst = new ArrayList<>();
		for (int i = 0 ; i < SOURCE_CHARACTERS.length; i++) {
			lst.add(SOURCE_CHARACTERS[i]);
		}

		// iterate over char[] array using enhanced for loop
		for (char ch : chars) {
			if (Character.isDigit(ch) || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || lst.contains(ch) || Character.isSpace(ch) || ch == '(' || ch == ')')
				output += Character.toString(ch);
		}

		return output;
	}
	private static List<String> getArray(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		// byte[] bytes = Files.readAllBytes(path);
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}
	
	public static void main(String[] args) throws Exception {
		List<String> input = getArray("D:\\research\\ocr_test\\2154678-NGUYENTHITHUYNGA_1.txt");
		String output = new String();
		for (String str : input) {
			output += str;
		}
//		System.out.println(getSHD(input));
//		System.out.println(getngayKyHopDong(input));
//		System.out.println(getHoTenKhachHang(input));
//		System.out.println(getSoCMT(input));
//		System.out.println(getNgayCap(input));
//		System.out.println(getNoiCap(input));
//		System.out.println(getSoTienVay(input));
//		System.out.println(getThoiHanVay(input));
//		System.out.println(getLaiSuatVay(input));
//		System.out.println(getPhiBaoHiem(input));
//		System.out.println(getTenCongTyBaoHiem(input));
//		System.out.println(getSoTaiKHoanNguoiNhan(input));
//		System.out.println(getNganHangNhan(input));
//		System.out.println(getDaiLyChiHo(input));
//		System.out.println(getPos(input));
//		System.out.println(getTenHangHoa(input));
//		System.out.println(getNhanHieu(input));
//		System.out.println(getMauMa(input));
//		System.out.println(getSoKhung(input));
//		System.out.println(getSoMay(input));
//		List<String> lstHangHoa = getListProduct(input);
//		for (String hangHoa : lstHangHoa) {
//			System.out.println(hangHoa);
//		}
	}
}
