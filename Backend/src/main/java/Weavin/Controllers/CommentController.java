package Weavin.Controllers;

import Weavin.Entities.Comment;
import Weavin.Entities.ForumPost;
import Weavin.Entities.MarketPost;
import Weavin.Entities.User;
import Weavin.Enums.Presence;
import Weavin.Enums.ReportStatus;
import Weavin.Repositories.CommentRepository;
import Weavin.Repositories.ForumPostRepository;
import Weavin.Repositories.MarketPostRepository;
import Weavin.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseStatus(HttpStatus.CREATED)
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MarketPostRepository marketPostRepository;
    @Autowired
    private ForumPostRepository forumPostRepository;

    @GetMapping("/marketPost/{marketPostId}/comments/{commentid}")
    @ResponseStatus(HttpStatus.OK)
    public Comment findMarketPostCommentById(@PathVariable Integer id) {
        Optional<Comment> commentOptional = this.commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }
        Comment commentToBeFound = commentOptional.get();
        return commentToBeFound;
    }

    @GetMapping("/marketPost/{marketPostId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllCommentsByMarketPostId(@PathVariable Integer id) {
        Optional<MarketPost> marketPostOptional = this.marketPostRepository.findById(id);
        if (marketPostOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marketpost not found.");
        }
        MarketPost marketPostToBeFound = marketPostOptional.get();
        return marketPostToBeFound.getCommentList();
    }
    @GetMapping("/forumPost/{forumPostId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllCommentsByForumPostId(@PathVariable Integer id) {
        Optional<ForumPost> forumPostOptional = this.forumPostRepository.findById(id);
        if (forumPostOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum post not found.");
        }
        ForumPost forumPostToBeFound = forumPostOptional.get();
        return forumPostToBeFound.getCommentList();
    }

    @GetMapping("/forumPosts/{forumPostId}/comments/{commentid}")
    @ResponseStatus(HttpStatus.OK)
    public Comment findForumPostCommentById(@PathVariable Integer id) {
        Optional<Comment> commentOptional = this.commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }
        Comment commentToBeFound = commentOptional.get();
        return commentToBeFound;
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    private void createComment(@RequestBody Comment comment) {
        this.commentRepository.save(comment);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Integer id) {
        Optional<Comment> commentOptional = this.commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }
        Comment commentToBeDeleted = commentOptional.get();
        this.commentRepository.delete(commentToBeDeleted);
    }
    @PutMapping("/comments/{commentId}")
    public void updateComment(@PathVariable("commentId") int id, @RequestBody Comment comment) {
        comment.setId(id);
        Optional<Comment> existingComment = commentRepository.findById(comment.getId());
        if (existingComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }
        Comment updatedComment = existingComment.get();
        updatedComment.setUpdatedAt(comment.getUpdatedAt());
        updatedComment.setLikes(comment.getLikes());
        updatedComment.setBody(comment.getBody());
        updatedComment.setReports(comment.getReports());
        commentRepository.save(updatedComment);
    }

    @PutMapping("/commentId")
    public void addLikes(@PathVariable int id) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found.");
        }
        Comment updatedComment = existingComment.get();
        updatedComment.setLikes(updatedComment.getLikes() + 1);
        commentRepository.save(updatedComment);
    }
}

