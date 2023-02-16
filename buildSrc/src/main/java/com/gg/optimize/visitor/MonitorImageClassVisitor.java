package com.gg.optimize.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @description:
 * @author: GG
 * @createDate: 2023 2.16 0016 13:32
 * @updateUser:
 * @updateDate: 2023 2.16 0016 13:32
 */
public class MonitorImageClassVisitor extends ClassVisitor {

    public MonitorImageClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (superName.equals("android/widget/ImageView") && !name.equals("com/gg/optimize/plugin/MonitorImageView")) {
            superName = "com/gg/optimize/plugin/MonitorImageView";
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
