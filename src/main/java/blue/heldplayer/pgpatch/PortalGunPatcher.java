package blue.heldplayer.pgpatch;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class PortalGunPatcher implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] original) {
        if (original == null) {
            return null;
        }
        if (name.equals("portalgun.client.thread.ThreadDownloadResources")) {
            ClassReader reader = new ClassReader(original);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor parentVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                    if (name.equals("run") && desc.equals(Type.getMethodDescriptor(Type.VOID_TYPE))) {
                        return new MethodVisitor(Opcodes.ASM4, parentVisitor) {
                            @Override
                            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                                if (opcode == Opcodes.INVOKEVIRTUAL) {
                                    if ("java/net/URLConnection".equals(owner) && "getInputStream".equals(name)
                                            && "()Ljava/io/InputStream;".equals(desc)) {
                                        super.visitInsn(Opcodes.POP);
                                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "blue/heldplayer/pgpatch/SslUtils",
                                                "openAssetsXmlStream", "()Ljava/io/InputStream;");
                                        return;
                                    }
                                }
                                super.visitMethodInsn(opcode, owner, name, desc);
                            }
                        };
                    }
                    return parentVisitor;
                }
            };

            reader.accept(visitor, ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }

        return original;
    }
}
