import org.junit.Test;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorFileMultiNumberMultiPatternProducer;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorFileMultiNumberOnePatternProducer;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorFileProducer;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorFileSingleNumberProducer;

import java.util.ArrayList;
import java.util.List;

public class ImitatorTest {
    @Test
    public void testOneNumberFile() throws Exception {
        AsteriskImitatorFileProducer producer = new AsteriskImitatorFileSingleNumberProducer("+380009013947");
        producer.doProduce();
    }

    @Test
    public void testMultiNumberOnePatternFile() throws Exception {
        List<String> numbers = new ArrayList<String>();
        numbers.add("+380009013946");
        numbers.add("+380009013947");
        numbers.add("+380009013948");
        numbers.add("+380009013949");
        AsteriskImitatorFileProducer producer =
                new AsteriskImitatorFileMultiNumberOnePatternProducer(numbers);
        producer.doProduce();
    }

    @Test
    public void testMultiNumberMultiPatternFile() throws Exception {
        List<String> numbers1 = new ArrayList<String>();
        numbers1.add("+380009013946");
        numbers1.add("+380009013947");
        List<String> numbers2 = new ArrayList<String>();
        numbers2.add("+380009013948");
        numbers2.add("+380009013949");
        AsteriskImitatorFileProducer producer =
                new AsteriskImitatorFileMultiNumberMultiPatternProducer(numbers1, numbers2);
        producer.doProduce();
    }
}
