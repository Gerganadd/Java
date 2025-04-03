package bg.sofia.uni.fmi.mjt.space.parsers;

import bg.sofia.uni.fmi.mjt.space.mission.Detail;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissionParserTest {
    @Test
    void testParseMissionFromWithNullReader() {
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMissionsFrom(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionFromIsCorrect() { // to-do add more info
        StringReader reader = new StringReader("\n0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success");
        Set<Mission> actual = MissionParser.parseMissionsFrom(reader);

        reader.close();

        assertEquals(1, actual.size());

        Mission actualMission = actual.iterator().next();
        assertEquals("0", actualMission.id());
        assertEquals("SpaceX", actualMission.company());
        assertEquals("LC-39A, Kennedy Space Center, Florida, USA", actualMission.location());
        assertEquals(LocalDate.of(2020, Month.AUGUST, 7), actualMission.date());
        assertEquals(new Detail("Falcon 9 Block 5", "Starlink V1 L9 & BlackSky"), actualMission.detail());
        assertEquals(RocketStatus.STATUS_ACTIVE, actualMission.rocketStatus());
        assertEquals(Optional.of(50.0), actualMission.cost());
        assertEquals(MissionStatus.SUCCESS, actualMission.missionStatus());
    }

    @Test
    void testParseMissionWithNullRow() {
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithEmptyRow() {
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(""),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission("    "),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutId() {
        String missionInfo = " ,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String missionInfo2 = "  ,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutCompanyName() {
        String missionInfo = "66,,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String missionInfo2 = "66,  ,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutLocation() {
        String missionInfo = "66,ULA,,\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,  ,\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutDate() {
        String missionInfo = "66,ULA,\"USA\",,Atlas | OFT,StatusActive,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,\"USA\",   ,Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutDetail() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\", ,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutRocketStatus() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,   ,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutMissionStatus() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",  ";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithoutCost() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,,Success";

        Mission actualMission = MissionParser.parseMission(missionInfo);

        assertEquals("66", actualMission.id());
        assertEquals("ULA", actualMission.company());
        assertEquals("USA", actualMission.location());
        assertEquals(LocalDate.of(2019, Month.DECEMBER, 20), actualMission.date());
        assertEquals(new Detail("Atlas", "OFT"), actualMission.detail());
        assertEquals(RocketStatus.STATUS_ACTIVE, actualMission.rocketStatus());
        assertEquals(Optional.empty(), actualMission.cost());
        assertEquals(MissionStatus.SUCCESS, actualMission.missionStatus());
    }

    @Test
    void testParseMissionWithInvalidMonthDateFormat() { //Date correct format : Fri Dec 20, 2019
        String invalidMonth = "66,ULA,\"USA\",\"Fri dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth2 = "66,ULA,\"USA\",\"Fri december 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth3 = "66,ULA,\"USA\",\"Fri December 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth4 = "66,ULA,\"USA\",\"Fri 12 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth2),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth3),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth4),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithInvalidDateFormat() { //Date correct format : Fri Dec 20, 2019
        String invalidMonth = "66,ULA,\"USA\",\"Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth2 = "66,ULA,\"USA\",\"Fri Aug 20| 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth3 = "66,ULA,\"USA\",\"Fri Sep 20; 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";
        String invalidMonth4 = "66,ULA,\"USA\",\"Fri 20 12, 2019\",Atlas | OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth2),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth3),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(invalidMonth4),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithInvalidDetailFormat() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas OFT,StatusActive,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas , OFT,StatusActive,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithInvalidRocketStatus() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,Status Active,\"3.0 \",Success";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,Status active,\"3.0 \",Success";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseMissionWithInvalidMissionStatus() {
        String missionInfo = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",success";
        String missionInfo2 = "66,ULA,\"USA\",\"Fri Dec 20, 2019\",Atlas | OFT,StatusActive,\"3.0 \",something";

        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> MissionParser.parseMission(missionInfo2),
                "IllegalArgumentException was expected but wasn't thrown");
    }

}
