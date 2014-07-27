package com.king.server.impl;

import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.king.bean.PlayerScores;
import com.king.bean.ScoreListEntry;
import com.king.bean.SessionKey;
import com.king.exception.SessionExpiredException;
import com.king.server.ScoreServer;

public class ScoreServerImplTest {

	private static final String UNKNOWN_KEY_VALUE = "unknownSessionKey";
	private static final String KEY_VALUE = "sessionKey";
	private static final long DEFAULT_EXPIRATION_DATE = 60 * 10 * 1000;
	private static final SessionKey SESSION_KEY = new SessionKey(KEY_VALUE, DEFAULT_EXPIRATION_DATE);
	private static final SessionKey EXPIRED_SESSION_KEY = new SessionKey(KEY_VALUE, -DEFAULT_EXPIRATION_DATE);
	private static final int PLAYER_ID_1 = 4;
	private static final int PLAYER_ID_2 = 6;
	private static final int PLAYER_ID_3 = 8;
	private static final int SCORE_1 = 1000;
	private static final int SCORE_2 = 2000;
	private static final int SCORE_3 = 3000;
	private static final int SCORE_4 = 4000;
	private static final int LEVEL = 3;
	
	private ScoreServer scoreServer;
	private ConcurrentHashMap<Integer, SessionKey> playerSessionKeys = null;
	private ConcurrentHashMap<Integer, PlayerScores> playersScores = null;
	
	@Before
	public void setup(){
		playerSessionKeys = new ConcurrentHashMap<Integer, SessionKey>();
		playersScores = new ConcurrentHashMap<Integer, PlayerScores>();
		scoreServer = new ScoreServerImpl(playerSessionKeys, playersScores);
	}
	
	@Test
	public void testGetSessionKey(){
		String sessionKey = scoreServer.getSessionKey(PLAYER_ID_1);
		
		Assert.assertTrue(playerSessionKeys.containsKey(PLAYER_ID_1));
		Assert.assertEquals(playerSessionKeys.get(PLAYER_ID_1).getKey(), sessionKey);
	}
	
	@Test
	public void testRegisterScore(){
		playerSessionKeys.put(PLAYER_ID_1, SESSION_KEY);
		
		scoreServer.registerScore(KEY_VALUE, LEVEL, SCORE_1);
		scoreServer.registerScore(KEY_VALUE, LEVEL, SCORE_2);
		scoreServer.registerScore(KEY_VALUE, LEVEL, SCORE_3);
		
		Assert.assertEquals(3, playersScores.get(PLAYER_ID_1).getScores(LEVEL).size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterScoreForInvalidUser(){
		scoreServer.registerScore(UNKNOWN_KEY_VALUE, LEVEL, SCORE_1);
	}
	
	@Test(expected = SessionExpiredException.class)
	public void testRegisterScoreThrowsSessionExpiredException(){
		playerSessionKeys.put(PLAYER_ID_1, EXPIRED_SESSION_KEY);
		
		scoreServer.registerScore(KEY_VALUE, LEVEL, SCORE_1);
	}
	
	@Test
	public void testGetTopList(){
		PlayerScores playerScores1 = new PlayerScores(PLAYER_ID_1);
		playerScores1.addScore(LEVEL, SCORE_1);
		playerScores1.addScore(LEVEL, SCORE_2);
		playerScores1.addScore(LEVEL, SCORE_3);
		playersScores.put(PLAYER_ID_1, playerScores1);
		
		PlayerScores playerScores2 = new PlayerScores(PLAYER_ID_2);
		playerScores2.addScore(LEVEL, SCORE_1);
		playerScores2.addScore(LEVEL, SCORE_4);
		playersScores.put(PLAYER_ID_2, playerScores2);
		
		PlayerScores playerScores3 = new PlayerScores(PLAYER_ID_3);
		playerScores3.addScore(LEVEL, SCORE_1);
		playerScores3.addScore(LEVEL, SCORE_2);
		playersScores.put(PLAYER_ID_3, playerScores3);
	
		ScoreListEntry[] toplist = scoreServer.getToplist(LEVEL);
		Assert.assertEquals(3, toplist.length);
		Assert.assertEquals(PLAYER_ID_2, toplist[0].getPlayerId());
		Assert.assertEquals(SCORE_4, toplist[0].getScore());
		Assert.assertEquals(PLAYER_ID_1, toplist[1].getPlayerId());
		Assert.assertEquals(SCORE_3, toplist[1].getScore());
		Assert.assertEquals(PLAYER_ID_3, toplist[2].getPlayerId());
		Assert.assertEquals(SCORE_2, toplist[2].getScore());
	}
	
	@Test
	public void testGetTopListReturnOnlyTop15(){
		for(int playerId = 1;playerId <= 20;playerId++){
			playersScores.put(playerId, new PlayerScores(playerId));
			playersScores.get(playerId).addScore(LEVEL, playerId);
		}
		ScoreListEntry[] toplist = scoreServer.getToplist(LEVEL);
		Assert.assertEquals(15, toplist.length);
	}	
}