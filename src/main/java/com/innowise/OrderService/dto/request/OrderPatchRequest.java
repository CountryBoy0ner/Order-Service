package com.innowise.OrderService.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class OrderPatchRequest {

    private Long userId;
    private String status;

}

