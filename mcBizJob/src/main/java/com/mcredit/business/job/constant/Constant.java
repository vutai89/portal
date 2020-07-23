package com.mcredit.business.job.constant;

public class Constant {
	public static interface SEND_MAIL {
		String SUBJECT = "Thông báo v/v hoàn thiện case [caseNumber] – Khách hàng: [custName]";
		//style=\"color:#2A6C89\"
		String BODY = "<div>"
				+ "	Kính gửi: Anh/chị </br> "
				+ " 	Đây là thông báo tự động từ hệ thống, đề nghị Anh/chị vui lòng hoàn thiện case: </br>"
				+ " •	Số case: caseNumber </br>"
				+ " •	Họ và tên khách hàng: custName </br>"
				+ " •	Số hợp đồng: contractCode        Ký ngày: contractDate </br>"
				+ " •	Trạng thái: status </br>"
				+ " •	Lý do: reason </br>"
				+ "     Mọi thông tin vui lòng liên hệ BP Giải ngân qua email: <u style=\"color:blue\"> Bophangiainganttvh@mcredit.com.vn </u>"
				+ "</div>";
	}

}
