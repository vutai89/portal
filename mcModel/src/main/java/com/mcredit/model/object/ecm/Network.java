package com.mcredit.model.object.ecm;

import com.google.api.client.util.Key;

/**
 * @author oanhlt.ho
 */
public class Network {
    @Key
    public String id;

    @Key
    public boolean homeNetwork;

    //@Key
    //DateTime createdAt;

    @Key
    public boolean paidNetwork;

    @Key
    public boolean isEnabled;

    @Key
    public String subscriptionLevel;
}
