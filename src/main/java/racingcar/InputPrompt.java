package racingcar;

import camp.nextstep.edu.missionutils.Console;
import racingcar.factories.CarRecordFactory;

public class InputPrompt {

    private GameRule rule;

    public InputPrompt(GameRule rule) {
        this.rule = rule;
    }

    public CarRecord readCarNames() {
        String rawInput = Console.readLine();
        String[] tokens = rawInput.split(",");
        return CarRecordFactory.createCarRecord(rule, tokens);
    }

    public NumberOfRepetitions readNumberOfRepetitions() {
        return new NumberOfRepetitions(Console.readLine().trim());
    }

    public void close() {
        Console.close();
    }
}
