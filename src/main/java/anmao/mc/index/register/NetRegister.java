package anmao.mc.index.register;

import anmao.mc.index.Index;
import anmao.mc.index.network.IndexBlockNet;
import anmao.mc.net.core.NetHandle;
import anmao.mc.net.core.NetReg;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class NetRegister {
    public static final DeferredRegister<NetHandle> EASY_NET = DeferredRegister.create(NetReg.KEY, Index.MOD_ID);
    public static final RegistryObject<NetHandle> INDEX_BLOCK = EASY_NET.register("index_block", IndexBlockNet::new);
    public static void reg(IEventBus eventBus){
        EASY_NET.register(eventBus);
    }
}
