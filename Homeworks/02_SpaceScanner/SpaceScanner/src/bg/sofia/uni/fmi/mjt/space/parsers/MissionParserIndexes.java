package bg.sofia.uni.fmi.mjt.space.parsers;

public class MissionParserIndexes {
    public static final int ID = 0;
    public static final int COMPANY_NAME = 1;
    public static final int LOCATION = 2;
    public static final int DATE = 3;
    public static final int DETAIL = 4;
    public static final int ROCKET_STATUS = 5;
    public static final int COST = 6;
    public static final int MISSION_STATUS = 7;

    // for LocalDate
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int YEAR = 3;

    // for Detail
    public static final int ROCKET_NAME = 0;
    public static final int ROCKET_PAYLOAD = 1;

    private MissionParserIndexes() {
        // don't want to make instances of this class
    }
}
