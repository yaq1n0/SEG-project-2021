import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.model.*;

public class CalculationsTest {
    /*
    private final Tarmac tarmac09R = new Tarmac(1, null);
    private final Tarmac tarmac09L = new Tarmac(2, null);

    private final RunwayValues originalValues09R = new RunwayValues(3660, 3660, 3660, 3353);
    private final RunwayValues originalValues27L = new RunwayValues(3660, 3660, 3660, 3660);
    private final RunwayValues originalValues09L = new RunwayValues(3902, 3902, 3902, 3595);
    private final RunwayValues originalValues27R = new RunwayValues(3884, 3962, 3884, 3884);

    private final Runway runway09R = new Runway("09R", tarmac09R, originalValues09R, 307);
    private final Runway runway27L = new Runway("27L", tarmac09R, originalValues27L, 0);
    private final Runway runway09L = new Runway("09L", tarmac09L, originalValues09L, 306);
    private final Runway runway27R = new Runway("27R", tarmac09L, originalValues27R, 0);

    private Obstacle obs1 = new Obstacle("Airbus A321", 45 , 34 , 12);
    private Obstacle obs2 = new Obstacle("Boeing 737", 40 , 36 , 25);
    private Obstacle obs3 = new Obstacle("Airbus A350", 67 , 64 , 15);
    private Obstacle obs4 = new Obstacle("Boeing 777", 64 , 61 , 20);

    @Test
    void test09LScenario1() {
        obs1.setPosition(new Position(-50,0));
        tarmac09L.setObstacle(obs1);
        runway09L.recalculate(300);

        Assertions.assertEquals(3346, runway09L.getCurrentValues().getTORA());
        Assertions.assertEquals(3346, runway09L.getCurrentValues().getTODA());
        Assertions.assertEquals(3346, runway09L.getCurrentValues().getASDA());
        Assertions.assertEquals(2985, runway09L.getCurrentValues().getLDA());
    }

    @Test
    void test27RScenario1() {
        obs1.setPosition(new Position(3646,0));
        tarmac09L.setObstacle(obs1);
        runway27R.recalculate(300);

        Assertions.assertEquals(2986, runway27R.getCurrentValues().getTORA());
        Assertions.assertEquals(2986, runway27R.getCurrentValues().getTODA());
        Assertions.assertEquals(2986, runway27R.getCurrentValues().getASDA());
        Assertions.assertEquals(3346, runway27R.getCurrentValues().getLDA());
    }

    @Test
    void test09RScenario2() {
        obs2.setPosition(new Position(2853,20));
        tarmac09R.setObstacle(obs2);
        runway09R.recalculate(300);

        Assertions.assertEquals(1850, runway09R.getCurrentValues().getTORA());
        Assertions.assertEquals(1850, runway09R.getCurrentValues().getTODA());
        Assertions.assertEquals(1850, runway09R.getCurrentValues().getASDA());
        Assertions.assertEquals(2553, runway09R.getCurrentValues().getLDA());
    }

    @Test
    void test27LScenario2() {
        obs2.setPosition(new Position(500,20));
        tarmac09R.setObstacle(obs2);
        runway27L.recalculate(300);

        Assertions.assertEquals(2860, runway27L.getCurrentValues().getTORA());
        Assertions.assertEquals(2860, runway27L.getCurrentValues().getTODA());
        Assertions.assertEquals(2860, runway27L.getCurrentValues().getASDA());
        Assertions.assertEquals(1850, runway27L.getCurrentValues().getLDA());
    }

    @Test
    void test09RScenario3() {
        obs3.setPosition(new Position(150,60));
        tarmac09R.setObstacle(obs3);
        runway09R.recalculate(300);

        Assertions.assertEquals(2903, runway09R.getCurrentValues().getTORA());
        Assertions.assertEquals(2903, runway09R.getCurrentValues().getTODA());
        Assertions.assertEquals(2903, runway09R.getCurrentValues().getASDA());
        Assertions.assertEquals(2393, runway09R.getCurrentValues().getLDA());
    }

    @Test
    void test27LScenario3() {
        obs3.setPosition(new Position(3203,60));
        tarmac09R.setObstacle(obs3);
        runway27L.recalculate(300);

        Assertions.assertEquals(2393, runway27L.getCurrentValues().getTORA());
        Assertions.assertEquals(2393, runway27L.getCurrentValues().getTODA());
        Assertions.assertEquals(2393, runway27L.getCurrentValues().getASDA());
        Assertions.assertEquals(2903, runway27L.getCurrentValues().getLDA());
    }

    @Test
    void test09LScenario4() {
        obs4.setPosition(new Position(3546,20));
        tarmac09L.setObstacle(obs4);
        runway09L.recalculate(300);

        Assertions.assertEquals(2792, runway09L.getCurrentValues().getTORA());
        Assertions.assertEquals(2792, runway09L.getCurrentValues().getTODA());
        Assertions.assertEquals(2792, runway09L.getCurrentValues().getASDA());
        Assertions.assertEquals(3246, runway09L.getCurrentValues().getLDA());
    }

    @Test
    void test27RScenario4() {
        obs4.setPosition(new Position(50,20));
        tarmac09L.setObstacle(obs4);
        runway27R.recalculate(300);

        Assertions.assertEquals(3534, runway27R.getCurrentValues().getTORA());
        Assertions.assertEquals(3534, runway27R.getCurrentValues().getTODA());
        Assertions.assertEquals(3612, runway27R.getCurrentValues().getASDA());
        Assertions.assertEquals(2774, runway27R.getCurrentValues().getLDA());
    }
    */
}
