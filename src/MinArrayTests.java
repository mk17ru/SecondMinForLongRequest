import org.junit.Test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinArrayTests {

    @Test
    public void test() {
        MinArray<Long> minArray = new MinArray<>();
        for (int iter = 0; iter < 10000; ++iter) {
            List<Long> list = Stream.generate(() -> new Random().nextLong())
                    .limit((Math.abs(new Random().nextInt()) % 1000) + 2).collect(Collectors.toList());
            int pos = -1;
            long min = Long.MAX_VALUE;
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i) < min) {
                    min = list.get(i);
                    pos = i;
                }
            }
            long min2 = Long.MAX_VALUE;
            for (int i = 0; i < list.size(); ++i) {
                if (i != pos && list.get(i) < min2) {
                    min2 = list.get(i);
                }
            }
            Long s = minArray.findSecondMin(list);
            if (min2 != s) {
                System.err.println(min2 + " " + s);
            }
        }
    }

    public static class LongCompareObject implements Comparable<LongCompareObject> {

        String s = Stream.generate(() -> "s").limit(500000).collect(Collectors.joining());

        @Override
        public int compareTo(LongCompareObject o) {
            return s.compareTo(o.s);
        }
    }

    @Test
    public void testSpeed() {
        MinArray<LongCompareObject> minArray = new MinArray<>();
        List<LongCompareObject> list = Stream.generate(LongCompareObject::new)
                .limit(2000).collect(Collectors.toList());
        LongCompareObject min = null;
        LongCompareObject min2 = null;
        long firstTime = System.currentTimeMillis();
        int pos = -1;
        for (int i = 0; i < list.size(); ++i) {
            if (min == null || list.get(i).compareTo(min) < 0) {
                min = list.get(i);
                pos = i;
            }
        }
        for (int i = 0; i < list.size(); ++i) {
            if (i != pos && (min2 == null || list.get(i).compareTo(min2) < 0)) {
                min2 = list.get(i);
            }
        }
        firstTime = System.currentTimeMillis() - firstTime;
        System.out.println(firstTime);
        // Run separate because of caching!
//        list = Stream.generate(LongCompareObject::new)
//                .limit(2000).collect(Collectors.toList());
//        long secondTime = System.currentTimeMillis();
//        LongCompareObject s = minArray.findSecondMin(list);
//        secondTime = System.currentTimeMillis() - secondTime;
//        System.out.println(secondTime);
//        if (min2.compareTo(s) != 0) {
//            System.err.println(min2.s + " " + s.s);
//        }

        // n = 2000
        //s.length = 100000
        // 154 - simple method = 2 * n => n = 77 for this time
        //96 - advanced method  77 + log(77) - 2 = 83


        // n = 2000
        //s.length = 500000
        // 843 - simple method = 2 * n => n = 421.5 for this time
        // 436 - advanced method = 421.5 + 8.71 - 2 = 428.21

    }
}
