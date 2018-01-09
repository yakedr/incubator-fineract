package org.mifosplatform.companymodule.company.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.mifosplatform.companymodule.source.data.SourceData;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompanyData implements Serializable {

    private Long id;
    private String name;
    private String ruc;
    private String dateOperationsStart;
    private int yearsOperating;
    private int cantEmployees;
    private Long sourceId;
    private SourceData source;
    private String sourceName;
    private String status;
    private String createdOn;
    private String createdBy;
    private String approvedOn;
    private String approvedBy;
    private String rejectedOn;
    private String rejectedBy;
    private String lastModifiedOn;
    private String lastModifiedBy;


    private CompanyData(final Long id, final String name, String ruc, String dateOperationsStart, int cantEmployees, Long sourceId, String sourceName, final String status, final String createdOn, final String createdBy,
                        final String approvedOn, final String approvedBy, final String rejectedOn, final String rejectedBy,
                        final String lastModifiedOn, final String lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.ruc = ruc;
        this.dateOperationsStart = dateOperationsStart;
        this.cantEmployees = cantEmployees;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.approvedOn = approvedOn;
        this.approvedBy = approvedBy;
        this.rejectedOn = rejectedOn;
        this.rejectedBy = rejectedBy;
        this.lastModifiedOn = lastModifiedOn;
        this.lastModifiedBy = lastModifiedBy;

        Period period = new Period(LocalDate.parse(this.dateOperationsStart), LocalDate.now());
        this.yearsOperating = period.getYears();
    }

    public static CompanyData instance(final Long id, final String name, String ruc, String dateOperationsStart, int cantEmployees, final Long sourceId, final String sourceName, final String status, final String createdOn, final String createdBy,
                                       final String approvedOn, final String approvedBy, final String rejectedOn, final String rejectedBy,
                                       final String lastModifiedOn, final String lastModifiedBy) {

        return new CompanyData(id, name, ruc, dateOperationsStart, cantEmployees, sourceId, sourceName, status, createdOn, createdBy, approvedOn, approvedBy, rejectedOn, rejectedBy, lastModifiedOn, lastModifiedBy);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRuc() {
        return ruc;
    }

    public String getDateOperationsStart() {
        return dateOperationsStart;
    }

    public int getYearsOperating() {
        return yearsOperating;
    }

    public int getCantEmployees() {
        return cantEmployees;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public String getRejectedOn() {
        return rejectedOn;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
}
