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
public class ListAnalyst {

    @XmlElement(name = "analyst_of_gateway")
    private List<Analyst> list;
    private static ListAnalyst instance;

    private ListAnalyst() {
        list = new ArrayList<Analyst>();
    }

    //singleton
    public synchronized static ListAnalyst getInstance() {
        if (instance == null)
            instance = new ListAnalyst();
        return instance;
    }

    //GET
    public synchronized List<Analyst> getAnalystList() {
        return new ArrayList<>(list);
    }

    //POST
    public synchronized Analyst createAnalyst(String analyst) {

        String[] all = analyst.split(",");
        int port = Integer.parseInt(all[0]);
        String ip = all[1];
        int id = Integer.parseInt(all[2]);
        Analyst an = new Analyst(port, ip, id);

        if (checkID(id) || checkPort(port)) {
            return null;
        } else {
            list.add(an);
            return an;
        }
    }

    //DELETE
    public synchronized void removeAnalyst(String idToDelete) {
        int id = Integer.parseInt(idToDelete);
        List<Analyst> copy = getAnalystList();

        for (Analyst analy : copy) {
            if (analy.getId() == id) {
                list.remove(analy);
            }
        }
    }

    private boolean checkPort(int portToVerify) {
        boolean exist = false;
        List<Node> copyNode = ListNodes.getInstance().getNodesList();
        List<Analyst> copyAnalyst = getAnalystList();

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
        List<Node> copyNode = ListNodes.getInstance().getNodesList();
        List<Analyst> copyAnalyst = getAnalystList();

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
