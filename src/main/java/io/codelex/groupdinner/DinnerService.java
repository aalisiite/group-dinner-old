package io.codelex.groupdinner;

import io.codelex.groupdinner.repository.model.Dinner;
import io.codelex.groupdinner.api.JoinDinnerRequest;

import java.util.List;
import java.util.Optional;

public class DinnerService {

    private List<Dinner> dinners;

    DinnerService(List<Dinner> dinners) {
        this.dinners = dinners;
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
