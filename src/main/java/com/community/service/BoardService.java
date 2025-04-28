package com.community.service;

import com.community.dto.board.BoardRequest;
import com.community.dto.board.BoardResponse;
import com.community.model.Board;
import com.community.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardResponse createBoard(BoardRequest boardRequest) {
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
}
