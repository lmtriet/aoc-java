package pb18;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BFS {

    private static Map<String, List<Vertex>> cache = new HashMap<>();
    private static Map<String, List<Vertex>> compressedCache = new HashMap<>();
    private static Map<String, Integer> lengthCache = new HashMap<>();

    public static List<Vertex> findShortestPath(Vertex start, Vertex end){
        if(end == null){
            return new ArrayList<>();
        }
        String cacheKey =  start.getValue()+"."+end.getValue();
        String cacheKeyInv =  end.getValue()+"."+start.getValue();
        if(!cache.containsKey(cacheKey)) {
            final List<Vertex> path = doBFS(start, end);
            cache.put(cacheKey, path);
            List<Vertex> pathInv = new ArrayList<>(path);
            Collections.reverse(pathInv);
            cache.put(cacheKeyInv, pathInv);
        }
        return cache.get(cacheKey);
    }

    public static List<Vertex> findShortestCompressedPath(Vertex start, Vertex end){
        if(end == null){
            return new ArrayList<>();
        }
        String cacheKey =  start.getValue()+"."+end.getValue();
        String cacheKeyInv =  end.getValue()+"."+start.getValue();
        if(!compressedCache.containsKey(cacheKey)) {
            final List<Vertex> path = findShortestPath(start, end).stream().filter(vertex -> !vertex.getValue().equals("."))
                    .collect(Collectors.toList());
            compressedCache.put(cacheKey, path);
            List<Vertex> pathInv = new ArrayList<>(path);
            Collections.reverse(pathInv);
            compressedCache.put(cacheKeyInv, pathInv);
        }
        return compressedCache.get(cacheKey);
    }

    public static Integer findShortestLengthPath(Vertex start, Vertex end){
        if(end == null){
            return 0;
        }
        String cacheKey =  start.getValue()+"."+end.getValue();
        String cacheKeyInv =  end.getValue()+"."+start.getValue();

        if(!lengthCache.containsKey(cacheKey)) {
            final List<Vertex> path = findShortestPath(start, end);
            int length = (path.size() - 1);
            lengthCache.put(cacheKey, length);
            lengthCache.put(cacheKeyInv, length);
//            log.debug("Length from {} to {} is {}", start, end, length);
        }
        return lengthCache.get(cacheKey);
    }

    /**
     * Recursive BFS
     */
    private static List<Vertex> doBFS(Vertex start, Vertex end){
        //log.debug("doBFS start={} end={}", start, end);

        Map<Vertex, Vertex> parents = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();
        Queue<Vertex> toBeVisited = new LinkedList<>();
        toBeVisited.add(start);

        while (!toBeVisited.isEmpty()){
            final Vertex visiting = toBeVisited.poll();
            visited.add(visiting);
            if(visiting.equals(end))
                return backtrace(start, end, parents);
            else {
                final List<Vertex> neighbors = visiting.getAdjs().stream()
                        .filter(vertex -> !visited.contains(vertex))
                        .collect(Collectors.toList()); // add only not visited vertex to the queue to be visited
                toBeVisited.addAll(neighbors);
                neighbors.forEach(neighbor -> parents.put(neighbor, visiting)); // record the parent of those new candidate
            }
        }
        return new ArrayList<>(); // no path found
    }

    private static List<Vertex> backtrace(Vertex start, Vertex end, Map<Vertex, Vertex> parents){
        Vertex last = end;
        List<Vertex> path = new ArrayList<>();
        while (!last.equals(start)){
            path.add(last);
            last = parents.get(last);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }
}
