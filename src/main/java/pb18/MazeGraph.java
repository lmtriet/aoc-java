package pb18;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class MazeGraph {
    private List<Vertex> vertices = new ArrayList<>();
    private Vertex start;
    private Map<String, Vertex> keys = new HashMap<>(); // all keys by key value
    private Map<String, Vertex> doors = new HashMap<>(); // doors by key value
}
