package com.example.zhongjianyusuan;

import java.util.List;

public class HyperModalDO {
    private String code;
    private String name;
    private List<String> PeriodType;
    private ModalType modalType;
    private PublishState publishState;
    private List<ModalField> dimList;
    private List<ModalField> meas;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModalType getModalType() {
        return modalType;
    }

    public void setModalType(ModalType modalType) {
        this.modalType = modalType;
    }

    public PublishState getPublishState() {
        return publishState;
    }

    public void setPublishState(PublishState publishState) {
        this.publishState = publishState;
    }

    public List<ModalField> getDimList() {
        return dimList;
    }

    public void setDimList(List<ModalField> dimList) {
        this.dimList = dimList;
    }

    public List<ModalField> getMeas() {
        return meas;
    }

    public void setMeas(List<ModalField> meas) {
        this.meas = meas;
    }

    public List<String> getPeriodType() {
        return PeriodType;
    }

    public void setPeriodType(List<String> periodType) {
        PeriodType = periodType;
    }
}
