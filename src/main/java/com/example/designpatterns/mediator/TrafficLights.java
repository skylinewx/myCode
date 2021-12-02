package com.example.designpatterns.mediator;

/**
 * 红绿灯
 *
 * @author skyline
 */
public class TrafficLights implements SignalLamp {
    /**
     * 状态，0红灯亮，东西方向的车不能走，1绿灯亮，东西方向的车可以走
     */
    private int status;

    /**
     * 状态，0红灯亮，东西方向的车不能走，1绿灯亮，东西方向的车可以走
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean canGo(Direction direction) {
        if (status == 1) {
            return direction == Direction.EW;
        } else {
            return direction == Direction.SN;
        }
    }

    @Override
    public String toString() {
        return "TrafficLights{" +
                "status=" + status +
                '}';
    }
}
