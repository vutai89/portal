package com.mcredit.model.object.ecm;

import java.util.ArrayList;

import com.google.api.client.util.Key;

/**
 * @author oanhlt.ho
 */
public class List<T extends Entry> {
    @Key
    public ArrayList<T> entries;

    @Key
    public Pagination pagination;
}
