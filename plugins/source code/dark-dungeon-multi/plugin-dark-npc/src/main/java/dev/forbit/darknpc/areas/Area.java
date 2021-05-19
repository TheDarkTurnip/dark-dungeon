package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.Setter;

public class Area {

    @Getter @Setter NPCType type;
    @Getter @Setter int level;

    Area(NPCType type, int level) {
        setType(type);
        setLevel(level);
    }

    public String getSchematic() {
        return type.isLevel() ? type.getSchemName()+"_"+level : type.getSchemName();
    }
}
