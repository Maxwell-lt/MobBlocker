package maxwell_lt.mobblocker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles gifting players ChunkProtectors upon first spawn
 */
@Mod.EventBusSubscriber
public class LoginEventHandler {

	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof PlayerEntity && Config.GIVE_NEW_PLAYERS_PROTECTOR.get()) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			CompoundNBT persistentTag;
			if (player.getPersistentData().contains(PlayerEntity.PERSISTED_NBT_TAG)) {
				persistentTag = player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
			} else {
				persistentTag = new CompoundNBT();
				player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, persistentTag);
			}
			CompoundNBT tag;
			if (persistentTag.contains("mobblocker")) {
				tag = persistentTag.getCompound("mobblocker");
			} else {
				tag = new CompoundNBT();
				persistentTag.put("mobblocker", tag);
			}
			if (!tag.getBoolean("receivedprotector")) {
				player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.CHUNKPROTECTOR));
				tag.putBoolean("receivedprotector", true);
			}
		}
	}
}
