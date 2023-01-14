package bg.sofia.uni.fmi.mjt.newsfeed.model;

public enum Country {

    UNITED_ARAB_EMIRATES("ае"),
    ARGENTINA("ar"),
    AUSTRIA("at"),
    AUSTRALIA("au"),
    BELGIUM("be"),
    BULGARIA("bg"),
    BRAZIL("br"),
    CANADA("ch"),
    SWITZERLAND("ch"),
    CHINA("cn"),
    COLOMBIA("co"),
    CUBA("cu"),
    CZECHIA("cz"),
    GERMANY("de"),
    EGYPT("eg"),
    FRANCE("fr"),
    UNITED_KINGDOM_OF_GREAT_BRITAIN_AND_NORTHERN_IRELAND("gb"),
    GREECE("gr"),
    HONG_KONG("hg"),
    HUNGARY("hu"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    INDIA("in"),
    ITALY("it"),
    JAPAN("jp"),
    KOREA("kr"),
    LITHUANIA("lt"),
    LATVIA("lv");

    private final String twoLetterCode;
    Country(String twoLetterCode) {
        this.twoLetterCode = twoLetterCode;
    }

    public String getTwoLetterCode() {
        return twoLetterCode;
    }
}
