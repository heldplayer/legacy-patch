package blue.heldplayer.legacypatch;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class PortalGunPatcher implements IClassTransformer {
    private static final String RUN_DESC = "()V";
    private static final String DOWNLOAD_RESOURCE_DESC = "(Ljava/net/URL;Ljava/io/File;J)V";

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
                    if (name.equals("run") && desc.equals(RUN_DESC)) {
                        return new MethodVisitor(Opcodes.ASM4, parentVisitor) {
                            @Override
                            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                                if (opcode == Opcodes.INVOKEVIRTUAL) {
                                    if ("java/net/URLConnection".equals(owner) && "getInputStream".equals(name)
                                            && "()Ljava/io/InputStream;".equals(desc)) {
                                        // Don't use the URLConnection, build one ourselves instead
                                        super.visitInsn(Opcodes.POP);
                                        super.visitMethodInsn(Opcodes.INVOKESTATIC, PatchTargets.CLASS_IN,
                                                "openAssetsXmlStream", PatchTargets.OPEN_ASSETS_XML_STREAM_DESC);
                                        return;
                                    }
                                }
                                super.visitMethodInsn(opcode, owner, name, desc);
                            }
                        };
                    }
                    if (name.equals("downloadResource") && desc.equals(DOWNLOAD_RESOURCE_DESC)) {
                        return new MethodVisitor(Opcodes.ASM4, parentVisitor) {
                            @Override
                            public void visitCode() {
                                // Ensure the URL parameter is updated right away
                                Label label = new Label();
                                super.visitLabel(label);
                                super.visitLineNumber(-1, label);
                                super.visitVarInsn(Opcodes.ALOAD, 1);
                                super.visitMethodInsn(Opcodes.INVOKESTATIC, PatchTargets.CLASS_IN,
                                        "upgradeUrl", PatchTargets.UPGRADE_URL_DESC);
                                super.visitVarInsn(Opcodes.ASTORE, 1);
                                super.visitCode();
                            }

                            @Override
                            public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                                if (opcode == Opcodes.INVOKEVIRTUAL) {
                                    if ("java/net/URLConnection".equals(owner) && "getInputStream".equals(name)
                                            && "()Ljava/io/InputStream;".equals(desc)) {
                                        // Modify the SSL Socket Factory first to load system certificates instead of
                                        // the outdated Java 7/8 cacerts store
                                        super.visitInsn(Opcodes.DUP);
                                        super.visitMethodInsn(Opcodes.INVOKESTATIC, PatchTargets.CLASS_IN,
                                                "patchSecurityCertificates", PatchTargets.PATCH_SECURITY_CERTIFICATES_DESC);
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
