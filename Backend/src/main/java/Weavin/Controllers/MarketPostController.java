package Weavin.Controllers;

import Weavin.Entities.ForumPost;
import Weavin.Entities.MarketPost;
import Weavin.Entities.User;
import Weavin.Repositories.MarketPostRepository;
import Weavin.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.error.Mark;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class MarketPostController {

    @Autowired
    private MarketPostRepository marketPostRepository;

    @Autowired
    private UserRepository userRepository;

    // GET request to get all market posts
    @GetMapping("/marketposts")
    @ResponseStatus(HttpStatus.OK)
    public List<MarketPost> getAllMarketPosts() {
        return marketPostRepository.findByReportStatusFalse();
    }

    // GET request to get specific market post by id
    @GetMapping("/marketposts/{marketPostId}")
    @ResponseStatus(HttpStatus.OK)
    public MarketPost getMarketPostById(@PathVariable int marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MarketPost not found with id: " + marketPostId));
        if (marketPost.isReportStatus()) {
            return generateReportedMarketPost();
        } else {
            marketPost.setViews(marketPost.getViews() + 1);
            this.marketPostRepository.save(marketPost);
            return marketPost;
        }
    }

    // GET request to get all market posts by a specific user
    @GetMapping("/users/{userId}/marketposts")
    @ResponseStatus(HttpStatus.OK)
    public List<MarketPost> getUserMarketPosts(@PathVariable Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        User user = userOptional.get();
        return this.marketPostRepository.findByUserAndReportStatusFalse(user);
    }

    // POST request to post a new market post by a specified user
    @PostMapping("/users/{userId}/marketposts")
    public void createMarketPost(@PathVariable int userId, @RequestBody MarketPost marketPost) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        User user = userOptional.get();
        marketPost.setUser(user);
        marketPost.setCreatedAt(new Date());
        marketPost.setUpdatedAt(new Date());
        this.marketPostRepository.save(marketPost);
    }

    // PUT request to update market post content
    @PutMapping("marketposts/{marketPostId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMarketPost(@PathVariable int marketPostId, @RequestBody MarketPost marketPost) {
        MarketPost existingMarketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MarketPost not found."));

        existingMarketPost.setProductName(marketPost.getProductName());
        existingMarketPost.setDescription(marketPost.getDescription());
        existingMarketPost.setUpdatedAt(new Date());
        existingMarketPost.setUpdated(true);
        existingMarketPost.setPhoto(marketPost.getPhoto());
        existingMarketPost.setPrice(marketPost.getPrice());
        existingMarketPost.setSold(marketPost.isSold());
        existingMarketPost.setStock(marketPost.getStock());
        this.marketPostRepository.save(existingMarketPost);
    }
    // PUT request to add likes on the market post
    @PutMapping("/marketposts/{marketPostId}/likes")
    public void addLikes(@PathVariable int id) {
        Optional<MarketPost> existingMarketPost = marketPostRepository.findById(id);
        if (existingMarketPost.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Market post not found.");
        }
        MarketPost updatedMarketPost = existingMarketPost.get();
        updatedMarketPost.setLikes(updatedMarketPost.getLikes() + 1);
        marketPostRepository.save(updatedMarketPost);
    }

    // DELETE request to delete market post
    @DeleteMapping("marketposts/{marketPostId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMarketPost(@PathVariable int marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Market post not found."));
        marketPostRepository.delete(marketPost);
    }

    // PUT request to report a market post
    @PutMapping("/marketposts/{id}/report")
    @ResponseStatus(HttpStatus.OK)
    public void reportMarketPost(@PathVariable int id) {
        Optional<MarketPost> marketPostOptional = this.marketPostRepository.findById(id);
        if (marketPostOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Market post not found.");
        }
        MarketPost marketPost = marketPostOptional.get();
        marketPost.setReports(marketPost.getReports() + 1);
        if (marketPost.getReports() > 15) {
            marketPost.setReportStatus(true);
        }
        this.marketPostRepository.save(marketPost);
    }

    // Generate market post with reported flag
    private MarketPost generateReportedMarketPost() {
        MarketPost reportedMarketPost = new MarketPost();
        reportedMarketPost.setReportStatus(true);
        return reportedMarketPost;
    }
}
