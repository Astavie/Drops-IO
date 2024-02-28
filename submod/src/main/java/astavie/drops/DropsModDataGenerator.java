package astavie.drops;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class DropsModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(MyModelGenerator::new);
	}

	private static class MyModelGenerator extends FabricModelProvider {
		private MyModelGenerator(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			// chute block
			TextureMap textureMap = TextureMap.all(Blocks.SMOOTH_STONE);
			TextureMap textureMap2 = TextureMap.sideEnd(TextureMap.getSubId(Blocks.SMOOTH_STONE_SLAB, "_side"), textureMap.getTexture(TextureKey.TOP));
			Identifier identifier = Models.SLAB.upload(DropsMod.CHUTE_BLOCK, textureMap2, blockStateModelGenerator.modelCollector);
			Identifier identifier2 = Models.SLAB_TOP.upload(DropsMod.CHUTE_BLOCK, textureMap2, blockStateModelGenerator.modelCollector);
			Identifier identifier3 = Models.CUBE_COLUMN.uploadWithoutVariant(DropsMod.CHUTE_BLOCK, "_double", textureMap2, blockStateModelGenerator.modelCollector);
			blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(DropsMod.CHUTE_BLOCK, identifier, identifier2, identifier3));
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			// ...
		}
	}
}
