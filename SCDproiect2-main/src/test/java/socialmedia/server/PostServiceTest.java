package socialmedia.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import socialmedia.server.post.Post;
import socialmedia.server.post.PostRepository;
import socialmedia.server.post.PostService;
import socialmedia.server.post.Status;
import socialmedia.server.user.User;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService; // this is the service under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAllEmployees(){
        // setup test
        User myUser = new User(1, "name", "email");
        Post p1 = new Post(1, "title", "post content", new Date(1), Status.PUBLISHED, myUser);
        Post p2 = new Post(2, "title2", "other content", new Date(2), Status.PENDING, myUser);

        List<Post> postssMock = List.of(p1, p2);
        when(postRepository.findAll()).thenReturn(postssMock);

        // call method under test
        List<Post> posts = postService.findAllPosts();

        // check what was called by the method and check the result
        verify(postRepository, times(1)).findAll();
        assertEquals(2, posts.size());
        assertEquals(p1, posts.get(0));
        assertEquals(p2, posts.get(1));
    }
}
