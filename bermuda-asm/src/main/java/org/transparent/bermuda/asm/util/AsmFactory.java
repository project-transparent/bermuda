package org.transparent.bermuda.asm.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.function.BiFunction;

public final class AsmFactory {
	private final byte[] bytes;
	private int asmVersion;
	private int crFlags;
	private int cwFlags;

	public AsmFactory(byte[] bytes, int asmVersion) {
		this.bytes = bytes;
		this.asmVersion = asmVersion;
	}

	public AsmFactory asmVersion(int version) {
		this.asmVersion = version;
		return this;
	}

	public AsmFactory readerFlags(int flags) {
		this.crFlags = flags;
		return this;
	}

	public AsmFactory writerFlags(int flags) {
		this.cwFlags = flags;
		return this;
	}

	public byte[] accept(BiFunction<Integer, ClassVisitor, ClassVisitor> biFunction) {
		final ClassReader cr = new ClassReader(bytes);
		final ClassWriter cw = new ClassWriter(cr, cwFlags);
		final ClassVisitor cv = biFunction.apply(asmVersion, cw);
		cr.accept(cv, crFlags);
		return cw.toByteArray();
	}
}