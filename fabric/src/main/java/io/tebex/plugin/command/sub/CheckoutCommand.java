package io.tebex.plugin.command.sub;

import com.mojang.brigadier.context.CommandContext;
import io.tebex.plugin.TebexPlugin;
import io.tebex.plugin.command.SubCommand;
import io.tebex.sdk.obj.CheckoutUrl;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.concurrent.ExecutionException;

public class CheckoutCommand extends SubCommand {
    public CheckoutCommand(TebexPlugin platform) {
        super(platform, "checkout", "tebex.checkout");
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> context) {
        TebexPlugin platform = getPlatform();

        if (!platform.isSetup()) {
            context.getSource().sendFeedback(() -> Text.of("§b[Tebex] §7This server is not connected to a webstore. Use /tebex secret to set your store key."), false);
            return;
        }

        Integer packageId = context.getArgument("packageId", Integer.class);
        try {
            CheckoutUrl checkoutUrl = platform.getSDK().createCheckoutUrl(packageId, context.getSource().getName()).get();
            context.getSource().sendFeedback(() -> Text.of("§b[Tebex] §7Checkout started! Click here to complete payment: " + checkoutUrl.getUrl()), false);
        } catch (InterruptedException|ExecutionException e) {
            context.getSource().sendError(Text.of("§b[Tebex] §7Failed to get checkout link for package: " + e.getMessage()));
        }
    }

    @Override
    public String getDescription() {
        return "Creates payment link for a package";
    }

    @Override
    public String getUsage() {
        return "<packageId>";
    }
}
