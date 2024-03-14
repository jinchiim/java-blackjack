package cardGame;

import card.Card;
import card.CardDeck;
import controller.dto.WinningResult;
import dealer.Dealer;
import dealer.dto.DealerWinningResult;
import java.util.List;
import java.util.stream.Collectors;
import player.Name;
import player.Player;
import player.Players;
import player.dto.CardsStatus;

public class BlackJackGame {

    private static final String DEALER_NAME = "딜러";

    private final Dealer dealer;
    private final CardDeck cardDeck;

    public BlackJackGame(CardDeck cardDeck, List<Card> cards) {
        this.cardDeck = cardDeck;
        this.dealer = new Dealer(cards);
    }

    public Players initGamePlayer(List<String> names) {
        List<Player> players = names.stream()
                .map(name -> Player.joinGame(name, cardDeck.firstCardSettings()))
                .toList();

        return new Players(players);
    }

    public Card getDealerFirstCard() {
        return dealer.getCards().getFirstCard();
    }

    public CardsStatus playDealerTurn() {
        dealer.getExtraCard(cardDeck);
        return new CardsStatus(new Name(DEALER_NAME), dealer.getCards());
    }

    public List<WinningResult> getPlayersResult(Players players) {
        return players.getPlayers().stream()
                .map(player -> new WinningResult(player.getName(), isPlayerWinner(player)))
                .collect(Collectors.toList());
    }

    public DealerWinningResult getDealerResult(Players players) {
        int winningCount = (int) players.getPlayers().stream()
                .filter(this::isDealerWinner)
                .count();

        return new DealerWinningResult(winningCount, players.getSize() - winningCount);
    }

    private boolean isDealerWinner(Player player) {
        if (isPush(player)) {
            return true;
        }
        if (isOnlyPlayerBust(player)) {
            return true;
        }
        return dealer.isWinner(player);
    }

    private boolean isPlayerWinner(Player player) {
        if (isPush(player)) {
            return true;
        }
        if (isOnlyDealerBust(player)) {
            return true;
        }
        return player.isWinner(dealer);
    }

    private boolean isPush(Player player) {
        if (dealer.isBust() && player.isBust()) {
            return false;
        }
        return player.getCardScore() == dealer.getCardScore();
    }

    private boolean isOnlyDealerBust(Player player) {
        return dealer.isBust() && !player.isBust();
    }

    private boolean isOnlyPlayerBust(Player player) {
        return !dealer.isBust() && player.isBust();
    }
}
