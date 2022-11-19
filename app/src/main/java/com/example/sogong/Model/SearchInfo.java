package com.example.sogong.Model;

public class SearchInfo {
    private String searchType;
    private String categories;
    private String keywordType;
    private String keyword;
    private int page;
    private String nickname;

    public SearchInfo() {}
    public SearchInfo(String searchType, String categories, String keywordType, String keyword, int page, String nickname) {
        this.searchType = searchType;
        this.categories = categories;
        this.keywordType = keywordType;
        this.keyword = keyword;
        this.page = page;
        this.nickname = nickname;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public void setKeywordType(String keywordType) {
        this.keywordType = keywordType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
