package com.cfecweb.leon.dto;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class ClientPaymentContext implements BeanModelTag, IsSerializable {
    private String result;

    private ArenewEntity entity;
    private ArenewPayment payment;
    private List<ArenewChanges> changeList;
    private List<ArenewPermits> plist;
    private List<ArenewVessels> vlist;

    private boolean halred;
    private boolean sabred;
    private FeeTotals feeTotals;
    private boolean firstTime;
    private String ryear;
    private String pmtvesCount;
    private String topLeftText;

    public ClientPaymentContext() {}

    public ClientPaymentContext(String result,
                                ArenewEntity entity, ArenewPayment payment, List<ArenewChanges> changeList, List<ArenewPermits> plist, List<ArenewVessels> vlist,
                                boolean halred, boolean sabred, FeeTotals feeTotals, boolean firstTime, String ryear, String pmtvesCount, String topLeftText) {
        this.result = result;

        this.entity = entity;
        this.payment = payment;
        this.changeList = changeList;
        this.plist = plist;
        this.vlist = vlist;

        this.halred = halred;
        this.sabred = sabred;
        this.feeTotals = feeTotals;
        this.firstTime = firstTime;
        this.ryear = ryear;
        this.pmtvesCount = pmtvesCount;
        this.topLeftText = topLeftText;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArenewEntity getEntity() {
        return entity;
    }

    public void setEntity(ArenewEntity entity) {
        this.entity = entity;
    }

    public ArenewPayment getPayment() {
        return payment;
    }

    public void setPayment(ArenewPayment payment) {
        this.payment = payment;
    }

    public List<ArenewChanges> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<ArenewChanges> changeList) {
        this.changeList = changeList;
    }

    public List<ArenewPermits> getPlist() {
        return plist;
    }

    public void setPlist(List<ArenewPermits> plist) {
        this.plist = plist;
    }

    public List<ArenewVessels> getVlist() {
        return vlist;
    }

    public void setVlist(List<ArenewVessels> vlist) {
        this.vlist = vlist;
    }

    public boolean isHalred() {
        return halred;
    }

    public void setHalred(boolean halred) {
        this.halred = halred;
    }

    public boolean isSabred() {
        return sabred;
    }

    public void setSabred(boolean sabred) {
        this.sabred = sabred;
    }

    public FeeTotals getFeeTotals() {
        return feeTotals;
    }

    public void setFeeTotals(FeeTotals feeTotals) {
        this.feeTotals = feeTotals;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getRyear() {
        return ryear;
    }

    public void setRyear(String ryear) {
        this.ryear = ryear;
    }

    public String getPmtvesCount() {
        return pmtvesCount;
    }

    public void setPmtvesCount(String pmtvesCount) {
        this.pmtvesCount = pmtvesCount;
    }

    public String getTopLeftText() {
        return topLeftText;
    }

    public void setTopLeftText(String topLeftText) {
        this.topLeftText = topLeftText;
    }
}
