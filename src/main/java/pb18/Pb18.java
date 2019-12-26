package pb18;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static pb18.MazeUtils.simplePath;

@Slf4j
public class Pb18 {

    static final Map<Integer, Integer> distanceToCollectKeysCache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        long t1 = System.currentTimeMillis();

        final MazeGraph mazeGraph = MazeUtils.load("input-final.txt");

        long t2 = System.currentTimeMillis();
        log.info("Input file loaded into a graph datastructure in {} ms", t2-t1);

        final int minSteps = distanceToCollectKeys(mazeGraph, mazeGraph.getStart(), new HashSet<>(mazeGraph.getKeys().values()), 0);

        log.info("Min steps {} computed in {} ms", minSteps, System.currentTimeMillis() - t2);
    }

    static int distanceToCollectKeys(MazeGraph mazeGraph, Vertex start, Set<Vertex> toBeCollectedKeys, int level) {
        String filled = StringUtils.repeat("    ", level);
        final int cacheKey = Objects.hash(start, toBeCollectedKeys);
        log.debug("{}Explore from {} with missing keys {} hash {}", filled, start, toBeCollectedKeys, cacheKey);
        if(distanceToCollectKeysCache.containsKey(cacheKey)){
            log.debug("{}=> Cache hit {}", filled, distanceToCollectKeysCache.get(cacheKey));
            return distanceToCollectKeysCache.get(cacheKey);
        }

        if(toBeCollectedKeys.isEmpty()){
            return 0;
        }

        Set<Vertex> freeKeys = getFreeKeys(mazeGraph, start, toBeCollectedKeys);

        log.debug("{}Free keys {}", filled, simplePath(freeKeys));

        int result = Integer.MAX_VALUE;
        for(Vertex freeKey : freeKeys) {
            toBeCollectedKeys.remove(freeKey);
            int length = BFS.findShortestLengthPath(start, freeKey);
            int pathLength = length + distanceToCollectKeys(mazeGraph, freeKey, toBeCollectedKeys, level + 1);
            log.debug("{}Length={} PathLength={} Result={}", filled, length, pathLength, result);
            result = Math.min(result, pathLength);
            toBeCollectedKeys.add(freeKey);
        }
        log.debug("{}From {} with missing keys {} result {}", filled, start, toBeCollectedKeys, result);
        distanceToCollectKeysCache.put(cacheKey, result);
        return result;
    }

    /**
     * Reachable key without obstacle
     * Free keys are not blocked by a door, neither by an another key.
     */
    static Set<Vertex> getFreeKeys(MazeGraph mazeGraph, Vertex start, Set<Vertex> toBeCollectedKeys){
        Set<Vertex> freeKeys = new HashSet<>();
        for(Vertex toBeCollectedKey : toBeCollectedKeys) {
            final List<Vertex> path = BFS.findShortestCompressedPath(start, toBeCollectedKey);
            boolean blockedDoor = false;
            boolean blockedKey = false;
            for (Vertex currentPosition : path) {
                if (currentPosition.isDoor() && toBeCollectedKeys.contains(mazeGraph.getKeys().get(currentPosition.getValue().toLowerCase()))) {
                    blockedDoor = true;
                    break;
                }
                if (currentPosition != start
                        && currentPosition.isKey()
                        && toBeCollectedKeys.contains(currentPosition) && toBeCollectedKey != currentPosition) {
                    blockedKey = true;
                    break;
                }
            }
            if (!blockedDoor && !blockedKey) {
                freeKeys.add(toBeCollectedKey);
            }
        }
        return freeKeys;
    }
}
