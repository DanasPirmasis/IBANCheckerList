package seb.homework.iban.types;

import java.util.HashMap;
import java.util.Map;

public enum BankValuesEnum {

    DEFAULT("", ""),

    SEB("70440", "UAB SEB Bankas"),
    SWEDBANK("73000", "UAB SwedBank Bankas"),
    LUMINOR("40100", "Luminor Bank");

    private final String bankCode;
    private final String bankDescription;

    BankValuesEnum(String bankCode, String bankDescription) {
        this.bankCode = bankCode;
        this.bankDescription = bankDescription;
    }

    private static final Map<String, BankValuesEnum> banksByCode;

    static {
        banksByCode = new HashMap<>();
        for (BankValuesEnum v : BankValuesEnum.values()) {
            banksByCode.put(v.bankCode, v);
        }
    }

    public static BankValuesEnum getByBankCode(String bankCode) {
        BankValuesEnum bankValuesEnum = banksByCode.get(bankCode);
        return bankValuesEnum != null ? bankValuesEnum : DEFAULT;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankDescription() {
        return bankDescription;
    }

}