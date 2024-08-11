package anmao.mc.index.screen.index;

import anmao.dev.core.color._ColorCDT;
import anmao.mc.amlib.enchantment.EnchantmentSupports;
import anmao.mc.amlib.screen.widget.DT_ListBoxData;
import anmao.mc.amlib.screen.widget.RenderWidgetCore;
import anmao.mc.amlib.screen.widget.simple.SimpleButton;
import anmao.mc.amlib.screen.widget.simple.SimpleLabel;
import anmao.mc.amlib.screen.widget.square.SquareListBox;
import anmao.mc.index.Index;
import anmao.mc.index.datatype.IndexData;
import anmao.mc.index.datatype.IndexEnchantData;
import anmao.mc.index.register.NetRegister;
import anmao.mc.net.core.NetCore;
import anmao.mc.net.core.NetPack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
    private static final ResourceLocation TEXTURE = ResourceLocation.tryBuild(Index.MOD_ID,"textures/gui/index.png");
    private final int textureWidth = 320,textureHeight = 256;
    private SimpleButton enchantItem;
    private int x,y;
    private final Font font = Minecraft.getInstance().font;
    private HashMap<Enchantment, IndexEnchantData> enchantData;
    private final HashMap<Holder<Enchantment>,Integer> selectEnchants = new HashMap<>();
    private int needMp = 0;
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
        addRenderableWidget(new SimpleSlot(x+80,y+138,20,20,Component.literal("slot")));
        addRenderableWidget(new SimpleSlot(x+80,y+191,20,20,Component.literal("slot")));
        addRenderableWidget(new SimpleInventory(x+141,y+138,Component.literal("inventory")));
        addRenderableWidget(new SimpleHotbar(x+141,y+196,Component.literal("hotbar")));
        progressBar = new SimpleProgressBar(x+85,y+159,8,30,0,28,1,Component.literal("progressBar"));
        addRenderableWidget(progressBar);

        saveLabel = new SimpleLabel(x+9,y+141,64,16,Component.translatable("tooltip.nu.index.save.all").append("0"),true,false,false);
        addRenderableWidget(saveLabel);

        mpLabel = new SimpleLabel(x+9,y+157,64,16,Component.translatable("tooltip.nu.index.mp").append(String.valueOf(menu.getMP())),true,false,false);
        addRenderableWidget(mpLabel);
    }
    private void getEnchantments() {
        this.enchantments = new SimpleListBox( x+7,y+17,297,108,50,16,Component.literal("enchants"),
                getAllEnchantment()
        );
    }
    private List<DT_ListBoxData> getAllEnchantment() {
        List<DT_ListBoxData> data = new ArrayList<>();
        if (!enchantData.isEmpty()){
            enchantData.forEach((enchantment, indexEnchantData) -> data.add(new DT_ListBoxData(
                    EnchantmentSupports.getEnchantmentDescString(indexEnchantData.getHolderEnchant()),
                    enchantment,
                    List.of(
                            EnchantmentSupports.getEnchantmentDescString(indexEnchantData.getHolderEnchant()),
                            Component.translatable("tooltip.nu.index.max_lvl").append(String.valueOf(indexEnchantData.getMaxLvl())),
                            Component.translatable("tooltip.nu.index.xp").append(indexEnchantData.getXp().toString())
                    ),
                    this::addEnchantmentToSelect)));
        }
        return data;
    }
    private SimpleButton creatImageButton(int x, int y, int width, int height, RenderWidgetCore.OnPress onPress, Component component){
        return new SimpleButton(x,y,width,height,component,onPress);
    }
    private void addEnchantItemButton(){
        enchantItem = creatImageButton(x + 112, y + 165, 18, 16,this::sendSelectEnchant, Component.literal("EnchantItem"));
        enchantItem.setAutoWidth(true).setCenterText(true);
        this.addRenderableWidget(enchantItem);
    }
    private void sendSelectEnchant(){
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
        if (v instanceof Enchantment enchantment) {
            int lvl = selectEnchants.getOrDefault(Holder.direct(enchantment), 0);
            if (lvl < enchantData.get(enchantment).getMaxLvl()) {
                selectEnchants.put(enchantData.get(enchantment).getHolderEnchant(), lvl + 1);
                needMp += 5;
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        //renderBackground(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


        progressBar.setMaxProgress(menu.getProgressMax());
        progressBar.setProgress(menu.getProgress());

        mpLabel.setMessage(Component.translatable("tooltip.nu.index.mp").append(String.valueOf(menu.getMP())));
        saveLabel.setMessage(Component.translatable("tooltip.nu.index.save.all").append(String.valueOf(enchantData.size())));
        //pGuiGraphics.drawString(font,Component.translatable("tooltip.nu.index.save.all").append(String.valueOf(enchantData.size())),x+9,y+141,_ColorCDT.green,false);
        renderTooltip(pGuiGraphics,pMouseX,pMouseY);
        renderSelectEnchantTooltip(pGuiGraphics,pMouseX,pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    private void renderSelectEnchantTooltip(GuiGraphics guiGraphics, int pMouseX, int pMouseY){
        if (enchantItem != null && enchantItem.isHovered()) {
            List<Component> tip = new ArrayList<>();
            tip.add(Component.translatable("tooltip.nu.index.select").append(String.valueOf(selectEnchants.size())));
            selectEnchants.forEach((enchantment, integer) -> tip.add(
                    Enchantment.getFullname(enchantment,integer)
            ));
            ChatFormatting color = ChatFormatting.GREEN;
            if (needMp > menu.getMP()){
                color = ChatFormatting.RED;
            }
            tip.add(Component.translatable("tooltip.nu.index.need_mp").append(String.valueOf(needMp)).withStyle(color));
            guiGraphics.renderTooltip(font, tip, java.util.Optional.empty(), pMouseX, pMouseY);
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
                    enchantData.put(holder.get(), indexEnchantData);
                });
            }
        }
        this.enchantments.setData(getAllEnchantment());
    }
}
