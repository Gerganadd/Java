package bg.sofia.uni.fmi.mjt.space.exception;

public class ExceptionMessages {
    public static final String READER_NULL = "Reader can't be null";
    public static final String MISSION_STATUS_NULL = "Mission status can't be null";
    public static final String ROCKET_STATUS_NULL = "Rocket status can't be null";
    public static final String DATE_NULL = "Local date can't be null";
    public static final String STREAM_NULL = "Stream can't be null";

    public static final String ROW_NULL_OR_EMPTY = "Row can't be null or empty";
    public static final String ID_NULL_OR_EMPTY = "Id can't be null or empty";
    public static final String NAME_NULL_OR_EMPTY = "Name can't be null or empty";
    public static final String LOCATION_NULL_OR_EMPTY = "Location can't be null or empty";
    public static final String DATE_NULL_OR_EMPTY = "Date can't be null or empty";
    public static final String MONTH_NULL_OR_EMPTY = "Month can't be null or empty";
    public static final String DETAIL_NULL_OR_EMPTY = "Detail can't be null or empty";
    public static final String PAYLOAD_NULL_OR_EMPTY = "Payload can't be null or empty";
    public static final String ROCKET_STATUS_NULL_OR_EMPTY = "Rocket status can't be null or empty";
    public static final String MISSION_STATUS_NULL_OR_EMPTY = "Mission status can't be null or empty";

    public static final String PROBLEM_WITH_FILE_PARSING = "A problem occurred while parsing the file";
    public static final String PROBLEM_WITH_ENCRYPTING = "A problem occurred while encrypting from stream";
    public static final String PROBLEM_WITH_DECRYPTING = "A problem occurred while decrypting the stream";

    public static final String UNKNOWN_MONTH = "Unknown month";
    public static final String UNKNOWN_ROCKET_STATUS = "Unknown rocket status";
    public static final String UNKNOWN_MISSION_STATUS = "Unknown mission status";

    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String INVALID_DETAIL_FORMAT = "Invalid detail format";
    public static final String INVALID_MISSION_FORMAT = "Invalid mission format";
    public static final String INVALID_N = "N can't be less or equals to 0";

    public static final String TIME_MISMATCH = "End date can't be before start date";

    private ExceptionMessages() {
        // don't want to make instances of this class
    }
}
