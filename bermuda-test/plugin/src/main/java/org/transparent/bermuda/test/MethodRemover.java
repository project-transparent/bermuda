package org.transparent.bermuda.test;

import org.objectweb.asm.*;
import org.transparent.bermuda.transform.ByteTransformer;

public final class MethodRemover extends ByteTransformer {
	@Override
	public byte[] transform(byte[] bytes) {
		final ClassReader cr = new ClassReader(bytes);
		final ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
		cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
				return null;
			}
		}, ClassReader.EXPAND_FRAMES);
		return cw.toByteArray();
	}
}