package com.mcredit.sharedbiz.manager;

import com.mcredit.model.dto.EventTableDTO;
import com.mcredit.model.enums.CacheTag;
import com.mcredit.sharedbiz.aggregate.CacheEventSourceAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;

public class CacheObserverManager {

	public static void run(CacheTag[] cacheTags) {        
        CacheEventSourceAggregate eventSource = new CacheEventSourceAggregate(cacheTags);

        eventSource.addObserver((obj, arg) -> {
            System.out.println("Received response: " + arg);
            
            try{
            	if(arg != null){
            		EventTableDTO tempValue = JSONConverter.toObject(String.valueOf(arg),EventTableDTO.class);
		            if(tempValue == null || tempValue.getRowIds() == null)
		            	return;
		            
		            @SuppressWarnings("resource")
					CacheManager cache = new CacheManager();
		            for (int i = 0; i < tempValue.getRowIds().size(); i++){
		            	
		            	if(!eventSource.isRefresh(CacheTag.from(tempValue.getRowIds().get(i).toString())))
		            		continue;
		            	
		            	System.out.println("START REFRESH CACHE FOR " + tempValue.getRowIds().get(i).toString());
		            	cache.refeshCache(tempValue.getRowIds().get(i).toString());
		            	System.out.println("DONE REFRESHING CACHE FOR " + tempValue.getRowIds().get(i).toString());
		            }
		            
	            }
            }catch(Throwable th){
            	th.printStackTrace();
            }
        });

        new Thread(eventSource).start();
    }
	
}
