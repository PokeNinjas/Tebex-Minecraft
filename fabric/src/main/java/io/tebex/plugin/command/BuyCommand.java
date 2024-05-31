package io.tebex.plugin.command;

import com.mojang.brigadier.context.CommandContext;
import io.tebex.plugin.TebexPlugin;
import io.tebex.plugin.gui.BuyGUI;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BuyCommand {
    private final TebexPlugin plugin;
    public BuyCommand(TebexPlugin plugin) {
        this.plugin = plugin;
    }

    public int execute(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();

        ServerPlayerEntity player = source.getPlayer();
        new BuyGUI(plugin).open(player);

        return 1;
    }
}
