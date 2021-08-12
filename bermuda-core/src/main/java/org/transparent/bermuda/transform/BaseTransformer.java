package org.transparent.bermuda.transform;

import java.io.IOException;

public interface BaseTransformer<T> extends Transformer<T> {
	@Override
	default int priority() {
		return 0;
	}

	@Override
	default int compareTo(Transformer<T> o) {
		return Integer.compare(priority(), o.priority());
	}

	@Override
	default void close() throws IOException {}
}