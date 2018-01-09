package org.mifosplatform.companymodule.source.service;

import org.joda.time.LocalDate;
import org.mifosplatform.companymodule.company.domain.CompanyRepository;
import org.mifosplatform.companymodule.exceptions.DataIntegrityException;
import org.mifosplatform.companymodule.exceptions.ResourceMustBePendingToBeDeletedException;
import org.mifosplatform.companymodule.exceptions.ResourceMustNotBeInUseToDeleteException;
import org.mifosplatform.companymodule.exceptions.ResourceNotFoundException;
import org.mifosplatform.companymodule.source.data.SourceData;
import org.mifosplatform.companymodule.source.domain.Source;
import org.mifosplatform.companymodule.source.domain.SourceRepository;
import org.mifosplatform.companymodule.source.domain.SourceStatusType;
import org.mifosplatform.companymodule.source.serialization.SourceCommandDeserializer;
import org.mifosplatform.companymodule.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SourceServiceImpl implements SourceService {

    private final static Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);
    private final SourceRepository sourceRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public SourceServiceImpl(final SourceRepository sourceRepository, CompanyRepository companyRepository) {
        this.sourceRepository = sourceRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Collection<SourceData> retrieveAllSources() {

     List<Source> sources = sourceRepository.findAll(new Sort(Sort.Direction.ASC,"name"));
     List<SourceData> out = new LinkedList<>();

        for (Source current: sources) {
            SourceData instance = mapSourceData(current);
             out.add(instance);
        }
        return out;
    }

    private SourceData mapSourceData(Source current) {

        Long id = current.getId();
        String name = current.getName();
        String status = SourceStatusType.fromInt(current.getStatus()).name();
        String createdOn = LocalDate.fromDateFields(current.getCreatedOn()).toString("yyyy-MM-dd");
        String createdBy = current.getCreatedBy();
        String approvedOn = null;
        if(current.getApprovedOn() != null)
            approvedOn =  LocalDate.fromDateFields(current.getApprovedOn()).toString("yyyy-MM-dd");
        String approvedBy = current.getApprovedBy();
        String rejectedOn = null;
        if (current.getRejectedOn() != null)
            rejectedOn =  LocalDate.fromDateFields(current.getRejectedOn()).toString("yyyy-MM-dd");
        String rejectedBy = current.getRejectedBy();
        String lastModifiedOn = null;
        if (current.getLastModifiedOn() != null)
          lastModifiedOn =  LocalDate.fromDateFields(current.getLastModifiedOn()).toString("yyyy-MM-dd");
        String lastModifiedBy = current.getLastModifiedBy();

        return SourceData.instance(id,name,status,createdOn,createdBy,approvedOn,approvedBy,
                rejectedOn, rejectedBy,lastModifiedOn,lastModifiedBy);
    }

    @Override
    public SourceData retrieveSource(Long sourceId) {

        Source source = sourceRepository.findById(sourceId);
        if(source == null) throw new ResourceNotFoundException(sourceId, "source");
        return mapSourceData(source);
    }

    @Override
    public SourceData createSource(final JsonHelper command) {
        try {
            SourceCommandDeserializer.validateForCreate(command.getJsonCommand());

            final Integer status = SourceStatusType.PENDING_APPROVAL.getValue();
            final Source source = Source.fromJson(status, null,null,null,null, command);
            Source saved = this.sourceRepository.save(source);

            return mapSourceData(saved);
        } catch (final DataIntegrityViolationException dve) {
            handleSourceDataIntegrityIssues(command.getJsonCommand(), dve);
            return null;
        }
    }

    @Override
    public Integer updateSource(Long sourceId, JsonHelper command) {

        try {
            SourceCommandDeserializer.validateForUpdate(command.getJsonCommand());

            final Source source = this.sourceRepository.findOne(sourceId);
            if (source == null) { throw new ResourceNotFoundException(sourceId, "source"); }

            final Map<String, Object> changes = source.update(command,null);
            if (!changes.isEmpty()) {
               this.sourceRepository.saveAndFlush(source);
            }
            return changes.size();
        } catch (final DataIntegrityViolationException dve) {
            handleSourceDataIntegrityIssues(command.getJsonCommand(), dve);
            return null;
        }
    }

    @Override
    public Long approveSource(Long sourceId, JsonHelper command) {
        try {
            SourceCommandDeserializer.validateForApprove(command.getJsonCommand());

            Source source = this.sourceRepository.findOne(sourceId);
            final LocalDate approvedOn = command.localDateValueOfParameterNamed("approvedOn");
            source.approve(approvedOn.toDate(),null);

            Source saved = this.sourceRepository.save(source);

            return saved.getId();
        } catch (final DataIntegrityViolationException dve) {
            handleSourceDataIntegrityIssues(command.getJsonCommand(), dve);
            return null;
        }
    }

    @Override
    public Long rejectSource(Long sourceId, JsonHelper command) {
        try {
            SourceCommandDeserializer.validateForReject(command.getJsonCommand());
            Source source = this.sourceRepository.findOne(sourceId);
            final LocalDate rejectedOn = command.localDateValueOfParameterNamed("rejectedOn");
            source.reject(rejectedOn.toDate(),null);

            Source saved = this.sourceRepository.save(source);

            return saved.getId();
        } catch (final DataIntegrityViolationException dve) {
            handleSourceDataIntegrityIssues(command.getJsonCommand(), dve);
            return null;
        }
    }

    @Override
    public Long deleteSource(final Long sourceId,JsonHelper command) {

        final Source sourceForDelete = this.sourceRepository.findOne(sourceId);

        if (sourceForDelete == null) { throw new ResourceNotFoundException(sourceId, "source"); }

        if (sourceForDelete.getStatus() != SourceStatusType.PENDING_APPROVAL.getValue()) { throw new ResourceMustBePendingToBeDeletedException(sourceId, "source"); }

        if (companyRepository.countCompaniesBySourceId(sourceId) > 0) {throw new ResourceMustNotBeInUseToDeleteException(sourceId, "source");}

        this.sourceRepository.delete(sourceForDelete);

        return sourceId;
    }

    private void handleSourceDataIntegrityIssues(final String command, final DataIntegrityViolationException dve) {

        final Throwable realCause = dve.getMostSpecificCause();
        logger.error(dve.getMessage(), dve);
        throw new DataIntegrityException("error.msg.source.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }
}