package org.u2soft.billtasticbackend.dto;

import jakarta.validation.constraints.NotBlank;

/** TODO ekleme / güncelleme isteği */
public record TodoRequest(
        @NotBlank String title,
        Boolean      completed,
        String       collection,
        String       description
) {}
