package org.transparent.bermuda.util;

import com.sun.source.util.TaskEvent;

public enum Stage {
	BEFORE_PARSE,
	BEFORE_ENTER,
	BEFORE_ANALYZE,
	BEFORE_GENERATE,

	AFTER_PARSE,
	AFTER_ENTER,
	AFTER_ANALYZE,
	AFTER_GENERATE,
	;

	public static Stage before(TaskEvent.Kind kind) {
		switch (kind) {
			case PARSE: return BEFORE_PARSE;
			case ENTER: return BEFORE_ENTER;
			case ANALYZE: return BEFORE_ANALYZE;
			case GENERATE: return BEFORE_GENERATE;
			default: throw new IllegalArgumentException("Stage only supports the enter, parse, analyze, and generate kinds.");
		}
	}

	public static Stage after(TaskEvent.Kind kind) {
		switch (kind) {
			case ENTER: return AFTER_ENTER;
			case PARSE: return AFTER_PARSE;
			case ANALYZE: return AFTER_ANALYZE;
			case GENERATE: return AFTER_GENERATE;
			default: throw new IllegalArgumentException("Stage only supports the enter, parse, analyze, and generate kinds.");
		}
	}
}