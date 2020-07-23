
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendShareDTO {

    @SerializedName("entry")
    @Expose
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

}
