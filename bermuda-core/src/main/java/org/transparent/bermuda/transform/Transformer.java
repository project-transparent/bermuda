package org.transparent.bermuda.transform;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import org.transparent.bermuda.util.Stage;

import java.io.Closeable;

public interface Transformer<T> extends Comparable<Transformer<T>>, Closeable {
	Stage stage();

	void transform(JavacTask task, TaskEvent e);

	T transform(T t);

	int priority();
}