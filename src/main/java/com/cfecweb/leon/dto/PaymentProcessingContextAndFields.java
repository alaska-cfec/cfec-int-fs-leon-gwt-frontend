package com.cfecweb.leon.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Map;
import java.util.Objects;

public class PaymentProcessingContextAndFields implements IsSerializable {
    private String clientReferenceInformationCode;
    private String redirectUrl;
    private Map<String, String> fields;

    public PaymentProcessingContextAndFields(String clientReferenceInformationCode, String redirectUrl, Map<String, String> fields) {
        this.clientReferenceInformationCode = clientReferenceInformationCode;
        this.redirectUrl = redirectUrl;
        this.fields = fields;
    }

    public PaymentProcessingContextAndFields() {
    }

    public String getClientReferenceInformationCode() {
        return clientReferenceInformationCode;
    }

    public void setClientReferenceInformationCode(String clientReferenceInformationCode) {
        this.clientReferenceInformationCode = clientReferenceInformationCode;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentProcessingContextAndFields)) return false;
        PaymentProcessingContextAndFields that = (PaymentProcessingContextAndFields) o;
        return Objects.equals(clientReferenceInformationCode, that.clientReferenceInformationCode) && Objects.equals(redirectUrl, that.redirectUrl) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientReferenceInformationCode, redirectUrl, fields);
    }

    @Override
    public String toString() {
        return "PaymentProcessingContextAndFields{" +
                "clientReferenceInformationCode='" + clientReferenceInformationCode + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", fields=" + fields +
                '}';
    }
}
