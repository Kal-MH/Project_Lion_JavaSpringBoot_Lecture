package deb.kalmh.jpa;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class BoardController {

    @PostMapping
    public BoardDto createBoard(@RequestBody BoardDto boardDto) {
        return new BoardDto(boardDto.getName());
    }
}
