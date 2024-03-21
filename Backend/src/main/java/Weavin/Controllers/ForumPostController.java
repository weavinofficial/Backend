package Weavin.Controllers;
import Weavin.Entities.ForumPost;
import Weavin.Entities.User;
import Weavin.Repositories.ForumPostRepository;
import Weavin.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class ForumPostController {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    // GET request to get all forum posts that are not banned
    @GetMapping("/forumposts")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<ForumPost> getForumPosts() {
        return this.forumPostRepository.findByReportStatusFalse();
    }

    // GET request to get specific forum post by id
    @GetMapping("/forumposts/{forumPostId}")
    @ResponseStatus(HttpStatus.OK)
    public ForumPost getForumPostById(@PathVariable("forumPostId") Integer id) {
        Optional<ForumPost> forumPostOptional = this.forumPostRepository.findById(id);
        if (forumPostOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum post not found.");
        }
        ForumPost forumPost = forumPostOptional.get();
        if (forumPost.isReportStatus()) {
            return generateReportedForumPost();
        } else {
            forumPost.setViews(forumPost.getViews() + 1);
            this.forumPostRepository.save(forumPost);
            return forumPost;
        }
    }

    // GET request to get all forum posts by a specific user
    @GetMapping("/users/{userId}/forumposts")
    @ResponseStatus(HttpStatus.OK)
    public List<ForumPost> getUserForumPosts(@PathVariable Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        User user = userOptional.get();
        return this.forumPostRepository.findByUserAndReportStatusFalse(user);
    }

    // POST request to post a new forum post by specified user
    @PostMapping("/users/{userId}/forumposts")
    @ResponseStatus(HttpStatus.CREATED)
    public void createForumPost(@PathVariable int userId, @RequestBody ForumPost forumPost) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        User user = userOptional.get();
        forumPost.setUser(user);
        forumPost.setCreatedAt(new Date());
        forumPost.setUpdatedAt(new Date());
        this.forumPostRepository.save(forumPost);
    }

    // PUT request to update forum post content
    @PutMapping("/forumposts/{id}")
    public void updateForumPost(@PathVariable int id, @RequestBody ForumPost forumPost) {
        Optional<ForumPost> existingForumPost = forumPostRepository.findById(id);
        if (existingForumPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum post not found.");
        }
        ForumPost updatedForumPost = existingForumPost.get();
        updatedForumPost.setTitle(forumPost.getTitle());
        updatedForumPost.setUpdated(true);
        updatedForumPost.setBody(forumPost.getBody());
        updatedForumPost.setField(forumPost.getField());
        updatedForumPost.setUpdatedAt(new Date());
        this.forumPostRepository.save(updatedForumPost);
    }

    // PUT request to add likes to a forum post
    @PutMapping("/forumposts/{forumPostId}/likes")
    public void addLikes(@PathVariable int id) {
        Optional<ForumPost> existingForumPost = forumPostRepository.findById(id);
        if (existingForumPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum post not found.");
        }
        ForumPost updatedForumPost = existingForumPost.get();
        updatedForumPost.setLikes(updatedForumPost.getLikes() + 1);
        forumPostRepository.save(updatedForumPost);
    }

    // DELETE request to delete forum post
    @DeleteMapping("/forumposts/{forumPostId}")
    public void deleteForumPost(@PathVariable("forumPostId") int id) {
        forumPostRepository.deleteById(id);
    }

    // PUT request to report a forum post
    @PutMapping("/forumposts/{id}/report")
    @ResponseStatus(HttpStatus.OK)
    public void reportForumPost(@PathVariable int id) {
        Optional<ForumPost> forumPostOptional = this.forumPostRepository.findById(id);
        if (forumPostOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum post not found.");
        }
        ForumPost forumPost = forumPostOptional.get();
        forumPost.setReports(forumPost.getReports() + 1);
        if (forumPost.getReports() > 15) {
            forumPost.setReportStatus(true);
        }
        this.forumPostRepository.save(forumPost);
    }

    // Generate blank forum post with report flag
    private ForumPost generateReportedForumPost() {
        ForumPost reportedForumPost = new ForumPost();
        reportedForumPost.setReportStatus(true);
        return reportedForumPost;
    }
}