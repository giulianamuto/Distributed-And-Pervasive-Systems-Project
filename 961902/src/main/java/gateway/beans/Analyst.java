package gateway.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Analyst {

    private int port;
    private String ipAddress;
    private int id;

    public Analyst() {
    }

    public Analyst(int port, String ipAddress, int id) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "Analyst{" +
                "port=" + port +
                ", ipAddress='" + ipAddress + '\'' +
                ", id=" + id +
                '}';
    }
}
