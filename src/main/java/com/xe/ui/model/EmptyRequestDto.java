package com.xe.ui.model;

import java.util.HashMap;
import java.util.Map;

public class EmptyRequestDto implements RequestDtoInterface<Object, Object> {

    @Override
    public Map<Object, Object> convertToMap() {
        return new HashMap<>();
    }
}
