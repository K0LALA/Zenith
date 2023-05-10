package safro.zenith.spawn.compat;

import mcp.mobius.waila.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import safro.zenith.Zenith;
import safro.zenith.spawn.modifiers.SpawnerStats;
import safro.zenith.util.IBaseSpawner;

public class SpawnerWTHITPlugin implements IWailaPlugin, IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    public static final String STATS = "spw_stats";

    @Override
    public void register(IRegistrar registrar) {
        if (!Zenith.enableSpawner) return;
        registrar.addBlockData(this, SpawnerBlockEntity.class);
        registrar.addComponent(this, TooltipPosition.BODY, SpawnerBlock.class);
    }

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (!Zenith.enableSpawner) return;
        if (Screen.hasControlDown()) {
            int[] stats = accessor.getServerData().getIntArray(STATS);
            if (stats.length != 12) return;
            tooltip.addLine(concat(SpawnerStats.MIN_DELAY.name(), stats[0]));
            tooltip.addLine(concat(SpawnerStats.MAX_DELAY.name(), stats[1]));
            tooltip.addLine(concat(SpawnerStats.SPAWN_COUNT.name(), stats[2]));
            tooltip.addLine(concat(SpawnerStats.MAX_NEARBY_ENTITIES.name(), stats[3]));
            tooltip.addLine(concat(SpawnerStats.REQ_PLAYER_RANGE.name(), stats[4]));
            tooltip.addLine(concat(SpawnerStats.SPAWN_RANGE.name(), stats[5]));
            if (stats[6] == 1) tooltip.addLine(SpawnerStats.IGNORE_PLAYERS.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[7] == 1) tooltip.addLine(SpawnerStats.IGNORE_CONDITIONS.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[8] == 1) tooltip.addLine(SpawnerStats.REDSTONE_CONTROL.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[9] == 1) tooltip.addLine(SpawnerStats.IGNORE_LIGHT.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[10] == 1) tooltip.addLine(SpawnerStats.NO_AI.name().withStyle(ChatFormatting.DARK_GREEN));
            if (stats[11] == 1) tooltip.addLine(SpawnerStats.SILENT.name().withStyle(ChatFormatting.DARK_GREEN));
        } else tooltip.addLine(Component.translatable("misc.zenith.ctrl_stats"));
    }

    @Override
    public void appendServerData(CompoundTag tag, IServerAccessor<BlockEntity> accessor, IPluginConfig config) {
        if (!Zenith.enableSpawner) return;
        if (accessor.getTarget() instanceof SpawnerBlockEntity spw) {
            BaseSpawner spawner = spw.getSpawner();
            //Formatter::off
            tag.putIntArray(STATS,
                    new int[] {
                            spawner.minSpawnDelay,
                            spawner.maxSpawnDelay,
                            spawner.spawnCount,
                            spawner.maxNearbyEntities,
                            spawner.requiredPlayerRange,
                            spawner.spawnRange,
                            (((IBaseSpawner) spw).getIgnoresPlayers()) ? 1 : 0,
                            (((IBaseSpawner) spw).getIgnoresConditions()) ? 1 : 0,
                            (((IBaseSpawner) spw).getRedstoneControl()) ? 1 : 0,
                            (((IBaseSpawner) spw).getIgnoreLight()) ? 1 : 0,
                            (((IBaseSpawner) spw).getNoAi()) ? 1 : 0,
                            (((IBaseSpawner) spw).getSilent()) ? 1 : 0
                    });
            //Formatter::on
        }

    }

    public static Component concat(Object... args) {
        return Component.translatable("misc.zenith.value_concat", args[0], Component.literal(args[1].toString()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GREEN);
    }
}
