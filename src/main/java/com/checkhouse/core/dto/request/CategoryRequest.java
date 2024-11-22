package com.checkhouse.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CategoryRequest {

    @Builder
    public record AddCategoryRequest(@NotNull String name){}

    @Builder
    public record UpdateCategoryByIdRequest (@NotNull UUID categoryId, @NotNull String name){ }

    @Builder
    public record DeleteCategoryRequest (UUID categoryId) {}

    @Builder
    public record GetCategoryByIdRequest (UUID categoryId) {}

}
