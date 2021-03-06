import java.io.*;

import java.util.*;

/*
    Authors: Andrea Corronca, Nicola Fadda, Alessio Murgioni

    English:
    This is an instance-parser for the Minimum Cost Flow datasets available at the Zuse Institute Berlin website.
    This parser transforms MCFP instances into MFP instances.
    It saves data about a graph G=(N,A) with:
    -Number of Nodes
    -Number of Arcs
    -Source Node (selected as the one with higher outflow value among the other source nodes
    -Sink Node (selected as the one with higher inflow value among the other sink nodes
    -Arcs in the form of [(tail, head) capacity]

    The resulting graph is guaranteed to have at least one directed path from Source to Sink
    In case of multiple sources, we choose the one with more outgoing arcs.
    In case of multiple sinks, we choose the one with more ingoing arcs.
    This parsing function has been tuned starting from the code snippet available at:
    https://stackoverflow.com/questions/5819772/java-parsing-text-file

    Italiano:
    Questo è un parser per le istanze di studio del Minimum Cost Flow Problem disponibili sul sito internet
    dello Zuse Institute Berlin. Questo parser trasforma le istanze da MCFP a MFP.
    Più precisamente, salva solo i dati su:
    -Quantità di Nodi
    -Quantità di Archi
    -Nodo Sorgente (selezionato come il nodo con maggior outflow
    -Nodo Pozzo (selezionato come il nodo con maggior inflow)
    -Archi nella forma [(coda, testa) capacità]

    Il grafo risultante possiederà sicuramente almeno un cammino orientato dalla Sorgente al Pozzo.
    Se dovessero essere presenti più Sorgenti, verrà scelta quella con più archi uscenti.
    Se dovessero essere presenti più Pozzi, verrà scelto quello con più archi entranti.
    La funzione di parsing è stata modificata a partire dallo snippet riportato a questo indirizzo:
    https://stackoverflow.com/questions/5819772/java-parsing-text-file
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        //Preparation for reading the file
        String path = "/home/nikola/Scrivania/OldTopologies/stndrd6.net";
        FileReader input = new FileReader(path);
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = null;

        //Auxiliary variables
        int nodeNumber, arcNumber=0, auxCapacity=0;
        Node[] nodeList=null;
        LinkedList<Node> sourceList= new LinkedList<Node>();
        LinkedList<Node> sinkList= new LinkedList<Node>();
        LinkedList<Arc> arcList=null;
        Node auxHead, auxTail;
        Arc auxArc;
        String[] row, rowElements;
        Set<Arc> arcSet;

        while ( (myLine = bufRead.readLine()) != null)
        {

            row = myLine.split("\n");
            // check to make sure you have valid data

            rowElements = row[0].split(" ");

            //"c" is the flag for comment
            if(rowElements[0].equals("c")){}

            //"p" is the flag about the general settings of the graph
            else if (rowElements[0].equals("p")){

                nodeNumber= Integer.parseInt(rowElements[2]);
                arcNumber=Integer.valueOf(rowElements[3]);
                System.out.println("# Nodes: "+nodeNumber+" # Arcs: "+arcNumber);
                nodeList = new Node[nodeNumber];
                arcList = new LinkedList<Arc>();

                for(int i=0;i<nodeNumber;i++){

                    nodeList[i]= new Node(i+1,0);

                }

            }

            //"n" is the flag for node
            else if(rowElements[0].equals("n")){

                //Initialization of a Source and insertion in both nodeList and sourceSet
                if (Integer.parseInt(rowElements[2])>0){

                    //nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),1));
                    nodeList[Integer.parseInt(rowElements[1])-1]= new Node(Integer.parseInt(rowElements[1]),1);
                    sourceList.add(new Node(Integer.parseInt(rowElements[1]),1));
                }

                //Initialization of a Sink and insertion in both nodeList and sinkSet
                if (Integer.parseInt(rowElements[2])<0){

                    //nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),-1));
                    nodeList[Integer.parseInt(rowElements[1])-1]= new Node(Integer.parseInt(rowElements[1]),-1);
                    sinkList.add(new Node(Integer.parseInt(rowElements[1]),-1));
                }

            }

            //"a" is the flag for arc
            if(rowElements[0].equals("a")){
                //I save the data about the tail from nodeList

                auxTail=new Node (nodeList[Integer.parseInt(rowElements[1])-1].ID,
                        nodeList[Integer.parseInt(rowElements[1])-1].type);

                //I save the data about the head from nodeList
                auxHead=new Node (nodeList[Integer.parseInt(rowElements[2])-1].ID,
                        nodeList[Integer.parseInt(rowElements[2])-1].type);

                //I save the arc capacity
                auxCapacity=Integer.parseInt(rowElements[4]);
                auxArc = new Arc(auxTail,auxHead,auxCapacity);
                //I add the arc into arcList, and I cannot have a double arc
                arcList.add(auxArc);

                //To be sure, I add the arc to the tail's ouflow and to the head's inflow

                //nodeList[auxHead.ID-1].inflow.add(auxArc);
                //nodeList[auxTail.ID-1].outflow.add(auxArc);
            }




        }

        //I'm erasing all double arcs
        arcSet= new LinkedHashSet<Arc>(arcList);
        arcList.clear();
        arcList.addAll(arcSet);

        for (Arc arc: arcList
             ) {

            System.out.println("Arco: "+ arc.tail.ID+"->"+arc.head.ID);
        }



        //I insert the arc in the correct inflow and outflow lists
        for (Arc a: arcList
             ) {

            nodeList[a.head.ID-1].inflow.add(a);
            nodeList[a.tail.ID-1].outflow.add(a);

        }

        //I guarantee that no arc will enter the source
        //I remove from arcList every arc inside the source's inflow list
        int auxIndex=0, auxSize=0, outflowSize=0;
        Arc toRemove, reverseToRemove ;

        //For every source node in the list
        for(int i=0; i<sourceList.size();i++){

            //I get the dimension of the source's inflow
            auxIndex=sourceList.get(i).ID;
            auxSize= nodeList[auxIndex-1].inflow.size();

            //For every arc in the source's inflow
            for(int j=0; j<auxSize; j++){


                toRemove=nodeList[auxIndex-1].inflow.get(j);
                //reverseToRemove= new Arc(toRemove.head, toRemove.tail, toRemove.capacity);
                //I remove it from the arcList

                arcList.remove(toRemove);

                //I also remove it from the tail node's outflow

                outflowSize=nodeList[toRemove.tail.ID-1].outflow.size();

                for (int a=0;a<outflowSize;a++){

                    if(nodeList[toRemove.tail.ID-1].outflow.get(a).head.ID==toRemove.head.ID){
                        reverseToRemove= nodeList[toRemove.tail.ID-1].outflow.get(a);
                        //arcList.remove(reverseToRemove);
                        nodeList[toRemove.tail.ID-1].outflow.remove(a);
                        outflowSize=nodeList[toRemove.tail.ID-1].outflow.size();
                    }
                }


            }

            nodeList[auxIndex-1].inflow.clear();

        }

        //I guarantee that no arc will exit the sink
        //I remove from arcList every arc inside the sink's outflow list
        for(int i=0; i<sinkList.size();i++){

            auxIndex=sinkList.get(i).ID;
            auxSize= nodeList[auxIndex-1].outflow.size();

            for(int j=0; j<auxSize; j++){

                toRemove=nodeList[auxIndex-1].outflow.get(j);
                arcList.remove(toRemove);

            }

            nodeList[auxIndex-1].outflow.clear();

        }



        Pathfinder pathfinder = new Pathfinder(sinkList, sourceList, nodeList, arcList);
        pathfinder.initialStep();

       while(pathfinder.toVisit.size()>0){
            pathfinder.andraStep();
        }

        String filename = pathfinder.nodes.length+"_nodes_"+pathfinder.visitedArcs.size()+"_arcs.txt";
        File newfile = new File ("/home/nikola/Scrivania/NewTopologies/"+filename);
        newfile.createNewFile();
        FileWriter writer = new FileWriter(newfile);

        int maxSource=0, maxSink=0, sourceID=0, sinkID=0;

        //I choose as my source the one among them that has the highest number of outgoing arcs
        for (Node n: pathfinder.sources
             ) {

            if(nodeList[n.ID-1].outflow.size()>maxSource){

                maxSource=n.outflow.size();
                sourceID=n.ID;
            }
        }

        //I choose as my sink the one among them that has the highest number of ingoing arcs
        for (Node n: pathfinder.sinks
             ) {

            if(nodeList[n.ID-1].inflow.size()>maxSink){

                maxSink=n.outflow.size();
                sinkID=n.ID;
            }

        }

        writer.write(pathfinder.nodes.length+"\n"+pathfinder.visitedArcs.size()+"\n"
                +sourceID+"\n"+sinkID+"\n");
        for (Arc a: pathfinder.visitedArcs
        ) {
            writer.write(a.tail.ID+" "+a.head.ID+" "+a.capacity+"\n");

        }

        writer.flush();
        writer.close();




    }


}



