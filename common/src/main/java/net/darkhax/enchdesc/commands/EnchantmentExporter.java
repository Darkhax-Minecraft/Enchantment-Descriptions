package net.darkhax.enchdesc.commands;

import net.darkhax.enchdesc.Constants;
import net.darkhax.enchdesc.DescriptionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EnchantmentExporter {

    public static int execute() {

        final Minecraft mc = Minecraft.getInstance();
        final String lang = mc.options.languageCode;

        final File outputFile = new File(
                mc.gameDirectory,
                "kubejs/assets/enchanted-descriptions/lang/" + lang + "_generated.json"
        );
        outputFile.getParentFile().mkdirs();

        final List<ResourceLocation> sortedKeys = new ArrayList<>(BuiltInRegistries.ENCHANTMENT.keySet());
        sortedKeys.sort(Comparator.comparing(ResourceLocation::getNamespace)
                                  .thenComparing(ResourceLocation::getPath));

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(outputFile, false), StandardCharsets.UTF_8)) {

            final List<String> entryLines = new ArrayList<>();
            final List<String> entryNamespaces = new ArrayList<>();

            for (ResourceLocation key : sortedKeys) {

                final Enchantment enchantment = BuiltInRegistries.ENCHANTMENT.get(key);

                if (enchantment == null) {
                    continue;
                }

                final String descKey = DescriptionManager.getKey(enchantment);
                final String value = I18n.exists(descKey) ? escapeJson(I18n.get(descKey)) : "";
                entryLines.add("    \"" + escapeJson(descKey) + "\": \"" + value + "\"");
                entryNamespaces.add(key.getNamespace());
            }

            writer.write("{\n");

            for (int i = 0; i < entryLines.size(); i++) {

                if (i > 0 && !entryNamespaces.get(i).equals(entryNamespaces.get(i - 1))) {
                    writer.write("\n");
                }

                writer.write(entryLines.get(i));
                writer.write(i < entryLines.size() - 1 ? ",\n" : "\n");
            }

            writer.write("}\n");

            final int count = entryLines.size();

            mc.player.sendSystemMessage(Component.literal(
                    "[" + Constants.MOD_NAME + "] Exported " + count +
                    " enchantment descriptions in " + lang + " to: " + outputFile.getAbsolutePath()));
            return count;

        } catch (IOException e) {

            Constants.LOG.error("Failed to export enchantment descriptions", e);
            mc.player.sendSystemMessage(Component.literal(
                    "[" + Constants.MOD_NAME + "] Export failed: " + e.getMessage()));
            return 0;
        }
    }

    private static String escapeJson(String input) {

        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
