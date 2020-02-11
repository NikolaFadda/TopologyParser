import java.util.LinkedList;
import java.util.List;

public class Pathfinder {

    //Data about the old topology
    public LinkedList<Node> sinks;
    public LinkedList<Node> sources;
    public Node[] nodes;
    public LinkedList<Arc> arcs;

    //Data about the new topology
    public LinkedList<Node> visitedNodes;
    public LinkedList<Arc> visitedArcs;
    public LinkedList<Node> toVisit;

    public LinkedList<Node> nodeToAdd;
    public LinkedList<Arc> arcToAdd;

    public Pathfinder(LinkedList<Node> sinks, LinkedList<Node> sources, Node[] nodes, LinkedList<Arc> arcs) {

        this.sinks = sinks;
        this.sources = sources;
        this.nodes = nodes;
        this.arcs = arcs;

        this.visitedNodes = new LinkedList<Node>();
        this.visitedArcs = new LinkedList<Arc>();
        this.toVisit = new LinkedList<Node>();

        this.arcToAdd = new LinkedList<Arc>();
        this.nodeToAdd = new LinkedList<Node>();


    }


    public void initialStep() {

        //For every source node

        for (Node s : this.sources
        ) {

            //For every arc in the source node's outflow
            for (Arc a : nodes[s.ID - 1].outflow
            ) {

                //If I have already visited this arc, do nothing
                System.out.println(this.visitedNodes.contains(a.head));
                if (this.visitedNodes.contains(a.head)) {
                }

                //Otherwise add the Arc to the visited arcs
                else {

                    this.visitedArcs.add(a);
                    this.toVisit.addLast(a.head);

                }
            }

            this.visitedNodes.add(s);

        }

    }

   /*public void step() {

        //For every node that I have yet to visit

        for (Node n : this.toVisit
        ) {
            //I visit the node
            System.out.println("Visiting node " + n.ID);

            //I visit every arc of its outflow
            for (Arc a : nodes[n.ID - 1].outflow
            ) {
                System.out.println("Visiting arc " + a.tail.ID + "->" + a.head.ID);

                //If I already passed this node, I do nothing
                System.out.println("Contains node? "+this.visitedNodes.contains(a.head));
                if (this.visitedNodes.contains(a.head)) {
                    System.err.println("Node " + a.head.ID + " already visited!");
                }
                //If I already visited this arc, I do nothing
                else if (this.visitedArcs.contains(a)) {
                    System.err.println("Arc (" + a.tail.ID + "-> " + a.head.ID + ") already visited!");
                }

                //Otherwise, I add the arc as some arc to visit
                //and the node as a node to visit
                else {

                    this.arcToAdd.addLast(a);
                    this.nodeToAdd.addLast(a.head);

                }

            }

            //I visited this node, so I add it
            System.out.println("Adding node " + n.ID + " to Nodes Visited");

            if (!visitedNodes.contains(n)) this.visitedNodes.addLast(n);
        }

        this.toVisit.clear();

        for (Arc a : arcToAdd
        ) {
            System.out.println("Adding arc " + a.tail.ID + "->" + a.head.ID + " to Arcs Visited");
            if (!this.visitedArcs.contains(a)) this.visitedArcs.addLast(a);

        }

        for (Node n : nodeToAdd
        ) {
            this.toVisit.addLast(n);
            System.out.println("Adding node " + n.ID + "to Nodes to Visit");

        }

        this.nodeToAdd.clear();
        this.arcToAdd.clear();

    }*/


    public void andraStep(){

        boolean visitedNode=false;
        //For every node still to visit
        for (Node n: toVisit
        ) {

            //Screen reading of the node visited
            System.out.println("Visiting node " + n.ID);

            //For every arc in the outflow of the node we still have to visit
            for (Arc a: nodes[n.ID-1].outflow
            ) {

                //To screen data
                System.out.println("Visiting arc " + a.tail.ID + "->" + a.head.ID);


                for (Node visited: visitedNodes
                     ) {

                    if(visited.equals(a.head)) visitedNode=true;

                }

                System.out.println("Contains node "+a.head.ID+"? "+visitedNode);

                //If I already visited this node, print it to screen
                if(visitedNode){

                    System.err.println("Node " + a.head.ID + " already visited!");

                }else{

                    //Otherwise I add this arc to the list of arcs to add
                    this.arcToAdd.addLast(a);

                    //And add its head (destination) to the list of node to add
                    System.out.println("In the future I will visit node"+a.head.ID);
                    this.nodeToAdd.addLast(a.head);

                }

                visitedNode=false;

            }

            //I proceed to add this node the Visited Nodes
            System.out.println("Adding node " + n.ID + " to Nodes Visited");


            if(visitedNodes.contains(n)){

            }else {
                this.visitedNodes.addLast(n);
            }

            //I transfer all the arcs belonging the list of arcs to add t inside the list of visited arcs
            for (Arc a: arcToAdd
            ) {

                if(visitedArcs.contains(a)){

                }else{

                    //To screen visualization
                    System.out.println("Adding arc " + a.tail.ID + "->"+a.head.ID+" to Arcs Visited");
                    visitedArcs.add(a);
                }
            }

            //Empty the list, sto there will be no repetition
            arcToAdd.clear();
        }

        //I empty the list of nodes to visit
        this.toVisit.clear();

        //I transfer all the nodes belonging to the lists of nodes to add inside the list of nodes to visit
        this.toVisit.addAll(nodeToAdd);

        

        this.nodeToAdd.clear();

    }



}

