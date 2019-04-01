package io.codelex.groupdinner;

import io.codelex.groupdinner.api.Dinner;

import java.util.List;

class DinnerService {

    private List<Dinner> dinners;

    DinnerService(List<Dinner> dinners) {
        this.dinners = dinners;
    }

    Dinner addDinner(Dinner dinner) {
        dinners.add(dinner);
        return dinner;
    }

}
