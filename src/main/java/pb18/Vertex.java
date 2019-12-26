package pb18;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Vertex {
    public static Vertex WALL = new Vertex();

    private String value;
    private boolean key = false;
    private boolean door = false;

    private int column; // 1 index
    private int row; // 1 index

    private List<Vertex> adjs = new ArrayList<>(); // adjacency list

    @Builder
    public Vertex(String value, int column, int row) {
        this.value = value;
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return value + "(" + row + ":" + column + ")";
    }

}
