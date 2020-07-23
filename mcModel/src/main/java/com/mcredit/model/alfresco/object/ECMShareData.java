
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ECMShareData {

    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("entries")
    @Expose
    private java.util.List<Entry> entries ;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public java.util.List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(java.util.List<Entry> entries) {
        this.entries = entries;
    }

}
