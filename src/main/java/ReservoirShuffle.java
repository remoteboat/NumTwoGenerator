import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReservoirShuffle {
    private Random random;

    public ReservoirShuffle() {
        this.random = new Random();
    }

    public Integer getSample(Map<Integer, Integer> frequencyMap) {
        Integer total = frequencyMap.values()
                .stream()
                .reduce(0, Integer::sum);

        // nothing to sample
        if (total == 0) {
            return null;
        }

        int sampleIdx = random.nextInt(total);
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            sampleIdx = sampleIdx - value;
            if (sampleIdx < 0) {
                return key;
            }
        }
        return null;
    }

    public Integer[] getShuffle(Map<Integer, Integer> frequencyMap) {

        // shuffleFrequencyMap is the original frequency minus the samples taken
        Map<Integer, Integer> shuffleFrequencyMap = new HashMap<>(frequencyMap);
        // sampleFrequencyMap is used to zero out the frequency of the previously selected sample
        // (this is what prevents consecutive samples being the same)
        Map<Integer, Integer> sampleFrequencyMap = new HashMap<>(frequencyMap);
        Integer total = shuffleFrequencyMap
                .values()
                .stream()
                .reduce(0, Integer::sum);
        Integer[] results = new Integer[total];

        int i = 0;
        Integer currentSample;
        while (i < total) {
            currentSample = getSample(sampleFrequencyMap);
            if (currentSample != null) {
                results[i] = currentSample;
                // update the frequencies
                shuffleFrequencyMap.put(
                        currentSample,
                        shuffleFrequencyMap.get(currentSample) - 1
                );
                // make new frequency map to be passed to getSample
                sampleFrequencyMap = new HashMap<>(shuffleFrequencyMap);
                sampleFrequencyMap.put(currentSample, 0);
                i++;
            } else {
                // hit a dead end trying to prevent the same consecutive element
                // try again
                shuffleFrequencyMap = new HashMap<>(frequencyMap);
                sampleFrequencyMap = new HashMap<>(frequencyMap);
                i = 0;
            }
        }

        return results;
    }

}
