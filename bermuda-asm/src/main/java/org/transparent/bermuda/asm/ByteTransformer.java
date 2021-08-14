package org.transparent.bermuda.asm;

import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.Context;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.transparent.bermuda.transform.BaseTransformer;
import org.transparent.bermuda.asm.util.AsmFactory;
import org.transparent.bermuda.util.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class ByteTransformer implements BaseTransformer<byte[]> {
	@Override
	public final Stage stage() {
		return Stage.AFTER_GENERATE;
	}

	@Override
	public void transform(JavacTask task, TaskEvent e) {
		final Context ctx = ((BasicJavacTask) task).getContext();
		final ClassWriter cw = ClassWriter.instance(ctx);
		final byte[] bytes = ByteUtils.getClassBytes(cw);
		ByteUtils.getClassFile(ctx, e.getTypeElement())
				.ifPresent(os0 -> {
					try (OutputStream os = os0) {
						os.write(transform(bytes));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
	}

	protected int asmVersion() {
		return Opcodes.ASM9;
	}

	protected final byte[] factory(byte[] bytes, BiFunction<Integer, ClassVisitor, ClassVisitor> biFunction) {
		final ClassReader cr = new ClassReader(bytes);
		final org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(cr, 0);
		final ClassVisitor cv = biFunction.apply(asmVersion(), cw);
		cr.accept(cv, 0);
		return cw.toByteArray();
	}

	protected final AsmFactory factory(byte[] bytes) {
		return new AsmFactory(bytes, asmVersion());
	}

	private byte[] fix(byte[] bytes, String name, String descriptor, InsnList list, Consumer<InsnList> consumer) {
		final ClassReader cr = new ClassReader(bytes);
		final ClassNode node = new ClassNode();
		cr.accept(node, 0);

		node.methods.forEach(method -> {
			if (name.equals(method.name)
					&& (descriptor == null || descriptor.equals(method.desc)))
				consumer.accept(list);
		});

		final org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(cr, 0);
		node.accept(cw);
		return cw.toByteArray();
	}

	protected final byte[] prefix(byte[] bytes, String name, String descriptor, InsnList list) {
		return fix(bytes, name, descriptor, list, list::insert);
	}

	protected final byte[] prefix(byte[] bytes, String name, InsnList list) {
		return prefix(bytes, name, null, list);
	}

	protected final byte[] postfix(byte[] bytes, String name, String descriptor, InsnList list) {
		return fix(bytes, name, descriptor, list, list::add);
	}

	protected final byte[] postfix(byte[] bytes, String name, InsnList list) {
		return postfix(bytes, name, null, list);
	}

	protected final byte[] modify(byte[] bytes, Consumer<ClassNode> consumer) {
		final ClassReader cr = new ClassReader(bytes);
		final ClassNode node = new ClassNode();
		cr.accept(node, 0);

		consumer.accept(node);

		final org.objectweb.asm.ClassWriter cw = new org.objectweb.asm.ClassWriter(cr, 0);
		node.accept(cw);
		return cw.toByteArray();
	}
}