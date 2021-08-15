[![Bermuda](https://raw.githubusercontent.com/project-transparent/bermuda/master/bermuda.png?token=AGX76VQSKJ45FPKIPV2EZX3BDB6BK)](https://github.com/project-transparent/bermuda)

## **Bermuda** is a modular framework for making powerful Javac compiler plugins.

***put shields.io badges here***

One of the primary goals of *Project Transparent* is providing Java developers with less boilerplate and more free-roam tooling.<br>
We have provided many different tools working with Javac, but this is the first to truly break new ground.

An issue with Javac is how unextensible it is, *until Java 1.6.*<br>
That is when Javac plugins were introduced, which allowed users to plug directly into Javac's stages for analysis.<br>
...But you don't have just the ability to analyze.

**Bermuda** offers the ability to change the output of a compile cycle with either AST manipulation or bytecode injection.

### Simple

**Bermuda** is quite simple to setup, as the `Plugin` class it provides comes with a few helpers for transforming/visiting Javac's output.

```java
import org.transparent.bermuda.Plugin;

public class MyPlugin extends Plugin {
    public MyPlugin() {
        // Javac uses a plugin name to identify names from console arguments.
        super("myplugin");
    }
    
    @Override
    public void apply() {
        // Register a transformer.
        // Transformers take in a value and return a value.
        // Modules come with their own transformers, such as 'TreeTransformer' from 'ast'.
        register(new MyTransformer());
    }
}
```

Note: You will need to create a services file for `com.sun.source.util.Plugin` so that Javac can locate your plugin class, you will also need to specify your plugin in the command-line arguments when compiling.

### Modular

However you want to use it, there's a method available:
- `core` — The primary module, contains all the base classes required for **Bermuda** to function. This is *required* for any plugin.
- `ast` &nbsp;&nbsp;— The AST module contains an extensive, clean wrapper for Javac's AST that allows easier manipulation and creation of types.
- `asm` &nbsp;&nbsp;— The ASM module contains small tools that allow you to directly modify Javac's *bytecode output.*

### Powerful

To best illustrate the flexibility of certain modules, we've provided a few code snippets.

#### AST

```java
// todo
```

#### ASM

```java
public class DeprecationAlerter extends ByteTransformer {
    @Override
    public byte[] transform(byte[] bytes) {
        // Utilize ASM's Tree API for easy, programmatic extensions.
        return modify(bytes, clazz -> {
            if (Annotations.isAnnotated(clazz, Deprecated.class)) {
                clazz.methods.forEach(method -> {
                    final InsnList list = method.instructions;
                    // Injects the following statement into every method in this class:
                    // System.err.println("This class is deprecated!");
                    list.insert(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;"));
                    list.insert(new LdcInsnNode("This class is deprecated!"));
                    list.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
                });
            }
        });
    }
}
```
