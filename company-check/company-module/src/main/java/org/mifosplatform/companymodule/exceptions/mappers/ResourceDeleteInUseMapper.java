package org.mifosplatform.companymodule.exceptions.mappers;

import org.mifosplatform.companymodule.exceptions.data.EntityException;
import org.mifosplatform.companymodule.exceptions.ResourceMustNotBeInUseToDeleteException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ResourceDeleteInUseMapper implements ExceptionMapper<ResourceMustNotBeInUseToDeleteException> {

    public Response toResponse(ResourceMustNotBeInUseToDeleteException ex)
    {
        return Response.status(Response.Status.PRECONDITION_FAILED)
                .entity(new EntityException(500, ex.getMessageCode(),ex.getDefaultUserMessage()))
                .build();
    }
}