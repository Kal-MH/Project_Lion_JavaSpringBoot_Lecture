package deb.kalmh.jpa;

//엔티티와는 대비되는 데이터를 저장하기 위한 순수 자바 객체
//요청자와 컨트롤러와 데이터를 주고받고, Dao까지 사용하게 되는 객체
//엔티티는 데이터 표현만을 나타내는 것이기도 하고,
//내부에 객체가 실제로 존재하기 때문에 데이터만 전송되지 않고 추가적인 정보가
// 많이 전달되어야 한다. -> 단점
// 단순한 crud 연산에서 엔티티를 dto를 있는 그대로 사용한는 것은 좋은 선택이 아님.
public class PostDto {
    private int id;
    private String title;
    private String content;
    private String writer;
//    private int boardId;

    public PostDto() {}
    public PostDto(int id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
//        this.boardId = boardId;
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

//    public int getBoardId() {
//        return boardId;
//    }
//
//    public void setBoardId(int boardId) {
//        this.boardId = boardId;
//    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
