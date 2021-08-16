package org.transparent.bermuda.ast.transform;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import org.transparent.bermuda.ast.tree.BMClass;
import org.transparent.bermuda.ast.tree.BMMethod;
import org.transparent.bermuda.ast.tree.BMVariable;
import org.transparent.bermuda.ast.tree.TreeConverter;
import org.transparent.bermuda.transform.BaseTransformer;
import org.transparent.bermuda.util.Stage;
import org.transparent.bermuda.ast.util.TreeFactory;

import static com.sun.tools.javac.tree.JCTree.*;

public abstract class TreeTransformer
		extends TreeTranslator
		implements BaseTransformer<JCTree> {
	private TreeConverter converter;
	protected TreeFactory factory;

	@Override
	public final Stage stage() {
		return Stage.AFTER_PARSE;
	}

	@Override
	public final void transform(JavacTask task, TaskEvent e) {
		final Context ctx = ((BasicJavacTask) task).getContext();
		factory = TreeFactory.instance(ctx);
		converter = new TreeConverter(factory);
		result = transform((JCCompilationUnit) e.getCompilationUnit());
	}

	@Override
	public JCTree transform(JCTree tree) {
		tree.accept(this);
		return tree;
	}

	protected final void add(JCClassDecl clazz, BMClass tree) {
		clazz.defs = clazz.defs.append(converter.visitClass(tree));
	}

	protected final void add(JCClassDecl clazz, BMVariable tree) {
		clazz.defs = clazz.defs.append(converter.visitVariable(tree));
	}

	protected final void add(JCClassDecl clazz, BMMethod tree) {
		clazz.defs = clazz.defs.append(converter.visitMethod(tree));
	}
}