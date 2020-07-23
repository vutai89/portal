package com.mcredit.model.dto.warehouse;

import java.util.List;

public class ContractCompareResult {
	private CompareInfoDTO shd;
	private CompareInfoDTO ngayKyHopDong;
	private CompareInfoDTO hoTenKhachHang;
	private CompareInfoDTO soCMT;
	private CompareInfoDTO ngayCap;
	private CompareInfoDTO noiCap;
	private CompareInfoDTO soTienVay;
	private CompareInfoDTO thoiHanVay;
	private CompareInfoDTO laiSuatVay;
	private CompareInfoDTO phiBaoHiem;
	private CompareInfoDTO tenCongTyBaoHiem;
	private CompareInfoDTO soTaiKhoanNguoiNhan;
	private CompareInfoDTO chiNhanhNganHang;
	private CompareInfoDTO daiLyChiHo;

	private List<String> scanProducts;
	private List<QRProduct> dbProducts;
	private CompareInfoDTO pos;

	public CompareInfoDTO getShd() {
		return shd;
	}

	public void setShd(CompareInfoDTO shd) {
		this.shd = shd;
	}

	public CompareInfoDTO getNgayKyHopDong() {
		return ngayKyHopDong;
	}

	public void setNgayKyHopDong(CompareInfoDTO ngayKyHopDong) {
		this.ngayKyHopDong = ngayKyHopDong;
	}

	public CompareInfoDTO getHoTenKhachHang() {
		return hoTenKhachHang;
	}

	public void setHoTenKhachHang(CompareInfoDTO hoTenKhachHang) {
		this.hoTenKhachHang = hoTenKhachHang;
	}

	public CompareInfoDTO getSoCMT() {
		return soCMT;
	}

	public void setSoCMT(CompareInfoDTO soCMT) {
		this.soCMT = soCMT;
	}

	public CompareInfoDTO getNgayCap() {
		return ngayCap;
	}

	public void setNgayCap(CompareInfoDTO ngayCap) {
		this.ngayCap = ngayCap;
	}

	public CompareInfoDTO getNoiCap() {
		return noiCap;
	}

	public void setNoiCap(CompareInfoDTO noiCap) {
		this.noiCap = noiCap;
	}

	public CompareInfoDTO getSoTienVay() {
		return soTienVay;
	}

	public void setSoTienVay(CompareInfoDTO soTienVay) {
		this.soTienVay = soTienVay;
	}

	public CompareInfoDTO getThoiHanVay() {
		return thoiHanVay;
	}

	public void setThoiHanVay(CompareInfoDTO thoiHanVay) {
		this.thoiHanVay = thoiHanVay;
	}

	public CompareInfoDTO getLaiSuatVay() {
		return laiSuatVay;
	}

	public void setLaiSuatVay(CompareInfoDTO laiSuatVay) {
		this.laiSuatVay = laiSuatVay;
	}

	public CompareInfoDTO getPhiBaoHiem() {
		return phiBaoHiem;
	}

	public void setPhiBaoHiem(CompareInfoDTO phiBaoHiem) {
		this.phiBaoHiem = phiBaoHiem;
	}

	public CompareInfoDTO getTenCongTyBaoHiem() {
		return tenCongTyBaoHiem;
	}

	public void setTenCongTyBaoHiem(CompareInfoDTO tenCongTyBaoHiem) {
		this.tenCongTyBaoHiem = tenCongTyBaoHiem;
	}

	public CompareInfoDTO getSoTaiKhoanNguoiNhan() {
		return soTaiKhoanNguoiNhan;
	}

	public void setSoTaiKhoanNguoiNhan(CompareInfoDTO soTaiKhoanNguoiNhan) {
		this.soTaiKhoanNguoiNhan = soTaiKhoanNguoiNhan;
	}

	public CompareInfoDTO getDaiLyChiHo() {
		return daiLyChiHo;
	}

	public void setDaiLyChiHo(CompareInfoDTO daiLyChiHo) {
		this.daiLyChiHo = daiLyChiHo;
	}

	public List<String> getScanProducts() {
		return scanProducts;
	}

	public void setScanProducts(List<String> scanProducts) {
		this.scanProducts = scanProducts;
	}

	public List<QRProduct> getDbProducts() {
		return dbProducts;
	}

	public void setDbProducts(List<QRProduct> dbProducts) {
		this.dbProducts = dbProducts;
	}

	public CompareInfoDTO getPos() {
		return pos;
	}

	public void setPos(CompareInfoDTO pos) {
		this.pos = pos;
	}

	public CompareInfoDTO getChiNhanhNganHang() {
		return chiNhanhNganHang;
	}

	public void setChiNhanhNganHang(CompareInfoDTO chiNhanhNganHang) {
		this.chiNhanhNganHang = chiNhanhNganHang;
	}

	public ContractCompareResult(CompareInfoDTO shd, CompareInfoDTO ngayKyHopDong, CompareInfoDTO hoTenKhachHang,
			CompareInfoDTO soCMT, CompareInfoDTO ngayCap, CompareInfoDTO noiCap, CompareInfoDTO soTienVay,
			CompareInfoDTO thoiHanVay, CompareInfoDTO laiSuatVay, CompareInfoDTO phiBaoHiem,
			CompareInfoDTO tenCongTyBaoHiem, CompareInfoDTO soTaiKhoanNguoiNhan, CompareInfoDTO chiNhanhNganHang,
			CompareInfoDTO daiLyChiHo, List<String> scanProducts, List<QRProduct> dbProducts, CompareInfoDTO pos) {
		super();
		this.shd = shd;
		this.ngayKyHopDong = ngayKyHopDong;
		this.hoTenKhachHang = hoTenKhachHang;
		this.soCMT = soCMT;
		this.ngayCap = ngayCap;
		this.noiCap = noiCap;
		this.soTienVay = soTienVay;
		this.thoiHanVay = thoiHanVay;
		this.laiSuatVay = laiSuatVay;
		this.phiBaoHiem = phiBaoHiem;
		this.tenCongTyBaoHiem = tenCongTyBaoHiem;
		this.soTaiKhoanNguoiNhan = soTaiKhoanNguoiNhan;
		this.chiNhanhNganHang = chiNhanhNganHang;
		this.daiLyChiHo = daiLyChiHo;
		this.scanProducts = scanProducts;
		this.dbProducts = dbProducts;
		this.pos = pos;
	}

	public ContractCompareResult() {
		this.shd = null;
		this.ngayKyHopDong = null;
		this.hoTenKhachHang = null;
		this.soCMT = null;
		this.ngayCap = null;
		this.noiCap = null;
		this.soTienVay = null;
		this.thoiHanVay = null;
		this.laiSuatVay = null;
		this.phiBaoHiem = null;
		this.tenCongTyBaoHiem = null;
		this.soTaiKhoanNguoiNhan = null;
		this.chiNhanhNganHang = null;
		this.daiLyChiHo = null;
		this.scanProducts = null;
		this.dbProducts = null;
		this.pos = null;
	}

}
