import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.model.*;

public class SchemaLoadingTest {

    @Test
    void testAirportFileCorrectFormat() {
        SystemModel.loadSchemas();
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("heathrow-correct.xml");});
    }

    @Test
    void testAirportFileWrongFormat() {
        SystemModel.loadSchemas();
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("heathrow-broken.xml");});
    }

    @Test
    void testAirportFileNegativeRunwayValue() {
        SystemModel.loadSchemas();
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("airport-brokenNegativeValue.xml");});
    }

    @Test
    void testAirportFileInvalidRunwayNumPerTarmac() {
        SystemModel.loadSchemas();
        Assertions.assertThrows(Exception.class, () -> {SystemModel.loadAirport("airport-brokenInvalidNumRunways.xml");});
    }

    @Test
    void testAirportFileCorrectFormat2() {
        SystemModel.loadSchemas();
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("gatwick.xml");});
    }

    @Test
    void testAirportFileCorrectFormat3() {
        SystemModel.loadSchemas();
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("manchester.xml");});
    }

    @Test
    void testAirportFileCorrectFormat4() {
        SystemModel.loadSchemas();
        Assertions.assertDoesNotThrow(() -> {SystemModel.loadAirport("edinburgh.xml");});
    }

    @Test
    void testObstaclesFileCorrectFormat() {
        SystemModel.loadSchemas();
        Assertions.assertDoesNotThrow(SystemModel::loadObstacles);
    }

}
