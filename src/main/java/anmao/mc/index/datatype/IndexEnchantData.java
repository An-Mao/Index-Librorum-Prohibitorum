package anmao.mc.index.datatype;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;

import java.math.BigInteger;

public class IndexEnchantData {
    private final Holder<Enchantment> enchant;
    private final String eid;
    private int maxLvl = 0;
    private BigInteger xp = new BigInteger("0");

    public IndexEnchantData(Holder<Enchantment> enchant) {
        this.enchant = enchant;
        this.eid = getHolderEnchant().getRegisteredName();
    }
    public IndexEnchantData(Holder<Enchantment> enchant,int maxLvl,BigInteger xp) {
        this(enchant);
        this.maxLvl = maxLvl;
        this.xp = xp;
    }
    public IndexEnchantData(Holder<Enchantment> enchant,int maxLvl,String xp) {
        this(enchant,maxLvl,new BigInteger(xp));
    }
    public Holder<Enchantment> getHolderEnchant() {
        return enchant;
    }
    public Enchantment getEnchant() {
        return getHolderEnchant().get();
    }

    public String getEid() {
        return eid;
    }


    public void setMaxLvl(int newLvl) {
        this.maxLvl = Math.max(this.maxLvl, newLvl);
    }
    public int getMaxLvl() {
        return maxLvl;
    }


    public void setXp(String xp) {
        this.xp = new BigInteger(xp);
    }
    public void setXp(BigInteger xp) {
        this.xp = xp;
    }

    public void addMaxLvl(int na) {
        this.maxLvl += na;
    }
    public void addXp(BigInteger nb) {
        xp = xp.add(nb);
    }
    public BigInteger getXp() {
        return xp;
    }

    public CompoundTag toNBT() {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString(IndexData.ID,getEid());
        compoundtag.putInt(IndexData.MAX_LVL,getMaxLvl());
        compoundtag.putString(IndexData.XP,getXp().toString());
        return compoundtag;
    }

    @Override
    public String toString() {
        return "{eid:"+getEid()+"}{max:" + getMaxLvl() +"}{xp:"+ getXp().toString()+"}";
    }
}
