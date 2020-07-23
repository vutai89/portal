package com.mcredit.sharedbiz.manager;
import com.mcredit.model.enums.CacheTableName;
import com.mcredit.sharedbiz.aggregate.CacheRefreherAggregate;


public class CacheRefreherManager extends BaseManager{

	private CacheRefreherAggregate _agg = null;	
	
	public void listen(CacheTableName[] tables ) throws Throwable {
		_agg = new CacheRefreherAggregate(tables);
		_agg.listen();
	}

}
