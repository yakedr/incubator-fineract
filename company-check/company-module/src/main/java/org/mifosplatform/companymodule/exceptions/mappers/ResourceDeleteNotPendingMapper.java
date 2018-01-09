package org.mifosplatform.companymodule.exceptions.mappers;

import org.mifosplatform.companymodule.exceptions.data.EntityException;
import org.mifosplatform.companymodule.exceptions.ResourceMustBePendingToBeDeletedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceDeleteNotPendingMapper implements ExceptionMapper<ResourceMustBePendingToBeDeletedException> {

    public Response toResponse(ResourceMustBePendingToBeDeletedException ex)
    {
        return Response.status(Response.Status.PRECONDITION_FAILED)
                .entity(new EntityException(500, ex.getMessageCode(),ex.getDefaultUserMessage()))
                .build();
    }
}
