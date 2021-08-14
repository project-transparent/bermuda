package org.transparent.bermuda.asm;

public abstract class ByteVisitor extends ByteTransformer {
	@Override
	public final byte[] transform(byte[] bytes) {
		visit(bytes);
		return bytes;
	}

	public abstract void visit(byte[] bytes);
}