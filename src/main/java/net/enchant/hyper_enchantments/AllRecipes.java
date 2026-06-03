package net.enchant.hyper_enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, HyperEnchantments.ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, HyperEnchantments.ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PedestalRecipe>> Pedestal_recipe_serializer =
             SERIALIZERS.register("infusion", PedestalRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PedestalRecipe>> Pedestal_recipe =
             RECIPE_TYPES.register("infusion", ()->new RecipeType<PedestalRecipe>() {
                 public String toString() { return "infusion"; } });

}