package deb.kalmh.jpa.exception;

/*
 * 게시판에 있는 게시글을 조회할 때, 게시글이 존재하지 않을 때의 에러
 */
public class PostNotInBoardException extends BaseException{
    public PostNotInBoardException() {
        super("Post not in board.");
    }
}
