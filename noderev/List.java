package ru.vlad24.interviewtasks.noderev;


/*
 * @author polina
 */
public class List {
    Node first;
    Node last;
    
    public List() {
    	first = null;
    	last = null;
    }
    
    void reverse() {
    	Node g = null;
    	Node prev = null;
    	Node cur = first;
    	while (cur != null){
        	g = cur.next;
        	cur.next = prev;
        	prev = cur;
        	cur = g;
    	}
    	g = first;
    	first = last;
    	last = g;
    }
    
    void add(int x) {
    	if (isEmpty()) {
    		first = new Node(x,null);
    		last = first;
    	}
    	else {
    		last.next = new Node(x, null);
    		last = last.next;
    	}
    }

	
    boolean isEmpty() {
    	return last == null;
    }
    
    void print() {
    	Node g = first;
    	while (g != null) {
			System.out.print(g.value + " ");
			g = g.next;
		}
    	System.out.println();
    }
    
    void delete(int x){
    	Node g = first;
    	Node prev = null;
    	while (g.value != x){
    		prev = g;
    		g = g.next;
    	}
    	if (g != null){
    		prev.next = g.next;
    	}
    }
	
    
	private static class Node{
		int value;
		Node next;
		
		public Node(int value, Node next) {
			super();
			this.value = value;
			this.next = next;
		}
	}
	
}


