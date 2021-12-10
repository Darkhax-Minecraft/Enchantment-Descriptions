# [Enchantment-Descriptions](https://minecraft.curseforge.com/projects/enchantment-descriptions)
When this mod is installed enchanted items will display a brief description of their enchantment effects in their tooltip. 

## FaQ

### Does this support modded enchantments?
Yes, it is possible for modded enchantments to work with this mod, and many mods already include support for this mod! If an enchantment description is not being displayed feel free to request support from me using the [issue tracker](https://github.com/Darkhax-Minecraft/Enchantment-Descriptions/issues).

### The tooltip is going off the side of my screen?
This is a vanilla bug that only affects Fabric users. Affected players will need to download a mod like [ToolTipFix](https://www.curseforge.com/minecraft/mc-mods/tooltipfix) which patches this bug for Fabric. Forge users will not experience this issue because Forge includes a built in fix for this bug.

### How do I add new descriptions?
The descriptions shown by this mod are taken from the localization map. Adding support for a new enchantment is as easy as adding the expected entry to your localization file. The expected localization key format is `enchantment.%MOD_ID%.%ENCH_ID%.desc`.