package org.tuincraft.blaatz0r.ColorSign;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.ChatColor;

import org.bukkit.entity.*;

public class ColorSignPlayerListener extends PlayerListener  {
	

	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {

		
		Logger log = Logger.getLogger("Minecraft");
		Player p = event.getPlayer();
		Block target = p.getTargetBlock(null, 5);
		
		BlockState state = target.getState();
		
		if (state instanceof Sign && state != null) {
			
			Sign sign = (Sign) state;
			String[] lines = sign.getLines();
			ChatColor color = ChatColor.BLACK;
			
			Pattern minecraftColor = Pattern.compile("\u00A7[0-9a-fA-F]");
			Matcher m = minecraftColor.matcher(lines[0]);
			log.info("Line 0: " + lines[0]);
			
			
			while (m.find()) {
				log.info("Group: " + m.group());
				
				// Get the color index from the first line of the sign
				String match = Character.toString(lines[0].charAt(m.end()-1));
				log.info("Match: " + match);
				int res = Integer.parseInt(match,16);
				
				log.info("New slot: " + event.getNewSlot());
				
				int newSlot = event.getNewSlot();
				int oldSlot = event.getPreviousSlot();
				
				// Scroll up: (slot+=1) or (slot 8 -> slot 0)
				if (newSlot > oldSlot || (newSlot < oldSlot && Math.abs(newSlot - oldSlot) != 1)) {
					res++;
				} else {
					res--;
				}
				
				color = ChatColor.getByCode(res % ChatColor.values().length);

				log.info("Color found: " + color.name());
			}
			
			colorSign(sign, color);
			
			//log.info("SIGN DETECTED! D:" + Arrays.toString(sign.getLines()));
		
		}
		
	}

	/**
	 * Adds color to every line on the sign.
	 * @param sign The sign to be modified
	 * @param color The color that each line will be
	 */
	private void colorSign(Sign sign, ChatColor color) {
		String[] lines = sign.getLines();
		int i = 0;
		
		for (String l : lines) {
			l = color + ChatColor.stripColor(l);
			sign.setLine(i, l);
			i++;
		}
		
		sign.update();
	}
	
	
}
