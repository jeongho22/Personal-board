package com.example.dy.controller;

import com.example.dy.entity.Board;
import com.example.dy.entity.Comment;
import com.example.dy.serivce.BoardService;
import com.example.dy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;




    @PostMapping("/boardview/{boardId}")
    public String addComment(@ModelAttribute Comment comment,
                             @PathVariable("boardId") Integer boardId,
                             @RequestParam(value="page", defaultValue = "0") int page,
                             @RequestParam(value="searchType", required = false) String searchType,
                             @RequestParam(value="searchKeyword", required = false) String searchKeyword,
                             Principal principal) {  // 수정된 부분
        Board board = boardService.boardView(boardId);  // 해당하는 Board를 가져옴
        comment.setBoard(board);  // Comment가 어떤 Board에 속하는지 설정
        comment.setAuthor(principal.getName());  // 로그인 사용자 이름을 댓글 작성자의 이름으로 설정
        commentService.save(comment);
        String encodedSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        System.out.println("댓글이 작성 되었습니다.");
        return "redirect:/boardview/" + boardId + "?page=" + page + "&searchType=" + searchType + "&searchKeyword=" + encodedSearchKeyword;
    }






    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Integer commentId,
                                @RequestParam(value="page", defaultValue = "0") int page,
                                @RequestParam(value="searchType", defaultValue = "") String searchType,
                                @RequestParam(value="searchKeyword", defaultValue = "") String searchKeyword,
                                Principal principal,
                                Model model) { // 수정된 부분
        Comment comment = commentService.findById(commentId);
        Integer boardId = comment.getBoard().getId();
        commentService.deleteById(commentId, principal.getName());
        model.addAttribute("username", principal.getName());
        String encodedSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        System.out.println("댓글이 삭제 되었습니다.");
        return "redirect:/boardview/" + boardId + "?page=" + page + "&searchType=" + searchType + "&searchKeyword=" + encodedSearchKeyword;
    }










}