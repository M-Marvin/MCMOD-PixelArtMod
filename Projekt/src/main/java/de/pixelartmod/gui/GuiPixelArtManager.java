package de.pixelartmod.gui;

import java.awt.Color;
import java.io.File;

import de.pixelartmod.PixelArtMod;
import de.pixelartmod.util.PixelArt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiPixelArtManager extends GuiScreen {
	
	public GuiButton select_schmatic;
	public GuiTextField schematic_name;
	public GuiTextField schematic_x;
	public GuiTextField schematic_y;
	public GuiTextField schematic_z;
	public GuiButton xUp;
	public GuiButton xDown;
	public GuiButton yUp;
	public GuiButton yDown;
	public GuiButton zUp;
	public GuiButton zDown;
	public GuiTextField schematic_visible_level;
	public GuiButton levelUp;
	public GuiButton levelDown;
	
	public int select_x;
	public int select_y;
	public int select_width;
	public int select_heigth;
	public int maxLevel;
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		
		Color color_line = new Color(64, 64, 64);
		Color color_fill = new Color(64, 64, 64, 120);
		
		drawRect(this.width - 122, 38, this.width, 40, color_line.getRGB());
		drawRect(this.width - 122, 40, this.width - 120, this.height, color_line.getRGB());
		drawRect(this.width - 120, 40, this.width, this.height, color_fill.getRGB());
				
		this.schematic_name.drawTextField(mouseX, mouseY, partialTicks);
		this.schematic_x.drawTextField(mouseX, mouseY, partialTicks);
		this.schematic_y.drawTextField(mouseX, mouseY, partialTicks);
		this.schematic_z.drawTextField(mouseX, mouseY, partialTicks);
		this.schematic_visible_level.drawTextField(mouseX, mouseY, partialTicks);
		
		drawString(Minecraft.getInstance().fontRenderer, new TextComponentTranslation("gui.position").getString(), this.width - 110, 137, new Color(255, 255, 255).getRGB());
		drawString(Minecraft.getInstance().fontRenderer, new TextComponentTranslation("gui.displaylevel").getString(), this.width - 110, 195, new Color(255, 255, 255).getRGB());
		drawString(Minecraft.getInstance().fontRenderer, new TextComponentTranslation("gui.selectschematic").getString(), this.width - 110, 65, new Color(255, 255, 255).getRGB());
		
		drawString(Minecraft.getInstance().fontRenderer, new TextComponentTranslation("gui.madeby").getString() + "M_Marvin", this.width - 110, 50, new Color(200, 200, 200).getRGB());
		
		drawString(Minecraft.getInstance().fontRenderer, new TextComponentTranslation("gui.folderinfo").getString(), 5, 224, new Color(154, 160, 157).getRGB());
		
		super.render(mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	protected void initGui() {
		
		PixelArtMod.displaySchematics = true;
		
		select_x = 10;
		select_y = 20;
		select_width = 100;
		select_heigth = 20;
		
		this.select_schmatic = new GuiButton(10, this.width - 110, 100, 100, 20, new TextComponentTranslation("gui.select_schematic.path").getString()) {
			@Override
			public void onClick(double mouseX, double mouseY) {
				
				GuiScreen gui = Minecraft.getInstance().currentScreen;
				if (gui instanceof GuiPixelArtManager) {
					
					File schematic = new File(PixelArtMod.schematicsPath, GuiPixelArtManager.this.schematic_name.getText());
					
					if (schematic.exists()) {
						
						BlockPos creatorPos = new BlockPos(Minecraft.getInstance().player);
						
						PixelArtMod.schematic = new PixelArt(schematic, creatorPos);
						
						GuiPixelArtManager.this.schematic_x.setText("" + creatorPos.getX());
						GuiPixelArtManager.this.schematic_y.setText("" + creatorPos.getY());
						GuiPixelArtManager.this.schematic_z.setText("" + creatorPos.getZ());
						
					}
					
				}
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.select_schmatic);
		
		schematic_name = new GuiTextField(12, Minecraft.getInstance().fontRenderer, this.width - 110, 80, 100, 18);
		this.children.add(schematic_name);
		
		//Position Buttons
		
		int width = 33;
		int heigth = 18;
		int x = this.width - 110;
		int z = 160;
		
		schematic_x = new GuiTextField(13, Minecraft.getInstance().fontRenderer, x + (width * 0), z, width, heigth);
		this.children.add(schematic_x);
		schematic_y = new GuiTextField(14, Minecraft.getInstance().fontRenderer, x + (width * 1), z, width, heigth);
		this.children.add(schematic_y);
		schematic_z = new GuiTextField(15, Minecraft.getInstance().fontRenderer, x + (width * 2), z, width, heigth);
		this.children.add(schematic_z);
		
		this.xUp = new GuiButton(20, x - 1, z - 9, 35, 8, "^") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_x.getText());
				i++;
				GuiPixelArtManager.this.schematic_x.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.xUp);
		this.xDown = new GuiButton(21, x - 1, z + 19, 35, 8, "v") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_x.getText());
				i--;
				GuiPixelArtManager.this.schematic_x.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.xDown);
		this.yUp = new GuiButton(22, x + 32, z - 9, 35, 8, "^") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_y.getText());
				i++;
				GuiPixelArtManager.this.schematic_y.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.yUp);
		this.yDown = new GuiButton(23, x + 32, z + 19, 35, 8, "v") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_y.getText());
				i--;
				GuiPixelArtManager.this.schematic_y.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.yDown);
		this.zUp = new GuiButton(24, x + 32 + 33, z - 9, 35, 8, "^") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_z.getText());
				i++;
				GuiPixelArtManager.this.schematic_z.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.zUp);
		this.zDown = new GuiButton(25, x + 32 + 33, z + 19, 35, 8, "v") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_z.getText());
				i--;
				GuiPixelArtManager.this.schematic_z.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.zDown);
		
		//Visible Level
		
		this.levelUp = new GuiButton(26, this.width - 110, 210, 98, 8, "^") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_visible_level.getText());
				i = Math.min(GuiPixelArtManager.this.maxLevel, i + 1);
				GuiPixelArtManager.this.schematic_visible_level.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.levelUp);
		this.levelDown = new GuiButton(27, this.width - 110, 238, 98, 8, "v") {
			@Override
			public void onClick(double mouseX, double mouseY) {
				int i = Integer.parseInt(GuiPixelArtManager.this.schematic_visible_level.getText());
				i = Math.max(0, i - 1);
				GuiPixelArtManager.this.schematic_visible_level.setText(i + "");
				super.onClick(mouseX, mouseY);
			}
		};
		this.addButton(this.levelDown);
		
		schematic_visible_level = new GuiTextField(15, Minecraft.getInstance().fontRenderer, this.width - 110, 219, 98, heigth);
		this.children.add(schematic_visible_level);
		
		updateGui();
		super.initGui();
		
	}
	
	@Override
	public IGuiEventListener getFocused() {
		if (this.schematic_name.isFocused()) {
			return this.schematic_name;
		} else if (this.schematic_x.isFocused()) {
			return this.schematic_x;
		} else if (this.schematic_y.isFocused()) {
			return this.schematic_y;
		} else if (this.schematic_z.isFocused()) {
			return this.schematic_z;
		} else if (this.schematic_visible_level.isFocused()) {
			return this.schematic_visible_level;
		}
		return null;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void updateGui() {
		
		PixelArt schem = PixelArtMod.schematic;
		
		this.schematic_name.setText(schem != null ? schem.getFile().getName() : "");
		this.schematic_x.setText("" + (schem != null ? schem.getPos().getX(): "0"));
		this.schematic_y.setText("" + (schem != null ? schem.getPos().getY() : "0"));
		this.schematic_z.setText("" + (schem != null ? schem.getPos().getZ() : "0"));
		this.schematic_visible_level.setText("" + (schem != null ? schem.getVisibleLevel() : "1"));
		this.maxLevel = (schem != null ? schem.getLength() - 1 : 1);
		
	}
	
	@Override
	public void tick() {
		
		if (PixelArtMod.schematic != null) {
			
			try {
				int x = Integer.parseInt(this.schematic_x.getText().equals("") || this.schematic_x.getText().equals("-") ? "0" : this.schematic_x.getText());
				int y = Integer.parseInt(this.schematic_y.getText().equals("") || this.schematic_y.getText().equals("-") ? "0" : this.schematic_y.getText());
				int z = Integer.parseInt(this.schematic_z.getText().equals("") || this.schematic_z.getText().equals("-") ? "0" : this.schematic_z.getText());
				int vl = Integer.parseInt(this.schematic_visible_level.getText().equals("") || this.schematic_visible_level.getText().equals("-") ? "0" : this.schematic_visible_level.getText());
				
				PixelArtMod.schematic.pos = new BlockPos(x, y, z);
				PixelArtMod.schematic.visibleLevel = vl;
			} catch(Exception e) {
				this.schematic_x.setText("" + PixelArtMod.schematic.getPos().getX());
				this.schematic_y.setText("" + PixelArtMod.schematic.getPos().getY());
				this.schematic_z.setText("" + PixelArtMod.schematic.getPos().getZ());
			}
		}
		
	}
	
}
