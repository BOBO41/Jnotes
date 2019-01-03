package com.hdr.learn.datastructuree.btree;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author hdr
 */
public class Trie {

	private class Node {
		public boolean isWord;
		public Map<Character, Node> next;

		public Node() {
			this.isWord = false;
			next = new TreeMap<>();
		}

		public Node(boolean isWord) {
			this.isWord = isWord;
			next = new TreeMap<>();
		}

	}

	private Node root;
	private int size;

	public Trie() {
		root = new Node();
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void add(String word) {
		Node cur = root;
		for (char c : word.toCharArray()) {
			if (cur.next.get(c) == null) {
				cur.next.put(c, new Node());
			}
			cur = cur.next.get(c);
		}

		if (!cur.isWord) {
			cur.isWord = true;
			size++;
		}

	}

	public boolean contains(String word) {
		Node cur = root;

		for (char c : word.toCharArray()) {

			if (cur.next.get(c) == null) {
				return false;
			}
			cur = cur.next.get(c);
		}

		return cur.isWord;
	}

	public boolean isPrefix(String prefix) {
		Node cur = root;
		for (char c : prefix.toCharArray()) {
			if (cur.next.get(c) == null) {
				return false;
			}
			cur = cur.next.get(c);
		}

		return true;
	}

	public boolean search(String word) {
		return match(root, word, 0);
	}

	private boolean match(Node cur, String word, int index) {
		if (index == word.length()) {
			return cur.isWord;
		}

		char c = word.charAt(index);
		if (c != '.') {
			if (cur.next.get(c) == null) {
				return false;
			}
			return match(cur.next.get(c), word, index + 1);
		}
		for (Character key : cur.next.keySet()) {
			if (match(cur.next.get(key), word, index + 1)) {
				return true;
			}
		}
		return false;
	}


	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.add("hello");
		trie.add("world");
		System.out.println("contains word hello " + trie.contains("hello"));
		System.out.println("contains word fuck " + trie.contains("fuck"));
		System.out.println("'he' is prefix " + trie.isPrefix("he"));
	}

}
