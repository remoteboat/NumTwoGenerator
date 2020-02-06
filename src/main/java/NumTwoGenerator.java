import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *  Calculates a random sequence of numbers with the specified distribution.
 *  
 *  Samples are taken with respect to their frequency (higher frequencies are more
 *  likely to be sampled) and the previously sampled number is prevented from being
 *  taken by temporarily zeroing out it's frequency (this prevents consecutive samples
 *  from being the same.
 */
public class NumTwoGenerator {

    public static void main(String[] args) {
        // read number frequency resource to hashmap
        InputStream in = NumTwoGenerator.class.getClassLoader().getResourceAsStream("frequencydata.txt");
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inReader);
        String str;
        Map<Integer, Integer> freqData = new HashMap<>();

        try {
            while ((str = reader.readLine()) != null) {
                String[] kv = str.split(" ");
                freqData.put(Integer.valueOf(kv[0]), Integer.valueOf(kv[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // generate the dataset
        ReservoirShuffle reservoirShuffle = new ReservoirShuffle();
        Integer[] out = reservoirShuffle.getShuffle(freqData);

        // write dataset to output file
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("./test.output");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = new PrintWriter((fileWriter));
        for(Integer i : out) {
            writer.println(i);
        }
        writer.close();

        // print line numbers that contain 20
        List<Integer> twentyIndices = IntStream
                .range(0, out.length)
                .filter(i -> out[i] == 20)
                .map(i -> i + 1)    // compensate for zero-index
                .boxed()
                .collect(Collectors.toList());
        System.out.println("20 appears on lines");
        System.out.println(twentyIndices.toString());

    }
}
