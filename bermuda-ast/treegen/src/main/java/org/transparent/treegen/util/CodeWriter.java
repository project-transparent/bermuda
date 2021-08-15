package org.transparent.treegen.util;

import java.util.function.Consumer;

public final class CodeWriter {
	private final StringBuilder content;
	private int indent;
	private final String indentString;

	public CodeWriter() {
		this.content = new StringBuilder();
		this.indent = 0;
		this.indentString = "\t";
	}

	public CodeWriter(int indent, String indentString) {
		this.content = new StringBuilder();
		this.indent = indent;
		this.indentString = indentString;
	}

	// Prints indentation.
	public void $i() {
		for (int i = 0; i < indent; i++)
			content.append(indentString);
	}

	// Prints newline.
	public void $n() {
		content.append('\n');
	}

	// Appends indent string, prints, then prints newline.
	public void $(String s) {
		$i(); content.append(s); $n();
	}

	// Prints then indents.
	public void $r(String s) {
		$(s); indent++;
	}

	// Unindents then prints.
	public void $l(String s) {
		indent--; $(s);
	}

	// Indents, prints, then unindents.
	public void $lr(String s) {
		indent++;
		$i();
		content.append(s);
		indent--;
		$n();
	}

	public void $c(Consumer<CodeWriter> c) {
		final CodeWriter cw = new CodeWriter(indent, indentString);
		c.accept(cw);
		content.append(cw);
	}

	public byte[] toBytes() {
		return content.toString().getBytes();
	}

	@Override
	public String toString() {
		return content.toString();
	}
}