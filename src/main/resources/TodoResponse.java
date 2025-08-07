package org.u2soft.billtasticbackend.dto;

/** Entity → DTO dönüştürme için ekstra ctor eklendi */
public record TodoResponse(
        Long     id,
        String   title,
        Boolean  completed,
        String   collection,
        String   description
) {
    public TodoResponse(Todo t) {
        this(t.getId(), t.getTitle(), t.getCompleted(),
                t.getCollection(), t.getDescription());
    }
}
