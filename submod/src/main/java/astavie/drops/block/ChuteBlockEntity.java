package astavie.drops.block;

import astavie.drops.DropsMod;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class ChuteBlockEntity extends BlockEntity {

    public ChuteBlockEntity(BlockPos pos, BlockState state) {
        super(DropsMod.CHUTE_BLOCK_ENTITY, pos, state);
    }

    public long insertItem(World world, BlockPos blockPos, BlockState blockState, ItemStack stack, @Nullable ItemEntity entity) {
        double size = blockState.get(ChuteBlock.TYPE) == SlabType.DOUBLE ? 1.0 : 0.5;

        // check bottom
        if (blockState.get(ChuteBlock.TYPE) != SlabType.TOP && !world.isAir(blockPos.down()) && !world.isWater(blockPos.down())) {
            // get inventory at bottom
            Storage<ItemVariant> bottom = ItemStorage.SIDED.find(world, blockPos.down(), Direction.UP);
            if (bottom != null) {
                try (Transaction t1 = Transaction.openOuter()) {
                    long count = bottom.insert(ItemVariant.of(stack), stack.getCount(), t1);
                    t1.commit();
                    return count;
                }
            } else {
                return 0;
            }
        } else {
            if (entity != null) {
                entity.setPosition(entity.getPos().add(0, -size - 0.5, 0));
                return 0;
            } else {
                world.spawnEntity(new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, stack, world.random.nextDouble() * 0.2 - 0.1, -0.2, world.random.nextDouble() * 0.2 - 0.1));
                return stack.getCount();
            }
        }
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, ChuteBlockEntity be) {
        // check top item
        if (world.isAir(blockPos.up()) || world.isWater(blockPos.up())) {
            if (blockState.get(ChuteBlock.TYPE) != SlabType.BOTTOM) {
                // top half
                List<ItemEntity> list = world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), new Box(blockPos.up()).shrink(0, 0.75, 0), e -> true);
                if (!list.isEmpty()) {
                    ItemEntity first = list.get(0);
                    long inserted = be.insertItem(world, blockPos, blockState, first.getStack(), first);
                    first.getStack().decrement((int) inserted);
                    if (first.getStack().isEmpty()) {
                        first.discard();
                    }
                }
            } else {
                // bottom half
                List<ItemEntity> list = world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), new Box(blockPos.up()).shrink(0, 0.75, 0).offset(0, -0.5, 0), e -> true);
                if (!list.isEmpty()) {
                    ItemEntity first = list.get(0);
                    long inserted = be.insertItem(world, blockPos, blockState, first.getStack(), first);
                    first.getStack().decrement((int) inserted);
                    if (first.getStack().isEmpty()) {
                        first.discard();
                    }
                }
            }
        }

        // check top inventory
        if (blockState.get(ChuteBlock.TYPE) != SlabType.BOTTOM) {
            // get inventory at top
            Storage<ItemVariant> top = ItemStorage.SIDED.find(world, blockPos.up(), Direction.DOWN);
            if (top != null) {
                Iterator<StorageView<ItemVariant>> views = top.nonEmptyIterator();
                if (views.hasNext()) {
                    StorageView<ItemVariant> next = views.next();
                    try (Transaction t1 = Transaction.openOuter()) {
                        ItemVariant item = next.getResource();
                        long count = next.extract(item, Math.min(next.getAmount(), item.getItem().getMaxCount()), t1);
                        if (count > 0) {
                            t1.abort();
                            long inserted = be.insertItem(world, blockPos, blockState, item.toStack((int) count), null);
                            try (Transaction t2 = Transaction.openOuter()) {
                                next.extract(item, inserted, t2);
                                t2.commit();
                            }
                        }
                    }
                }
            }
        }
    }
}
