package com.gg.optimize.visitor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @description:
 * @author: GG
 * @createDate: 2023 2.16 0016 17:16
 * @updateUser:
 * @updateDate: 2023 2.16 0016 17:16
 */
public class SampleMethodVisitor extends AdviceAdapter {
    private String mMethodName;

    /**
     * Constructs a new {@link AdviceAdapter}.
     *
     * @param api           the ASM API version implemented by this visitor. Must be one of {@link
     *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
     * @param methodVisitor the method visitor to which this adapter delegates calls.
     * @param access        the method's access flags (see {@link Opcodes}).
     * @param name          the method's name.
     * @param descriptor    the method's descriptor (see {@link Type Type}).
     */
    protected SampleMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(Opcodes.ASM5, methodVisitor, access, name, descriptor);
        this.mMethodName = name;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        if (mMethodName.equals("onCreate")) {
            System.out.println("owner -> " + owner + ", name -> " + name + ", descriptor -> " + descriptor);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (mMethodName.equals("onCreate")) {
            System.out.println("visitEnd methodName -> " + mMethodName);
            // 参数怎么写
            mv.visitLdcInsn("TAG");
            mv.visitLdcInsn("enterMethod");
            // 这里一定是要字节码的方法
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e",
                    "(Ljava/lang/String;Ljava/lang/String;)I", false);
        }
    }
}
