package racingcar;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import racingcar.factories.CarFactory;
import racingcar.factories.CarRecordFactory;

public class IOTest {

    GameRule rule;
    InputStream stdIn;
    InputPrompt prompt;

    @BeforeEach
    void init() {
        rule = new GameRule(5, 0, 9);
        InputStream stdIn = System.in;
        prompt = new InputPrompt(rule);
    }

    @AfterEach
    void clean() {
        prompt.close();
    }

    @Test
    void 정상적인_자동차_이름_입력() {
        // given
        String[] names = new String[] {"BMW", "pobi", "woni", "abcde"};
        CarRecord expectedNames = CarRecordFactory.createEmptyCarRecord();
        for (int i = 0; i < names.length; i++) {
            expectedNames.register(CarFactory.car(rule, names[i]));
        }
        String rawInputString = String.join(",", names) + "\n";
        // when
        stdInWillRead(rawInputString);
        CarRecord actualNames = prompt.readCarNames();
        // then
        assertThat(actualNames).isEqualTo(expectedNames);
        restoreStdIn();
   }

    /**
     * 자동차 이름은 대소문자 알파벳만 가능하다고 가정
     */
    @Test
    void 비정상적인_자동차_이름_입력() {
        // given
        String[] names = new String[] {"woni1", "abcdef"};
        for (int i = 0; i < names.length; i++) {
            // when
            System.setIn(new ByteArrayInputStream(names[i].getBytes()));
            // then
            Assertions.assertThatThrownBy(() -> prompt.readCarNames()).isInstanceOf(IllegalArgumentException.class);
            prompt.close();
        }
        restoreStdIn();
    }

   @Test
    void 정상적인_이동_횟수_입력() {
       // given
       String[] inputs = new String[] {"5", "50", "05", "010201", "    3     "};
       int[] numbers = new int[] {5, 50, 5, 10201, 3};
       for (int i = 0; i < inputs.length; i++) {
           // when
           stdInWillRead(inputs[i]);
           NumberOfRepetitions n = prompt.readNumberOfRepetitions();
           // then
           assertThat(n).isEqualTo(new NumberOfRepetitions(numbers[i]));
           prompt.close();
       }
       restoreStdIn();
   }

    @Test
    void 비정상적인_이동_횟수_입력() {
        // given
        String[] inputs = new String[] {"5k", "0", " k  3     ", "1   3"};
        int[] numbers = new int[] {5, 50, 5, 10201, 3};
        for (int i = 0; i < inputs.length; i++) {
            // when
            stdInWillRead(inputs[i]);
            // then
            Assertions.assertThatThrownBy(() -> prompt.readNumberOfRepetitions())
                    .isInstanceOf(IllegalArgumentException.class);
            prompt.close();
        }
        System.setIn(stdIn);
    }

    void stdInWillRead(String input) {
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
        System.setIn(bis);
    }

    void restoreStdIn() {
        System.setIn(stdIn);
    }
}
