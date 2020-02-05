public class Arc {

    //An Arc is expressed in the form (tail)->(head) and has a nonnegative capacity value

    public Node head;
    public Node tail;
    public int capacity;



    public Arc(Node tail, Node head, int capacity){

        this.head= head;
        this.tail=tail;
        this.capacity=capacity;
    }


    public boolean equals (Arc a){

        if((this.head.equals(a.head))&&(this.tail.equals(a.tail))) return true;
        if((this.head.equals(a.tail))&&(this.tail.equals(a.head))) return true;

        return false;
    }
}
