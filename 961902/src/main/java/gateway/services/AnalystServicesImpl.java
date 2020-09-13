package gateway.services;

import gateway.beans.Analyst;
import gateway.list.ListAnalyst;
import gateway.list.ListNodes;
import gateway.list.ListStatistics;
import gateway.interfaces.AnalystServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/gatewayAnalyst")
public class AnalystServicesImpl implements AnalystServices {

    @Override
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/getNumberNodes")
    public Response getNumberNodes() {
        return Response.ok(ListNodes.getInstance().getNodesList().size()).build();
    }

    @Override
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/calculateStatistics/{n}")
    public Response calculateMeanVariance(@PathParam("n") String n) {
        double[] ris = ListStatistics.getInstance().MeanAndVariance(n);
        System.out.println(ris[0] + ris[1]);
        return Response.ok("Means: " + ris[0] + "Standard deviation : " + ris[1]).build();
    }

    @Override
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/getStatistics/{s}")
    public Response getStatistics(@PathParam("s") String s) {
        String stati = "" + s;
        return Response.ok(ListStatistics.getInstance().getStatisticsN(stati)).build();
    }

    @Override
    @GET
    @Path("/getAnalysts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnalystList() {
        return Response.ok(ListAnalyst.getInstance().getAnalystList()).build();
    }

    @Override
    @Path("addAnalyst")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addedAnalyst(String analyst) {
        Analyst n = ListAnalyst.getInstance().createAnalyst(analyst);

        if (n != null) {
            return getAnalystList();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @Override
    @DELETE
    @Path("deleteAnalyst")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteAnalyst(String id) {
        ListAnalyst.getInstance().removeAnalyst(id);
        return Response.ok().build();
    }
}

