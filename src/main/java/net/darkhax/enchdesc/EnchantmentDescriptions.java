package net.darkhax.enchdesc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class EnchantmentDescriptions implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ItemTooltipCallback.EVENT.register(EnchantmentDescriptions::onTooltipDisplayed);
  }

  private static void onTooltipDisplayed(ItemStack stack, TooltipContext context, List<Text> lines) {
    Set<Enchantment> enchants = EnchantmentHelper.get(stack).keySet();
    if (
            stack.getItem() instanceof EnchantedBookItem &&
                    !enchants.isEmpty()
    ) {
      if (Screen.hasShiftDown()) {
        insertDescriptionTooltips(lines, enchants);
      } else {
        lines.add(
                new TranslatableText(
                        "enchdesc.tooltip.activate",
                        "shift"
                ).formatted(Formatting.DARK_GRAY)
        );
      }
    }
  }

  private static void insertDescriptionTooltips(List<Text> lines, Set<Enchantment> enchants) {
    for (Enchantment enchant : enchants) {
      for (Text tooltipLine : lines) {
        if (
                tooltipLine instanceof TranslatableText &&
                        ((TranslatableText) tooltipLine).getKey().equals(enchant.getTranslationKey())
        ) {
          lines.add(
                  lines.indexOf(tooltipLine) + 1,
                  new TranslatableText(enchant.getTranslationKey() + ".desc")
                          .formatted(Formatting.DARK_GRAY)
          );
          break;
        }
      }
    }
  }
}
