package org.transparent.bermuda.ast.util;

import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import java.util.List;

import static com.sun.tools.javac.tree.JCTree.*;

public final class TreeFactory {
	private final TreeMaker maker;
	private final Names names;

	public TreeFactory(TreeMaker factory, Names names) {
		this.maker = factory;
		this.names = names;
	}

	public JCExpression id(String id) {
		final String[] ids = id.split("\\.");
		return (ids.length == 0)
				? maker.Ident(names.fromString(id))
				: id(ids);
	}

	public JCExpression id(String[] ids) {
		JCExpression expr = null;
		for (String id : ids) {
			if (expr == null) expr = maker.Ident(names.fromString(id));
			else expr = maker.Select(expr, names.fromString(id));
		}
		return expr;
	}

	public <T> com.sun.tools.javac.util.List<T> list(List<T> oList) {
		com.sun.tools.javac.util.List<T> list = com.sun.tools.javac.util.List.nil();
		for (T tree : oList) list = list.append(tree);
		return list;
	}

	public TreeMaker maker() {
		return maker;
	}

	public Names names() {
		return names;
	}
}