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
	
	private static final Logger log = Logger.getLogger("Minecraft");
	

	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {

		
		
		Player p = event.getPlayer();
		Block target = p.getTargetBlock(null, 5);
		
		BlockState state = target.getState();
		
		// Check if the current state is a Sign and 
		// If the sign it is not null (otherwise causes nullpointer if the sign is removed and player scrolls while looking at the block)
		if (state instanceof Sign && state != null) {
			
			Sign sign = (Sign) state;
			String[] lines = sign.getLines();
			
			// Find the first non-empty line which could have a color
			String line = "";
			boolean found = false;
			for(int i = 0; i < lines.length && !found; i++){
				if(lines[i].length() > 0 && lines[i].length() < 14) {
					line = lines[i];
					found = true;
				}
			}
				
			
			// Init new color to black in case there is no color yet
			ChatColor color = ChatColor.BLACK;
			
			// Match the line of the sign on minecraft character for colour: \u00A7 + [0-f]
			Pattern minecraftColor = Pattern.compile("\u00A7[0-9a-fA-F]");
			Matcher m = minecraftColor.matcher(line);			
			
			// Find all color codes in the line
			while (m.find()) {
				
				// Get the color index from the line of the sign
				String match = Character.toString(line.charAt(m.end()-1));
				int res = Integer.parseInt(match,16);
								
				int newSlot = event.getNewSlot();
				int oldSlot = event.getPreviousSlot();
				int numColors = ChatColor.values().length;
				
				// Scroll up means (slot+=1) or (slot 8 -> slot 0)
				if (newSlot == oldSlot + 1 || (newSlot < oldSlot && Math.abs(newSlot - oldSlot) != 1)) {
					res++;
				} else {
					res--;
				}
				
				// Fix for modulo operator in java: -1 % 16 == -1; should be: -1 % 16 == 15
				if (res < 0) {
					res += numColors;
				}
				
				color = ChatColor.getByCode(res % numColors);
			}
			
			// Change the color of the whole sign
			colorSign(sign, color);
		
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
			l = ChatColor.stripColor(l);
			// Don't color a line if it will overwrite the max length (15)
			if(l.length() < 14 && l.length() > 0) {
				sign.setLine(i, ""+ color + l);
			}
			i++;
		}
		
		sign.update();
	}
	
	
}
