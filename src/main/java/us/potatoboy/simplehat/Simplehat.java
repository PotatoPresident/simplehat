package us.potatoboy.simplehat;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import static net.minecraft.server.command.CommandManager.*;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class Simplehat implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            dispatcher.register(literal("hat").executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();

                ItemStack hatStack = player.inventory.getMainHandStack();
                if (hatStack.getItem() == Items.AIR) {
                    context.getSource().sendError(new TranslatableText("command.simplehat.not_item"));
                    return -1;
                }

                ItemStack currentHat = player.getEquippedStack(EquipmentSlot.HEAD).copy();

                if (currentHat.isEmpty()) {
                    player.equipStack(EquipmentSlot.HEAD, hatStack.copy());
                    hatStack.setCount(0);
                } else {
                    player.equipStack(EquipmentSlot.HEAD, hatStack);
                    player.setStackInHand(Hand.MAIN_HAND, currentHat);
                }

                return 1;
            }));
        }));
    }
}
