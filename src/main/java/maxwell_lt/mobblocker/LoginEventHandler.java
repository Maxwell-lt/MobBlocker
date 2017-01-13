package maxwell_lt.mobblocker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LoginEventHandler {
	
	@SubscribeEvent
	public void playerLogin(EntityJoinWorldEvent login) {
		if (login.getEntity() instanceof EntityPlayer && !login.getWorld().isRemote) {
			EntityPlayer player = (EntityPlayer) login.getEntity();
			//WIP
		}
	}

}
