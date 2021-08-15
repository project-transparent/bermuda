package org.transparent.bermuda.ast.transform;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import org.transparent.bermuda.transform.BaseTransformer;
import org.transparent.bermuda.util.Stage;
import org.transparent.bermuda.ast.util.TreeFactory;

import static com.sun.tools.javac.tree.JCTree.*;

public abstract class TreeTransformer
		extends TreeTranslator
		implements BaseTransformer<JCTree> {
	protected TreeFactory factory;

	@Override
	public final Stage stage() {
		return Stage.AFTER_PARSE;
	}

	@Override
	public void transform(JavacTask task, TaskEvent e) {
		final Context ctx = ((BasicJavacTask) task).getContext();
		factory = TreeFactory.instance(ctx);
		result = transform((JCCompilationUnit) e.getCompilationUnit());
	}

	@Override
	public JCTree transform(JCTree tree) {
		tree.accept(this);
		return tree;
	}
}