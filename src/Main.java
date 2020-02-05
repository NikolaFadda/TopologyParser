import java.io.*;
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


        FileReader input = new FileReader("/home/nikola/Scrivania/OldTopologies/gte_bad.20");
        BufferedReader bufRead = new BufferedReader(input);
        String myLine = null;
        int nodeNumber=0, arcNumber=0, auxCapacity=0;
        List<Node> nodeList = null;
        Set<Arc> arcSet=null;
        Node auxHead, auxTail;

        while ( (myLine = bufRead.readLine()) != null)
        {

            String[] row = myLine.split("\n");
            // check to make sure you have valid data

            String[] rowElements = row[0].split(" ");

            //"c" is the flag for comment
            if(rowElements[0]=="c"){}

            //"p" is the flag about the general settings of the graph
            if (rowElements[0]=="p"){
                nodeNumber=Integer.parseInt(rowElements[2]);
                arcNumber=Integer.parseInt(rowElements[3]);
                System.out.println("# Nodes: "+nodeNumber+" # Arcs: "+arcNumber);

            }

            //Initialization of node List after retrieval of
            for(int i=0;i<nodeNumber;i++){

                nodeList.add(new Node(i,0));

            }

            //"n" is the flag for node
            if(rowElements[0]=="n"){

                //Initialization of the Source
                if (Integer.parseInt(rowElements[2])>0){

                    nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),1));

                }

                //Initialization of the Sink
                if (Integer.parseInt(rowElements[2])<0){

                    nodeList.set(Integer.parseInt(rowElements[1]) -1, new Node(Integer.parseInt(rowElements[1]),-1));

                }

            }

            //"a" is the flag for arc
            if(rowElements[0]=="a"){

                auxTail=new Node(nodeList.get(Integer.parseInt(rowElements[1])-1).ID,
                                 nodeList.get(Integer.parseInt(rowElements[1])-1).type);

                auxHead=new Node(nodeList.get(Integer.parseInt(rowElements[2])-1).ID,
                        nodeList.get(Integer.parseInt(rowElements[2])-1).type);

                auxCapacity=Integer.parseInt(rowElements[4]);

                arcSet.add(new Arc(auxTail,auxHead,auxCapacity));

            }




        }

    }
}

//"/home/nikola/Scrivania/OldTopologies/gte_bad.20"