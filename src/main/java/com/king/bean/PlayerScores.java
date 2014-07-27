package com.king.bean;

import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerScores {

	private final Map<Integer, LevelScores> scores;
	private final int playerId;
	
	public int getPlayerId() {
		return playerId;
	}

	public PlayerScores(int playerId) {
		this.playerId = playerId;
		this.scores = new ConcurrentHashMap<Integer, LevelScores>();
	}

	public void addScore(int level, int score) {
		LevelScores levelScores = scores.get(level);
		if (levelScores == null){
			levelScores = new LevelScores(level);
			scores.put(level, levelScores);
		}
		levelScores.addScore(score);
	}
	
	public int getLevelHighestScore(int level){
		int highestScore = 0;
		LevelScores levelScores = scores.get(level);
		if (levelScores != null){
			highestScore = scores.get(level).getHighestScore();
		}
		return highestScore;
	}
	
	public SortedSet<Integer> getScores(int level){
		return scores.get(level).getScores();
	}
}
