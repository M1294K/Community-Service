package com.community.controller;

import com.community.dto.board.BoardRequest;
import com.community.dto.board.BoardResponse;
import com.community.service.BoardService;
import com.community.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        BoardResponse response = boardService.createBoard(boardRequest, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @PutMapping("/{boardId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequest boardRequest,
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        boardService.updateBoard(boardId, boardRequest, email);
        return ResponseEntity.ok("게시판이 수정되었습니다.");
    }

    @DeleteMapping("/{boardId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId,
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        boardService.deleteBoard(boardId, email);
        return ResponseEntity.ok("게시판이 삭제되었습니다.");
    }

}
