package pb18;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static pb18.Vertex.WALL;

public class MazeUtils {

    static MazeGraph load(String fileName) throws Exception {
        final MazeGraph mazeGraph = new MazeGraph();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Pb18.class.getResourceAsStream("/pb18/" + fileName)))) {
            String line;
            int rowIdx = 1;
            List<Vertex> lastLine = null;
            while ((line = reader.readLine()) != null) {
                List<Vertex> currentLine = new ArrayList<>();
                for (int column = 0; column < line.length(); column++) {
                    char value = line.charAt(column);
                    if (value == '#') {
                        currentLine.add(WALL);
                    } else {
                        final Vertex vertex = Vertex.builder()
                                .row(rowIdx)
                                .column(column + 1)
                                .value("" + value)
                                .build();
                        if (currentLine.size() > 1 && currentLine.get(column - 1) != WALL) {
                            vertex.getAdjs().add(currentLine.get(column - 1));
                            currentLine.get(column - 1).getAdjs().add(vertex);
                        }
                        if ((lastLine != null) && (lastLine.get(column) != WALL)) {
                            vertex.getAdjs().add(lastLine.get(column));
                            lastLine.get(column).getAdjs().add(vertex);
                        }
                        mazeGraph.getVertices().add(vertex);
                        if ('@' == value)
                            mazeGraph.setStart(vertex);
                        else if (value >= 'a' && value <= 'z') {
                            mazeGraph.getKeys().put("" + value, vertex);
                            vertex.setKey(true);
                        } else if (value >= 'A' && value <= 'Z') {
                            mazeGraph.getDoors().put(("" + value).toLowerCase(), vertex);
                            vertex.setDoor(true);
                        }
                        currentLine.add(vertex);
                    }
                }
                lastLine = currentLine;
                rowIdx++;
            }
        }
        return mazeGraph;
    }

    public static String simplePath(Collection<Vertex> path){
        return path.stream().map(vertex -> vertex.getValue()).collect(Collectors.toList()).toString();
    }
}
