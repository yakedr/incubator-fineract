package org.mifosplatform.companymodule.company.service;

import org.joda.time.LocalDate;
import org.mifosplatform.companymodule.company.data.CompanyData;
import org.mifosplatform.companymodule.company.domain.Company;
import org.mifosplatform.companymodule.company.domain.CompanyRepository;
import org.mifosplatform.companymodule.company.domain.CompanyStatusType;
import org.mifosplatform.companymodule.company.serialization.CompanyCommandDeserializer;
import org.mifosplatform.companymodule.exceptions.DataIntegrityException;
import org.mifosplatform.companymodule.exceptions.ResourceMustBePendingToBeDeletedException;
import org.mifosplatform.companymodule.exceptions.ResourceNotFoundException;
import org.mifosplatform.companymodule.source.domain.Source;
import org.mifosplatform.companymodule.source.domain.SourceRepository;
import org.mifosplatform.companymodule.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final static Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final SourceRepository sourceRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, SourceRepository sourceRepository) {
        this.companyRepository = companyRepository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Collection<CompanyData> retrieveAllCompanies() {

        List<Company> companies = companyRepository.findAll(new Sort(Sort.Direction.ASC,"name"));
        List<CompanyData> out = new LinkedList<>();

        for (Company current: companies) {
            CompanyData instance = mapCompanyData(current);
            out.add(instance);
        }

        return out;
    }

    private CompanyData mapCompanyData(Company current) {
        final Long id = current.getId();
        final String name = current.getName();
        final String status = CompanyStatusType.fromInt(current.getStatus()).name();
        String createdOn = null;
        if(current.getCreatedOn() != null)
          createdOn =  LocalDate.fromDateFields(current.getCreatedOn()).toString("yyyy-MM-dd");
        final String createdBy = current.getCreatedBy();
        String approvedOn = null;
        if(current.getApprovedOn() != null)
            approvedOn =  LocalDate.fromDateFields(current.getApprovedOn()).toString("yyyy-MM-dd");
        final String approvedBy = current.getApprovedBy();
        String rejectedOn = null;
        if(current.getRejectedOn() != null)
            rejectedOn =  LocalDate.fromDateFields(current.getRejectedOn()).toString("yyyy-MM-dd");
        final String rejectedBy = current.getRejectedBy();
        String lastModifiedOn = null;
        if(current.getLastModifiedOn() != null)
            lastModifiedOn =  LocalDate.fromDateFields(current.getLastModifiedOn()).toString("yyyy-MM-dd");
        final String lastModifiedBy = current.getLastModifiedBy();

        final String ruc = current.getRuc();
        String dateOperationsStart = null;
        if(current.getDateOperationsStart() != null)
            dateOperationsStart =  LocalDate.fromDateFields(current.getDateOperationsStart()).toString("yyyy-MM-dd");
        int cantEmployees = current.getCantEmployees();
        String sourceName = current.getSource().getName();
        Long sourceId = current.getSource().getId();

        return CompanyData.instance(id, name, ruc, dateOperationsStart, cantEmployees, sourceId, sourceName, status,
                createdOn, createdBy, approvedOn, approvedBy, rejectedOn, rejectedBy, lastModifiedOn, lastModifiedBy);

    }

    @Override
    public CompanyData retrieveCompany(Long companyId) {
        try {
            Company company = companyRepository.findById(companyId);
            CompanyData selectedCompany = mapCompanyData(company);

            return selectedCompany;
        } catch (final EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(companyId, "company");
        }
    }

    @Override
    public CompanyData createCompany(JsonHelper command) {
        try {
            CompanyCommandDeserializer.validateForCreate(command.getJsonCommand());

            final Integer status = CompanyStatusType.PENDING_APPROVAL.getValue();
            final Source source = sourceRepository.findOne(command.longValueOfParameterNamed("sourceId"));

            final Company company = Company.fromJson(source,status,null,null,null,null, command);

            Company saved = this.companyRepository.save(company);

            return mapCompanyData(saved);
        } catch (final DataIntegrityViolationException dve) {
            handleCompanyDataIntegrityIssues(command, dve);
            return null;
        }
    }

    @Override
    public Integer updateCompany(Long companyId, JsonHelper command) {

        try {
            CompanyCommandDeserializer.validateForUpdate(command.getJsonCommand());

            final Company company = this.companyRepository.findOne(companyId);
            if (company == null) { throw new ResourceNotFoundException(companyId, "company"); }

            final Map<String, Object> changes = company.update(command,null);
            if (!changes.isEmpty()) {
                if(changes.containsKey("sourceId")){
                    Source source = sourceRepository.findOne((Long) changes.get("sourceId"));
                    company.setSource(source);
                }
                this.companyRepository.saveAndFlush(company);
            }
            return changes.size();
        } catch (final DataIntegrityViolationException dve) {
            handleCompanyDataIntegrityIssues(command, dve);
            return null;
        }
    }

    @Override
    public Long approveCompany(Long companyId, JsonHelper command) {
        try {

            CompanyCommandDeserializer.validateForApprove(command.getJsonCommand());

            Company company = this.companyRepository.findOne(companyId);

            final LocalDate approvedOn = command.localDateValueOfParameterNamed("approvedOn");
            company.approve(approvedOn.toDate(),null);

            Company saved = this.companyRepository.save(company);

            return saved.getId();
        } catch (final DataIntegrityViolationException dve) {
            handleCompanyDataIntegrityIssues(command, dve);
            return null;
        }
    }

    @Override
    public Long rejectCompany(Long companyId, JsonHelper command) {
        try {

            CompanyCommandDeserializer.validateForReject(command.getJsonCommand());

            Company company = this.companyRepository.findOne(companyId);

            final LocalDate rejectedOn = command.localDateValueOfParameterNamed("rejectedOn");
            company.reject(rejectedOn.toDate(),null);

            Company saved = this.companyRepository.save(company);

            return saved.getId();
        } catch (final DataIntegrityViolationException dve) {
            handleCompanyDataIntegrityIssues(command, dve);
            return null;
        }
    }

    @Override
    public Long deleteCompany(final Long companyId,JsonHelper command) {

        final Company companyForDelete = this.companyRepository.findOne(companyId);

        if (companyForDelete == null) { throw new ResourceNotFoundException(companyId, "company"); }

        if (companyForDelete.getStatus() != CompanyStatusType.PENDING_APPROVAL.getValue()) { throw new ResourceMustBePendingToBeDeletedException(companyId, "company"); }

        this.companyRepository.delete(companyForDelete);

        return companyId;
    }

    private void handleCompanyDataIntegrityIssues(final JsonHelper command, final DataIntegrityViolationException dve) {

        final Throwable realCause = dve.getMostSpecificCause();

        logger.error(dve.getMessage(), dve);
        throw new DataIntegrityException("error.msg.company.unknown.data.integrity.issue",
                "Unknown data integrity issue with recompany: " + realCause.getMessage());
    }
}
