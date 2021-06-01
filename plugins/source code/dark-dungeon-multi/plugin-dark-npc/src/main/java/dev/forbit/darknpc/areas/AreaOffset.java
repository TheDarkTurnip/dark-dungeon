package dev.forbit.darknpc.areas;

import lombok.Getter;
import lombok.Setter;

public enum AreaOffset {
    AREA_1(-11, -1, 13);

    @Getter @Setter double xOffset;
    @Getter @Setter double yOffset;
    @Getter @Setter double zOffset;
    AreaOffset(double xOffset, double yOffset, double zOffset) {
        setXOffset(xOffset);
        setYOffset(yOffset);
        setZOffset(zOffset);
    }

    /**
     * gets the area offset by id
     * @param id (in most cases use id+1, since this enum starts counting at 1).
     * @return
     */
    public static AreaOffset getOffset(int id) {
        assert(id > 0);
        try {
            return AreaOffset.valueOf("AREA_" + id);
        } catch (Exception e) {
            throw new ArrayIndexOutOfBoundsException("Could not find Area Offset for AREA_"+id);
        }
    }
}
