package com.example.juc.transaction;

import java.math.BigDecimal;

/**
 * 账户对象
 *
 * @author wangxing
 */
public class AccountDO {
    private String userId;
    private BigDecimal money;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
