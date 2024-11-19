package com.checkhouse.core.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CategoryRequest {

    @Builder
    public record AddCategoryRequest(String name){}

    @Builder
    public record UpdateCategoryByIdRequest (UUID categoryId, String name){ }

    @Builder
    public record DeleteCategoryRequest (UUID categoryId) {}

    @Builder
    public record GetCategoryByIdRequest (UUID categoryId) {}

}
