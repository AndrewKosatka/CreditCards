package it.myke.CreditCards.object;

import it.myke.CreditCards.utils.PlayerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Banknote {
    @Getter
    @Setter
    private Material material;
    @Getter
    @Setter
    private String name, lore;
    @Getter
    @Setter
    private double value;
    @Getter
    @Setter
    private ItemStack itemstack;
    @Getter
    @Setter
    private String configName;



    // A Banknote constructor.
    public Banknote(String name, String lore, double value, Material material, String configName) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.value = value;
        this.configName = configName;
        this.itemstack = PlayerUtils.getItemStack(name, lore, material);
    }





}
