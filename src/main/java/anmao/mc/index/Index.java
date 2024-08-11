package anmao.mc.index;

import anmao.dev.core.system._File;
import anmao.mc.index.register.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Index.MOD_ID)
public class Index
{
    public static final String MOD_ID = "index";
    public static final String ConfigDir = _File.getFileFullPathWithRun("/config/Index/");
    public Index() {
        _File.checkAndCreateDir(ConfigDir);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegister.CreativeTabs.reg(modEventBus);
        ItemRegister.register(modEventBus);
        BlockRegister.reg(modEventBus);
        BlockEntityRegister.reg(modEventBus);
        NetRegister.reg(modEventBus);
        MenuTypeRegister.reg(modEventBus);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configs.SPEC);
    }
}
