import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.model.*;

public class SchemaLoadingTest {

    @Test
    void testAirportFileCorrectFormat() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.openAirport("heathrow.xml");});
    }

    @Test
    void testAirportFileWrongFormat() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.openAirport("airport-brokenIncorrectSchema.xml");});
    }

    @Test
    void testAirportFileNegativeRunwayValue() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.openAirport("airport-brokenNegativeValue.xml");});
    }

    @Test
    void testAirportFileInvalidRunwayNumPerTarmac() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.openAirport("airport-brokenInvalidNumRunways.xml");});
    }

    @Test
    void testAirportFileCorrectFormat2() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.openAirport("gatwick.xml");});
    }

    @Test
    void testAirportFileCorrectFormat3() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.openAirport("manchester.xml");});
    }

    @Test
    void testAirportFileCorrectFormat4() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.openAirport("edinburgh.xml");});
    }

    @Test
    void testObstaclesFileCorrectFormat() {
        Assertions.assertDoesNotThrow(SystemModel::loadObstacles);
    }

}
