/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.cache.engine;

import gnu.trove.set.hash.TLongHashSet;


/**
 * @author <a href="mailto:wanglianguang@139130.netGuang Wang</a>
 * @Data 2010-6-27
 * @Version 1.0.0
 */
public class LongHashCache implements PhoneCache {
    private static final long MAX_PHONE_NUMBER = 0x174876E800L;
    public final static int INIT_CAPACITY = 100000;
    public final static float LOAD_FACTORY = 0.5f;

    /**
     * The repository
     */
    TLongHashSet store;

    public LongHashCache() {
        store = new TLongHashSet(INIT_CAPACITY, LOAD_FACTORY);
    }

    public LongHashCache(int capacity) {
        store = new TLongHashSet(capacity, LOAD_FACTORY);
    }

    public LongHashCache(int capacity, float loadFactory) {
        store = new TLongHashSet(capacity, loadFactory);
    }

    @Override
    public int getSize() {
        return store.size();
    }

    @Override
    public long tran2Code(String phone, int type, int target) {
        long tPhone = Long.parseLong(phone);
        if (tPhone >= MAX_PHONE_NUMBER)
            throw new RuntimeException("Bad phone number exceed the max size!");
        return tPhone << 27 | (((long) type & 0x7) << 24)
                | ((long) target & 0xFFFFFF);
    }

    @Override
    public void putPhonePara(String phone, int type, int target) {
        store.add(tran2Code(phone, type, target));
    }

    @Override
    public void putPhonePara(long phone, int type, int target) {
        if (phone >= MAX_PHONE_NUMBER)
            throw new RuntimeException("Bad phone number exceed the max size!");
        store.add(phone << 27 | (((long) type & 0x7) << 24)
                | ((long) target & 0xFFFFFF));
    }

    @Override
    public void removePhonePara(String phone, int type, int target) {
        store.remove(tran2Code(phone, type, target));
    }

    @Override
    public void removePhonePara(long phone, int type, int target) {
        store.remove(phone << 27 | (((long) type & 0x7) << 24)
                | ((long) target & 0xFFFFFF));
    }

    @Override
    public boolean containPhonePara(String phone, int type, int target) {
        return store.contains(tran2Code(phone, type, target));
    }

    @Override
    public boolean containPhonePara(long phone, int type, int target) {
        return store.contains(phone << 27 | (((long) type & 0x7) << 24)
                | ((long) target & 0xFFFFFF));
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public void putPhonePara(long zipMes) {
        store.add(zipMes);
    }

    @Override
    public void removePhonePara(long zipMes) {
        store.remove(zipMes);
    }
}
