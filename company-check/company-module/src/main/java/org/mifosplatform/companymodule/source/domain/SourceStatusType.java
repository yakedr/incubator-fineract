package org.mifosplatform.companymodule.source.domain;

/**
 * Enum representation of {@link Source} status.
 */

public enum SourceStatusType {

    INVALID(0, "sourceStatusType.invalid"),
    PENDING_APPROVAL(100, "sourceStatusType.pending.approval"),
    APPROVED(200, "sourceStatusType.approved"),
    REJECTED(500, "sourceStatusType.rejected");

    private final Integer value;
    private final String code;

    public static SourceStatusType fromInt(final Integer value) {

        SourceStatusType enumeration = SourceStatusType.INVALID;
        switch (value){
            case 100:
                enumeration = SourceStatusType.PENDING_APPROVAL;
                break;
            case 200:
                enumeration = SourceStatusType.APPROVED;
                break;
            case 500:
                enumeration = SourceStatusType.REJECTED;
                break;
        }
        return enumeration;
    }

    private SourceStatusType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasStateOf(final SourceStatusType state) {
        return this.value.equals(state.getValue());
    }

    public boolean isPendingApproval() {
        return this.value.equals(SourceStatusType.PENDING_APPROVAL.getValue());
    }

    public boolean isApproved(){
        return this.value.equals(SourceStatusType.APPROVED.getValue());
    }

    public boolean isRejected(){
        return this.value.equals(SourceStatusType.REJECTED.getValue());
    }
}
