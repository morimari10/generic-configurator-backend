package com.se.deserializer;

import com.se.common.Structure;
import com.se.common.deserializer.AbstractConfigParamTableDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PanelStructureDeserializer extends AbstractConfigParamTableDeserializer<PanelStructureHolder> {

    private static final String TABLE_NAME_PATTERN = "PanelStructure";

    private static final String ZONE = "Zone";
    private static final String PRODUCT_TYPE = "ProductType";
    private static final String STATUS = "Status";
    private static final String QUANTITY = "Quantity";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public PanelStructureHolder deserializeFromParams(List<Map<String, String>> params) {
        PanelStructureHolder holder = new PanelStructureHolder();

        List<Structure> structures = new ArrayList<>();
        for (Map<String, String> param : params) {
            Structure structure = new Structure();
            structure.setZone(param.remove(ZONE));
            structure.setProductType(param.remove(PRODUCT_TYPE));
            structure.setStatus(param.remove(STATUS));
            structure.setQuantity(param.remove(QUANTITY));
            structures.add(structure);
        }
        holder.setStructures(structures);

        return holder;
    }
    
}
