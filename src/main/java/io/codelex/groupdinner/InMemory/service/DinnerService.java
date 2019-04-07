package io.codelex.groupdinner.InMemory.service;

import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.api.JoinDinnerRequest;

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

    public Optional<Dinner> getDinner(JoinDinnerRequest request) {
        return dinners.stream()
                .filter(it -> it.equals(request.getDinner()))
                .findFirst();
    }

}
