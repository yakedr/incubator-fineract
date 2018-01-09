package org.mifosplatform.companymodule.company.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Company findById(Long id);
    Integer countCompaniesBySourceId(Long sourceId);

}
