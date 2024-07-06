package Weavin.Bootstrap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import Weavin.Entities.Comment;
import Weavin.Entities.ForumPost;
import Weavin.Entities.Tag;
import Weavin.Entities.User;
import Weavin.Enums.Field;
import Weavin.Repositories.CommentRepository;
import Weavin.Repositories.ForumPostRepository;
import Weavin.Repositories.SemesterRepository;
import Weavin.Repositories.TagRepository;
import Weavin.Repositories.UserRepository;

@Profile("dev")
@Component
public class InjectTestData implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public void run(String... args) throws Exception {
        List<User> testUsers = createUserData();
        List<ForumPost> testPosts = createForumPostData(testUsers);
        createTagData(testPosts);
        createCommentData(testUsers, testPosts);
        createSemesterData();
    }

    private List<User> createUserData() {
        User user1 = User.builder()
                .username("user1")
                .email("testEmail@gmail.com")
                .build();
        
        User user2 = User.builder()
                .username("user2")
                .email("testEmail2@gmail.com")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        return users;
    }

    private List<ForumPost> createForumPostData(List<User> users) {
        ForumPost post1 = ForumPost.builder()
                .title("Post 1")
                .body("sample body")
                .user(users.get(0))
                .field(Field.BIZ)
                .build();

        ForumPost post2 = ForumPost.builder()
                .title("Post 2")
                .body("sample body2")
                .user(users.get(1))
                .field(Field.SOC)
                .build();

        forumPostRepository.save(post1);
        forumPostRepository.save(post2);

        List<ForumPost> posts = new ArrayList<ForumPost>();
        posts.add(post1);
        posts.add(post2);

        return posts;
    }

    private void createTagData(List<ForumPost> posts) {
        Tag tag1 = Tag.builder()
                .forumPost(posts.get(0))
                .field(Field.CDE)
                .build();

        Tag tag2 = Tag.builder()
                .forumPost(posts.get(1))
                .field(Field.CHS)
                .build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);
    }

    private void createCommentData(List<User> users, List<ForumPost> posts) {
        Comment comment1 = Comment.builder()
                .body("comment 1")
                .user(users.get(1))
                .forumPost(posts.get(0))
                .build();

        Comment comment2 = Comment.builder()
                .body("comment 2")
                .user(users.get(0))
                .forumPost(posts.get(1))
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    private void createSemesterData() {
        
    }
    
}
