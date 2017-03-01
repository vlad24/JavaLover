package ru.vlad24.interviewtasks.noderev;

public class Main {
	public static void main(String[] args) {
		//Basic test
		Node node4 = new Node(4, null);
		Node node3 = new Node(3, node4);
		Node node2 = new Node(2, node3);
		Node node1 = new Node(1, node2);
		Node node0 = new Node(0, node1);
		Node.print(node0);
		Node.reverse(node0);
		Node.print(node4);
		//Two node list test
		Node node6 = new Node(6, null);
		Node node5 = new Node(5, node6);
		Node.print(node5);
		Node.reverse(node5);
		Node.print(node6);
		//One node list test
		Node node7 = new Node(7, null);
		Node.print(node7);
		Node.reverse(node7);
		Node.print(node7);
		//Empty list reverse test
		Node node8 = null;
		Node.print(node8);
		Node.reverse(node8);
		Node.print(node8);
	}
	
}
