package org.transparent.bermuda.ast.tree;

import java.util.List;

public class BMCompilationUnit implements BMTree {
	private List<BMAnnotation> annotations;
	private String packageName;

	public BMCompilationUnit(List<BMAnnotation> annotations, String packageName) {
		this.annotations = annotations;
		this.packageName = packageName;
	}

	public List<BMAnnotation> getAnnotations() {
		return annotations;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setAnnotations(List<BMAnnotation> annotations) {
		this.annotations = annotations;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
