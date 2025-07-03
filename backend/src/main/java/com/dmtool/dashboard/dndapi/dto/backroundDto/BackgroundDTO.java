package com.dmtool.dashboard.dndapi.dto.backroundDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BackgroundDTO {
    private String index;
    private String name;
    private Feature feature;
    private List<StartingEquipment> starting_equipment;
    private String url;

    @Setter
    @Getter
    public static class Feature {
        private String name;
        private List<String> desc;

    }

    @Setter
    @Getter
    public static class StartingEquipment {
        private EquipmentReference equipment;
        private int quantity;

    }

    @Setter
    @Getter
    public static class EquipmentReference {
        private String index;
        private String name;
        private String url;

    }

}
