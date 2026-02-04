package com.cfecweb.leon.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSessionSettings implements IsSerializable {
    private Integer userSessionTimeoutMillis;
    private Integer renewalYear;
    private String recaptchaSiteKey;
    private String recaptchaAction;

    public UserSessionSettings() {
    }

    public UserSessionSettings(Integer userSessionTimeoutMillis, Integer renewalYear, String recaptchaSiteKey, String recaptchaAction) {
        this.userSessionTimeoutMillis = userSessionTimeoutMillis;
        this.renewalYear = renewalYear;
        this.recaptchaSiteKey = recaptchaSiteKey;
        this.recaptchaAction = recaptchaAction;
    }

    public Integer getUserSessionTimeoutMillis() {
        return userSessionTimeoutMillis;
    }

    public void setUserSessionTimeoutMillis(Integer userSessionTimeoutMillis) {
        this.userSessionTimeoutMillis = userSessionTimeoutMillis;
    }

    public Integer getRenewalYear() {
        return renewalYear;
    }

    public void setRenewalYear(Integer renewalYear) {
        this.renewalYear = renewalYear;
    }

    public String getRecaptchaSiteKey() {
        return recaptchaSiteKey;
    }

    public void setRecaptchaSiteKey(String recaptchaSiteKey) {
        this.recaptchaSiteKey = recaptchaSiteKey;
    }

    public String getRecaptchaAction() {
        return recaptchaAction;
    }

    public void setRecaptchaAction(String recaptchaAction) {
        this.recaptchaAction = recaptchaAction;
    }
}
