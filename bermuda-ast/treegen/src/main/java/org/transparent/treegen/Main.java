package org.transparent.treegen;

import org.transparent.treegen.model.Field;
import org.transparent.treegen.model.Tree;
import org.transparent.treegen.util.CodeWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public final class Main {
	private static Path input = Paths.get("model.xml");
	private static Path trees = Paths.get("trees/org/transparent/bermuda/ast/tree");

	public static void main(String[] args) throws IOException, XMLStreamException {
		if (Files.notExists(input)) {
			System.err.println("model.xml file does not exist");
			System.exit(1);
		}
		if (Files.notExists(trees))
			Files.createDirectories(trees);

		xml();
	}

	private static void xml() throws IOException, XMLStreamException {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		final XMLEventReader reader = factory.createXMLEventReader(Files.newInputStream(input));

		final List<String> imports = new ArrayList<>();
		Tree tree = null;

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				final StartElement start = event.asStartElement();
				final String name = start.getName().getLocalPart();
				switch (name) {
					case "import":
						event = reader.nextEvent();
						imports.add(event.asCharacters().getData());
						break;
					case "tree":
						tree = new Tree(
								start.getAttributeByName(new QName("name")).getValue()
						);
						break;
					case "field":
						if (tree != null) {
							event = reader.nextEvent();
							tree.fields.add(new Field(
									start.getAttributeByName(new QName("type")).getValue(),
									start.getAttributeByName(new QName("name")).getValue()
							));
						}
						break;
				}
			}
			if (event.isEndElement()) {
				final EndElement end = event.asEndElement();
				if (end.getName().getLocalPart().equals("tree")) {
					if (tree != null) {
						write(imports, tree);
						tree = null;
					}
				}
			}
		}
	}

	private static void write(List<String> imports, Tree tree) throws IOException {
		final List<Field> fields = tree.fields;
		final int fSize = fields.size();
		final CodeWriter cw = new CodeWriter();
		cw.$("package org.transparent.bermuda.ast.tree;");
		cw.$n();
		imports.forEach(i -> cw.$("import " + i + ";"));
		cw.$n();
		cw.$r("public class " + tree.name + " implements BMTree {");
		fields.forEach(field -> {
			// Fields
			cw.$("private " + field.type + " " + field.name + ";");
		});
		cw.$n();
		// Constructor Header
		final StringBuilder sb = new StringBuilder();
		sb
				.append("public ")
				.append(tree.name)
				.append('(');
		for (int i = 0; i < fSize; i++) {
			final Field field = fields.get(i);
			sb
					.append(field.type)
					.append(' ')
					.append(field.name);
			if (i != fSize - 1)
				sb.append(", ");
		}
		sb.append(") {");
		// Constructor Initializers
		cw.$r(sb.toString());
		fields.forEach(field ->
			cw.$("this." + field.name + " = " + field.name + ";"));
		cw.$l("}");
		cw.$n();
		// Getters
		fields.forEach(field -> {
			cw.$r("public " + field.type + " get" + capitalize(field.name) + "() {");
			cw.$("return " + field.name + ";");
			cw.$l("}");
			cw.$n();
		});
		// Setters
		for (int i = 0; i < fSize; i++) {
			final Field field = fields.get(i);
			cw.$r("public void set" + capitalize(field.name) + "(" + field.type + " " + field.name + ") {");
			cw.$("this." + field.name + " = " + field.name + ";");
			cw.$l("}");
			if (i != fSize - 1)
				cw.$n();
		}
		cw.$l("}");

		Files.write(
				trees.resolve(tree.name + ".java"),
				cw.toBytes(),
				WRITE, CREATE, TRUNCATE_EXISTING
		);
	}

	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
}