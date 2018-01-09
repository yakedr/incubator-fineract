package org.mifosplatform.companymodule.source.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SourceRepository extends JpaRepository<Source, Long>, JpaSpecificationExecutor<Source> {

    Source findById(Long id);

}
