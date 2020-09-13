package gateway.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Node {

    public int id;
    public int port;
    public String ipaddress;
    public int portGatweay;

    public Node() {
    }

    public Node(int id, int port, String ipaddress, int portGatweay) {
        this.id = id;
        this.ipaddress = ipaddress;
        this.port = port;
        this.portGatweay = portGatweay;
    }


    public int getPortGatweay() {
        return portGatweay;
    }

    public void setPortGatweay(int portGatweay) {
        this.portGatweay = portGatweay;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "id=" + id +
                ", port=" + port +
                ", ipaddress='" + ipaddress + '\'' +
                ", portGatweay=" + portGatweay +
                '}';
    }
}
