package com.king.bean;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class LevelScores {

	private final int level;
	private final SortedSet<Integer> scores;
	
	public LevelScores(int level) {
		this.level = level;
		this.scores = new ConcurrentSkipListSet<Integer>();
	}
	
	public LevelScores(int level, ConcurrentSkipListSet<Integer> scores) {
		this.level = level;
		this.scores = scores;
	}
	
	public void addScore(int score){
		scores.add(score);
	}
	
	public int getHighestScore(){
		int highestScore = 0;
		if (!scores.isEmpty()){
			highestScore = scores.last();
		}
		return highestScore;
	}

	public int getLevel() {
		return level;
	}
	
	public SortedSet<Integer> getScores(){
		return new TreeSet<Integer>(scores);
	}
}
