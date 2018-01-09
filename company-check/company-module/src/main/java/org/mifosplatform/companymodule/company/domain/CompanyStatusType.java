package org.mifosplatform.companymodule.company.domain;

/**
 * Enum representation of {@link Company} status.
 */

public enum CompanyStatusType {

    INVALID(0, "companyStatusType.invalid"),
    PENDING_APPROVAL(100, "companyStatusType.pending.approval"),
    APPROVED(200, "companyStatusType.approved"),
    REJECTED(500, "companyStatusType.rejected");

    private final Integer value;
    private final String code;

    public static CompanyStatusType fromInt(final Integer value) {

        CompanyStatusType enumeration = CompanyStatusType.INVALID;
        switch (value){
            case 100:
                enumeration = CompanyStatusType.PENDING_APPROVAL;
                break;
            case 200:
                enumeration = CompanyStatusType.APPROVED;
                break;
            case 500:
                enumeration = CompanyStatusType.REJECTED;
                break;
        }
        return enumeration;
    }

    private CompanyStatusType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasStateOf(final CompanyStatusType state) {
        return this.value.equals(state.getValue());
    }

    public boolean isPendingApproval() {
        return this.value.equals(CompanyStatusType.PENDING_APPROVAL.getValue());
    }

    public boolean isApproved(){
        return this.value.equals(CompanyStatusType.APPROVED.getValue());
    }

    public boolean isRejected(){
        return this.value.equals(CompanyStatusType.REJECTED.getValue());
    }
}
