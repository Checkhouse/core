package com.checkhouse.core.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class CategoryRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCategoryRequest {

        @NotNull
        UUID categoryId;

        @NotNull
        String name;
    }

}
