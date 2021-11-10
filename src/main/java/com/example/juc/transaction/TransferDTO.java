package com.example.juc.transaction;

import java.math.BigDecimal;

/**
 * 转账对象
 *
 * @author wangxing
 */
public class TransferDTO {
    private String fromUserId;
    private String toUserId;
    private BigDecimal money;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", money=" + money +
                '}';
    }
}
