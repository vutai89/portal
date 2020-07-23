package com.mcredit.model.dto.warehouse;

import java.util.List;

public class ContractInfo {
	private String shd;
	private String ngayKyHopDong;
	private String hoTenKhachHang;
	private String soCMT;
	private String ngayCap;
	private String noiCap;
	private String soTienVay;
	private String thoiHanVay;
	private String laiSuatVay;
	private String phiBaoHiem;
	private String tenCongTyBaoHiem;
	private String soTaiKhoanNguoiNhan;
	private String nganHangNhan;
	private String daiLyChiHo;

	private List<String> lstProduct;
	private String pos;

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getShd() {
		return shd;
	}

	public void setShd(String shd) {
		this.shd = shd;
	}

	public String getNgayKyHopDong() {
		return ngayKyHopDong;
	}

	public void setNgayKyHopDong(String ngayKyHopDong) {
		this.ngayKyHopDong = ngayKyHopDong;
	}

	public String getHoTenKhachHang() {
		return hoTenKhachHang;
	}

	public void setHoTenKhachHang(String hoTenKhachHang) {
		this.hoTenKhachHang = hoTenKhachHang;
	}

	public String getSoCMT() {
		return soCMT;
	}

	public void setSoCMT(String soCMT) {
		this.soCMT = soCMT;
	}

	public String getNgayCap() {
		return ngayCap;
	}

	public void setNgayCap(String ngayCap) {
		this.ngayCap = ngayCap;
	}

	public String getNoiCap() {
		return noiCap;
	}

	public void setNoiCap(String noiCap) {
		this.noiCap = noiCap;
	}

	public String getSoTienVay() {
		return soTienVay;
	}

	public void setSoTienVay(String soTienVay) {
		this.soTienVay = soTienVay;
	}

	public String getThoiHanVay() {
		return thoiHanVay;
	}

	public void setThoiHanVay(String thoiHanVay) {
		this.thoiHanVay = thoiHanVay;
	}

	public String getLaiSuatVay() {
		return laiSuatVay;
	}

	public void setLaiSuatVay(String laiSuatVay) {
		this.laiSuatVay = laiSuatVay;
	}

	public String getPhiBaoHiem() {
		return phiBaoHiem;
	}

	public void setPhiBaoHiem(String phiBaoHiem) {
		this.phiBaoHiem = phiBaoHiem;
	}

	public String getTenCongTyBaoHiem() {
		return tenCongTyBaoHiem;
	}

	public void setTenCongTyBaoHiem(String tenCongTyBaoHiem) {
		this.tenCongTyBaoHiem = tenCongTyBaoHiem;
	}

	public String getSoTaiKhoanNguoiNhan() {
		return soTaiKhoanNguoiNhan;
	}

	public void setSoTaiKhoanNguoiNhan(String soTaiKhoanNguoiNhan) {
		this.soTaiKhoanNguoiNhan = soTaiKhoanNguoiNhan;
	}

	public String getNganHangNhan() {
		return nganHangNhan;
	}

	public void setNganHangNhan(String nganHangNhan) {
		this.nganHangNhan = nganHangNhan;
	}

	public String getDaiLyChiHo() {
		return daiLyChiHo;
	}

	public void setDaiLyChiHo(String daiLyChiHo) {
		this.daiLyChiHo = daiLyChiHo;
	}

	public List<String> getLstProduct() {
		return lstProduct;
	}

	public void setLstProduct(List<String> lstProduct) {
		this.lstProduct = lstProduct;
	}

	public String toString() {
		return String.format("%s - %s - %s - %s - %s - %s - %s - %s - %s - %s - %s - %s - %s", getShd(),
				getNgayKyHopDong(), getHoTenKhachHang(), getSoCMT(), getNgayCap(), getNoiCap(), getSoTienVay(),
				getThoiHanVay(), getLaiSuatVay(), getPhiBaoHiem(), getTenCongTyBaoHiem(), getDaiLyChiHo(), getPos());
	}

	public ContractInfo() {
		super();
	}

	public ContractInfo(String shd, String ngayKyHopDong, String hoTenKhachHang, String soCMT, String ngayCap,
			String noiCap, String soTienVay, String thoiHanVay, String laiSuatVay, String phiBaoHiem,
			String tenCongTyBaoHiem, String soTaiKhoanNguoiNhan, String nganHangNhan, String daiLyChiHo,
			List<String> lstProduct, String pos) {
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
		this.nganHangNhan = nganHangNhan;
		this.daiLyChiHo = daiLyChiHo;
		this.lstProduct = lstProduct;
		this.pos = pos;
	}

}
