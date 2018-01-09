package org.mifosplatform.companymodule.source.api;

import org.apache.commons.lang3.StringUtils;
import org.mifosplatform.companymodule.exceptions.UnrecognizedQueryParamException;
import org.mifosplatform.companymodule.source.data.SourceData;
import org.mifosplatform.companymodule.source.service.SourceService;
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

@Path("/sources")
@Component
public class SourceApiResource {

    private final SourceService sourceService;
    private JsonHelper jsonHelper;

    @Autowired
    public SourceApiResource(JsonHelper jsonHelper, final SourceService sourceService) {
        this.jsonHelper = jsonHelper;
        this.sourceService = sourceService;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response retrieveSources(@Context final UriInfo uriInfo) {

        final Collection<SourceData> sources = this.sourceService.retrieveAllSources();
        return Response.status(Response.Status.OK).entity(sources).build();
    }

    @GET
    @Path("{sourceId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response retrieveSource(@PathParam("sourceId") final Long sourceId, @Context final UriInfo uriInfo) {

        final SourceData source = this.sourceService.retrieveSource(sourceId);
        return Response.status(Response.Status.OK).entity(source).build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response createSource(final String apiRequestBodyAsJson) {

        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);
        SourceData result = this.sourceService.createSource(jsonHelper);

        return Response.status(Response.Status.OK).entity(result).build();
    }

    @POST
    @Path("{sourceid}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response changeStatus(@PathParam("sourceid") final Long sourceId, @QueryParam("command") final String commandParam,
                           final String apiRequestBodyAsJson) {

        Long result = null;
        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);

        if (is(commandParam, "approve")) {
            result = this.sourceService.approveSource(sourceId, jsonHelper);
        } else if (is(commandParam, "reject")) {
            result = this.sourceService.rejectSource(sourceId, jsonHelper);
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
    @Path("{sourceId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateSource(@PathParam("sourceId") final Long sourceId, final String apiRequestBodyAsJson) {

        this.jsonHelper = JsonHelper.instance(apiRequestBodyAsJson);
        Integer changesSize = this.sourceService.updateSource(sourceId, jsonHelper);

        Map<String, Integer> result = new HashMap<>();
        result.put("changes", changesSize);

        return Response.status(Response.Status.OK).entity(result).build();
    }

    @DELETE
    @Path("{sourceId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response delete(@PathParam("sourceId") final Long sourceId) {

        this.jsonHelper = JsonHelper.instance(String.valueOf(sourceId));
        Long result = this.sourceService.deleteSource(sourceId, jsonHelper);

        return Response.status(Response.Status.OK).entity(result).build();
    }
}
