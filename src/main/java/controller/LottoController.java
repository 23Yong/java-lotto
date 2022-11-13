package controller;

import camp.nextstep.edu.missionutils.Randoms;
import constants.LottoConstants;
import lotto.Lotto;
import lotto.Wins;
import utils.InputUtils;
import user.PurchaseAmount;
import utils.OutputUtils;
import utils.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LottoController {

    private PurchaseAmount purchaseAmount;
    private List<Lotto> lottos;
    private List<Integer> winNumbers;
    private int bonusNumber;

    public LottoController() {
        lottos = new ArrayList<>();
        winNumbers = new ArrayList<>();
    }

    private void generateLotteryNumbers() {
        List<Integer> lotteryNumbers = Randoms.pickUniqueNumbersInRange(
                LottoConstants.MIN_LOTTO_VALUE,
                LottoConstants.MAX_LOTTO_VALUE,
                LottoConstants.LOTTO_NUM);

        lottos.add(new Lotto(lotteryNumbers));
    }

    private void getLottoPurchaseAmountFromUser() {
        String userInput = InputUtils.getMoneyFromUser();

        Validator.checkLottoPurchaseAmountIsValid(userInput);

        purchaseAmount = new PurchaseAmount(Integer.parseInt(userInput));
    }

    private void printPurchasedLottoNumbers() {
        OutputUtils.printLottoPurchaseCount(purchaseAmount.getPurchaseCount());
        OutputUtils.printPurchasedLottoNumbers(lottos);
    }

    private void getWinNumbersFromUser() {
        String userInput = InputUtils.getWinNumbersFromUser();
        Validator.checkWinsNumberIsValid(userInput);

        winNumbers = Arrays.stream(userInput.split(LottoConstants.SEPARATOR))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    private void getBonusNumberFromUser() {
        String userInput = InputUtils.getBonusNumberFromUser();
        Validator.checkBonusNumberIsValid(userInput, winNumbers);

        bonusNumber = Integer.parseInt(userInput);
    }

    private void calculateWinningRate() {
        OutputUtils.printCalculateWins();
        for (Lotto lotto : lottos) {
            lotto.countMatchingNumber(winNumbers, bonusNumber);
        }
        OutputUtils.printWinningStats(Wins.getWinningStats());
    }

    public void start() {
        getLottoPurchaseAmountFromUser();

        int purchaseCount = purchaseAmount.getPurchaseCount();
        while (purchaseCount > 0) {
            generateLotteryNumbers();
            purchaseCount--;
        }

        printPurchasedLottoNumbers();

        getWinNumbersFromUser();
        getBonusNumberFromUser();

        calculateWinningRate();
    }
}
