package org.transparent.bermuda.transform;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.Context;
import org.transparent.bermuda.util.Stage;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ByteTransformer implements BaseTransformer<byte[]> {
	@Override
	public final Stage stage() {
		return Stage.AFTER_GENERATE;
	}

	@Override
	public void transform(JavacTask task, TaskEvent e) {
		final Context ctx = ((BasicJavacTask) task).getContext();
		final ClassWriter cw = ClassWriter.instance(ctx);
		final byte[] bytes = Utils.getClassBytes(cw);
		Utils.getClassFile(ctx, e.getTypeElement())
				.ifPresent(os0 -> {
					try (OutputStream os = os0) {
						os.write(transform(bytes));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
	}
}