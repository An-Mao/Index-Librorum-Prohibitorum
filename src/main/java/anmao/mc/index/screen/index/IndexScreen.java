package anmao.mc.index.screen.index;

import anmao.mc.amlib.enchantment.EnchantmentSupports;
import anmao.mc.amlib.screen.widget.DT_ListBoxData;
import anmao.mc.amlib.screen.widget.simple.SimpleButton;
import anmao.mc.amlib.screen.widget.simple.SimpleLabel;
import anmao.mc.index.datatype.IndexData;
import anmao.mc.index.datatype.IndexEnchantData;
import anmao.mc.index.register.NetRegister;
import anmao.mc.net.core.NetCore;
import anmao.mc.net.core.NetPack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexScreen extends AbstractContainerScreen<IndexMenu> {
    private int x,y;
    private HashMap<Holder<Enchantment>, IndexEnchantData> enchantData;
    private final HashMap<Holder<Enchantment>,Integer> selectEnchants = new HashMap<>();
    private int needMp = 0;

    private SimpleButton enchantItem;
    private SimpleListBox enchantments;
    private SimpleProgressBar progressBar;
    private SimpleLabel saveLabel,mpLabel;

    public IndexScreen(IndexMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        enchantData = menu.getEnchantData();
    }
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        imageHeight = 222;
        imageWidth = 311;
        leftPos = x = (width - imageWidth) / 2;
        topPos = y = (height - imageHeight) / 2;
        addEnchantItemButton();
        getEnchantments();
        addRenderableWidget(this.enchantments);
        addRenderableWidget(new SimpleSlot(x+34,y+139,20,20,Component.literal("slot")));
        addRenderableWidget(new SimpleSlot(x+92,y+139,20,20,Component.literal("slot")));
        addRenderableWidget(new SimpleInventory(x+141,y+138,Component.literal("inventory")));
        addRenderableWidget(new SimpleHotbar(x+141,y+196,Component.literal("hotbar")));
        progressBar = new SimpleProgressBar(x+63,y+145,26,8,0,28,0,Component.literal("progressBar")).setLayerZ(1);
        addRenderableWidget(progressBar);
        saveLabel = new SimpleLabel(x+7,y+200,64,16,Component.translatable("tooltip.index.index.save.all").append("0"),true,false,true).setLayerZ(1);
        addRenderableWidget(saveLabel);
        mpLabel = new SimpleLabel(saveLabel.getX()+saveLabel.getWidth()+10,saveLabel.getY(),64,16,Component.translatable("tooltip.index.index.mp").append(String.valueOf(menu.getMP())),true,false,true).setLayerZ(1);
        //mpLabel.setDrawY(mpLabel.getDrawY()+4);
        addRenderableWidget(mpLabel);
    }
    private void getEnchantments() {
        this.enchantments = new SimpleListBox( x+7,y+17,297,108,50,16,Component.literal("enchants"),
                getAllEnchantment()
        ).setLayerZ(1);
    }
    private List<DT_ListBoxData> getAllEnchantment() {
        List<DT_ListBoxData> data = new ArrayList<>();
        if (!enchantData.isEmpty()){
            enchantData.forEach((holder, indexEnchantData) -> data.add(new DT_ListBoxData(
                    EnchantmentSupports.getEnchantmentDescString(indexEnchantData.getHolderEnchant()),
                    holder,
                    List.of(
                            EnchantmentSupports.getEnchantmentDescString(indexEnchantData.getHolderEnchant()),
                            Component.translatable("tooltip.index.index.max_lvl").append(String.valueOf(indexEnchantData.getMaxLvl())),
                            Component.translatable("tooltip.index.index.xp").append(indexEnchantData.getXp().toString())
                    ),
                    this::addEnchantmentToSelect)));
        }
        return data;
    }
    private void addEnchantItemButton(){
        enchantItem =  new  SimpleButton(x + 34, y + 166, 79, 16, Component.translatable("screen.index.index.button.enchant_item"),false,false,true,this::sendSelectEnchantPack);
        enchantItem.setLayerZ(1);
        this.addRenderableWidget(enchantItem);
    }
    private void sendSelectEnchantPack(){
        if (selectEnchants.isEmpty())return;
        ListTag tags = new ListTag();
        selectEnchants.forEach((enchantment, integer) -> {
            CompoundTag ed = new CompoundTag();
            ed.putString(IndexData.ID,enchantment.getRegisteredName());
            ed.putInt(IndexData.LVL,integer);
            tags.add(ed);
        });
        CompoundTag lDat = new CompoundTag();
        lDat.put("enchants",tags);
        lDat.putInt("be.x",menu.getX());
        lDat.putInt("be.y",menu.getY());
        lDat.putInt("be.z",menu.getZ());
        NetCore.sendToServer(NetPack.createServerPack(lDat, NetRegister.INDEX_BLOCK));
    }

    public void addEnchantmentToSelect(Object v){
        if (v instanceof Holder<?> holder) {
            if (holder.get() instanceof Enchantment) {
                @SuppressWarnings("unchecked")
                Holder<Enchantment> enchantment = (Holder<Enchantment>) v;
                int lvl = selectEnchants.getOrDefault(enchantment, 0);
                if (lvl < enchantData.get(enchantment).getMaxLvl()) {
                    selectEnchants.put(enchantData.get(enchantment).getHolderEnchant(), lvl + 1);
                    needMp += 5;
                }
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        //renderBackground(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        progressBar.setMaxProgress(menu.getProgressMax());
        progressBar.setProgress(menu.getProgress());
        mpLabel.setMessage(Component.translatable("tooltip.index.index.mp").append(String.valueOf(menu.getMP())));
        saveLabel.setMessage(Component.translatable("tooltip.index.index.save.all").append(String.valueOf(enchantData.size())));

        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
        updateSelectEnchantTooltip();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.fill(RenderType.endPortal(),0,0,this.width,this.height,0xff000000);
    }

    private void updateSelectEnchantTooltip(){
        if (enchantItem != null) {
            List<Component> tip = new ArrayList<>();
            tip.add(Component.translatable("tooltip.index.index.select").append(String.valueOf(selectEnchants.size())));
            selectEnchants.forEach((enchantment, integer) -> tip.add(Enchantment.getFullname(enchantment, integer)));
            ChatFormatting color = ChatFormatting.GREEN;
            if (needMp > menu.getMP()){
                color = ChatFormatting.RED;
            }
            tip.add(Component.translatable("tooltip.index.index.need_mp").append(String.valueOf(needMp)).withStyle(color));
            enchantItem.setCustomToolTip(tip);
        }
    }
    public void handlePacket(CompoundTag msg) {
        this.enchantData = new HashMap<>();
        ListTag es = msg.getList("index.enchants", Tag.TAG_COMPOUND);
        for (int i = 0; i < es.size(); i++){
            CompoundTag compoundtag = es.getCompound(i);
            if (Minecraft.getInstance().level != null) {
                EnchantmentSupports.getRegistry(Minecraft.getInstance().level).getHolder(ResourceLocation.parse(compoundtag.getString(IndexData.ID))).ifPresent((holder) -> {

                    IndexEnchantData indexEnchantData = new IndexEnchantData(holder);
                    int lvl = Mth.clamp(compoundtag.getInt("max"), 0, Integer.MAX_VALUE);
                    indexEnchantData.setXp(compoundtag.getString("xp"));
                    indexEnchantData.setMaxLvl(lvl);
                    enchantData.put(holder, indexEnchantData);
                });
            }
        }
        this.enchantments.setData(getAllEnchantment());
    }
}
