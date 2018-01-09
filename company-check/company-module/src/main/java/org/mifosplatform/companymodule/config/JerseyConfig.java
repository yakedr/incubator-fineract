package org.mifosplatform.companymodule.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.mifosplatform.companymodule.company.api.CompanyApiResource;
import org.mifosplatform.companymodule.exceptions.mappers.GenericExceptionMapper;
import org.mifosplatform.companymodule.exceptions.mappers.ResourceDeleteInUseMapper;
import org.mifosplatform.companymodule.exceptions.mappers.ResourceDeleteNotPendingMapper;
import org.mifosplatform.companymodule.source.api.SourceApiResource;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(SourceApiResource.class);
        register(CompanyApiResource.class);

        //CORS Support
        register(ResponseCorsFilter.class);

        //Exceptions Mappers
        register(ResourceDeleteNotPendingMapper.class);
        register(ResourceDeleteInUseMapper.class);
        register(GenericExceptionMapper.class);
    }
}
