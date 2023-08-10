package BusinessLogic;

public class RankingSystem {
    private static final int[] ranksSeparator = new int[]{20, 40, 60, 80, 100};

    public static int countNewScore(int currScore, int percentage) {
        if (currScore >= 800 && currScore < 900) {
            return currScore + percentage - 92;
        }
        if (currScore >= 900) {
            return currScore + percentage - 95;
        }
        return currScore + percentage - currScore / 10;
    }

    public static int countRank(int currScore) {
        for (int i = 0; i < ranksSeparator.length; i++) {
            if (currScore < ranksSeparator[i] * 10) {
                return i;
            }
        }
        return ranksSeparator.length;
    }
}
