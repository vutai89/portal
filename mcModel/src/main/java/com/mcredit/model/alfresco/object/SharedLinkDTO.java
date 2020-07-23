
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SharedLinkDTO {

    @SerializedName("list")
    @Expose
    private ECMShareData list;

    public ECMShareData getList() {
        return list;
    }

    public void setList(ECMShareData list) {
        this.list = list;
    }

}
