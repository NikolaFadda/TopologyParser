import java.io.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    La funzione di parsing è stata modificata a partire dallo snippet riportato a questo indirizzo:
    https://stackoverflow.com/questions/5819772/java-parsing-text-file
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        //Preparation for reading the file
        FileReader input = new FileReader("/home/nikola/Scrivania/OldTopologies/gte_bad.20");
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = null;

        //Auxiliary variables
        int nodeNumber, arcNumber=0, auxCapacity=0;
        Node[] nodeList=null;
        Set<Arc> arcSet=null;
        Node auxHead, auxTail;
        Arc auxArc;
        String[] row, rowElements;
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
                arcSet = new HashSet<Arc>(arcNumber);
                for(int i=0;i<nodeNumber;i++){

                    nodeList[i]= new Node(i+1,0);

                }

            }

            //Initialization of node List after retrieval of

            //System.out.println("La grandezza di nodelist è: "+nodeList.length);
            //"n" is the flag for node
            else if(rowElements[0].equals("n")){

                //Initialization of the Source
                if (Integer.parseInt(rowElements[2])>0){

                    //nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),1));
                    nodeList[Integer.parseInt(rowElements[1])-1]= new Node(Integer.parseInt(rowElements[1]),1);
                }

                //Initialization of the Sink
                if (Integer.parseInt(rowElements[2])<0){

                    //nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),-1));
                    nodeList[Integer.parseInt(rowElements[1])-1]= new Node(Integer.parseInt(rowElements[1]),-1);
                }

            }

            //"a" is the flag for arc
            if(rowElements[0].equals("a")){
                //I save the data about the tail from nodeList
                //auxTail=new Node(nodeList.get(Integer.parseInt(rowElements[1])-1).ID,
                //                 nodeList.get(Integer.parseInt(rowElements[1])-1).type);

                auxTail=new Node (nodeList[Integer.parseInt(rowElements[1])-1].ID,
                                  nodeList[Integer.parseInt(rowElements[1])-1].type);

                //I save the data about the head from nodeList
                auxHead=new Node (nodeList[Integer.parseInt(rowElements[2])-1].ID,
                                  nodeList[Integer.parseInt(rowElements[2])-1].type);

                //I save the arc capacity
                auxCapacity=Integer.parseInt(rowElements[4]);
                auxArc = new Arc(auxTail,auxHead,auxCapacity);
                //I add the arc into arcSet, and I cannot have a double arc
                arcSet.add(auxArc);

                //To be sure, I add the arc to the tail's ouflow and to the head's inflow
                nodeList[auxHead.ID-1].inflow.add(auxArc);
                nodeList[auxTail.ID-1].outflow.add(auxArc);
            }


        }

        System.out.println("NodeList size:"+nodeList.length+"\nSink node is node: 47\nInflow List\t\t\tOutflow List");
        Node sink = nodeList[46];
        for (int i=0;i<sink.inflow.size();i++){
            System.out.println(sink.inflow.get(i).tail.ID+" "+sink.inflow.get(i).head.ID+" "+sink.inflow.get(i).capacity+
            "\t\t"+sink.outflow.get(i).tail.ID+" "+sink.outflow.get(i).head.ID+" "+sink.outflow.get(i).capacity);
        }



//        File newTopology = new File("/home/nikola/Scrivania/OldTopologies/"+nodeNumber+"nodes_"+arcNumber+"arcs.txt");
//        FileWriter writer = new FileWriter()

    }
}

//"/home/nikola/Scrivania/OldTopologies/gte_bad.20"