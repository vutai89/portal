package com.mcredit.model.dto.warehouse;

public class QRProduct {
	private int stt;
	private String tenHangHoa;
	private String nhanHieu;
	private String mauMa;
	private String soKhung;
	private String soMay;
	
	

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public String getTenHangHoa() {
		return tenHangHoa;
	}

	public void setTenHangHoa(String tenHangHoa) {
		this.tenHangHoa = tenHangHoa;
	}

	public String getNhanHieu() {
		return nhanHieu;
	}

	public void setNhanHieu(String nhanHieu) {
		this.nhanHieu = nhanHieu;
	}

	public String getMauMa() {
		return mauMa;
	}

	public void setMauMa(String mauMa) {
		this.mauMa = mauMa;
	}

	public String getSoKhung() {
		return soKhung;
	}

	public void setSoKhung(String soKhung) {
		this.soKhung = soKhung;
	}

	public String getSoMay() {
		return soMay;
	}

	public void setSoMay(String soMay) {
		this.soMay = soMay;
	}

	

	public QRProduct(int stt, String tenHangHoa, String nhanHieu, String mauMa, String soKhung, String soMay) {
		super();
		this.stt = stt;
		this.tenHangHoa = tenHangHoa;
		this.nhanHieu = nhanHieu;
		this.mauMa = mauMa;
		this.soKhung = soKhung;
		this.soMay = soMay;
	}

	public QRProduct() {
		super();
		this.stt = 0;
		this.tenHangHoa = "";
		this.nhanHieu = "";
		this.mauMa = "";
		this.soKhung = "";
		this.soMay = "";
	}


}
