package gateway.interfaces;

import javax.ws.rs.core.Response;

public interface AnalystServices {

    //numeri di nodi presenti nella rete
    public Response getNumberNodes();

    //calcolo di media e deviazione standard di n statistiche
    public Response calculateMeanVariance(String n);

    //Ultime n statistiche
    public Response getStatistics(String n);

    //ritorna tutti gli elementi della lista analisti
    public Response getAnalystList();

    //aggiunge un nuovo analista
    public Response addedAnalyst(String analyst);

    //rimuove un analista
    public Response deleteAnalyst(String id);
}
