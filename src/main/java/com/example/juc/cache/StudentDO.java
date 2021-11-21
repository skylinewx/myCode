package com.example.juc.cache;

/**
 * 学生对象
 *
 * @author wangxing
 */
public class StudentDO {
    private String id;
    private String name;
    private Integer mathScore;
    private Integer languageScore;
    private Integer englishScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMathScore() {
        return mathScore;
    }

    public void setMathScore(Integer mathScore) {
        this.mathScore = mathScore;
    }

    public Integer getLanguageScore() {
        return languageScore;
    }

    public void setLanguageScore(Integer languageScore) {
        this.languageScore = languageScore;
    }

    public Integer getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(Integer englishScore) {
        this.englishScore = englishScore;
    }

    @Override
    public String toString() {
        return "StudentDO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mathScore=" + mathScore +
                ", languageScore=" + languageScore +
                ", englishScore=" + englishScore +
                '}';
    }
}
