package storm.applications.bolt;

import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import storm.applications.bolt.base.AbstractBolt;

import java.util.HashMap;
import java.util.Map;

import static storm.applications.constants.ClickAnalyticsConstants.Field;

/**
 * User: domenicosolazzo
 */
public class RepeatVisitBolt extends AbstractBolt {
    private Map<String, Void> map;

    @Override
    public void initialize() {
        map = new HashMap<>();
    }

    @Override
    public void execute(Tuple input) {
        String clientKey = input.getStringByField(Field.CLIENT_KEY);
        String url = input.getStringByField(Field.URL);
        String key = url + ":" + clientKey;

        if (map.containsKey(key)) {
            collector.emit(input, new Values(clientKey, url, Boolean.FALSE.toString()));
        } else {
            map.put(key, null);
            collector.emit(input, new Values(clientKey, url, Boolean.TRUE.toString()));
        }

        collector.ack(input);
    }

    @Override
    public Fields getDefaultFields() {

        return new Fields(Field.CLIENT_KEY, Field.URL, Field.UNIQUE);
    }
}
