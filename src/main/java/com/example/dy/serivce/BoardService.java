package com.example.dy.service;

import com.example.dy.entity.Board;
import com.example.dy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Page<Board> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        boards.getContent().forEach(board -> board.getComments().size()); // This will load the comments due to the FetchType.LAZY
        return boards;
    }

    @Transactional
    public Board getBoardById(Integer id) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            board.getComments().size(); // This will load the comments due to the FetchType.LAZY
        }
        return board;
    }

    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board updateBoard(Integer id, Board boardDetails) {
        Board board = boardRepository.findById(id).orElse(null);

        if (board != null) {
            board.setName(boardDetails.getName());
            board.setJob(boardDetails.getJob());
            board.setAge(boardDetails.getAge());
            return boardRepository.save(board);
        }
        return null;
    }

    public void deleteBoard(Integer id) {
        boardRepository.deleteById(id);
    }
}
