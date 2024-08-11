package anmao.mc.index.datagen;

import anmao.mc.index.register.BlockRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DataGen_Recipe extends RecipeProvider implements IConditionBuilder {
    public DataGen_Recipe(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }


    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.INDEX.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.SNOW_BLOCK)
                .define('B', Items.CRYING_OBSIDIAN)
                .define('C', Items.DRAGON_EGG)
                .unlockedBy(getHasName(Items.DRAGON_EGG),has(Items.DRAGON_EGG))
                .save(pRecipeOutput);
    }
}
