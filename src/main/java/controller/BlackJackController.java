package controller;

import card.CardDeck;
import cardGame.BlackJackGame;
import controller.dto.WinningResult;
import dealer.Dealer;
import java.util.List;
import player.Player;
import player.Players;
import view.InputView;
import view.OutputView;

// Todo: 클래식하게 짜기
public class BlackJackController {

    private final InputView inputView;
    private final OutputView outputView;

    public BlackJackController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        CardDeck cardDeck = new CardDeck();
        Dealer dealer = new Dealer(cardDeck.firstCardSettings());

        BlackJackGame blackJackGame = new BlackJackGame(cardDeck, cardDeck.firstCardSettings());
        Players players = new Players(startBetting(inputView.inputPlayerNames(), cardDeck));

        runBlackJackGame(blackJackGame, players, cardDeck, dealer);
        showResult(blackJackGame, players);
    }

    public List<Player> startBetting(List<String> names, CardDeck cardDeck) {
        return names.stream()
                .map(name -> Player.joinGame(name, cardDeck.firstCardSettings(), inputView.inputBettingMoney(name)))
                .toList();
    }

    private void runBlackJackGame(BlackJackGame blackJackGame, Players players, CardDeck cardDeck, Dealer dealer) {
        outputView.printInitCardStatus(players, blackJackGame.getDealerFirstCard());

        for (Player player : players.getPlayers()) {
            playSingleMatch(player, cardDeck);
        }

        dealer.getExtraCard(cardDeck);

        outputView.printExtraCardInfo(dealer);
        outputView.printDealerHand(dealer);
        outputView.printPlayerHand(players);
    }

    private void playSingleMatch(Player player, CardDeck cardDeck) {
        while (isCanPlayPlayer(player)) {
            player.receiveCard(cardDeck.pickCard());
            outputView.printPlayerCardStatus(player);
        }
    }


    private void showResult(BlackJackGame blackJackGame, Players players) {
        List<WinningResult> result = blackJackGame.getPlayersResult(players);
        outputView.printDealerResult(blackJackGame.getDealerResult(players));
        outputView.printPlayersResult(result);
    }

    private boolean isCanPlayPlayer(Player player) {
        return !player.isBust() && inputView.inputPlayerCommand(
                player.getName());
    }
}
