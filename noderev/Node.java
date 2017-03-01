package ru.vlad24.interviewtasks.noderev;

class Node {
    int payload;
    Node next;
    
    static Node reverse(Node head) {
    	Node result = null;
    	if (head != null) {
    		Node leftNode = null;
    		Node centerNode = head;
    		Node rightNode = head.next;
    		while (rightNode != null){
    			centerNode.next = leftNode;
    			leftNode = centerNode;
    			centerNode = rightNode;
    			rightNode = rightNode.next;
    		}
    		centerNode.next = leftNode;
    		result = centerNode;
    	}
    	return result;
    }

	
    static void print(Node head) {
    	Node current = head;
    	System.out.print("[ ");
    	while (current != null){
    		System.out.print(current.payload + " ");
    		current = current.next;
    	}
    	System.out.print("]\n");
    }
	

	public Node(int payload, Node next) {
		super();
		this.payload = payload;
		this.next = next;
	}
    
}


