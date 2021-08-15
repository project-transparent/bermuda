package org.transparent.treegen.model;

public final class Field {
	public final String type;
	public final String name;

	public Field(String type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Field{" +
				"type='" + type + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}