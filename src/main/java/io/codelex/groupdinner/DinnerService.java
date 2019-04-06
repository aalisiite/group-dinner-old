package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Dinner;
import io.codelex.groupdinner.api.JoinDinnerRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class DinnerService {

    private List<Dinner> dinners;

    DinnerService(List<Dinner> dinners) {
        this.dinners = dinners;
    }

    Dinner addDinner(Dinner dinner) {
        dinners.add(dinner);
        return dinner;
    }
    
    Optional<Dinner> getDinner (JoinDinnerRequest request) {
        return dinners.stream()
                .filter(it -> it.equals(request.getDinner()))
                .findFirst();
    }

}
