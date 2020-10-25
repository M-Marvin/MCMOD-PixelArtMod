package de.pixelartmod;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import de.pixelartmod.util.Events;
import de.pixelartmod.util.PixelArt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pixelartmod")
public class PixelArtMod {
	
	private static final Logger LOGGER = LogManager.getLogger();
	public static PixelArt schematic;
    public static KeyBinding openSchematicManager = new KeyBinding("key.openpixelartmenu", GLFW.GLFW_KEY_L, "kategory.pixelartmod");
    public static KeyBinding toggleSchematics = new KeyBinding("key.togglepixelarts", GLFW.GLFW_KEY_K, "kategory.pixelartmod");
    public static boolean displaySchematics;
    public static File schematicsPath;
    public static final String MODID = "pixelartmod";
	
    public PixelArtMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(new Events());
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
        
    	File gameDir = new File(Minecraft.getInstance().gameDir.getAbsolutePath());
    	schematicsPath = new File(gameDir, "config/PixelArts/");
    	if (!schematicsPath.exists()) {
    		schematicsPath.mkdirs();
    		LOGGER.info("PixelArt Directory createt: " + schematicsPath.getAbsolutePath());
    	}
    	
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        
    	ClientRegistry.registerKeyBinding(openSchematicManager);
    	ClientRegistry.registerKeyBinding(toggleSchematics);
    	
    }
    
}
