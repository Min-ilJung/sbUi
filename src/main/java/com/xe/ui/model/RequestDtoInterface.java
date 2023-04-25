package com.xe.ui.model;

import java.util.Map;

public interface RequestDtoInterface<K, V> {
    Map<K, V> convertToMap();
}
