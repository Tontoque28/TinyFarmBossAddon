package com.tontoque.sg2hotfix.mixin;

import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = StringDecomposer.class, priority = 2000)
public class TooltipCrashFixMixin {

    /**
     * @author Tontoque28 & JARVIS
     * @reason Reemplaza el método para añadir un null-check y prevenir el crasheo.
     */
    @Overwrite
    public static boolean iterateFormatted(String text, Style style, FormattedCharSink sink) {
        if (text == null) {
            return true;
        }
        
        int i = text.length();
        for(int j = 0; j < i; ++j) {
            char c0 = text.charAt(j);
            if (Character.isHighSurrogate(c0)) {
                if (j + 1 >= i) {
                    if (!sink.accept(j, style, 65533)) return false;
                    break;
                }
                char c1 = text.charAt(j + 1);
                if (Character.isLowSurrogate(c1)) {
                    if (!sink.accept(j, style, Character.toCodePoint(c0, c1))) return false;
                    ++j;
                } else if (!sink.accept(j, style, 65533)) {
                    return false;
                }
            } else if (!sink.accept(j, style, c0)) {
                return false;
            }
        }
        return true;
    }
}
