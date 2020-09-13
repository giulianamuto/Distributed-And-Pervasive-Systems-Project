package simulatore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class BufferImpl implements Buffer {

    ArrayList<Measurement> bin = new ArrayList<>();
    public Queue<Measurement> measurementDTOQueue = new LinkedList<>();
    private static final Logger log = Logger.getLogger("global");
    int count = 0;
    boolean isBaseCase = true;
    int windows = 11;
    int index = 0;
    int overlap = 6;
    long time;
    double mediaValue = 0;

    public BufferImpl() {
    }

    @Override
    public void addMeasurement(Measurement m) {
        bin.add(m);
        count++;
        if (count == 12 && isBaseCase) {
            count = 0;
            isBaseCase = false;
            slidingWindows();
        }
        if (count == 6 && !isBaseCase) {
            count = 0;
            slidingWindows();
        }
    }

    private void slidingWindows() {

        for (int k = index; k < windows; k++) {
            mediaValue = bin.get(k).getValue() + mediaValue;
            time = bin.get(k).getTimestamp();
        }

        //  log.info("W: " + windows + "I: "+ index);
        index = windows;
        windows = windows + overlap;

        Measurement dto = new Measurement(null, null, mediaValue, time);
        measurementDTOQueue.add(dto);
        //System.out.println(dto.toString());
    }


}
