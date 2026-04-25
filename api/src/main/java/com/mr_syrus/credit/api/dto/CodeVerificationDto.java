package com.mr_syrus.credit.api.dto;

public class CodeVerificationDto {
    private String codeId;
    private String code;

    public String getCodeId() { return codeId; }
    public void setCodeId(String codeId) { this.codeId = codeId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}