package com.example.junze.kanzhihu.model;

/**
 * Created by junze on 16-05-12.
 */
public class Answer {
    private String title;
    private String time;
    private String summary;
    private String questionId;
    private String answerId;
    private String authorId;
    private String authorName;
    private String authorHash;
    private String avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (title != null ? !title.equals(answer.title) : answer.title != null) return false;
        if (time != null ? !time.equals(answer.time) : answer.time != null) return false;
        if (summary != null ? !summary.equals(answer.summary) : answer.summary != null)
            return false;
        if (questionId != null ? !questionId.equals(answer.questionId) : answer.questionId != null)
            return false;
        if (answerId != null ? !answerId.equals(answer.answerId) : answer.answerId != null)
            return false;
        if (authorId != null ? !authorId.equals(answer.authorId) : answer.authorId != null)
            return false;
        if (authorName != null ? !authorName.equals(answer.authorName) : answer.authorName != null)
            return false;
        if (authorHash != null ? !authorHash.equals(answer.authorHash) : answer.authorHash != null)
            return false;
        if (avatar != null ? !avatar.equals(answer.avatar) : answer.avatar != null) return false;
        return vote != null ? vote.equals(answer.vote) : answer.vote == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (questionId != null ? questionId.hashCode() : 0);
        result = 31 * result + (answerId != null ? answerId.hashCode() : 0);
        result = 31 * result + (authorId != null ? authorId.hashCode() : 0);
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (authorHash != null ? authorHash.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (vote != null ? vote.hashCode() : 0);
        return result;
    }

    private String vote;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAuthorHash() {
        return authorHash;
    }

    public void setAuthorHash(String authorHash) {
        this.authorHash = authorHash;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
