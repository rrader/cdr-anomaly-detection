import org.junit.Test;
import ua.kpi.rrader.cdr.producers.AsteriskImitatorFileProducer;


public class ImitatorTest {
    @Test
    public void testOneNumberFile() throws Exception {
        AsteriskImitatorFileProducer producer = new AsteriskImitatorFileProducer(
                GeneratorFabric.oneNumber()
        );
        producer.doProduce();
    }

    @Test
    public void testMultiNumberOnePatternFile() throws Exception {
        AsteriskImitatorFileProducer producer =
                new AsteriskImitatorFileProducer(
                        GeneratorFabric.multiNumberOnePattern()
                );
        producer.doProduce();
    }

    @Test
    public void testMultiNumberMultiPatternFile() throws Exception {
        AsteriskImitatorFileProducer producer =
                new AsteriskImitatorFileProducer(
                        GeneratorFabric.multiNumberMultiPatternSmall()
                );
        producer.doProduce();
    }

    @Test
    public void testMassive() throws Exception {
        AsteriskImitatorFileProducer producer =
                new AsteriskImitatorFileProducer(
                        GeneratorFabric.multiNumberMultiPatternBig()
                );
        producer.doProduce();
    }
}
