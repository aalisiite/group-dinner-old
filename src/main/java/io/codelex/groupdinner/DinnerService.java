package io.codelex.groupdinner;

import io.codelex.groupdinner.repository.model.DinnerRecord;
import io.codelex.groupdinner.api.JoinDinnerRequest;

import java.util.List;
import java.util.Optional;

public class DinnerService {

    private List<DinnerRecord> dinners;

    DinnerService(List<DinnerRecord> dinners) {
        this.dinners = dinners;
    }

   public DinnerRecord addDinner(DinnerRecord dinner) {
        dinners.add(dinner);
        return dinner;
    }

    public Optional<DinnerRecord> getDinner(JoinDinnerRequest request) {
        return dinners.stream()
                .filter(it -> it.equals(request.getDinner()))
                .findFirst();
    }

}
