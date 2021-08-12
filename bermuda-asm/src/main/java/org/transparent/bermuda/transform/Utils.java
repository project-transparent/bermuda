package org.transparent.bermuda.transform;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.ByteBuffer;
import com.sun.tools.javac.util.Context;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

final class Utils {
	private static final Field poolbufField;

	static byte[] getClassBytes(ClassWriter cw) {
		try {
			final ByteBuffer poolbuf = (ByteBuffer) poolbufField.get(cw);
			final int len = poolbuf.length;
			final byte[] b = new byte[len];
			System.arraycopy(poolbuf.elems, 0, b, 0, len);
			return b;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	static Optional<OutputStream> getClassFile(Context ctx, TypeElement element) {
		final JavaFileManager fm = ctx.get(JavaFileManager.class);
		final ClassSymbol symbol = (ClassSymbol) element;
		try {
			final JavaFileObject file = fm
					.getJavaFileForOutput(
							StandardLocation.CLASS_OUTPUT,
							String.valueOf(symbol.flatname),
							JavaFileObject.Kind.CLASS,
							symbol.sourcefile);
			return Optional.of(file.openOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	static {
		Field poolbufField0 = null;
		try {
			poolbufField0 = ClassWriter.class.getDeclaredField("poolbuf");
			poolbufField0.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		Objects.requireNonNull(poolbufField0);
		poolbufField = poolbufField0;
	}
}