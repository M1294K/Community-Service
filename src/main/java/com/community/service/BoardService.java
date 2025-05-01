package com.community.service;

import com.community.dto.board.BoardRequest;
import com.community.dto.board.BoardResponse;
import com.community.model.Board;
import com.community.model.Role;
import com.community.model.User;
import com.community.repository.BoardRepository;
import com.community.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public BoardResponse createBoard(BoardRequest boardRequest, String email) {
        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }

        Board board = new Board();
        board.setName(boardRequest.getName());
        board.setDescription(boardRequest.getDescription());

        Board savedBoard = boardRepository.save(board);
        return new BoardResponse(savedBoard.getId(), savedBoard.getName(), savedBoard.getDescription());
    }

    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(board -> new BoardResponse(board.getId(), board.getName(), board.getDescription()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBoard(Long boardId, BoardRequest boardRequest, String email) {
        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));

        board.setName(boardRequest.getName());
        board.setDescription(boardRequest.getDescription());
        boardRepository.save(board);
    }
    @Transactional
    public void deleteBoard(Long boardId, String email) {
        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));

        boardRepository.delete(board);
    }

}
