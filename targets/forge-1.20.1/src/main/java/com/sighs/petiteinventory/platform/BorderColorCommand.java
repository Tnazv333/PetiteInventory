package com.sighs.petiteinventory.platform;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sighs.petiteinventory.inventory.BorderTheme;
import com.sighs.petiteinventory.config.BorderThemeCache;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class BorderColorCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("petiteborder")
                        .requires(src -> src.hasPermission(2))
                        /* ========== set ========== */
                        .then(
                                Commands.literal("set")
                                        .then(
                                                Commands.argument("theme", StringArgumentType.string())
                                                        .suggests((ctx, builder) -> {
                                                            for (BorderTheme theme : BorderTheme.values()) {
                                                                if (theme != BorderTheme.DEFAULT) {
                                                                    builder.suggest(theme.getId(),
                                                                            Component.literal(theme.getDisplayName()));
                                                                }
                                                            }
                                                            return builder.buildFuture();
                                                        })
                                                        .executes(ctx -> {
                                                            ServerPlayer player = ctx.getSource().getPlayer();
                                                            if (player == null) {
                                                                ctx.getSource().sendFailure(
                                                                        Component.literal("❌ 必须是玩家才能使用手持物品模式！"));
                                                                return 0;
                                                            }
                                                            ItemStack held = player.getMainHandItem();
                                                            if (held.isEmpty()) {
                                                                ctx.getSource().sendFailure(
                                                                        Component.literal("❌ 你必须手持一个物品！"));
                                                                return 0;
                                                            }

                                                            String itemId = ForgeRegistries.ITEMS
                                                                    .getKey(held.getItem()).toString();
                                                            String themeId = StringArgumentType.getString(ctx, "theme");
                                                            BorderTheme theme = BorderTheme.fromId(themeId);

                                                            /* ===== 精确到 GunId ===== */
                                                            String configKey = itemId;
                                                            if (itemId.equals("tacz:modern_kinetic_gun") && held.hasTag()) {
                                                                String gunId = held.getTag().getString("GunId");
                                                                if (gunId != null && !gunId.isEmpty()) {
                                                                    // ✅ 修正：NBT直接在根层级，不是s子标签
                                                                    configKey = itemId + "{GunId:\"" + gunId + "\"}";
                                                                }
                                                            }

                                                            BorderThemeCache.setTheme(configKey, theme);
                                                            String finalConfigKey = configKey;
                                                            ctx.getSource().sendSuccess(() ->
                                                                            Component.literal("✅ 已设置 ")
                                                                                    .append(Component.literal(finalConfigKey)
                                                                                            .withStyle(ChatFormatting.YELLOW))
                                                                                    .append(Component.literal(" 的边框颜色为 "))
                                                                                    .append(Component.literal(theme.getDisplayName())
                                                                                            .withStyle(ChatFormatting.AQUA)),
                                                                    true);
                                                            return 1;
                                                        })
                                        )
                        )
                        /* ========== get ========== */
                        .then(
                                Commands.literal("get")
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayer();
                                            if (player == null) {
                                                ctx.getSource().sendFailure(
                                                        Component.literal("❌ 必须是玩家才能使用手持物品模式！"));
                                                return 0;
                                            }
                                            ItemStack held = player.getMainHandItem();
                                            if (held.isEmpty()) {
                                                ctx.getSource().sendFailure(
                                                        Component.literal("❌ 你必须手持一个物品！"));
                                                return 0;
                                            }
                                            String itemId = ForgeRegistries.ITEMS
                                                    .getKey(held.getItem()).toString();
                                            BorderTheme theme = BorderThemeCache.getTheme(held.getItem(), held);
                                            ctx.getSource().sendSuccess(() ->
                                                            Component.literal("📋 物品 ")
                                                                    .append(Component.literal(itemId)
                                                                            .withStyle(ChatFormatting.YELLOW))
                                                                    .append(Component.literal(" 的边框颜色是: "))
                                                                    .append(Component.literal(theme.getDisplayName())
                                                                            .withStyle(ChatFormatting.AQUA)),
                                                    false);
                                            return 1;
                                        })
                                        .then(
                                                Commands.argument("item", StringArgumentType.string())
                                                        .suggests((ctx, builder) -> {
                                                            ForgeRegistries.ITEMS.getKeys()
                                                                    .forEach(key -> builder.suggest(key.toString()));
                                                            return builder.buildFuture();
                                                        })
                                                        .executes(ctx -> {
                                                            String itemId = StringArgumentType.getString(ctx, "item");
                                                            Item item = ForgeRegistries.ITEMS
                                                                    .getValue(new net.minecraft.resources.ResourceLocation(itemId));
                                                            if (item == null) {
                                                                ctx.getSource().sendFailure(
                                                                        Component.literal("❌ 无效的物品ID: " + itemId));
                                                                return 0;
                                                            }
                                                            BorderTheme theme = BorderThemeCache.getTheme(item);
                                                            ctx.getSource().sendSuccess(() ->
                                                                            Component.literal("📋 物品 ")
                                                                                    .append(Component.literal(itemId)
                                                                                            .withStyle(ChatFormatting.YELLOW))
                                                                                    .append(Component.literal(" 的边框颜色是: "))
                                                                                    .append(Component.literal(theme.getDisplayName())
                                                                                            .withStyle(ChatFormatting.AQUA)),
                                                                    false);
                                                            return 1;
                                                        })
                                        )
                        )
                        /* ========== list ========== */
                        .then(
                                Commands.literal("list")
                                        .executes(ctx -> {
                                            Map<String, BorderTheme> themes = BorderThemeCache.getAllThemes();
                                            if (themes.isEmpty()) {
                                                ctx.getSource().sendSuccess(
                                                        () -> Component.literal("📋 当前没有自定义颜色配置")
                                                                .withStyle(ChatFormatting.GRAY),
                                                        false);
                                                return 1;
                                            }
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("=== 自定义边框颜色配置 ===")
                                                            .withStyle(ChatFormatting.GOLD),
                                                    false);
                                            themes.forEach((id, t) -> {
                                                Component line = Component.literal("  • ")
                                                        .append(Component.literal(id)
                                                                .withStyle(ChatFormatting.YELLOW))
                                                        .append(Component.literal(" → "))
                                                        .append(Component.literal(t.getDisplayName())
                                                                .withStyle(ChatFormatting.AQUA));
                                                ctx.getSource().sendSuccess(() -> line, false);
                                            });
                                            return 1;
                                        })
                        )
/* ========== reset ========== */
                        .then(
                                Commands.literal("reset")
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayer();
                                            if (player == null) {
                                                ctx.getSource().sendFailure(
                                                        Component.literal("❌ 必须是玩家才能使用手持物品模式！"));
                                                return 0;
                                            }
                                            ItemStack held = player.getMainHandItem();
                                            if (held.isEmpty()) {
                                                ctx.getSource().sendFailure(
                                                        Component.literal("❌ 你必须手持一个物品！"));
                                                return 0;
                                            }

                                            String itemId = ForgeRegistries.ITEMS
                                                    .getKey(held.getItem()).toString();

                                            // 生成NBT精确键（如果适用）
                                            String configKey = itemId;
                                            if (itemId.equals("tacz:modern_kinetic_gun") && held.hasTag()) {
                                                String gunId = held.getTag().getString("GunId");
                                                if (gunId != null && !gunId.isEmpty()) {
                                                    configKey = itemId + "{GunId:\"" + gunId + "\"}";
                                                }
                                            }

                                            // 直接删这一条
                                            BorderThemeCache.setTheme(configKey, BorderTheme.DEFAULT);

                                            String finalConfigKey = configKey;
                                            ctx.getSource().sendSuccess(() ->
                                                            Component.literal("✅ 已重置 ")
                                                                    .append(Component.literal(finalConfigKey)
                                                                            .withStyle(ChatFormatting.YELLOW))
                                                                    .append(Component.literal(" 的边框颜色")),
                                                    true);
                                            return 1;
                                        })
                        )
                        /* ========== reload ========== */
                        .then(
                                Commands.literal("reload")
                                        .executes(ctx -> {
                                            BorderThemeCache.load();
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("🔄 已重新加载边框颜色配置")
                                                            .withStyle(ChatFormatting.GREEN),
                                                    true);
                                            return 1;
                                        })
                        )
        );
    }
}
