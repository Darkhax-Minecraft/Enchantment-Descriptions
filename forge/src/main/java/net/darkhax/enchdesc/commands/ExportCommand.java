package net.darkhax.enchdesc.commands;

import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;

public class ExportCommand {

    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {

        event.getDispatcher().register(
            Commands.literal("enchdesc")
                .then(Commands.literal("export")
                    .executes(ctx -> EnchantmentExporter.execute())
                )
        );
    }
}
