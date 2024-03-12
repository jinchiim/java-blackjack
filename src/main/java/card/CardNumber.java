package card;

import java.util.List;

public enum CardNumber {

    ACE("A", List.of(1, 11), 0),
    TWO("2", List.of(2), 1),
    THREE("3", List.of(3), 2),
    FOUR("4", List.of(4), 3),
    FIVE("5", List.of(5), 4),
    SIX("6", List.of(6), 5),
    SEVEN("7", List.of(7), 6),
    EIGHT("8", List.of(8), 7),
    NINE("9", List.of(9), 8),
    TEN("10", List.of(10), 9),
    JACK("J", List.of(10), 10),
    QUEEN("Q", List.of(10), 11),
    KING("K", List.of(10), 12);

    final String number;
    final List<Integer> scores;
    final int numberId;

    CardNumber(String number, List<Integer> scores, int numberId) {
        this.number = number;
        this.scores = scores;
        this.numberId = numberId;
    }

    public int getCardNumber(int numberPosition) {
        validateIsOverScoreRange(numberPosition);
        return scores.get(numberPosition);
    }

    private void validateIsOverScoreRange(int numberPosition) {
        if (scores.size() <= numberPosition) {
            throw new IllegalArgumentException("가능한 범위의 점수 값이 아닙니다.");
        }
    }

    public static int calculatePlusAceCardScore() {
        return ACE.getCardNumber(1) - ACE.getCardNumber(0);
    }
}
