package gateway.list;

import gateway.beans.Analyst;
import gateway.beans.Node;
import simulatore.Measurement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListStatistics {

    @XmlElement(name = "statistics")
    private List<Measurement> statistics;
    private static ListStatistics instance;

    private ListStatistics() {
        statistics = new ArrayList<Measurement>();
    }

    //singleton
    public synchronized static ListStatistics getInstance() {
        if (instance == null)
            instance = new ListStatistics();
        return instance;
    }

    //GET
    public synchronized List<Measurement> getStatistics() {
        return new ArrayList<>(statistics);
    }

    //GET
    public synchronized String getStatisticsN(String n) {
        int size = Integer.parseInt(n);

        if (size > getStatistics().size()) {
            size = getStatistics().size();
        }

        List<Measurement> list = getStatistics();
        String toString = "";

        for (int i = 0; i < size; i++) {
            toString = toString + "," + list.get(i).toString();
        }
        return toString;
    }


    //POST
    public synchronized String addedStatistics(String s) {
        String[] all = s.split(",");
        double value = Double.parseDouble(all[0]);
        long timestamp = Long.parseLong(all[1]);

        Measurement statistic = new Measurement(null, null, value, timestamp);
        statistics.add(statistic);
        String result = "" + value + timestamp;
        return result;
    }

    //GET
    public synchronized double[] MeanAndVariance(String n) {
        int size = Integer.parseInt(n);

        if (size > getStatistics().size()) {
            size = getStatistics().size();
        }

        double[] values = new double[size];
        double[] ris = new double[2];

        for (int i = 0; i < size; i++) {
            values[i] = getStatistics().get(i).getValue();
        }

        double mean = CalculateMean(values);
        double variance = CalculateVariance(values, mean);
        double deviation = Math.pow(variance, 2);
        ris[0] = mean;
        ris[1] = deviation;

        if (size == 0) {
            ris[0] = 0;
            ris[1] = 0;
        }
        return ris;
    }

    private double CalculateMean(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum = values[i] + sum;
        }
        return sum / values.length;
    }

    private double CalculateVariance(double[] values, double means) {
        double variance = 0;
        for (int i = 0; i < values.length; i++) {
            variance = variance + Math.pow(values[i] - means, 2);
        }
        return variance / values.length;
    }


}
