package dev.forbit.items.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.items.items.types.ItemGroup;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public abstract class DarkItem {
    // the base item for all items.
    @Getter @Setter int customModelData;
    @Getter @Setter ItemGroup itemGroup;
    @Getter @Setter Material material;

    @Override public String toString() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return this.getClass().getName()+gson.toJson(this);
    }
}
