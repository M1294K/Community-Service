package com.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String name;
    private String description;
}
