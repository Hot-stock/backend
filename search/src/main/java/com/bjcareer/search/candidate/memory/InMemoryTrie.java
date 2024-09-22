package com.bjcareer.search.candidate.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bjcareer.search.candidate.Trie;

public class InMemoryTrie implements Trie {
	private Node root = new Node();

	public void update(String str) {
		Node current = root;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			boolean containKey = current.child.containsKey(c);

			if (containKey) {
				current = current.child.get(c);
			} else {
				Node newNode = new Node();
				newNode.keyword = current.keyword + c;

				current.child.put(c, newNode);
				current = current.child.get(c);
			}
		}

		current.searchCount = 0L;
		current.endOfWord = true;
	}

	public List<String> search(String query) {
		Node current = root;
		List<String> result = new ArrayList<>();

		for (int i = 0; i < query.length(); i++) {
			char c = query.charAt(i);
			boolean containKey = current.child.containsKey(c);

			if (!containKey) {
				return result;
			}
			current = current.child.get(c);
		}
		search(current, result, 0);
		return result;
	}

	private void search(Node node, List<String> result, int idx) {
		Node current = node;

		if (current.endOfWord) {
			result.add(current.keyword);
		}

		Set<Character> characters = current.child.keySet();

		for (Character character : characters) {
			Node next = current.child.get(character);

			if (next == null) {
				continue;
			}

			search(next, result, idx + 1);
		}
	}

	@Override
	public String toString() {
		return root.toString();
	}
}
