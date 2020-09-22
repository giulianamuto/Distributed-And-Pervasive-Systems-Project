package gateway.services;

import gateway.pushNot.PushNotification;
import gateway.beans.Node;
import gateway.list.ListAnalyst;
import gateway.list.ListNodes;
import gateway.list.ListStatistics;
import gateway.interfaces.NodeServices;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/gatewayNode")
public class NodoServicesImpl implements NodeServices {

    @Override
    @GET
    @Path("/getNodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNodesList() {
        return Response.ok(ListNodes.getInstance().getNodesList()).build();
    }

    @Override
    @Path("addNode")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addedNodo(String nodo) {
        Node n = ListNodes.getInstance().createNode(nodo);
        if (n != null) {
            setPushNotification("Added node");
            return getNodesList();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @Override
    @DELETE
    @Path("deleteNode")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deletedNodo(String id) {
        ListNodes.getInstance().removeNodo(id);
        setPushNotification("Removed node");
        return Response.ok().build();
    }

    @Override
    @Path("addStatistics")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addedStatistics(String statistic) {
        ListStatistics.getInstance().addedStatistics(statistic);
        setPushNotification("Added statistic");
        return Response.ok().build();
    }

    public void setPushNotification(String message) {
        PushNotification pushNotification = new PushNotification(ListAnalyst.getInstance().getAnalystList(), message);
        pushNotification.start();
    }
}