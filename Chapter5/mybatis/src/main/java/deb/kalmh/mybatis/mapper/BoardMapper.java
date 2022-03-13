package deb.kalmh.mybatis.mapper;

import deb.kalmh.mybatis.dto.BoardDto;

public interface BoardMapper {
    int createBoard(BoardDto dto);
}
