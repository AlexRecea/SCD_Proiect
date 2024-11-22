package socialmedia.server.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post create(@RequestBody Post myPost)
    {
        return postService.create(myPost);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.findAllPosts();
    }
}
