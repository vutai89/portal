
package com.mcredit.model.alfresco.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

    @SerializedName("entry")
    @Expose
    private EntryObject entry;

    public EntryObject getEntry() {
        return entry;
    }

    public void setEntry(EntryObject entry) {
        this.entry = entry;
    }

}
