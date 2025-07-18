package com.example.itemrenamer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ItemRenamer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandManager.DISPATCHER.register(
                ClientCommandManager.literal("rename")
                        .then(ClientCommandManager.argument("name", net.minecraft.command.argument.StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String rawName = net.minecraft.command.argument.StringArgumentType.getString(ctx, "name");
                                    renameItem(rawName);
                                    return 1;
                                })
                        )
        );
    }

    private void renameItem(String rawName) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        ItemStack heldItem = client.player.getMainHandStack();
        if (heldItem.isEmpty()) {
            client.player.sendMessage(Text.of("§cYou must hold an item to rename it!"));
            return;
        }

        // Translate & color codes to § format
        String coloredName = rawName.replaceAll("&", "§");

        heldItem.setCustomName(Text.of(coloredName));
        client.player.sendMessage(Text.of("§aRenamed item to: " + coloredName));
    }
}
