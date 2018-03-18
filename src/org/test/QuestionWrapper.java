package org.test;

public class QuestionWrapper {
	
	private String questionId;
	private String tagId;
	private String difficultyLeve;
	
	public QuestionWrapper(String questionId, String tagId, String difficultyLevel) {
		this.questionId =questionId;
		this.tagId = tagId;
		this.difficultyLeve = difficultyLevel;
	}
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getDifficultyLeve() {
		return difficultyLeve;
	}
	public void setDifficultyLeve(String difficultyLeve) {
		this.difficultyLeve = difficultyLeve;
	}
}
