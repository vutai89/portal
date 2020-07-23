/**
 * 
 */
package com.mcredit.model.dto.card;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.mcredit.model.dto.CardInformationDTO;

/**
 * @author anhdv.ho
 *
 */
public class ObjectResultSuccessDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1684947641878787414L;
	
	public ObjectResultSuccessDTO(CardInformationDTO obj) {
		setResult(obj);
	}
	
	private CardInformationDTO Result;

	public CardInformationDTO getResult() {
		return Result;
	}

	public void setResult(CardInformationDTO result) {
		Result = result;
	}
}
