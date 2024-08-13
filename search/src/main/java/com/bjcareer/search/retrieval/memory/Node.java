package com.bjcareer.search.retrieval.memory;

import java.util.HashMap;
import java.util.Map;

public class Node {
	Map<Character, Node> child = new HashMap<>();
	String keyword = "";
	Long searchCount = 0L;
	boolean endOfWord = false;

	public Node() {
	}

	public Node(String keyword, Long search_count, boolean endOfWord) {
		this.keyword = keyword;
		this.searchCount = search_count;
		this.endOfWord = endOfWord;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		printNode(this, builder, "");
		return builder.toString();
	}

	private void printNode(Node node, StringBuilder builder, String prefix) {
		for (Map.Entry<Character, Node> entry : node.child.entrySet()) {
			builder.append(prefix).append(entry.getKey());
			if (entry.getValue().endOfWord) {
				builder.append(" (end)");
			}
			builder.append("\n");
			printNode(entry.getValue(), builder, prefix + "  ");
		}
	}
}