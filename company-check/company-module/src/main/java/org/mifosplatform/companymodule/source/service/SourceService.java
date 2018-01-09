package org.mifosplatform.companymodule.source.service;

import org.mifosplatform.companymodule.source.data.SourceData;
import org.mifosplatform.companymodule.utils.JsonHelper;

import java.util.Collection;

public interface SourceService {

    Collection<SourceData> retrieveAllSources();

    SourceData retrieveSource(Long sourceId);

    SourceData createSource(JsonHelper command);

    Integer updateSource(Long sourceId, JsonHelper command);

    Long approveSource(Long sourceId, JsonHelper command);

    Long rejectSource(Long sourceId, JsonHelper command);

    Long deleteSource(final Long sourceId,JsonHelper command);
}
