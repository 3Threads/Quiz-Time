package BusinessLogic;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

class RankingSystemTest {

    @Test
    void testNewScoreBasic() {
        Assert.assertEquals(155, RankingSystem.countNewScore(150, 20));
        Assert.assertEquals(235, RankingSystem.countNewScore(150, 100));
        Assert.assertEquals(263, RankingSystem.countNewScore(270, 20));
        Assert.assertEquals(100, RankingSystem.countNewScore(0, 100));
        Assert.assertEquals(1002, RankingSystem.countNewScore(1000, 97));
    }

    @Test
    void testNewScoreFromZero() {
        int currScore = 0;
        currScore = RankingSystem.countNewScore(currScore, 60);
        Assert.assertEquals(60, currScore);
        currScore = RankingSystem.countNewScore(currScore, 100);
        Assert.assertEquals(154, currScore);
        currScore = RankingSystem.countNewScore(currScore, 80);
        Assert.assertEquals(219, currScore);
        currScore = RankingSystem.countNewScore(currScore, 22);
        Assert.assertEquals(220, currScore);
        currScore = RankingSystem.countNewScore(currScore, 72);
        Assert.assertEquals(270, currScore);
        currScore = RankingSystem.countNewScore(currScore, 77);
        Assert.assertEquals(320, currScore);
    }

    @Test
    void testCountRank() {
        for (int i = 0; i < 200; i++)
            Assert.assertEquals(0, RankingSystem.countRank(i));
        for (int i = 200; i < 400; i++)
            Assert.assertEquals(1, RankingSystem.countRank(i));
        for (int i = 400; i < 600; i++)
            Assert.assertEquals(2, RankingSystem.countRank(i));
        for (int i = 600; i < 800; i++)
            Assert.assertEquals(3, RankingSystem.countRank(i));
        for (int i = 800; i < 1000; i++)
            Assert.assertEquals(4, RankingSystem.countRank(i));
        for (int i = 1000; i < 1250; i++)
            Assert.assertEquals(5, RankingSystem.countRank(i));
    }
}