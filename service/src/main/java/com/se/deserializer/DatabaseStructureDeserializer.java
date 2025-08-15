package com.se.deserializer;

import com.se.common.Structure;
import com.se.common.deserializer.AbstractConfigParamTableDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseStructureDeserializer extends AbstractConfigParamTableDeserializer<DatabaseStructureHolder> {

    private static final String TABLE_NAME_PATTERN = "DatabaseStructure";

    private static final String ZONE = "Zone";
    private static final String PRODUCT_TYPE = "ProductType";
    private static final String STATUS = "Status";
    private static final String QUANTITY = "Quantity";

    @Override
    public String getConfigTableName() {
        return TABLE_NAME_PATTERN;
    }

    @Override
    public DatabaseStructureHolder deserializeFromParams(List<Map<String, String>> params) {
        DatabaseStructureHolder holder = new DatabaseStructureHolder();

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
