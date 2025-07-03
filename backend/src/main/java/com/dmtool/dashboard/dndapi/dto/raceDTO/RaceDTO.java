package com.dmtool.dashboard.dndapi.dto.raceDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RaceDTO {
    private String index;
    private String name;
    private int speed;
    private String alignment;
    private String age;
    private String size;
    private String size_description;
    private String language_desc;
    private String url;

    private List<AbilityBonus> ability_bonuses;
    private List<Proficiency> starting_proficiencies;
    private StartingProficiencyOptions starting_proficiency_options;
    private List<Language> languages;
    private List<Trait> traits;
    private List<Subrace> subraces;

    @Getter @Setter
    public static class AbilityBonus {
        private AbilityScore ability_score;
        private int bonus;
    }

    @Getter @Setter
    public static class AbilityScore {
        private String index;
        private String name;
        private String url;
    }

    @Getter @Setter
    public static class Proficiency {
        private String index;
        private String name;
        private String url;
    }

    @Getter @Setter
    public static class StartingProficiencyOptions {
        private String desc;
        private int choose;
        private String type;
        private From from;

        @Getter @Setter
        public static class From {
            private String option_set_type;
            private List<Option> options;
        }

        @Getter @Setter
        public static class Option {
            private String option_type;
            private Item item;
        }

        @Getter @Setter
        public static class Item {
            private String index;
            private String name;
            private String url;
        }
    }

    @Getter @Setter
    public static class Language {
        private String index;
        private String name;
        private String url;
    }

    @Getter @Setter
    public static class Trait {
        private String index;
        private String name;
        private String url;
    }

    @Getter @Setter
    public static class Subrace {
        private String index;
        private String name;
        private String url;
    }
}
