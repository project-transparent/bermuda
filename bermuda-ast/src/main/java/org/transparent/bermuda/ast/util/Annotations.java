package org.transparent.bermuda.ast.util;

import com.sun.tools.javac.util.Name;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.sun.tools.javac.tree.JCTree.*;

public final class Annotations {
	private Annotations() {
		throw new InstantiationError("Cannot instantiate utility class");
	}

	private static boolean containsAnnotation(List<JCAnnotation> annotations, String name) {
		for (JCAnnotation annotation : annotations) {
			Name aName = annotation.type.tsym.getQualifiedName();
			return aName.contentEquals(name);
		}
		return false;
	}

	private static boolean isAnnotated(JCClassDecl tree, Class<? extends Annotation> annotation) {
		return containsAnnotation(tree.mods.annotations, annotation.getCanonicalName());
	}

	private static boolean isAnnotated(JCMethodDecl tree, Class<? extends Annotation> annotation) {
		return containsAnnotation(tree.mods.annotations, annotation.getCanonicalName());
	}

	private static boolean isAnnotated(JCVariableDecl tree, Class<? extends Annotation> annotation) {
		return containsAnnotation(tree.mods.annotations, annotation.getCanonicalName());
	}
}