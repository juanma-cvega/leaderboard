package com.king.bean;

public class ScoreListEntry implements Comparable<ScoreListEntry>{

	private final int playerId;
	private final int score;
	private final int level;
	private final long timestamp;
	
	public ScoreListEntry(int playerId, int score, int level) {
		this.playerId = playerId;
		this.score = score;
		this.level = level;
		this.timestamp = System.nanoTime();
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + level;
		result = prime * result + playerId;
		result = prime * result + score;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScoreListEntry other = (ScoreListEntry) obj;
		if (level != other.level)
			return false;
		if (playerId != other.playerId)
			return false;
		if (score != other.score)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public int compareTo(ScoreListEntry o) {
		int compare = Integer.compare(this.score, o.getScore());
		if (compare == 0){
			compare = Long.compare(timestamp,o.getTimestamp());
		}
		return compare;
	}
	
}
