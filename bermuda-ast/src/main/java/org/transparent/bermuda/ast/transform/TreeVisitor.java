package org.transparent.bermuda.ast.transform;

import com.sun.tools.javac.tree.JCTree;

public abstract class TreeVisitor extends TreeTransformer {
	@Override
	public final JCTree transform(JCTree tree) {
		visit(tree);
		return tree;
	}

	public abstract void visit(JCTree tree);
}
