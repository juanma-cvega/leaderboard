package com.king.bean;

import junit.framework.Assert;

import org.junit.Test;

public class PlayerScoresTest {

	private static final int PLAYER_ID = 10;
	private static final int LEVEL_1 = 4;
	private static final int LEVEL_2 = 6;
	private static final int LEVEL_3 = 8;
	private static final int SCORE_1 = 1000;
	private static final int SCORE_2 = 1001;
	private static final int HIGHEST_SCORE = 10000;
	
	private PlayerScores playerScores;
	
	@Test
	public void testAddScores(){
		playerScores = new PlayerScores(PLAYER_ID);
		
		playerScores.addScore(LEVEL_1, SCORE_1);
		playerScores.addScore(LEVEL_1, HIGHEST_SCORE);
		playerScores.addScore(LEVEL_1, SCORE_2);
		
		playerScores.addScore(LEVEL_3, SCORE_1);
		
		playerScores.addScore(LEVEL_2, SCORE_1);
		playerScores.addScore(LEVEL_2, SCORE_2);
		
		
		Assert.assertEquals(HIGHEST_SCORE, playerScores.getLevelHighestScore(LEVEL_1));
		Assert.assertEquals(SCORE_2, playerScores.getLevelHighestScore(LEVEL_2));
		Assert.assertEquals(SCORE_1, playerScores.getLevelHighestScore(LEVEL_3));
	}
}
