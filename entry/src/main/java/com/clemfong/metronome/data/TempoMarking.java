package com.clemfong.metronome.data;

public enum TempoMarking {

    LARGHISSIMO(0, 24, "Larghissimo"),
    GRAVE(25, 40, "Grave"),
    Lento(41, 60, "Lento"),
    LARGHETTO(61, 65,"Larghetto"),
    ADAGIO(66,72, "Adagio"),
    ADAGIETTO(73, 76, "Adagietto"),
    ANDANTE(77, 85, "Andante"),
    ANDANTINO(86, 102, "Andantino"),
    ALLEGRETTO(103, 110, "Allegretto"),
    ALLEGRO(111, 156, "Allegro"),
    VIVACE(157, 176, "Vivace"),
    PRESTO(177, 200, "Presto"),
    PRESTISSIMO(201, 300, "Prestissimo");

    private final int min;
    private final int max;
    private final String name;

    TempoMarking(final int min, final int max, final String name) {
        this.min = min;
        this.max = max;
        this.name = name;
    }

    public static String getName(int tempo) {
        for (TempoMarking marking: TempoMarking.values()) {
            if (tempo >= marking.min && tempo <= marking.max ) {
                return marking.name;
            }
        }
        return "";
    }
}