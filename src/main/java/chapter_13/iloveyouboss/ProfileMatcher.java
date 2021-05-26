/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
 ***/
/*
관련있는 모든 프로파일을 수집한다.
클라이언트에서 주어진 조건 집합에서 ProfileMatcher 인스턴스는 프로파일을 순회하여 조건에 매칭되는 결과를 MatchSet 인스턴스와 함께 반환한다.
 */
package chapter_13.iloveyouboss;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.stream.*;

public class ProfileMatcher {

    private Map<String, Profile> profiles = new HashMap<>();
    private static final int DEFAULT_POOL_SIZE = 4;

    public void add(Profile profile) {
        profiles.put(profile.getId(), profile);
    }

    private ExecutorService executor =
        Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);

    ExecutorService getExecutor() {
        return executor;
    }

    public void findMatchingProfiles(
        Criteria criteria,
        MatchListener listener,
        List<MatchSet> matchSets,
        BiConsumer<MatchListener, MatchSet> processFunction) {
        for (MatchSet set : matchSets) {
            Runnable runnable = () -> processFunction.accept(listener, set);
            executor.execute(runnable);
        }
        executor.shutdown();
    }

    public void findMatchingProfiles(
        Criteria criteria, MatchListener listener) {
        findMatchingProfiles(
            criteria, listener, collectMatchSets(criteria), this::process);
    }

    public void process(MatchListener listener, MatchSet set) {
        if (set.matches()) {
            listener.foundMatch(profiles.get(set.getProfileId()), set);
        }
    }

    List<MatchSet> collectMatchSets(Criteria criteria) {
        List<MatchSet> matchSets = profiles.values().stream()
            .map(profile -> profile.getMatchSet(criteria))
            .collect(Collectors.toList());
        return matchSets;
    }
}
