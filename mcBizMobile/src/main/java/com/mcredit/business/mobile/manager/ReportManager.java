package com.mcredit.business.mobile.manager;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import com.mcredit.business.mobile.aggregate.ReportAggregate;
import com.mcredit.business.mobile.factory.MobileFactory;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.sharedbiz.dto.UserDTO;

public class ReportManager implements Closeable{

	private UserDTO _user;
	private ReportAggregate _docAgg = new ReportAggregate();
	
	public ReportManager(UserDTO user) {
		_user = user;
	}
	
	public List<DataReport> getReport(String dateExport) throws Exception {
		MobileFactory.getReportValidation().validateReport(dateExport);
		return this._docAgg.getReport(dateExport, this._user);
	}
	
	public List<ApprovalReport> getApprovalReport(String dateExport) throws Exception {
		MobileFactory.getReportValidation().validateReport(dateExport);
		return _docAgg.getApprovalReport(dateExport, this._user);
	}

	@Override
	public void close() throws IOException {
	}
	
}
