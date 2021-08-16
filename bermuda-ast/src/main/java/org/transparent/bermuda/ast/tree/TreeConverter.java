package org.transparent.bermuda.ast.tree;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import org.transparent.bermuda.ast.util.TreeFactory;

import static com.sun.tools.javac.tree.JCTree.*;

public final class TreeConverter implements BMTreeVisitor<JCTree> {
	private final TreeFactory factory;

	public TreeConverter(Context context) {
		this.factory = TreeFactory.instance(context);
	}

	public TreeConverter(TreeFactory factory) {
		this.factory = factory;
	}

	@Override
	public JCCompilationUnit visitCompilationUnit(BMCompilationUnit tree) {
		return factory.maker().TopLevel(
				factory.map(tree.getAnnotations(), this::visitAnnotation),
				factory.id(tree.getPackageName()),
				factory.map(tree.getDefinitions(), this::visit)
		);
	}

	@Override
	public JCAnnotation visitAnnotation(BMAnnotation tree) {
		return factory.maker().Annotation(
				factory.id(tree.getName()),
				factory.list(tree.getArguments())
		);
	}

	@Override
	public JCClassDecl visitClass(BMClass tree) {
		return factory.maker().ClassDef(
				factory.mods(tree.getModifiers()),
				factory.name(tree.getName()),
				factory.map(tree.getTypeParameters(), this::visitTypeParameter),
				tree.getSuperclass(),
				factory.list(tree.getInterfaces()),
				factory.map(tree.getDefinitions(), this::visit)
		);
	}

	@Override
	public JCMethodDecl visitMethod(BMMethod tree) {
		return factory.maker().MethodDef(
				factory.mods(tree.getModifiers()),
				factory.name(tree.getName()),
				tree.getReturnType(),
				factory.map(tree.getTypeParameters(), this::visitTypeParameter),
				factory.map(tree.getParameters(), this::visitVariable),
				factory.list(tree.getExceptions()),
				tree.getBody(),
				tree.getDefaultValue()
		);
	}

	@Override
	public JCVariableDecl visitVariable(BMVariable tree) {
		return factory.maker().VarDef(
				factory.mods(tree.getModifiers()),
				factory.name(tree.getName()),
				tree.getType(),
				tree.getInitializer()
		);
	}

	@Override
	public JCTypeParameter visitTypeParameter(BMTypeParameter tree) {
		return factory.maker().TypeParameter(
				factory.name(tree.getName()),
				factory.list(tree.getBounds()),
				factory.map(tree.getAnnotations(), this::visitAnnotation)
		);
	}
}