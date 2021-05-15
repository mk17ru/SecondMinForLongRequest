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

}
