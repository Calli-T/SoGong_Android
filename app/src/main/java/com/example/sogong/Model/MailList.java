package com.example.sogong.Model;

import java.util.List;

public class MailList {
    private List<Mail> mailList;
    private int total_page;

    public MailList() {}
    public MailList(List<Mail> mailList, int total_page) {
        this.mailList = mailList;
        this.total_page = total_page;
    }

    public List<Mail> getMailList() {
        return mailList;
    }

    public void setMailList(List<Mail> mailList) {
        this.mailList = mailList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
