import ua.kpi.rrader.cdr.producers.strategy.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric of call generators
 */
public class GeneratorFabric {
    public static PatternCollectionGenerationStrategy oneNumber() {
        return new SingleNumber("+380000000128");
    }

    public static PatternCollectionGenerationStrategy oneNumberWithIntervention() {
        return new SingleNumberWithIntervention("+380000000128");
    }

    public static PatternCollectionGenerationStrategy multiNumberOnePattern() {
        List<String> numbers = new ArrayList<String>();
        numbers.add("+380009013946");
        numbers.add("+380009013947");
        numbers.add("+380009013948");
        numbers.add("+380009013949");
        return new MultiNumberOnePattern(numbers);
    }

    public static PatternCollectionGenerationStrategy multiNumberOneInterventionPattern() {
        List<String> numbers = new ArrayList<String>();
        numbers.add("+380009013946");
        numbers.add("+380009013947");
        numbers.add("+380009013948");
        numbers.add("+380009013949");
        return new MultiNumberOneInterventionPattern(numbers);
    }

    public static PatternCollectionGenerationStrategy multiNumberMultiPatternSmall() {
        List<String> numbers1 = new ArrayList<String>();
        numbers1.add("+380009013946");
        numbers1.add("+380009013947");
        List<String> numbers2 = new ArrayList<String>();
        numbers2.add("+380009013948");
        numbers2.add("+380009013949");
        return new MultiNumberMultiPattern(numbers1, numbers2);
    }

    public static PatternCollectionGenerationStrategy multiNumberMultiPatternBig() {
        List<String> numbers1 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers1.add("+3800000001" + i);
        }

        List<String> numbers2 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers2.add("+3800000002" + i);
        }
        return new MultiNumberMultiPattern(numbers1, numbers2);
    }

    public static PatternCollectionGenerationStrategy multiNumberMultiPatternBigWithIntervention() {
        List<String> numbers1 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers1.add("+3800000001" + i);
        }

        List<String> numbers2 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers2.add("+3800000002" + i);
        }
        return new MultiNumberMultiPatternWithOneIntervention(numbers1, numbers2);
    }

    public static PatternCollectionGenerationStrategy multiNumberMultiPatternBigWithClassIntervention() {
        List<String> numbers1 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers1.add("+3800000001" + i);
        }

        List<String> numbers2 = new ArrayList<String>();
        for (int i=10; i<60; i++) {  // 50 numbers
            numbers2.add("+3800000002" + i);
        }
        return new MultiNumberMultiPatternWithClassIntervention(numbers1, numbers2);
    }
}
