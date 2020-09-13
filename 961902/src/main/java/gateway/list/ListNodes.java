package gateway.list;

import gateway.beans.Analyst;
import gateway.beans.Node;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListNodes {

    @XmlElement(name = "nodes_of_gateway")
    private List<Node> list;
    private static ListNodes instance;

    private ListNodes() {
        list = new ArrayList<Node>();
    }

    //singleton
    public synchronized static ListNodes getInstance() {
        if (instance == null)
            instance = new ListNodes();
        return instance;
    }

    //GET
    public synchronized List<Node> getNodesList() {
        return new ArrayList<>(list);
    }

    //POST
    public synchronized Node createNode(String node) {

        String[] all = node.split(",");
        int id = Integer.parseInt(all[0]);
        int port = Integer.parseInt(all[1]);
        String address = all[2];
        int gatewayPort = Integer.parseInt(all[3]);
        Node nodo = new Node(id, port, address, gatewayPort);

        if (checkID(id) || checkPort(port)) {
            return null;
        } else {
            list.add(nodo);
            return nodo;
        }
    }

    //DELETE
    public synchronized void removeNodo(String idToDelete) {
        int id = Integer.parseInt(idToDelete);
        List<Node> copy = getNodesList();

        for (Node nodo : copy) {
            if (nodo.getId() == id) {
                list.remove(nodo);
            }
        }
    }

    private boolean checkPort(int portToVerify) {
        boolean exist = false;
        List<Node> copyNode = getNodesList();
        List<Analyst> copyAnalyst = ListAnalyst.getInstance().getAnalystList();

        for (Node nod : copyNode) {
            if (nod.getPort() == portToVerify) {
                exist = true;
            }
            if (!exist) {
                for (Analyst an : copyAnalyst) {
                    if (an.getPort() == portToVerify) {
                        exist = true;
                    }
                }
            }
        }
        return exist;
    }

    private boolean checkID(int idToVerify) {
        boolean exist = false;
        List<Node> copyNode = getNodesList();
        List<Analyst> copyAnalyst = ListAnalyst.getInstance().getAnalystList();

        for (Node nod : copyNode) {
            if (nod.getPort() == idToVerify) {
                exist = true;
            }
            if (!exist) {
                for (Analyst an : copyAnalyst) {
                    if (an.getPort() == idToVerify) {
                        exist = true;
                    }
                }
            }
        }
        return exist;
    }

}