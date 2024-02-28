package astavie.drops;

import astavie.drops.block.ChuteBlock;
import astavie.drops.block.ChuteBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "drops-io";
    public static final Logger LOGGER = LoggerFactory.getLogger("Drops I/O");

	public static final ChuteBlock CHUTE_BLOCK = new ChuteBlock(FabricBlockSettings.copyOf(Blocks.HOPPER));
	public static final BlockEntityType<ChuteBlockEntity> CHUTE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ChuteBlockEntity::new, CHUTE_BLOCK).build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		registerBlock(CHUTE_BLOCK, "chute");
		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MODID, "chute_entity"), CHUTE_BLOCK_ENTITY);
	}

	private void registerBlock(Block block, String name) {
		Registry.register(Registries.BLOCK, new Identifier(MODID, name), block);
		Registry.register(Registries.ITEM, new Identifier(MODID, name), new BlockItem(block, new FabricItemSettings()));
	}
}