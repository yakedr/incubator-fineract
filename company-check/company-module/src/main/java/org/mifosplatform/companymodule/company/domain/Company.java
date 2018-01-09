package org.mifosplatform.companymodule.company.domain;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.mifosplatform.companymodule.source.domain.Source;
import org.mifosplatform.companymodule.utils.JsonHelper;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "m_company")
public class Company extends AbstractPersistable<Long>{

    @Column(name = "name", nullable = false)
    private String name;
	
	@Column(name = "ruc", nullable = false)
    private String ruc;
	
	@Column(name = "cant_employees", nullable = false)
    private Integer cantEmployees;
	
	@Column(name = "date_operations_start", nullable = false)
    private Date dateOperationsStart;

	@ManyToOne(optional = false)
    @JoinColumn(name = "sourceid")
    private Source source;

    @Column(name = "status_enum", nullable = false)
    private Integer status;

    @Column(name = "createdon_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @Column(name = "createdby_userid", nullable = true)
    private String createdBy;

    @Column(name = "approvedon_date")
    @Temporal(TemporalType.DATE)
    private Date approvedOn;

    @Column(name = "approvedby_userid", nullable = true)
    private String approvedBy;

    @Column(name = "rejectedon_date")
    @Temporal(TemporalType.DATE)
    private Date rejectedOn;

    @Column(name = "rejectedby_userid", nullable = true)
    private String rejectedBy;

    @Column(name = "lastmodifiedon_date")
    @Temporal(TemporalType.DATE)
    private Date lastModifiedOn;

    @Column(name = "lastmodifiedby_userid", nullable = true)
    private String lastModifiedBy;

    public Company() {
    }

    public Company(Source source, String name,Integer cantEmployees, String ruc, LocalDate operationsStart, Integer status, LocalDate createdOn, String createdBy, LocalDate approvedOn, String approvedBy,
                  LocalDate rejectedOn, String rejectedBy, String lastModifiedBy) {

        this.name = name;
        this.cantEmployees = cantEmployees;
        this.dateOperationsStart = operationsStart.toDateTimeAtStartOfDay().toDate();
        this.ruc = ruc;
        this.source = source;

        this.status = status;
        if (createdOn != null) {
            this.createdOn = createdOn.toDateTimeAtStartOfDay().toDate();
            this.lastModifiedOn = this.createdOn;
        }
        this.createdBy = createdBy;
        if (approvedOn != null) {
            this.approvedOn = approvedOn.toDateTimeAtStartOfDay().toDate();
            this.lastModifiedOn = this.approvedOn;
        }
        this.approvedBy = approvedBy;
        if (rejectedOn != null) {
            this.rejectedOn = rejectedOn.toDateTimeAtStartOfDay().toDate();
            this.lastModifiedOn = this.rejectedOn;
        }
        this.rejectedBy = rejectedBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public void approve(final Date approvedDate, final String approvedUser) {
        this.approvedOn = approvedDate;
        this.approvedBy = approvedUser;
        this.status = CompanyStatusType.APPROVED.getValue();

        this.lastModifiedOn = this.approvedOn;
        this.lastModifiedBy = approvedUser;
    }

    public void reject(final Date rejectedDate, final String rejectedUser) {
        this.rejectedOn = rejectedDate;
        this.rejectedBy = rejectedUser;
        this.status = CompanyStatusType.REJECTED.getValue();

        this.lastModifiedOn = this.rejectedOn;
        this.lastModifiedBy = rejectedUser;
    }

    public Map<String, Object> update(final JsonHelper command, final String updatedBy) {
        final Map<String, Object> actualChanges = new LinkedHashMap<>(7);

        final String nameParamName = "name";
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String employeesParamName = "cantEmployees";
        if (command.isChangeInIntegerParameterNamed(employeesParamName, this.cantEmployees)) {
            final int newValue = command.integerValueOfParameterNamed(employeesParamName);
            actualChanges.put(employeesParamName, newValue);
            this.cantEmployees = newValue;
        }

        final String localeParamName = "locale";
        final String dateFormatParamName = "dateFormat";
        final String startDateParamName = "dateOperationsStart";
        if (command.isChangeInLocalDateParameterNamed(startDateParamName, getLocalDate(this.dateOperationsStart))) {
            final String valueAsInput = command.stringValueOfParameterNamed(startDateParamName);
            actualChanges.put(startDateParamName, valueAsInput);
            actualChanges.put(dateFormatParamName, command.dateFormat());
            actualChanges.put(localeParamName, command.locale());

            final LocalDate newValue = command.localDateValueOfParameterNamed(startDateParamName);
            if (newValue != null) {
                this.dateOperationsStart = newValue.toDate();
            } else {
                this.dateOperationsStart = null;
            }
        }

        final String rucParamName = "ruc";
        if (command.isChangeInStringParameterNamed(rucParamName, this.ruc)) {
            final String newValue = command.stringValueOfParameterNamed(rucParamName);
            actualChanges.put(rucParamName, newValue);
            this.ruc = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String sourceIdParamName = "sourceId";
        if (command.isChangeInLongParameterNamed(sourceIdParamName, this.source.getId())) {
            final Long newValue = command.longValueOfParameterNamed(sourceIdParamName);
            actualChanges.put(sourceIdParamName, newValue);
        }

        if(actualChanges.size() > 0){
            final LocalDate updatedOn = command.localDateValueOfParameterNamed("updatedOn");
            this.lastModifiedOn = updatedOn.toDate();
            this.lastModifiedBy = updatedBy;
        }


        return actualChanges;
    }

    public LocalDate getLocalDate(Date date) {
        LocalDate startLocalDate = null;
        if (date != null) {
            startLocalDate = LocalDate.fromDateFields(date);
        }
        return startLocalDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setRejectedOn(Date rejectedOn) {
        this.rejectedOn = rejectedOn;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public static Company fromJson(Source source, Integer status, final String createdBy, final String approvedBy, final String rejectedBy,
                                  final String lastModifiedBy, final JsonHelper command) {

        final String name = command.stringValueOfParameterNamed("name");

        final LocalDate dateOperationsStart = command.localDateValueOfParameterNamed("dateOperationsStart");
        final int cantEmployees = command.integerValueOfParameterNamed("cantEmployees");
        final String ruc = command.stringValueOfParameterNamed("ruc");

        final LocalDate createdOn = command.localDateValueOfParameterNamed("createdOn");
        final LocalDate approvedOn = command.localDateValueOfParameterNamed("approvedOn");
        final LocalDate rejectedOn = command.localDateValueOfParameterNamed("rejectedOn");

        return new Company(source, name,cantEmployees,ruc,dateOperationsStart, status,createdOn,createdBy,approvedOn,approvedBy,rejectedOn,rejectedBy,lastModifiedBy);
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getRuc() {
        return ruc;
    }

    public Integer getCantEmployees() {
        return cantEmployees;
    }

    public Date getDateOperationsStart() {
        return dateOperationsStart;
    }

    public Source getSource() {
        return source;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getApprovedOn() {
        return approvedOn;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public Date getRejectedOn() {
        return rejectedOn;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
}
