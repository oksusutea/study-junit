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
            MatchListener.class); // (1) ???????????? ?????? mock()???????????? ???????????? MatchListener ??? ???????????? ??????. ??? ??????????????? ?????? ?????? ??????

    }

    @Test
    public void processNotifiesListenerOnMatch() {
        matcher.add(matchingProfile);  // (2)  ???????????? ??????????????? matcher ????????? ??????
        MatchSet set = matchingProfile
            .getMatchSet(criteria); // (3) ????????? ?????? ????????? ???????????? ??????????????? ?????? MatchSet ?????? ??????

        matcher.process(listener, set); // (4) ??? ???????????? MatcherSet ????????? ?????? matcher ????????? ?????? ?????? ??????

        verify(listener).foundMatch(matchingProfile,
            set); // (5) ???????????? ????????? ????????? ?????? ????????? ????????? foundMatch() aptjemrk ghcnfehldjTsmswl ghkrdls.
    }

    @Test
    public void gathersMatchingProfiles() {
        Set<String> processedSets =
            Collections.synchronizedSet(
                new HashSet<>()); // (1) ???????????? ???????????? MatchSet ???????????? ???????????? ID ????????? Set????????? ??????
        BiConsumer<MatchListener, MatchSet> processFunction =
            (listener, set) -> { // (2) processFunction ?????? ??????
                processedSets
                    .add(set.getProfileId()); // (3) ???????????? ?????? ??? ???????????? MatchSet ????????? ???????????? Id??? ??????
            };
        List<MatchSet> matchSets = createMatchSets(100); // (4) ????????? ???????????? ???????????? ???????????? MatchSet ?????? ??????

        matcher.findMatchingProfiles( // (5) ????????? ????????? ?????? ?????? ????????? ??????, process Function ????????? ?????????
            criteria, listener, matchSets, processFunction);

        while (!matcher.getExecutor().isTerminated()) // (6) ?????? ???????????? ????????? ???????????? ?????? ??????
        {
            ;
        }
        assertThat(processedSets, equalTo(matchSets.stream()
            .map(MatchSet::getProfileId).collect(Collectors
                .toSet()))); // (7) processedSets ???????????? ??????????????? ????????? ?????? MatchSet ????????? ???????????? ??????????????? ??????
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
