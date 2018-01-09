package org.mifosplatform.companymodule.company.api;

import org.apache.commons.lang3.StringUtils;
import org.mifosplatform.companymodule.company.data.CompanyData;
import org.mifosplatform.companymodule.company.service.CompanyService;
import org.mifosplatform.companymodule.exceptions.UnrecognizedQueryParamException;
import org.mifosplatform.companymodule.utils.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("/companies")
@Component
public class CompanyApiResource {

    private final CompanyService companyService;
    private JsonHelper jsonHelper;

    @Autowired
    public CompanyApiResource(JsonHelper jsonHelper, final CompanyService companyService) {
        this.companyService = companyService;
        this.jsonHelper = jsonHelper;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response retrieveCompanies(@Context final UriInfo uriInfo) {

        final Collection<CompanyData> result = this.companyService.retrieveAllCompanies();
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @GET
    @Path("{companyId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response retrieveCompany(@PathParam("companyId") final Long companyId, @Context final UriInfo uriInfo) {

        final CompanyData result = this.companyService.retrieveCompany(companyId);
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response createCompany(final String apiRequestBodyAsJson) {

        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);
        CompanyData result = this.companyService.createCompany(this.jsonHelper);

        return Response.ok().entity(result).build();
    }


    @POST
    @Path("{companyId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response changeStatus(@PathParam("companyId") final Long companyId, @QueryParam("command") final String commandParam,
                           final String apiRequestBodyAsJson) {

        Long result = null;
        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);

        if (is(commandParam, "approve")) {
            result = this.companyService.approveCompany(companyId, jsonHelper);
        } else if (is(commandParam, "reject")) {
            result = this.companyService.rejectCompany(companyId, jsonHelper);
        }
        if (result == null) {
            throw new UnrecognizedQueryParamException("command", commandParam, new Object[] { "approve", "reject" });
        }

        return Response.status(Response.Status.OK).entity(result).build();
    }

    private boolean is(final String commandParam, final String commandValue) {
        return StringUtils.isNotBlank(commandParam) && commandParam.trim().equalsIgnoreCase(commandValue);
    }

    @PUT
    @Path("{companyId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateCompany(@PathParam("companyId") final Long companyId, final String apiRequestBodyAsJson) {

        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);
        Integer changesSize = this.companyService.updateCompany(companyId, jsonHelper);

        Map<String, Integer> result = new HashMap<>();
        result.put("changes", changesSize);

        return Response.status(Response.Status.OK).entity(result).build();
    }

    @DELETE
    @Path("{companyId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response delete(@PathParam("companyId") final Long companyId) {

        this.jsonHelper = JsonHelper.instance(String.valueOf(companyId));
        Long result = this.companyService.deleteCompany(companyId, jsonHelper);

        return Response.status(Response.Status.OK).entity(result).build();
    }
}