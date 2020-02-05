import java.util.List;

public class Node {

    public int ID;
    public int type; //type -1 sink; type 0 transport; type 1 source
    public List<Arc> outflow;
    public List<Arc> inflow;


    public Node (int ID, int type){

        this.ID=ID;
        this.type=type;
        this.outflow=null;
        this.inflow=null;


    }



    public boolean equals(Node o) {
        if(this.ID < o.ID) return false;
        if(this.ID > o.ID) return false;

        return true;
    }
}
