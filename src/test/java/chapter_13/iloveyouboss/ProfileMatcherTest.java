/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
package chapter_13.iloveyouboss;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.*;
import org.junit.*;

public class ProfileMatcherTest {

    private BooleanQuestion question;
    private Criteria criteria;
    private ProfileMatcher matcher;
    private Profile matchingProfile;
    private Profile nonMatchingProfile;

    @Before
    public void create() {
        question = new BooleanQuestion(1, "");
        criteria = new Criteria();
        criteria.add(new Criterion(matchingAnswer(), Weight.MustMatch));
        matchingProfile = createMatchingProfile("matching");
        nonMatchingProfile = createNonMatchingProfile("nonMatching");
    }

    private Profile createMatchingProfile(String name) {
        Profile profile = new Profile(name);
        profile.add(matchingAnswer());
        return profile;
    }

    private Profile createNonMatchingProfile(String name) {
        Profile profile = new Profile(name);
        profile.add(nonMatchingAnswer());
        return profile;
    }

    @Before
    public void createMatcher() {
        matcher = new ProfileMatcher();
    }

    @Test
    public void collectsMatchSets() {
        matcher.add(matchingProfile);
        matcher.add(nonMatchingProfile);

        List<MatchSet> sets = matcher.collectMatchSets(criteria);

        assertThat(sets.stream()
                .map(set -> set.getProfileId()).collect(Collectors.toSet()),
            equalTo(new HashSet<>
                (Arrays.asList(matchingProfile.getId(), nonMatchingProfile.getId()))));
    }

    private MatchListener listener;

    @Before
    public void createMatchListener() {
        listener = mock(
            MatchListener.class); // (1) 모키토의 정적 mock()메소드를 사용하여 MatchListener 목 인스턴스 생성. 이 인스턴스로 기대 사항 검증

    }

    @Test
    public void processNotifiesListenerOnMatch() {
        matcher.add(matchingProfile);  // (2)  매칭되는 프로파일을 matcher 변수에 추가
        MatchSet set = matchingProfile
            .getMatchSet(criteria); // (3) 주어진 조건 집합에 매칭되는 프로파일에 대한 MatchSet 객체 요청

        matcher.process(listener, set); // (4) 목 리스너와 MatcherSet 객체를 넘겨 matcher 변수에 매칭 처리 지시

        verify(listener).foundMatch(matchingProfile,
            set); // (5) 모키토를 활용해 목으로 만든 리스너 객체에 foundMatch() aptjemrk ghcnfehldjTsmswl ghkrdls.
    }

    @Test
    public void gathersMatchingProfiles() {
        Set<String> processedSets =
            Collections.synchronizedSet(
                new HashSet<>()); // (1) 리스너가 수신하는 MatchSet 객체들의 프로파일 ID 목록을 Set객체에 생성
        BiConsumer<MatchListener, MatchSet> processFunction =
            (listener, set) -> { // (2) processFunction 함수 정의
                processedSets
                    .add(set.getProfileId()); // (3) 리스너에 대한 각 콜백에서 MatchSet 객체의 프로파일 Id를 추가
            };
        List<MatchSet> matchSets = createMatchSets(100); // (4) 도우미 메서드를 사용하여 테스트용 MatchSet 객체 생성

        matcher.findMatchingProfiles( // (5) 인수로 함수를 갖는 해당 메서드 호출, process Function 구현을 넘기기
            criteria, listener, matchSets, processFunction);

        while (!matcher.getExecutor().isTerminated()) // (6) 모든 스레드의 실행이 완료될때 까지 반복
        {
            ;
        }
        assertThat(processedSets, equalTo(matchSets.stream()
            .map(MatchSet::getProfileId).collect(Collectors
                .toSet()))); // (7) processedSets 컬렉션이 테스트에서 생성된 모든 MatchSet 객체의 아이디와 매칭되는지 검증
    }

    private List<MatchSet> createMatchSets(int count) {
        List<MatchSet> sets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            sets.add(new MatchSet(String.valueOf(i), null, null));
        }
        return sets;
    }

    @Test
    public void processDoesNotNotifyListenerWhenNoMatch() {
        matcher.add(nonMatchingProfile);
        MatchSet set = nonMatchingProfile.getMatchSet(criteria);

        matcher.process(listener, set);

        verify(listener, never()).foundMatch(nonMatchingProfile, set);
    }

    private Answer matchingAnswer() {
        return new Answer(question, Bool.TRUE);
    }

    private Answer nonMatchingAnswer() {
        return new Answer(question, Bool.FALSE);
    }


}
