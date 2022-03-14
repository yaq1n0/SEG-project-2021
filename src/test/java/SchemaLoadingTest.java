import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.model.*;

public class SchemaLoadingTest {

    @Test
    void testAirportFileCorrectFormat() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("heathrow-correct.xml");});
    }

    @Test
    void testAirportFileWrongFormat() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("heathrow-broken.xml");});
    }

    @Test
    void testAirportFileNegativeRunwayValue() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("airport-brokenNegativeValue.xml");});
    }

    @Test
    void testAirportFileInvalidRunwayNumPerTarmac() {
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("airport-brokenInvalidNumRunways.xml");});
    }

    @Test
    void testAirportFileCorrectFormat2() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("gatwick.xml");});
    }

    @Test
    void testAirportFileCorrectFormat3() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("manchester.xml");});
    }

    @Test
    void testAirportFileCorrectFormat4() {
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("edinburgh.xml");});
    }

    @Test
    void testObstaclesFileCorrectFormat() {
        Assertions.assertDoesNotThrow(SystemModel::loadObstacles);
    }

}
