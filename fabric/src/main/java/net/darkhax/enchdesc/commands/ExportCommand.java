package net.darkhax.enchdesc.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ExportCommand {

    public static void register() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                ClientCommandManager.literal("enchdesc")
                    .then(ClientCommandManager.literal("export")
                        .executes(ctx -> EnchantmentExporter.execute())
                    )
            )
        );
    }
}
