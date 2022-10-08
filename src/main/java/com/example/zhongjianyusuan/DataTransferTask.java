package com.example.zhongjianyusuan;

import java.util.List;
import java.util.Map;

public class DataTransferTask {
    private int batchNum;
    private List<Map<String, Object>> dataList;

    public int getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(int batchNum) {
        this.batchNum = batchNum;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
