package com.king.bean;

import java.util.concurrent.ConcurrentSkipListSet;

import junit.framework.Assert;

import org.junit.Test;

public class LevelScoresTest {

	private static final int LEVEL = 10;
	private static final int SCORE_1 = 1000;
	private static final int SCORE_2 = 1001;
	private static final int HIGHEST_SCORE = 10000;
	private LevelScores levelScores;
	
	@Test
	public void testScores(){
		ConcurrentSkipListSet<Integer> scores = new ConcurrentSkipListSet<Integer>();
		levelScores = new LevelScores(LEVEL, scores);
		
		levelScores.addScore(SCORE_2);
		levelScores.addScore(HIGHEST_SCORE);
		levelScores.addScore(SCORE_1);
		
		Assert.assertEquals(HIGHEST_SCORE, levelScores.getHighestScore());
		Assert.assertEquals(3, scores.size());
	}
}
