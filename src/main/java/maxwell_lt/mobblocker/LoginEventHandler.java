package maxwell_lt.mobblocker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class LoginEventHandler {
	
	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound persistentTag;
			if (player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
				persistentTag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			} else {
				persistentTag = new NBTTagCompound();
				player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
			}
			NBTTagCompound tag;
			if (persistentTag.hasKey("mobblocker")) {
				tag = persistentTag.getCompoundTag("mobblocker");
			} else {
				tag = new NBTTagCompound();
				persistentTag.setTag("mobblocker", tag);
			}
			if (!tag.getBoolean("receivedprotector")) {
				player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.chunkProtector));
				tag.setBoolean("receivedprotector", true);
			}
		}
	}
}
