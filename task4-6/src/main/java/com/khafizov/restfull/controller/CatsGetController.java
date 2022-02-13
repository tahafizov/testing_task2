package com.khafizov.restfull.controller;

import com.khafizov.restfull.dto.CatDto;
import com.khafizov.restfull.model.CatEntity;
import com.khafizov.restfull.repository.CatsRepository;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CatsGetController {
    private final CatsRepository catsRepository;
    private final Bucket bucket;

    @GetMapping("/cats")
    public ResponseEntity<List<CatDto>> getCats(@RequestParam(required = false) List<String> attribute,
                                                @RequestParam(required = false) List<String> sort,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        validateRequestParam(attribute, sort, page, size);

        Sort sorting = getSort(attribute, sort);
        Pageable pageable = getPageable(page, size, sorting);

        List<CatDto> catDtoList;
        if (pageable != null) {
            catDtoList = catsRepository.findAll(pageable)
                    .stream()
                    .map(entity -> CatDto.builder()
                        .name(entity.getName().getName())
                        .color(entity.getColor().getName())
                        .tailLength(entity.getTailLength())
                        .whiskersLength(entity.getWhiskersLength())
                        .build())
                .collect(Collectors.toList());
        } else {
            Iterable<CatEntity> catEntityIterable = (sorting != null) ? catsRepository.findAll(sorting) :
                    catsRepository.findAll();
            catDtoList = new ArrayList<>();
            catEntityIterable.forEach(entity -> catDtoList.add(CatDto.builder()
                    .name(entity.getName().getName())
                    .color(entity.getColor().getName())
                    .tailLength(entity.getTailLength())
                    .whiskersLength(entity.getWhiskersLength())
                    .build()));
        }
        return ResponseEntity.ok(catDtoList);
    }

    private void validateRequestParam(List<String> attributes, List<String> sortes, Integer page, Integer size)  {
        try {
            int attributesSize = 0;
            int sortesSize = 0;
            if (attributes != null) {
                Class<?> c = Class.forName("com.khafizov.restfull.dto.CatDto");
                if (Arrays.stream(c.getDeclaredFields())
                        .noneMatch(field -> attributes.stream().anyMatch(a -> a.equalsIgnoreCase(field.getName())))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attribute is incorrect!");
                }
                attributesSize = attributes.size();
            }
            if (sortes != null) { //!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc")
                if (sortes.stream().noneMatch(s -> s.equalsIgnoreCase("asc") ||
                        s.equalsIgnoreCase("desc"))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort is incorrect!");
                }
                sortesSize = sortes.size();
            }
            if (attributes != null && sortes == null || attributes == null && sortes != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attribute and Sort should be together!");
            }
            if (attributesSize != sortesSize) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Count of Attribute and Sort should be equals!");
            }
            if (page != null) {
                if (page < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page is incorrect!");
                }
            }
            if (size != null) {
                if (size < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size is incorrect!");
                }
            }
            if (page != null && size == null || page == null && size != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offset and Size should be together!");
            }
        } catch (ClassNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Class not found!");
        }
    }

    private Sort getSort(List<String> attribute, List<String> sort) {
        Sort sorting = null;
        if (attribute != null) {
            for (int i = 0; i < attribute.size(); i++) {
                String attributeCurrent = attribute.get(i);
                String sortNameCurrent = sort.get(i);
                Sort sortCurrent = (sortNameCurrent.equalsIgnoreCase("desc")) ?
                        Sort.by(attributeCurrent).descending() : Sort.by(attributeCurrent);
                sorting = (i == 0) ? sortCurrent : sorting.and(sortCurrent);
            }
        }
        return sorting;
    }

    private Pageable getPageable(Integer page, Integer size, Sort sorting) {
        Pageable pageable = null;
        if (page != null) {
            pageable = PageRequest.of(page, size);
            if (sorting != null) {
                pageable = PageRequest.of(page, size, sorting);
            }
        }
        return pageable;
    }
}
