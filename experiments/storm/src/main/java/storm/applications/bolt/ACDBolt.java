package storm.applications.bolt;

import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.applications.bolt.base.AbstractScoreBolt;
import storm.applications.model.cdr.CallDetailRecord;

import static storm.applications.constants.VoIPSTREAMConstants.Field;

/**
 * @author Maycon Viana Bordin <mayconbordin@gmail.com>
 */
public class ACDBolt extends AbstractScoreBolt {
    private static final Logger LOG = LoggerFactory.getLogger(ACDBolt.class);

    private double avg;

    public ACDBolt() {
        super("acd");
    }

    @Override
    public void execute(Tuple input) {
        Source src = parseComponentId(input.getSourceComponent());

        if (src == Source.GACD) {
            avg = input.getDoubleByField(Field.AVERAGE);
        } else {
            CallDetailRecord cdr = (CallDetailRecord) input.getValueByField(Field.RECORD);
            String number = (String) input.getValueByField(Field.CALLING_NUM);
            long timestamp = (long) input.getValueByField(Field.TIMESTAMP);
            double rate = (double) input.getValueByField(Field.RATE);


            String key = String.format("%s:%d", number, timestamp);

            if (map.containsKey(key)) {
                Entry e = map.get(key);
                e.set(src, rate);

                if (e.isFull()) {
                    // calculate the score for the ratio
                    double ratio = (e.get(Source.CT24) / e.get(Source.ECR24)) / avg;
                    double score = score(thresholdMin, thresholdMax, ratio);

                    LOG.debug(String.format("T1=%f; T2=%f; CT24=%f; ECR24=%f; AvgCallDur=%f; Ratio=%f; Score=%f",
                            thresholdMin, thresholdMax, e.get(Source.CT24), e.get(Source.ECR24), avg, ratio, score));

                    collector.emit(new Values(number, timestamp, score, cdr));
                    map.remove(key);
                } else {
//                    LOG.debug(String.format("Inconsistent entry: source=%s; %s",
//                            input.getSourceComponent(), e.toString()));
                }
            } else {
                Entry e = new Entry(cdr);
                e.set(src, rate);
                map.put(key, e);
            }
        }
        collector.ack(input);
    }

    @Override
    protected Source[] getFields() {
        return new Source[]{Source.CT24, Source.ECR24};
    }
}