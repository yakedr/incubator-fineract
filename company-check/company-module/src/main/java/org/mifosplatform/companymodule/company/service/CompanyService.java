package org.mifosplatform.companymodule.company.service;

import org.mifosplatform.companymodule.company.data.CompanyData;
import org.mifosplatform.companymodule.utils.JsonHelper;

import java.util.Collection;

public interface CompanyService {

    Collection<CompanyData> retrieveAllCompanies();

    CompanyData retrieveCompany(Long companyId);

    CompanyData createCompany(JsonHelper command);

    Integer updateCompany(Long companyId, JsonHelper command);

    Long approveCompany(Long companyId, JsonHelper command);

    Long rejectCompany(Long companyId, JsonHelper command);

    Long deleteCompany(Long companyId,JsonHelper command);
}
