package com.khafizov.restfull.controller;

import com.khafizov.restfull.dto.CatDto;
import com.khafizov.restfull.enums.CatColor;
import com.khafizov.restfull.model.CatEntity;
import com.khafizov.restfull.model.CatEntityId;
import com.khafizov.restfull.repository.CatsRepository;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class CatsPostController {
    private final CatsRepository catsRepository;
    private final Bucket bucket;

    @PostMapping("/cat")
    public ResponseEntity<?> addCat(@Valid @RequestBody CatDto catDto) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        validateRequest(catDto);

        catsRepository.save(CatEntity.builder()
                .name(CatEntityId.builder().name(catDto.getName()).build())
                .color(getCatColor(catDto.getColor()))
                .tailLength(catDto.getTailLength())
                .whiskersLength(catDto.getWhiskersLength())
                .build());
        return ResponseEntity.ok(null);
    }

    private void validateRequest(CatDto catDto) {
        if (catsRepository.findByName(CatEntityId.builder().name(catDto.getName()).build()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cat with name <" + catDto.getName() + "> is exists!");
        }
        if (Stream.of(CatColor.values()).noneMatch(color -> color.getName().equals(catDto.getColor()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Color is incorrect!");
        }
    }

    private CatColor getCatColor(String colorString) {
        return Stream.of(CatColor.values())
                .filter(c -> c.getName().equals(colorString))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
