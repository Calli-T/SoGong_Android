package com.example.sogong.Model;

public class SortInfo {
    private String nickname;
    private int page;
    private String arrangeBy;

    public SortInfo() {}
    public SortInfo(int page, String arrangeBy) {
        this.page = page;
        this.arrangeBy = arrangeBy;
        this.nickname = "";
    }
    public SortInfo(String nickname, String arrangeBy) {
        this.page = 0;
        this.nickname = nickname;
        this.arrangeBy = arrangeBy;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getArrangeBy() {
        return arrangeBy;
    }

    public void setArrangeBy(String arrangeBy) {
        this.arrangeBy = arrangeBy;
    }
}
