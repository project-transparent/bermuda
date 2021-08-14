package org.transparent.bermuda.asm.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.annotation.Annotation;
import java.util.List;

public final class Annotations {
	private Annotations() {
		throw new InstantiationError("Cannot instantiate utility class");
	}

	private static boolean containsAnnotation(List<AnnotationNode> nodes, String descriptor) {
		if (nodes == null) return false;
		for (AnnotationNode node : nodes)
			if (descriptor.equals(node.desc))
				return true;
		return false;
	}

	public static boolean isAnnotated(ClassNode node, Class<? extends Annotation> annotation) {
		final String descriptor = Type.getDescriptor(annotation);
		return containsAnnotation(node.visibleAnnotations, descriptor)
				|| containsAnnotation(node.invisibleAnnotations, descriptor);
	}

	public static boolean isAnnotated(MethodNode node, Class<? extends Annotation> annotation) {
		final String descriptor = Type.getDescriptor(annotation);
		return containsAnnotation(node.visibleAnnotations, descriptor)
				|| containsAnnotation(node.invisibleAnnotations, descriptor);
	}

	public static boolean isAnnotated(FieldNode node, Class<? extends Annotation> annotation) {
		final String descriptor = Type.getDescriptor(annotation);
		return containsAnnotation(node.visibleAnnotations, descriptor)
				|| containsAnnotation(node.invisibleAnnotations, descriptor);
	}
}