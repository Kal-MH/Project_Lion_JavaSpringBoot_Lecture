package deb.kalmh.mybatis.dto;

/*
id int
title varchar
content varchar
writer varchar
board int
 */
// table 속성 이름과 동일하게 멤버 변수들의 이름을 정하자.
public class PostDto {
    private int id;
    private String title;
    private String content;
    private String writer;
    private int board;

    public PostDto() {}
    public PostDto(int id, String title, String content, String writer, int board) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", board=" + board +
                '}';
    }
}
