package com.mcredit.model.object.ecm;
import com.google.api.client.util.Key;

/**
 * @author oanhlt.ho
 */
public class Pagination {
    @Key
    public int count;

    @Key
    public boolean hasMoreItems;

    @Key
    public int totalItems;

    @Key
    public int skipCount;

    @Key
    public int maxItems;
}
