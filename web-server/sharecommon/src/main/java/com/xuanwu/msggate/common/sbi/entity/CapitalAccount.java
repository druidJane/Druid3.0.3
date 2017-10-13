/*
 * Copyright (c) 2010 by XUANWU INFORMATION TECHNOLOGY CO. 
 *             All rights reserved                         
 */
package com.xuanwu.msggate.common.sbi.entity;

/**
 * Capital account
 * 
 * @author <a href="mailto:hotstong@gmail.com">LianGuang Wang</a>
 * @Data 2010-8-18
 * @Version 1.0.0
 */
public class CapitalAccount {
    public enum BillingType {
        NORMAL(0), MONTH(1);
        private final int index;

        private BillingType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public static BillingType getType(int index) {
            switch (index) {
            case 0:
                return NORMAL;
            case 1:
                return MONTH;
            default:
                return NORMAL;
            }
        }
    }

    /**
     * Identity
     */
    private int id;

    /**
     * Account type
     */
    private BillingType type;

    /**
     * Version
     */
    private int version;

    /**
     * Balance of the account
     */
    private float balance;
    
    /** Parent capital account */
    private CapitalAccount parent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BillingType getType() {
        return type;
    }

    public void setType(BillingType type) {
        this.type = type;
    }

    public void setTypeIndex(int index) {
        this.type = BillingType.getType(index);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

	public CapitalAccount getParent() {
		return parent;
	}

	public void setParent(CapitalAccount parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "CapitalAccount [id=" + id + ", type=" + type + ", version="
				+ version + ", balance=" + balance + ", parent="
				+ parent + "]";
	}
}
