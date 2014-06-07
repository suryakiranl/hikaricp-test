package com.ssn.test;

public class Score {
	private long id;
	private float scoreValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(float scoreValue) {
		this.scoreValue = scoreValue;
	}

	@Override
	public String toString() {
		return "Score [id=" + id + ", scoreValue=" + scoreValue + "]";
	}

}
