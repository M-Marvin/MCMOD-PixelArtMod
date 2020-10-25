package de.pixelartmod.util;

import java.awt.Color;

import de.pixelartmod.PixelArtMod;
import de.pixelartmod.gui.GuiPixelArtManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Events {
	
	@SubscribeEvent
	public void onRenderEvent(net.minecraftforge.client.event.RenderWorldLastEvent event) {
		
		if (PixelArtMod.displaySchematics && PixelArtMod.schematic != null) {
			
			PixelArtMod.schematic.render();
			
		}
		
	}
	
	@SubscribeEvent
	public void onKeyTyped(net.minecraftforge.client.event.InputEvent event) {
		
		System.out.println("TEST PixelArt Mod");
		
		if (PixelArtMod.openSchematicManager.isKeyDown()) {
			Minecraft.getInstance().displayGuiScreen(new GuiPixelArtManager());
		} else if (PixelArtMod.toggleSchematics.isKeyDown()) {
			PixelArtMod.displaySchematics = !PixelArtMod.displaySchematics;
		}
		
		event.setResult(Result.ALLOW);
		
	}
	
	@SubscribeEvent
	public void onRenderGUIEvent(net.minecraftforge.client.event.RenderGameOverlayEvent event) {
		
		if (PixelArtMod.schematic != null && PixelArtMod.displaySchematics && event.getType() == net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE) {
			
			int xPlayerPos = (Minecraft.getInstance().player.getPosition().getX() - PixelArtMod.schematic.pos.getX()) / 2;
			int zLevel = PixelArtMod.schematic.visibleLevel;
			BlockPos rasterPos = new BlockPos(xPlayerPos, 0, zLevel);
			
			BlockObject obj = PixelArtMod.schematic.getBlockObject(rasterPos);
			
			if (obj != null) {
				
				String blockName = "Block: " + obj.getState().getBlock().getNameTextComponent().getUnformattedComponentText();
				String stateName = "State: " + obj.getState().toString().split("\\{")[1].split("\\}")[0];
				String position = "Position: " + rasterPos.getX() + " " + rasterPos.getZ();
				
				Gui.drawRect(0, 0, Minecraft.getInstance().fontRenderer.getStringWidth(stateName) + 10, 40, new Color(0, 0, 255, 60).getRGB());
				Minecraft.getInstance().fontRenderer.drawString(blockName, 5, 5, new Color(255, 255, 255).getRGB());
				Minecraft.getInstance().fontRenderer.drawString(stateName, 5, 15, new Color(255, 255, 255).getRGB());
				Minecraft.getInstance().fontRenderer.drawString(position, 5, 25, new Color(255, 255, 255).getRGB());
				
			}
			
		}
		
	}
		
}
