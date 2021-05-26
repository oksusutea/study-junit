package chapter_12.iloveyouboss;

import java.util.HashMap;
import java.util.Map;

public class Profile {

    private Map<String, Answer> answers = new HashMap<>();

    private Answer getMatchingProfileAnswer(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public boolean matches(Criteria criteria) {
        boolean matches = false;
        for (Criterion criterion : criteria) {
            if (matches(criterion)) {
                matches = true;
            } else if (criterion.getWeight() == Weight.MustMatch) {
                return false;
            }
        }
        return matches;
    }

    public boolean matches(Criterion criterion) {
        Answer answer = getMatchingProfileAnswer(criterion);
        return criterion.getWeight() == Weight.DontCare || criterion.getAnswer().match(answer);
    }

    public void add(Answer answer) {
        answers.put(answer.getQuestionText(), answer);
    }

    public ProfileMatch match(Criteria criteria) {
        return new ProfileMatch();
    }
}
