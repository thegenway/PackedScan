package com.dzw.packetscan.vo;

import com.dzw.packetscan.adapter.ResultDataAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;

public class WaybillOCR {

    //收件人姓名
    private String RecName;

    //收件人手机号
    private String RecNum;

    //收件人地址
    private String RecAddr;

    //寄件人姓名
    private String SenderName;

    //寄件人手机号
    private String SenderNum;

    //寄件人地址
    private String SenderAddr;

    //运单号
    private String WaybillNum;

    public String getRecName() {
        return RecName;
    }

    public void setRecName(String recName) {
        RecName = recName;
    }

    public String getRecNum() {
        return RecNum;
    }

    public void setRecNum(String recNum) {
        RecNum = recNum;
    }

    public String getRecAddr() {
        return RecAddr;
    }

    public void setRecAddr(String recAddr) {
        RecAddr = recAddr;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getWaybillNum() {
        return WaybillNum;
    }

    public void setWaybillNum(String waybillNum) {
        WaybillNum = waybillNum;
    }

    public String getSenderNum() {
        return SenderNum;
    }

    public void setSenderNum(String senderNum) {
        SenderNum = senderNum;
    }

    public String getSenderAddr() {
        return SenderAddr;
    }

    public void setSenderAddr(String senderAddr) {
        SenderAddr = senderAddr;
    }
    
    public List<Dict> getList() {
        List<Dict> list = new ArrayList<>();
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "收件人姓名").set(ResultDataAdapter.VALUE_KEY, RecName));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "收件人手机号").set(ResultDataAdapter.VALUE_KEY, RecNum));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "收件人地址").set(ResultDataAdapter.VALUE_KEY, RecAddr));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "寄件人姓名").set(ResultDataAdapter.VALUE_KEY, SenderName));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "寄件人手机号").set(ResultDataAdapter.VALUE_KEY, SenderNum));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "寄件人地址").set(ResultDataAdapter.VALUE_KEY, SenderAddr));
        list.add(Dict.create().set(ResultDataAdapter.TITLE_KEY, "运单号").set(ResultDataAdapter.VALUE_KEY, WaybillNum));
        return list;
    }
}
