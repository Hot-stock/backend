package com.bjcareer.search.retrieval;

import java.util.HashMap;
import java.util.Map;

public class Node {
	Map<Character, Node> child = new HashMap<>();
	String keyword = "";
	Long search_count = 0L;
	boolean endOfWord = false;

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
