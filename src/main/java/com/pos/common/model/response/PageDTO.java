package com.pos.common.model.response;

import java.util.List;

public record PageDTO<T> (List<T> content, int page, int size, long totalElements, long totalPages) {}

