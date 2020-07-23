/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.business.gendoc.manager;

import com.mcredit.business.gendoc.aggregate.GendocAggregate;
import com.mcredit.business.gendoc.validation.GendocValidation;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;

import java.text.ParseException;
import java.util.Map;

public class GendocManager extends BaseManager {

    private GendocAggregate _GendocAggregate = null;
    private GendocValidation _genDocValidate = new GendocValidation();

    public GendocManager() {
        this._GendocAggregate = new GendocAggregate();
    }

    public Map<String, String> getfileGendoc(String appId, String typeOfDocument, String typeOfLoans, String userName, String appNum) throws Exception {        
        _genDocValidate.gendocFile(appId, typeOfDocument, typeOfLoans, userName, appNum);
        return this._GendocAggregate.genDocProvider(appId, typeOfDocument, typeOfLoans, userName, appNum);
    }

}
