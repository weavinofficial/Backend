package Weavin.Controllers;

import Weavin.Entities.Tag;
import Weavin.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Tag> getAllTags() {
        Iterable<Tag> tags = tagRepository.findAll();
        return tags;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Tag findById(@PathVariable Integer id) {
        Optional<Tag> tagOptional = this.tagRepository.findById((id));
        if (tagOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        Tag tagToBeFound = tagOptional.get();
        return tagToBeFound;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private void createTag(@RequestBody Tag tag) { this.tagRepository.save(tag); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTag(@PathVariable Integer id) {
        Optional<Tag> tagOptional = this.tagRepository.findById((id));
        if (tagOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found.");
        }
        Tag tagToBeDeleted = tagOptional.get();
        this.tagRepository.delete(tagToBeDeleted);
    }
}