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

		final CodeWriter cw = new CodeWriter();
		cw.$("package org.transparent.bermuda.ast.tree;");
		cw.$n();
		cw.$r("public interface BMTree {");
		cw.$("<T> T accept(BMTreeVisitor<T> visitor);");
		cw.$l("}");
		write("BMTree", cw.toBytes());

		xml();
	}

	private static void xml() throws IOException, XMLStreamException {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		final XMLEventReader reader = factory.createXMLEventReader(Files.newInputStream(input));

		final List<String> imports = new ArrayList<>();
		Tree tree = null;

		CodeWriter tw = new CodeWriter();
		tw.$("package org.transparent.bermuda.ast.tree;");
		tw.$n();
		tw.$r("public interface BMTreeVisitor<T> {");
		tw.$r("default T visit(BMTree tree) {");
		tw.$("return tree.accept(this);");
		tw.$l("}");
		tw.$n();

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
						tw.$("T visit" + tree.name.substring(2) + "(" + tree.name + " tree);");
						write(imports, tree);
						tree = null;
					}
				}
			}
		}

		tw.$l("}");
		write("BMTreeVisitor", tw.toBytes());
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
		for (Field field : fields) {
			cw.$r("public void set" + capitalize(field.name) + "(" + field.type + " " + field.name + ") {");
			cw.$("this." + field.name + " = " + field.name + ";");
			cw.$l("}");
			cw.$n();
		}
		// Visitor method
		cw.$r("public <T> T accept(BMTreeVisitor<T> visitor) {");
		cw.$("return visitor.visit" + tree.name.substring(2) + "(this);");
		cw.$l("}");
		cw.$l("}");

		write(tree.name, cw.toBytes());
	}

	public static void write(String name, byte[] bytes) throws IOException {
		Files.write(
				trees.resolve(name + ".java"),
				bytes,
				WRITE, CREATE, TRUNCATE_EXISTING
		);
	}

	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
}