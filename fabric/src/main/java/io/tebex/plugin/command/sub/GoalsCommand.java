package io.tebex.plugin.command.sub;

import com.mojang.brigadier.context.CommandContext;
import io.tebex.plugin.TebexPlugin;
import io.tebex.plugin.command.SubCommand;
import io.tebex.sdk.obj.CommunityGoal;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoalsCommand extends SubCommand {
    public GoalsCommand(TebexPlugin platform) {
        super(platform, "goals", "tebex.goals");
    }

    @Override
    public void execute(CommandContext<ServerCommandSource> sender) {
        TebexPlugin platform = getPlatform();

        try {
            List<CommunityGoal> goals = platform.getSDK().getCommunityGoals().get();
            for (CommunityGoal goal: goals) {
                if (goal.getStatus() != CommunityGoal.Status.DISABLED) {
                    sender.getSource().sendFeedback(() -> Text.of("§b[Tebex] §7Community Goals: "), false);
                    sender.getSource().sendFeedback(() -> Text.of(String.format("§b[Tebex] §7- %s (%.2f/%.2f) [%s]", goal.getName(), goal.getCurrent(), goal.getTarget(), goal.getStatus())), false);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            sender.getSource().sendFeedback(() -> Text.of("§b[Tebex] §7Unexpected response: " + e.getMessage()), false);
        }
    }

    @Override
    public String getDescription() {
        return "Shows active and completed community goals.";
    }
}
