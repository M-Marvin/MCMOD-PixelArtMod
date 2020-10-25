package de.pixelartmod.render;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderHelper {

	@SuppressWarnings("deprecation")
	public static void renderBlockAsGhost(IBlockState iblockstate, BlockPos blockpos, BlockPos structurePos) {
		
	    if (iblockstate != null ? (iblockstate.getBlock() != Blocks.AIR && iblockstate.getBlock() != Blocks.CAVE_AIR) : false) {
	    	
		    	 World world = Minecraft.getInstance().world;
		         BlockPos blockpos1 = blockpos;
		    	 IBlockState existstate = world.getBlockState(blockpos.add(structurePos));
		    	 
		    	 GlStateManager.pushMatrix();
		         GlStateManager.enableLighting();
		         
		         if (existstate.equals(iblockstate)) {
		        	 
		        	 GlStateManager.colorMask(false, true, false, false);
		        	 blockpos1 = blockpos.up();
		        	 
		         }
		         
		         if (iblockstate.getBlock() instanceof BlockContainer) {
	        	    	
		        	 TileEntity tileentity = iblockstate.getBlock().createTileEntity(iblockstate, world);
	        	    	
	        	    if (tileentity != null) {
	        	    		
	        	    	TileEntityRendererDispatcher tileentityrenderdispatcher = TileEntityRendererDispatcher.instance;
	        	    	TileEntityRenderer<TileEntity> tileentityrenderer = tileentityrenderdispatcher.getRenderer(tileentity.getClass());
		        	    	
	        	    	if (tileentityrenderer != null) {
		        	    		
	        	    		World tileentityworld = Minecraft.getInstance().world;
	        	    		tileentity.setWorld(tileentityworld);
	        	    		tileentity.setPos(blockpos1.add(structurePos));
	        	    		IBlockState oldstate = tileentityworld.getBlockState(blockpos1.add(structurePos));
	        	    		tileentityworld.setBlockState(blockpos1.add(structurePos), iblockstate);
	        	    		tileentity.markDirty();
	        	    		tileentityworld.setBlockState(blockpos1.add(structurePos), oldstate);
			        	    
	        	    		try {

	        	    			tileentityrenderer.render(tileentity, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 0, -1);
			        	    		
	        	    		} catch (IllegalArgumentException e) {}
			        	    	
	        	    	}
	        	    		
	        	    }

		         }
	        	 
		         Minecraft.getInstance().getRenderManager().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	        	 	
		         Tessellator tessellator = Tessellator.getInstance();
		         BufferBuilder bufferbuilder = tessellator.getBuffer();
		         bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
		         
		         BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
		         blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, new Random(), iblockstate.getPositionRandom(blockpos));
		         	
		         tessellator.draw();
		         
	        	 GlStateManager.colorMask(true, true, true, true);
		         
		         GlStateManager.popMatrix();
		         
	    }
		
	    
	}
		
}
