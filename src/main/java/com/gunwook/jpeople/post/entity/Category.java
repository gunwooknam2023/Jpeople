package com.gunwook.jpeople.post.entity;

public enum Category {
    FREE_BOARD("자유게시판"),
    NOTIFICATION_BOARD("공지사항"),
    REPORT_BOARD("신고된 게시글");

    private final String categoryName;

    Category(String categoryname){
        this.categoryName = categoryname;
    }

    public String getCategoryName(){
        return categoryName;
    }
}
