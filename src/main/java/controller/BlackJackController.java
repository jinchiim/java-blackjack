package controller;

import cardGame.BlackJackGame;
import cardGame.SingleMatch;
import controller.dto.SinglePlayerResultDto;
import java.util.ArrayList;
import java.util.List;
import player.Player;
import player.dto.SinglePlayerStatusDto;
import view.InputView;
import view.OutputView;

public class BlackJackController {

    private final InputView inputView;
    private final OutputView outputView;

    public BlackJackController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void playGame() {
        BlackJackGame blackJackGame = getBlackJackGame();
        int dealerExtraCardCount = blackJackGame.countDealerExtraCard();

        List<SinglePlayerResultDto> winOrNotResults = runGame(blackJackGame);
        List<Boolean> playersWinningInfo = getPlayersWinningInfo(winOrNotResults);

        outputView.printExtraCardInfo(dealerExtraCardCount);
        outputView.printPariticipantsScore(blackJackGame.getParticipantScore());
        outputView.printDealerResult(countDealerWinning(playersWinningInfo), countDealerFail(playersWinningInfo));
        outputView.printPlayerResult(winOrNotResults);
    }

    private int countDealerFail(List<Boolean> playersWinningInfo) {
        return playersWinningInfo.size() - countDealerWinning(playersWinningInfo);
    }

    private BlackJackGame getBlackJackGame() {
        List<String> nameString = inputView.inputPlayerNames();

        BlackJackGame blackJackGame = new BlackJackGame(nameString);
        outputView.printInitCardStatus(blackJackGame.getBackJackGameStatus());

        return blackJackGame;
    }

    private List<SinglePlayerResultDto> runGame(BlackJackGame blackJackGame) {

        List<SinglePlayerResultDto> gameResults = new ArrayList<>();

        List<SingleMatch> matches = blackJackGame.readySingleMatches();

        for (SingleMatch singleMatch : matches) {
            Player player = singleMatch.getPlayer();
            playMatch(singleMatch, player);

            boolean isPlayerWins = singleMatch.isPlayerWins();
            gameResults.add(new SinglePlayerResultDto(player.getName(), isPlayerWins));
        }
        return gameResults;
    }

    private List<Boolean> getPlayersWinningInfo(List<SinglePlayerResultDto> singlePlayerResultDtos) {
        List<Boolean> playersWinningInfo = new ArrayList<>();

        for (SinglePlayerResultDto singleResult : singlePlayerResultDtos) {
            playersWinningInfo.add(singleResult.isWinner());
        }
        return playersWinningInfo;
    }

    private void playMatch(SingleMatch singleMatch, Player player) {
        while (isCanPlayPlayer(singleMatch, player)) {
            singleMatch.getMoreCard();
            outputView.printCardsStatus(SinglePlayerStatusDto.from(player));
        }
    }

    private boolean isCanPlayPlayer(SingleMatch singleMatch, Player player) {
        return !singleMatch.isCanPlayGamePlayer() && inputView.inputPlayerCommand(player.getName());
    }

    private int countDealerWinning(List<Boolean> playersWinningInfo) {
        return (int) playersWinningInfo.stream()
                .filter(winInfo -> !winInfo)
                .count();
    }
}
