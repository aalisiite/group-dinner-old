package io.codelex.groupdinner.inMemory.service;

import io.codelex.groupdinner.api.Dinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DinnerService {

    private List<Dinner> dinners = new ArrayList<>();

    public DinnerService() {
    }

    public Dinner addDinner(Dinner dinner) {
        dinners.add(dinner);
        return dinner;
    }

    public Optional<Dinner> getDinner(Long id) {
        return dinners.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

}
