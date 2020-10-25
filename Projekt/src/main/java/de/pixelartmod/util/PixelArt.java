package de.pixelartmod.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import de.pixelartmod.render.RenderHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class PixelArt {
	
	public BlockPos pos;
	public BlockObject[] blockObjects;
	public HashMap<Integer, String> palette;
	public int width;
	public int length;
	public int size;
	public int fileVersion;
	public File file;
	public int visibleLevel;
	
	public PixelArt(File file, BlockPos position) {
		
		this.pos = position;
		this.file = file;
		this.visibleLevel = 0;
		
		try {
			
			InputStream is = new FileInputStream(file);
			NBTTagCompound nbtdata = CompressedStreamTools.readCompressed(is);
			is.close();
			
			width = nbtdata.getShort("Width");
			length = nbtdata.getShort("Length");
			size = width * length;
			fileVersion = nbtdata.getInt("DataVersion");
			
			blockObjects = new BlockObject[size];
			
			byte[] blocks = nbtdata.getByteArray("BlockData");
			
			NBTTagCompound palette = nbtdata.getCompound("Palette");
			this.palette = new HashMap<Integer, String>();
			for (String k : palette.keySet()) {
				this.palette.put(palette.getInt(k), k);
			}
			
			int counter = 0;
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < width; k++) {
					
					BlockPos pos = new BlockPos(k, 0 , j);

					int id = blocks[counter];
					
					if (id < 0) id *= -1;
					
					IBlockState state = getStateFromID(id);
					
					blockObjects[counter] = new BlockObject(pos, state);
					
					counter++;
					
				}
			}
 			
		} catch (Exception e) {
			System.err.println("ERROR Cant load as PixelArt: " + file);
			e.printStackTrace();
			this.width = 0;
			this.length = 0;
			this.size = 0;
			this.blockObjects = null;
			this.palette = null;
		}
		
	}
	
	public File getFile() {
		return file;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public int getVisibleLevel() {
		return this.visibleLevel;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isValid() {
		return this.palette != null && this.blockObjects != null;
	}
	
	public BlockObject getBlockObject(BlockPos rasterPos) {
		
		if (this.isValid()) {
			
			for (BlockObject obj : this.blockObjects) {
				
				if (obj.getPosition().equals(rasterPos)) return obj;
				
			}
			
		}

		return null;
		
	}
	
	private static int list = -1;
	
	public void render() {
		
		if (Minecraft.getInstance().world.isRemote && this.isValid()) {
			
			GL11.glPushMatrix();
			EntityPlayer player = Minecraft.getInstance().player;
        	GlStateManager.translated(-player.lastTickPosX, -player.lastTickPosY, -player.lastTickPosZ);
			GlStateManager.translated(pos.getX(), pos.getY(), pos.getZ());
			
			//Render Border
			
			if (list == -1) {
				
				list = GL11.glGenLists(1);
				GL11.glNewList(list, GL11.GL_COMPILE);
				
				RenderHelper.renderBlockAsGhost(Blocks.WHITE_WOOL.getDefaultState(), new BlockPos(-1, 1 ,-1), this.pos);
				RenderHelper.renderBlockAsGhost(Blocks.WHITE_WOOL.getDefaultState(), new BlockPos(-1, 0 ,-1), this.pos);
				RenderHelper.renderBlockAsGhost(Blocks.RED_WOOL.getDefaultState(), new BlockPos(0, 1, -1), this.pos);
				RenderHelper.renderBlockAsGhost(Blocks.RED_WOOL.getDefaultState(), new BlockPos(1, 1, -1), this.pos);
				RenderHelper.renderBlockAsGhost(Blocks.BLUE_WOOL.getDefaultState(), new BlockPos(-1, 1, 0), this.pos);
				RenderHelper.renderBlockAsGhost(Blocks.BLUE_WOOL.getDefaultState(), new BlockPos(-1, 1, 1), this.pos);
				
				boolean color = true;
				for (int z = 0; z <= this.length * 2; z++) {
					RenderHelper.renderBlockAsGhost(color ? Blocks.YELLOW_WOOL.getDefaultState() : Blocks.BLACK_WOOL.getDefaultState(), new BlockPos(-1, 0, z), this.pos);
					color = !color;
				}
				for (int z = 0; z <= this.length * 2; z++) {
					RenderHelper.renderBlockAsGhost(color ? Blocks.YELLOW_WOOL.getDefaultState() : Blocks.BLACK_WOOL.getDefaultState(), new BlockPos(this.width * 2, 0, z), this.pos);
					color = !color;
				}
				for (int x = 0; x <= this.width * 2; x++) {
					RenderHelper.renderBlockAsGhost(color ? Blocks.YELLOW_WOOL.getDefaultState() : Blocks.BLACK_WOOL.getDefaultState(), new BlockPos(x, 0, -1), this.pos);
					color = !color;
				}
				for (int x = 0; x <= this.width * 2; x++) {
					RenderHelper.renderBlockAsGhost(color ? Blocks.YELLOW_WOOL.getDefaultState() : Blocks.BLACK_WOOL.getDefaultState(), new BlockPos(x, 0, this.length * 2), this.pos);
					color = !color;
				}
				
				GL11.glEndList();
				
			}
			
			GL11.glCallList(list);
			
			try {
				
				int xPlayerPos = (Minecraft.getInstance().player.getPosition().getX() - this.pos.getX()) / 2;
				int zLevel = this.visibleLevel;
				BlockPos rasterPos = new BlockPos(xPlayerPos, 0, zLevel);
				
				BlockObject obj = getBlockObject(rasterPos);
				
				if (obj != null ? obj.getPosition().equals(rasterPos) : false) {
					
					BlockPos position = new BlockPos(obj.getPosition().getX() * 2, obj.getPosition().getY(), obj.getPosition().getZ() * 2);
					
					RenderHelper.renderBlockAsGhost(obj.getState(), position, this.pos);
					RenderHelper.renderBlockAsGhost(obj.getState(), position.south(), this.pos);
					
				}
				
			} catch (Exception e) {}
			
			GL11.glPopMatrix();
			
		}
		
	}
	
	public IBlockState getStateFromID(int id) {

		String iblockstateS = this.palette.get(id);
		
		try {
			
			BlockStateParser parser = new BlockStateParser(new StringReader(iblockstateS), true);
			parser.parse(false);

			return parser.getState();
			
		} catch (CommandSyntaxException e) {
			System.out.println("Warning! block " + iblockstateS + " nicht gefunden!");
			return Blocks.AIR.getDefaultState();
		}
		
	}
	
}
