package domain.club;

public enum Grade {
    NO_ANSWER, BRONZE, SILVER, GOLD, VIP;

    public static boolean isUnderBronze(Grade grade) {
        return grade == NO_ANSWER || grade == BRONZE;
    }

    public static boolean isAboveGold(Grade grade) {
        return grade == GOLD || grade == VIP;
    }
}
