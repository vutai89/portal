package com.mcredit.business.pcb.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.mcredit.business.pcb.aggregate.PCBAggregate;
import com.mcredit.business.pcb.jsonobject.PcbResponse;
import com.mcredit.business.pcb.validation.PCBValidation;
import com.mcredit.model.dto.pcb.IdCheckPcbDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class PCBManager extends BaseManager {
	private PCBAggregate _agg;
	private PCBValidation _pCBValidation = new PCBValidation();

	public PCBManager() {
		_agg = new PCBAggregate(this.uok);
	}

	/**
	 * @author sonhv.ho
	 * @param payload : thong tin check PCB (BPM gui sang)
	 * @param appId : appId tuong ung voi ho so
	 * @return
	 * @throws Exception
	 */
	public ArrayList<IdCheckPcbDTO> checkPCB(Object[] payload, String appId, String idCard,String idCard1, String birthDay,String stepBpm,String type,Integer s37Result,Double loanAmount ) throws Exception {
		return this.tryCatch(() -> {
			_pCBValidation.validateCheckPcb(payload, appId, idCard, birthDay);
			return _agg.checkPCB(payload, appId.trim(), idCard.trim(),idCard1.trim(), new SimpleDateFormat("dd-MM-yyyy").parse(birthDay),stepBpm,type,s37Result,loanAmount,"B");
		});
	}

	/**
	 * @author sonhv.ho
	 * @param id : id cua bang CREDIT_BUREAU_DATA
	 * @return PcbResponse : Thong tin check PCB
	 * @throws Exception
	 */
	public PcbResponse getPcbInfo(String id,String channel) throws Exception {
		return this.tryCatch(() -> {
			return _agg.getPcbInfo(id,channel);
		});

	}
}
