package com.mcredit.business.ocr.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcredit.model.dto.warehouse.CompareInfoDTO;
import com.mcredit.model.dto.warehouse.ContractCompareResult;
import com.mcredit.model.dto.warehouse.ContractInfo;
import com.mcredit.model.dto.warehouse.QRProduct;
import com.mcredit.model.enums.ocr.ContractEnum;

public class Comparator {
	public static ContractCompareResult compare(ContractInfo info, String shd, String ngayKyHopDong,
			String hoTenKhachHang, String soCMT, String ngayCap, String noiCap, String soTienVay, String thoiHanVay,
			String laiSuatVay, String phiBaoHiem, String tenCongTyBaoHiem, String soTaiKhoanNguoiNhan,
			String nganHangNhan, String daiLyChiHo, String tenHangHoa, String nhanHieu, String mauMa, String soKhung,
			String soMay, String pos, String fileType) {
		ContractCompareResult compare = new ContractCompareResult();

		compareBase(compare, info, shd, ngayKyHopDong, hoTenKhachHang, soCMT, ngayCap, noiCap, soTienVay, thoiHanVay,
				laiSuatVay);

		if (fileType.equals(ContractEnum.TIEN_MAT.value()) || fileType.equals(ContractEnum.TIEN_MAT_BAO_HIEM.value())) {
			compareTienMat(compare, info, soTaiKhoanNguoiNhan, nganHangNhan, daiLyChiHo);
		}
		if (fileType.equals(ContractEnum.TIEN_MAT_BAO_HIEM.value())
				|| fileType.equals(ContractEnum.TRA_GOP_BAO_HIEM.value())) {
			compareBaoHiem(compare, info, phiBaoHiem, tenCongTyBaoHiem);
		}
		if (fileType.equals(ContractEnum.TRA_GOP.value()) || fileType.equals(ContractEnum.TRA_GOP_BAO_HIEM.value())) {
			compareTraGop(compare, info, tenHangHoa, nhanHieu, mauMa, soKhung, soMay, pos);
		}
		return compare;
	}

	private static void compareBase(ContractCompareResult compare, ContractInfo info, String shd, String ngayKyHopDong,
			String hoTenKhachHang, String soCMT, String ngayCap, String noiCap, String soTienVay, String thoiHanVay,
			String laiSuatVay) {

		compare.setShd(compare(info.getShd(), shd));
		compare.setNgayKyHopDong(compare(info.getNgayKyHopDong(), ngayKyHopDong));
		compare.setHoTenKhachHang(compare(info.getHoTenKhachHang(), hoTenKhachHang));
		compare.setSoCMT(compare(info.getSoCMT(), soCMT));
		compare.setNgayCap(compare(info.getNgayCap(), ngayCap));
		compare.setNoiCap(compare(info.getNoiCap(), noiCap));
		compare.setSoTienVay(compare(info.getSoTienVay(), soTienVay));
		compare.setThoiHanVay(compare(info.getThoiHanVay(), thoiHanVay));
		compare.setLaiSuatVay(compare(info.getLaiSuatVay(), laiSuatVay));
	}

	private static void compareTienMat(ContractCompareResult compare, ContractInfo info, String soTaiKhoanNguoiNhan,
			String nganHangNhan, String daiLyChiHo) {

		compare.setSoTaiKhoanNguoiNhan(compare(info.getSoTaiKhoanNguoiNhan(), soTaiKhoanNguoiNhan));
		compare.setChiNhanhNganHang(compare(info.getNganHangNhan(), nganHangNhan));
		compare.setDaiLyChiHo(compare(info.getDaiLyChiHo(), daiLyChiHo));

	}

	private static void compareBaoHiem(ContractCompareResult compare, ContractInfo info, String phiBaoHiem,
			String tenCongTyBaoHiem) {

		compare.setPhiBaoHiem(compare(info.getPhiBaoHiem(), phiBaoHiem));
		compare.setTenCongTyBaoHiem(compare(info.getTenCongTyBaoHiem(), tenCongTyBaoHiem));

	}

	private static void compareTraGop(ContractCompareResult compare, ContractInfo info, String tenHangHoa,
			String nhanHieu, String mauMa, String soKhung, String soMay, String pos) {
		List<String> lstHangHoa = new ArrayList<>();
		List<String> lstNhanHieu = new ArrayList<>();
		List<String> lstMauMa = new ArrayList<>();
		List<String> lstSoKhung = new ArrayList<>();
		List<String> lstSoMay = new ArrayList<>();

		if (null != tenHangHoa) {
			lstHangHoa = Arrays.asList(tenHangHoa.split(",_,"));
		}

		if (null != nhanHieu) {
			lstNhanHieu = Arrays.asList(nhanHieu.split(",_,"));
		}

		if (null != mauMa) {
			lstMauMa = Arrays.asList(mauMa.split(",_,"));
		}

		if (null != soKhung) {
			lstSoKhung = Arrays.asList(soKhung.split(",_,"));
		}

		if (null != soMay) {
			lstSoMay = Arrays.asList(soMay.split(",_,"));
		}

		List<String> scanProducts = info.getLstProduct();
		compare.setScanProducts(scanProducts);
		List<QRProduct> dbProducts = new ArrayList<>();

		int index = 0;
		while (index < lstHangHoa.size()) {
			QRProduct prod = new QRProduct(index + 1, index < lstHangHoa.size() ? lstHangHoa.get(index) : "",
					index < lstNhanHieu.size() ? lstNhanHieu.get(index) : "",
					index < lstMauMa.size() ? lstMauMa.get(index) : "",
					index < lstSoKhung.size() ? lstSoKhung.get(index) : "",
					index < lstSoMay.size() ? lstSoMay.get(index) : "");
			
			index++;
			dbProducts.add(prod);

		}

		compare.setDbProducts(dbProducts);

		compare.setPos(compare(info.getPos(), pos));

	}

	private static CompareInfoDTO compare(String scan, String db) {
		if (null == scan && null == db) {
			return new CompareInfoDTO(scan, db, ContractEnum.GIONG.value());
		} else if (null == scan || null == db) {
			return new CompareInfoDTO(scan, db, ContractEnum.KHAC.value());
		}
		String ckScan = scan.toLowerCase();
		String ckDb = db.toLowerCase();
		if (ckScan.equals(ckDb)) {
			return new CompareInfoDTO(scan, db, ContractEnum.GIONG.value());
		} else {
			return new CompareInfoDTO(scan, db, ContractEnum.KHAC.value());
		}
	}
}
