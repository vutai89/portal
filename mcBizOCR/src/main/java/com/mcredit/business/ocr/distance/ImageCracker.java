package com.mcredit.business.ocr.distance;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageCracker {

	// Mang cac ky tu goc co dau
	private static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù',
			'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ',
			'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ',
			'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề',
			'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ',
			'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ',
			'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', 'ỳ' };

	// Mang cac ky tu thay the khong dau
	private static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O',
			'U', 'U', 'Y', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A', 'a',
			'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
			'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e',
			'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
			'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u',
			'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'y' };

	private static String DAI_LY_CHI_HO_ERR = "VIETTELIVNPOST";
	private static String DAI_LY_CHI_HO = "VIETTEL/VNPOST";
	private static String regexShd = "((1|Í)000)(\\d{11,12})";
	private static String regexNgayKyHopDong = "(?<=ngày )(\\d{0,2})";
	private static String regexThangKyHopDong = "(?<=tháng )(\\d{0,2})";
	private static String regexNamKyHopDong = "(?<=năm )(\\d{4})";
	private static String regexHoTenKH = "(?<=[ÔO][Nn][Gg]\\/B[AÀÂaà])(.*?)(?=\\()";
	private static String regexSoCMT = "((?<=CCCD(.*?)?:)(.*?))(\\d+)";
	private static String regexNgayCap = "(?<=C[aâấăắ]p ngày:)(.*?)(?= [Tt]ại)";
	private static String regexNoiCap = "(?<=[Tt][aạ]i:)(.*)(?=Địa)";
	private static String regexSoTienVay = "(?<=(tiền|mức cho)\\s?vay:)(.*?)(?=VND)";
	private static String regexThoiHanVay = "(?<=hạn\\s?(vay|mức):)(.*?)(?=\\s?(Kỳ|[Tt]háng))";
	private static String regexLaiSuatVay = "(?<=hạn(\\sthông thường)?(\\”|\\'|\\!|\\?|\\^|\\'')?:)(.*?)(?=%)";
//	private static String regexLaiSuatVay = "(?<=hạn(\\sthông thường)?(.*?):)(.*?)(?=%)";
	private static String regexSoTKNhan = "(?<=S[ốôoó](\\s)?Tài\\s?Khoản:\\s?)(.*?)(?=\\s?[Tt]ại)";
//	private static String regexNganHangNhan = "(?<=PGD:)(.*?)(?=(Sau khi|(\\(2\\) Hoặc))";
	private static String regexNganHangNhan = "(?<=PGD:)(.*?)(?=(Sau khi|(\\(2\\) Hoặc)))";
	private static String regexDaiLyChiHo = "(?<=hộ của(\\s)?Mcredit(\\:)?)(.*?)(?=và )";
	private static String regexPhiBaoHiem = "(?<=[B|b]ảo hiểm\\:)(.*?)(?=VND)";
	private static String regexTenCtyBaoHiem = "(?<=Công\\s?ty\\s?[Bb]ảo\\s?hiểm:)(.*?)(?=\\-)";
	private static String regexLstProduct = "(?<=Serial)(.*?)(?=Giá\\sbán)";
	private static String regexPos = "(?<=bán\\s?hàng:\\s?)(.*?)(?=(\\- Giải|11\\.|12\\.))";

	public static String getSHD(String input) {
		String shd = getTextByRegex(input, regexShd);
		return shd;
	}

	public static String getngayKyHopDong(String input) {
		String ngayKy = getTextByRegex(input, regexNgayKyHopDong);
		String thangKy = getTextByRegex(input, regexThangKyHopDong);
		String namKy = getTextByRegex(input, regexNamKyHopDong);
		return ngayKy + "/" + thangKy + "/" + namKy;
	}

	public static String getHoTenKhachHang(String input) {
		String hoTenKH = getTextByRegex(input, regexHoTenKH);
		return hoTenKH;
	}

	public static String getSoCMT(String input) {
		String soCMT = getTextByRegex(input, regexSoCMT);
		return soCMT;
	}

	public static String getNgayCap(String input) {
		String ngayCapCMT = getTextByRegex(input, regexNgayCap);
		return ngayCapCMT;
	}

	public static String getNoiCap(String input) {
		return getTextByRegex(input, regexNoiCap);
	}

	public static String getSoTienVay(String input) {
		String soTienVay = getTextByRegex(input, regexSoTienVay);
		return replaceLetters(soTienVay);
	}

	public static String getThoiHanVay(String input) {
		return getTextByRegex(input, regexThoiHanVay);
	}

	public static String getLaiSuatVayBH(String input) {
		return getTextByRegex(input, regexLaiSuatVay);
	}

	public static String getSoTaiKHoanNguoiNhan(String input) {
		return getTextByRegex(input, regexSoTKNhan);
	}

	public static String getNganHangNhan(String input) {
		return getTextByRegex(input, regexNganHangNhan);
	}

	public static String getDaiLyChiHo(String input) {
		String daiLyChiHo = getTextByRegex(input, regexDaiLyChiHo);
		if (daiLyChiHo.equals(DAI_LY_CHI_HO_ERR)) {
			daiLyChiHo = DAI_LY_CHI_HO;
		}
		return daiLyChiHo;
	}

	public static String getPhiBaoHiem(String input) {
		String phiBaoHiem = getTextByRegex(input, regexPhiBaoHiem);
		return replaceLetters(phiBaoHiem);
	}

	public static String getTenCongTyBaoHiem(String input) {
		return getTextByRegex(input, regexTenCtyBaoHiem);
	}

	public static List<String> getListProduct(String input) {
		String product = getTextByRegex(input, regexLstProduct);
		List<String> lstProducts = new ArrayList<String>();
		lstProducts.add(product);
		return lstProducts;
	}

	public static String getPos(String input) {
		return getTextByRegex(input, regexPos);
	}

	private static String getTextByRegex(String data, String patten) {
		try {
			Pattern pattern = Pattern.compile(patten);
			Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
//				System.out.println("Pattern: " + patten);
//				for(int i = 0; i < matcher.groupCount(); i++) {
//					System.out.println(matcher.group(i).trim());
//				}
				return matcher.group(0).trim();
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

	private static String getArray(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		// byte[] bytes = Files.readAllBytes(path);
		List<String> array = Files.readAllLines(path, StandardCharsets.UTF_8);
		StringBuffer output = new StringBuffer();
		for (String str : array) {
			output.append(str);
		}
		return output.toString();
	}
	
	/*
	 * @SuppressWarnings("unused") public static void main(String[] args) throws
	 * Exception { String output = getArray(
	 * "C:\\Users\\huyendtt.ho\\Documents\\check_scan_file\\limit\\output3.txt");
	 * System.out.println("Output: " + output); String shd = getTextByRegex(output,
	 * regexShd); String ngayKy = getTextByRegex(output, regexNgayKyHopDong); String
	 * thangKy = getTextByRegex(output, regexThangKyHopDong); String namKy =
	 * getTextByRegex(output, regexNamKyHopDong); String hoTenKH =
	 * getTextByRegex(output, regexHoTenKH); String soCMT = getTextByRegex(output,
	 * regexSoCMT); String ngayCapCMT = getTextByRegex(output, regexNgayCap); String
	 * noiCapCMT = getTextByRegex(output, regexNoiCap); String soTienVay =
	 * getTextByRegex(output, regexSoTienVay); String thoiHanVay =
	 * getTextByRegex(output, regexThoiHanVay); String laiSuatVay =
	 * getTextByRegex(output, regexLaiSuatVay); String phiBaoHiem =
	 * getTextByRegex(output, regexPhiBaoHiem); String tenCtyBaoHiem =
	 * getTextByRegex(output, regexTenCtyBaoHiem); String stkNguoiNhan =
	 * getTextByRegex(output, regexSoTKNhan); String nganHangNhan =
	 * getTextByRegex(output, regexNganHangNhan); String daiLyChiHo =
	 * getTextByRegex(output, regexDaiLyChiHo); String pos = getTextByRegex(output,
	 * regexPos); String products = getTextByRegex(output, regexLstProduct);
	 * System.out.println("SHD: " + getSHD(output)); System.out.println("ngayKyHD: "
	 * + getngayKyHopDong(output)); System.out.println("hoTenKH: " +
	 * getHoTenKhachHang(output)); System.out.println("soCMT: " + getSoCMT(output));
	 * System.out.println("ngayCapCMT: " + getNgayCap(output));
	 * System.out.println("noiCapCMT: " + getNoiCap(output));
	 * System.out.println("soTienVay: " + getSoTienVay(output));
	 * System.out.println("thoiHanVay: " + getThoiHanVay(output));
	 * System.out.println("laiSuatVay: " + getLaiSuatVayBH(output));
	 * System.out.println("phiBaoHiem: " + getPhiBaoHiem(output));
	 * System.out.println("tenCtyBaoHiem: " + getTenCongTyBaoHiem(output));
	 * System.out.println("stkNguoiNhan: " + getSoTaiKHoanNguoiNhan(output));
	 * System.out.println("nganHangNhan: " + getNganHangNhan(output));
	 * System.out.println("daiLyChiHo: " + getDaiLyChiHo(output));
	 * System.out.println("pos: " + getPos(output)); System.out.println("products: "
	 * + getListProduct(output)); }
	 */

}
