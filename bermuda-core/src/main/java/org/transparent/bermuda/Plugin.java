package org.transparent.bermuda;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import org.transparent.bermuda.transform.Transformer;
import org.transparent.bermuda.util.SourceProcessor;
import org.transparent.bermuda.util.Stage;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public abstract class Plugin implements com.sun.source.util.Plugin {
	private final String name;
	private final Set<SourceProcessor> sourceProcessors;
	private final EnumMap<Stage, List<Transformer<?>>> transformers;

	public Plugin(String name) {
		this.name = name;
		this.sourceProcessors = new HashSet<>();
		this.transformers = new EnumMap<>(Stage.class);
		applySourceProcessors();
		apply();
	}

	public void apply() {}

	public void start(JavacTask task, String... args) {}

	public void end() {}

	@Override
	public final void init(JavacTask task, String... args) {
		start(task, args);
		task.addTaskListener(new TransformListener(task, transformers));
		end();
	}

	private void applySourceProcessors() {

	}

	protected final void register(Transformer<?> transformer) {
		transformers
				.computeIfAbsent(
						transformer.stage(),
						s -> new ArrayList<>())
				.add(transformer);
	}

	@Override
	public final String getName() {
		return name;
	}

	public final EnumMap<Stage, List<Transformer<?>>> getTransformers() {
		return transformers;
	}

	private static class TransformListener implements TaskListener {
		private final JavacTask task;
		private final EnumMap<Stage, List<Transformer<?>>> transformers;

		private TransformListener(JavacTask task,
		                          EnumMap<Stage, List<Transformer<?>>> transformers) {
			this.task = task;
			this.transformers = transformers;
		}

		@Override
		public void started(TaskEvent e) {
			transform(Stage::before, e);
		}

		@Override
		public void finished(TaskEvent e) {
			transform(Stage::after, e);
		}

		private void transform(Function<TaskEvent.Kind, Stage> function, TaskEvent event) {
			final Stage stage = function.apply(event.getKind());
			final List<Transformer<?>> transformers = this.transformers
					.computeIfAbsent(stage, s -> new ArrayList<>());
			transformers.sort(null);
			for (Transformer<?> value : transformers) {
				try (Transformer<?> transformer = value) {
					transformer.transform(task, event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}