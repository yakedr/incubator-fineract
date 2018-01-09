package org.mifosplatform.companymodule.exceptions.mappers;

import org.mifosplatform.companymodule.exceptions.data.EntityException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new EntityException(500, ex.getMessage(), null))
                .build();
    }
}
