package source;

public enum Genre {

    PROSE("Проза"),
    FANTASY("Фэнтези"),
    DETECTIVE("Детектив"),
    HIST("История"),
    CLASSIC("Классика"),
    FICTION("Фантастика");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
