package deb.kalmh.jpa.entity;
/*
id int
title varchar
content varchar
writer varchar
board int
 */


import javax.persistence.*;

@Entity
@Table(name = "post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //table을 생성하면서 AI등과 같이, id생성 규칙을 설정
    private Long id; //@Id라는 어노테이션을 통해서 PK임을 알려줌
    private String title;
    private String content;
    private String writer;

    //Board의 PK인 id를 가리키는 foreign key라고 말할 수 있다.
//    @ManyToOne(
//            targetEntity = BoardEntity.class,
//            fetch = FetchType.LAZY
//    )
//    @JoinColumn(name = "board_id")
//    private BoardEntity boardEntity;

    public PostEntity() {}

    public PostEntity(Long id, String title, String content, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

//    public BoardEntity getBoardEntity() {
//        return boardEntity;
//    }
//
//    public void setBoardEntity(BoardEntity boardEntity) {
//        this.boardEntity = boardEntity;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
