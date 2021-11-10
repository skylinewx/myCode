package com.example.juc.transaction;

import java.math.BigDecimal;

/**
 * 账户对象变化
 *
 * @author wangxing
 */
public class AccountChangeDTO {
    private String userId;
    private BigDecimal changeMoney;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(BigDecimal changeMoney) {
        this.changeMoney = changeMoney;
    }

    @Override
    public String toString() {
        return "AccountChangeDTO{" +
                "userId='" + userId + '\'' +
                ", changeMoney=" + changeMoney +
                '}';
    }
}
