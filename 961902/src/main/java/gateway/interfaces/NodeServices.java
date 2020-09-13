package gateway.interfaces;

import javax.ws.rs.core.Response;

public interface NodeServices {

    //ritorna tutti gli elementi della lista nodi
    public Response getNodesList();

    //aggiunge un nuovo nodo
    public Response addedNodo(String nodo);

    //rimuove un nodo
    public Response deletedNodo(String id);

    //aggiunge una statistica
    public Response addedStatistics(String statistic);
}
