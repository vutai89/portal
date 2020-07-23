package com.mcredit.model.dto.product;

public enum ProductCategoryEnum {
	IS_TW("IS_TW"), 					// CHO VAY MUA XE MÁY
	IS_CD("IS_CD"), 					// CHO VAY MUA THIẾT BỊ VÀ CÁC SẢN PHẨM CÔNG NGHỆ CAO
	CS_CAT("CS_CAT"), 					// CHO VAY TIỀN MẶT QUA LƯƠNG
	CS_BANK("CS_BANK"), 				// CHO VAY TIỀN MẶT QUA TÀI KHOẢN NGÂN HÀNG
	CS_EVN("CS_EVN"), 					// CHO VAY TIỀN MẶT QUA HÓA ĐƠN ĐIỆN,
	CS_INSURANCE("CS_INSURANCE"), 		// CHO VAY TIỀN MẶT QUA HỢP ĐỒNG BẢO HIỂM NHÂN THỌ
	CS_SELF_EMPLOY("CS_SELF_EMPLOY"), 	// CHO VAY TIỀN MẶT VỚI CHỦ HỘ KINH DOANH TẠI NHÀ
	CARD("CARD"), 					 	// SẢN PHẢM CARD
	CS_XS("CS_XS"),  					// CHO VAY ĐỐI VỚI KHÁCH HÀNG ĐÃ/ĐANG CÓ KHOẢN VAY TẠI MCREDIT
	CS_BHYT("CS_BHYT"),  				// CHO VAY ĐỐI VỚI KHÁCH HÀNG CÓ BẢO HIỂM Y TẾ
	CS_STAFF("CS_STAFF"),  				// CHO VAY ĐỐI VỚI KHÁCH HÀNG LÀ CÁN BỘ NHÂN VIÊN
	CS_COOPERATE("CS_COOPERATE"), 		// CHO VAY ĐỐI VỚI KHÁCH HÀNG NHẬN LƯƠNG QUA TÀI KHOẢN MB
	CS_XS_2("CS_XS_2"),  				// CHO VAY TIỀN MẶT BÁN CHÉO
	CS_LG("CS_LG"),  					// CHO VAY TIỀN MẶT KHÁCH HÀNG TỪ THIRD PARTY
	CS_MBIKE("CS_MBIKE"),  				// CHO VAY TIỂN MẶT ĐỐI VỚI KHÁCH HÀNG CÓ GIẤY CHỨNG NHẬN ĐĂNG KÝ XE MÁY
	CS_CF("CS_CF"),  					// CHO VAY TIỂN MẶT ĐỐI VỚI KHÁCH HÀNG ĐÃ/ĐANG CÓ KHOẢN VAY TẠI CÁC TCTD KHÁC
	CS_UBILL("CS_UBILL"),  				// CHO VAY TIỀN MẶT ĐỐI VỚI KHÁCH HÀNG CÁ NHÂN CÓ HÓA ĐƠN TIỆN ÍCH
	CS_SCO_TS("CS_SCO_TS"),  			// CHO VAY TIỀN MẶT ĐỐI VỚI K.H TRUSTING SOCIAL
	CS_LG_CIC("CS_LG_CIC"),  			// CHO VAY TIỀN MẶT ĐỐI VỚI KH LG CIC
	TW_NO_MRC("TW_NO_MRC"),  			// CHO VAY TRẢ GÓP KHÔNG LƯU GIỮ GIẤY CHỨNG NHẬN ĐĂNG KÝ XE MÁY
	CS_SY_MB("CS_SY_MB"),  				// CHO VAY TIỀN MẶT DÀNH CHO KHÁCH HÀNG THUỘC CÁC TỔ CHỨC/DOANH NGHIỆP NHẬN LƯƠNG QUA TKTT MB
	CS_RL("CS_RL"),  					// CHO VAY TIỀN MẶT THEO HẠN MỨC
	CREDIT_CARD("CREDIT_CARD");  		// THẺ TÍN DỤNG
	
	private final String value;

	ProductCategoryEnum(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
	
	public static ProductCategoryEnum from(String text) {
        for (ProductCategoryEnum b : ProductCategoryEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
