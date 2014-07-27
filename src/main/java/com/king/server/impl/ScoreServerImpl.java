package com.king.server.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.king.bean.PlayerScores;
import com.king.bean.ScoreListEntry;
import com.king.bean.SessionKey;
import com.king.exception.SessionExpiredException;
import com.king.server.ScoreServer;

public class ScoreServerImpl implements ScoreServer {

	private final int MAX_TOP_PLAYERS = 15;
	private final long EXPIRATION_TIME = 10 * 60 * 1000;
	private final Map<Integer, SessionKey> playerSessionKeys;
	private final Map<Integer, PlayerScores> playersScores;

	public ScoreServerImpl() {
		playerSessionKeys = new ConcurrentHashMap<Integer, SessionKey>();
		playersScores = new ConcurrentHashMap<Integer, PlayerScores>();
	}

	public ScoreServerImpl(
			ConcurrentHashMap<Integer, SessionKey> playerSessionKeys,
			ConcurrentHashMap<Integer, PlayerScores> playersScores) {
		this.playerSessionKeys = playerSessionKeys;
		this.playersScores = playersScores;
	}

	@Override
	public String getSessionKey(int playerId) {
		String sessionId = UUID.randomUUID().toString();
		playerSessionKeys.put(playerId, new SessionKey(sessionId, EXPIRATION_TIME));
		return sessionId;
	}

	@Override
	public ScoreListEntry[] getToplist(int level) {
		SortedSet<ScoreListEntry> topScores = getSortedHighestScores(level);
		
		return getLimitedHighestScores(topScores, MAX_TOP_PLAYERS);
	}

	private SortedSet<ScoreListEntry> getSortedHighestScores(int level) {
		SortedSet<ScoreListEntry> topScores = new TreeSet<ScoreListEntry>(
				new Comparator<ScoreListEntry>() {

					@Override
					public int compare(ScoreListEntry o1, ScoreListEntry o2) {
						return -o1.compareTo(o2);
					}
				});
		
		for (Map.Entry<Integer, PlayerScores> entry : playersScores.entrySet()) {
			int levelHighestScore = entry.getValue()
					.getLevelHighestScore(level);
			Integer playerId = entry.getKey();
			topScores.add(new ScoreListEntry(playerId, levelHighestScore, level));
		}
		return topScores;
	}
	
	private ScoreListEntry[] getLimitedHighestScores(SortedSet<ScoreListEntry> topScores, int maximum) {
		List<ScoreListEntry> topList = new ArrayList<ScoreListEntry>(topScores);
		int currentTopScoresSize = maximum;
		if (topList.size() < maximum){
			currentTopScoresSize = topList.size();
		}
		return topList.subList(0, currentTopScoresSize).toArray(new ScoreListEntry[currentTopScoresSize]);
	}

	@Override
	public void registerScore(String sessionKey, int level, int score) {
		int playerId = getPlayerId(sessionKey);
		PlayerScores playerScores = playersScores.get(playerId);
		if (playerScores == null) {
			playerScores = new PlayerScores(playerId);
			playersScores.put(playerId, playerScores);
		}
		playerScores.addScore(level, score);
	}

	private int getPlayerId(String sessionKey) {
		for (Entry<Integer, SessionKey> entry : playerSessionKeys.entrySet()) {
			if (entry.getValue().getKey().equals(sessionKey)) {
				checkSessionKeyExpirationDate(entry);
				return entry.getKey();
			}
		}
		throw new IllegalArgumentException("Session id not found");
	}

	private void checkSessionKeyExpirationDate(Entry<Integer, SessionKey> entry) {
		if (entry.getValue().getExpirationDay().getTime() < System.currentTimeMillis()){
			throw new SessionExpiredException();
		}
	}
}
