package org.transparent.bermuda.test;

import com.sun.tools.javac.code.Flags;
import org.transparent.bermuda.transform.TreeTransformer;

import static com.sun.tools.javac.tree.JCTree.*;

public final class FieldInjector extends TreeTransformer {
	@Override
	public void visitClassDef(JCClassDecl tree) {
		super.visitClassDef(tree);
		tree.defs = tree.defs.append(
				factory.VarDef(
						factory.Modifiers(Flags.PUBLIC),
						names.fromString("field"),
						id("java.lang.String"), null
				));
	}

	private JCExpression id(String id) {
		final String[] ids = id.split("\\.");
		return (ids.length == 0)
				? factory.Ident(names.fromString(id))
				: id(ids);
	}

	private JCExpression id(String[] ids) {
		JCExpression expr = null;
		for (String id : ids) {
			if (expr == null) expr = factory.Ident(names.fromString(id));
			else expr = factory.Select(expr, names.fromString(id));
		}
		return expr;
	}
}