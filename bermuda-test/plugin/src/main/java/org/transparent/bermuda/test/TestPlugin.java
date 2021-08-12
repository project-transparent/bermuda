package org.transparent.bermuda.test;

import org.transparent.bermuda.Plugin;

public final class TestPlugin extends Plugin {
	public TestPlugin() {
		super("test");
	}

	@Override
	public void apply() {
		System.out.println("Oops, there goes your methods!\nI'll send this field your way to compensate.");
		register(new MethodRemover());
		register(new FieldInjector());
	}
}