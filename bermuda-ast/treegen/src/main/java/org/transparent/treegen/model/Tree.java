package org.transparent.treegen.model;

import java.util.ArrayList;
import java.util.List;

public final class Tree {
	public final String name;
	public final List<Field> fields;

	public Tree(String name) {
		this.name = name;
		this.fields = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "Tree{" +
				"name='" + name + '\'' +
				", fields=" + fields +
				'}';
	}
}