package com.tontoque28.tinyfarmboss.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TfbTransformer implements IClassTransformer {
    private static final String FARM = "cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm";
    private static final String HELPER = "cn.davidma.tinymobfarm.core.util.EntityHelper";
    private static final String LIVING_BASE = "net.minecraft.entity.EntityLiving";
    private static final String HOOKS = "com/tontoque28/tinyfarmboss/TfbHooks";
    private static final String RENAMED_SUFFIX = "$tfb";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || transformedName == null) {
            return basicClass;
        }
        ClassNode node;
        boolean changed;
        if (transformedName.equals(FARM)) {
            node = read(basicClass);
            changed = wrapIsWorking(node);
        } else if (transformedName.equals(HELPER)) {
            node = read(basicClass);
            changed = wrapLootTableLocation(node);
        } else if (transformedName.equals(LIVING_BASE)) {
            node = read(basicClass);
            changed = hookDropLoot(node);
        } else {
            return basicClass;
        }
        if (!changed) {
            return basicClass;
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static ClassNode read(byte[] bytes) {
        ClassNode node = new ClassNode();
        new ClassReader(bytes).accept(node, 0);
        return node;
    }

    private static boolean wrapIsWorking(ClassNode node) {
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("isWorking".equals(method.name) && "()Z".equals(method.desc)) {
                original = method;
            }
        }
        if (original == null) {
            return false;
        }
        original.name = "isWorking" + RENAMED_SUFFIX;
        MethodNode wrapper = new MethodNode(Opcodes.ACC_PUBLIC, "isWorking", "()Z", null, null);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, "forceWorking", "(Ljava/lang/Object;)Z", false));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, node.name, original.name, "()Z", false));
        wrapper.instructions.add(new InsnNode(Opcodes.IOR));
        wrapper.instructions.add(new InsnNode(Opcodes.IRETURN));
        wrapper.maxStack = 2;
        wrapper.maxLocals = 1;
        node.methods.add(wrapper);
        return true;
    }

    private static boolean wrapLootTableLocation(ClassNode node) {
        String desc = "(Lnet/minecraft/entity/EntityLiving;)Ljava/lang/String;";
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("getLootTableLocation".equals(method.name) && desc.equals(method.desc) && (method.access & Opcodes.ACC_STATIC) != 0) {
                original = method;
            }
        }
        if (original == null) {
            return false;
        }
        original.name = "getLootTableLocation" + RENAMED_SUFFIX;
        MethodNode wrapper = new MethodNode(original.access, "getLootTableLocation", desc, null, null);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, node.name, original.name, desc, false));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, "lootTableLocation", "(Lnet/minecraft/entity/EntityLiving;Ljava/lang/String;)Ljava/lang/String;", false));
        wrapper.instructions.add(new InsnNode(Opcodes.ARETURN));
        wrapper.maxStack = 2;
        wrapper.maxLocals = 1;
        node.methods.add(wrapper);
        return true;
    }

    private static boolean hookDropLoot(ClassNode node) {
        boolean changed = false;
        for (MethodNode method : node.methods) {
            if ((method.access & Opcodes.ACC_STATIC) != 0 || method.instructions == null) continue;
            AbstractInsnNode insn = method.instructions.getFirst();
            while (insn != null) {
                AbstractInsnNode next = insn.getNext();
                if (insn instanceof MethodInsnNode && insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                    MethodInsnNode call = (MethodInsnNode) insn;
                    if (("getLootTable".equals(call.name) || "func_184647_J".equals(call.name))
                            && "()Lnet/minecraft/util/ResourceLocation;".equals(call.desc)) {
                        method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, "lootTable", "(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/util/ResourceLocation;", false));
                        method.instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 0));
                        changed = true;
                    }
                }
                insn = next;
            }
        }
        return changed;
    }
}
