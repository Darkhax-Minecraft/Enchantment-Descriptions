package net.darkhax.enchdesc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TempRenderUtils {

    public static void renderLinesWrapped (PoseStack matrix, int x, int y, Component text, int textWidth) {

        final Font font = Minecraft.getInstance().font;
        renderLinesWrapped(matrix, font, x, y, font.lineHeight, 0, text, textWidth);
    }

    public static void renderLinesWrapped (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, Component text, int textWidth) {

        // trimStringToWidth is actually wrapToWidth
        renderLinesWrapped(matrix, fontRenderer, x, y, spacing, defaultColor, fontRenderer.split(text, textWidth));
    }

    public static void renderLinesWrapped (PoseStack matrix, Font fontRenderer, int x, int y, int spacing, int defaultColor, List<FormattedCharSequence> lines) {

        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {

            final FormattedCharSequence lineFragment = lines.get(lineNum);
            fontRenderer.draw(matrix, lineFragment, x, y + lineNum * spacing, defaultColor);
        }
    }
}
