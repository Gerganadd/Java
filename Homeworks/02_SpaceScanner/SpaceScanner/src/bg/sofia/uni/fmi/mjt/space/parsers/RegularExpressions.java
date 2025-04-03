package bg.sofia.uni.fmi.mjt.space.parsers;

public class RegularExpressions {
    public static final String MATCH_COMMA = ",";
    public static final String MATCH_SPACE = " ";
    public static final String MATCH_SPACE_OR_COMMA_SPACE = "\\s|,\\s";
    public static final String MATCH_VERTICAL_BAR = " *\\| *";
    public static final String MATCH_QUOTATION_MARK = "\"";
    public static final String MATCH_ALL_COMPONENTS = "(\"[^\"]+\"|[^,]+)";

    private RegularExpressions() {
        // don't want to make instances of this class
    }
}
