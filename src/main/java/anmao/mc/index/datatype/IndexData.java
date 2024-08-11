package anmao.mc.index.datatype;

import anmao.mc.amlib.enchantment.EnchantmentSupports;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.math.BigInteger;
import java.util.HashMap;

public class IndexData {
    public static final String ID = "id";
    public static final String MAX_LVL = "max";
    public static final String LVL = "lvl";
    public static final String XP = "xp";
    private Level level;
    private final HashMap<Enchantment, IndexEnchantData> enchantData;
    public IndexData() {
        enchantData = new HashMap<>();
    }

    public IndexData(Level level) {
        this();
        setLevel(level);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        if (this.level != null) {
            this.level = level;
        }
    }

    public ListTag formatEnchants(){
        ListTag es = new ListTag();
        if (!enchantData.isEmpty()){
            enchantData.forEach((eid,intInt)-> es.add(intInt.toNBT()));
        }
        return es;
    }
    public void loadEnchants(ListTag es){
        for (int i = 0; i < es.size(); i++){
            CompoundTag compoundtag = es.getCompound(i);
            String eid = compoundtag.getString(ID);
            EnchantmentSupports.getRegistry().getHolder(ResourceLocation.parse(eid)).ifPresent((holder) -> {
                int lvl = Mth.clamp(compoundtag.getInt(MAX_LVL), 0, Integer.MAX_VALUE);
                IndexEnchantData stringIntInt = new IndexEnchantData(holder,lvl,compoundtag.getString(XP));
                enchantData.put(holder.get(), stringIntInt);
            });
        }

    }

    public void addEnchant(String eid,int pLvl){
        EnchantmentSupports.getRegistry().getHolder(ResourceLocation.parse(eid)).ifPresent((holder) -> addEnchant(holder,pLvl));
    }
    public void addEnchant(Holder<Enchantment> holder, int pLvl){
        Enchantment lEnchant =holder.get();
        IndexEnchantData lEnchantData ;
        if (enchantData.containsKey(lEnchant)){
            lEnchantData = enchantData.get(lEnchant);
        }else {
            lEnchantData = new IndexEnchantData(holder);
        }
        lEnchantData.addXp(lvlToXp(pLvl));
        lEnchantData.setMaxLvl(pLvl);
        enchantData.put(lEnchant,lEnchantData);
    }



    public void addToEnchantData(ItemStack item){
        ItemEnchantments enchantments = EnchantmentSupports.getItemEnchantments(item);
        if (enchantments == null)return;
        enchantments.keySet().forEach(holder -> addEnchant(holder, enchantments.getLevel(holder)));
    }
    private BigInteger lvlToXp(int lvl){
        //return 1 << (lvl - 1);
        BigInteger base = new BigInteger("2");
        return base.pow(lvl - 1);
    }
    public HashMap<Enchantment, IndexEnchantData> getEnchantData() {
        return enchantData;
    }

    public int getLvl(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getMaxLvl();
    }
    public BigInteger getXp(Enchantment pEnchantment){
        return enchantData.get(pEnchantment).getXp();
    }

    public boolean dimXp(Enchantment pEnchantment,int pLvl){
        BigInteger lXp = getXp(pEnchantment).subtract(lvlToXp(pLvl));
        if (lXp.compareTo(BigInteger.ZERO) < 0){
            return false;
        }
        enchantData.get(pEnchantment).setXp(lXp);
        return true;
    }
}
