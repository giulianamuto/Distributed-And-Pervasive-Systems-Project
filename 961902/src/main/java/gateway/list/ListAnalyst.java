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
        Analyst an = new Analyst(port, ip);

        if ( checkPort(port)) {
            return null;
        } else {
            list.add(an);
            return an;
        }
    }

    //DELETE
    public synchronized void removeAnalyst(String portToDelete) {
        int port = Integer.parseInt(portToDelete);
        List<Analyst> copy = getAnalystList();

        for (Analyst analy : copy) {
            if (analy.getPort() == port) {
                list.remove(analy);
                break;
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
                break;
            }
            if (!exist) {
                for (Analyst an : copyAnalyst) {
                    if (an.getPort() == portToVerify) {
                        exist = true;
                        break;
                    }
                }
            }
        }
        return exist;
    }



}
