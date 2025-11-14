package com.innowise.OrderService.dto.request;

import lombok.Data;

@Data
public class OrderPatchRequest {

    // всё ОПЦИОНАЛЬНО, никаких @NotNull/@NotBlank
    private Long userId;
    private String status;
    // можно добавить что-то ещё, если разрешаешь
}

