package com.king.server;

import com.king.bean.ScoreListEntry;

public interface ScoreServer {

	String getSessionKey(int playerId);

	ScoreListEntry[] getToplist(int level);

	void registerScore(String sessionKey, int level, int score);
}
