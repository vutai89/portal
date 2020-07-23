package com.mcredit.data.warehouse;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.warehouse.entity.WhCavetInfo;
import com.mcredit.data.warehouse.repository.WareHouseCavetInfoRepository;
import com.mcredit.data.warehouse.repository.WareHouseDocumentBorrowRepository;
import com.mcredit.data.warehouse.repository.WhCodeRepository;
import com.mcredit.data.warehouse.repository.WhDocumentChangeRepository;
import com.mcredit.data.warehouse.repository.WhDocumentErrRepository;
import com.mcredit.data.warehouse.repository.WhDocumentRepository;
import com.mcredit.data.warehouse.repository.WhExpectedDateRepository;
import com.mcredit.data.warehouse.repository.WhMapDocCodeRepository;

public class UnitOfWorkWareHouse extends BaseUnitOfWork {

	@SuppressWarnings("rawtypes")
	private IRepository _whDocumentRepository = null;

	@SuppressWarnings("rawtypes")
	private IRepository _whDocumentChangeRepository = null;

	@SuppressWarnings("rawtypes")
	private IRepository _whCodeRepository = null;

	@SuppressWarnings("rawtypes")
	private IRepository _whMapDocCodeRepository = null;


	private IRepository<WhCavetInfo> _whCavetInfoRepository = null;

	@SuppressWarnings("rawtypes")
	private IRepository _wareHouseDocBorrowRepository = null;
	
	@SuppressWarnings("rawtypes")
	private IRepository _whExpectedDateRepository = null;
	
/*	@SuppressWarnings("rawtypes")    //Khong thay dumg
	private IRepository _whQRCodeRepository = null;*/
	
	@SuppressWarnings("rawtypes")
	private IRepository _whDocumentErrRepository = null;

	public UnitOfWorkWareHouse() {
		super();
	}

	public UnitOfWorkWareHouse(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	
	public WhDocumentRepository whDocumentRepo() {

		if (_whDocumentRepository == null) {
			_whDocumentRepository = new WhDocumentRepository(this.session);
		}

		return (WhDocumentRepository) _whDocumentRepository;
	}

	public WhDocumentChangeRepository whDocumentChangeRepo() {

		if (_whDocumentChangeRepository == null) {
			_whDocumentChangeRepository = new WhDocumentChangeRepository(
					this.session);
		}

		return (WhDocumentChangeRepository) _whDocumentChangeRepository;
	}

	public WhCodeRepository whCodeRepos() {

		if (_whCodeRepository == null) {
			_whCodeRepository = new WhCodeRepository(this.session);
		}

		return (WhCodeRepository) _whCodeRepository;
	}

	public WhMapDocCodeRepository whMapDocCodeRepo() {

		if (_whMapDocCodeRepository == null) {
			_whMapDocCodeRepository = new WhMapDocCodeRepository(this.session);
		}

		return (WhMapDocCodeRepository) _whMapDocCodeRepository;
	}

	public WareHouseDocumentBorrowRepository wareHouseBorrowDocumentRepository() {

		if (_wareHouseDocBorrowRepository == null) {
			_wareHouseDocBorrowRepository = new WareHouseDocumentBorrowRepository(
					this.session);
		}

		return (WareHouseDocumentBorrowRepository) _wareHouseDocBorrowRepository;
	}


	public WareHouseCavetInfoRepository WareHouseCavetInfoRepository() {
		if (_whCavetInfoRepository == null) {
			_whCavetInfoRepository = new WareHouseCavetInfoRepository(
					this.session);
		}

		return (WareHouseCavetInfoRepository) _whCavetInfoRepository;
	}
	
	public WhDocumentErrRepository whDocumentErrRepo() {
		if (_whDocumentErrRepository == null) {
			_whDocumentErrRepository = new WhDocumentErrRepository(
					this.session);
		}

		return (WhDocumentErrRepository) _whDocumentErrRepository;
	}
	
	public WhExpectedDateRepository whExpectedDateRepo() {
		if (_whExpectedDateRepository == null) {
			_whExpectedDateRepository = new WhExpectedDateRepository(
					this.session);
		}

		return (WhExpectedDateRepository) _whExpectedDateRepository;
	}

	@Override
	public void reset() {
		super.reset();
		_whDocumentRepository = null;
		_whDocumentChangeRepository = null;
		_whCodeRepository = null;
		_whMapDocCodeRepository = null;
		//_whQRCodeRepository = null;
		_whDocumentErrRepository = null;
		_whExpectedDateRepository = null ;
		_whDocumentRepository = new WhDocumentRepository(this.session);
		_whDocumentChangeRepository = new WhDocumentChangeRepository(this.session);
		_whCodeRepository = new WhCodeRepository(this.session);
		_whMapDocCodeRepository = new WhMapDocCodeRepository(this.session);
		_whDocumentErrRepository = new WhDocumentErrRepository(this.session);
		_whExpectedDateRepository = new WhExpectedDateRepository(this.session);

	}

}
