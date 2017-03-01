package ru.vlad24.trees;

public class BST<T extends Comparable<T>> {
	
	private Node<T> root;
	
	public void insert(T element){
		if (isEmpty()){
			root = new Node<T>(element, null, null);
		}else{
			add(element, root);
		}
	}
	
	private void add(T payload, Node<T> node) {
		if (node.isLeaf()){
			if (node.payload.compareTo(payload) < 0)
				node.right = new Node<T>(payload, null, null);
			else
				node.left = new Node<T>(payload, null, null);
		}else if(node.payload.compareTo(payload) < 0){
			add(payload, node.right);
		}else if(node.payload.compareTo(payload) > 0){
			add(payload, node.left);
		}else{
			return;
		}
		
	}

	public boolean remove(T element){
		return !isEmpty() && del(element, root);
	}
	
	private boolean del(T payload, Node<T> node) {
		//Not implemented yet;
		return true;
	}

	public boolean isEmpty(){
		return (root == null);
	}
	
	public int height(){
		if (isEmpty()){
			return 0;
		}
		else{
			return calculateHeight(root);
		}
	}
	
	private int calculateHeight(Node<T> node) {
		if (node.isLeaf()){
			return 1;
		}else{
			return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
		}
	}

	public boolean contains(T payload){
		return search(payload, root) != null;
	}
	
	private Node<T> search(T payload, Node<T> node) {
		if (node == null){
			return null;
		}else if(node.payload.compareTo(payload) < 0){
			return search(payload, node.right);
		}else if(node.payload.compareTo(payload) > 0){
			return search(payload, node.left);
		}else{
			return node;
		}
	}

	private static class Node<T>{
		private T payload;
		private Node<T> left;
		private Node<T> right;
		public Node(T payload, Node<T> left, Node<T> right) {
			this.payload = payload;
			this.left = left;
			this.right = right;
		}
		public boolean isLeaf(){
			return (left == null && right == null);
		}
	}
}


