package com.operalot.validator;

import com.operalot.validator.factory.ValidatorBuilder;
import com.operalot.validator.persistence.jpa.model.ValidatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Alejandro Mendez on 09/07/2014.
 */

@SuppressWarnings("SpringJavaAutowiringInspection") @Component
public class ValidatorBuildHashedFileFacade {

    private Validator validator;

    @Autowired
    private ValidatorBuilder validatorBuilder;

    private Validator performValidation() {
        if (this.validator != null) {
            this.validator.performValidation();
            return this.validator;
        } else {
            return null;
        }
    }

    public Validator performValidation(String gameName, ValidatorEntity validator) {
        if (gameName != null && validator != null) {
            this.validator = validatorBuilder.buildFilledValidator(gameName, validator.getInputFieldEntities());
            performValidation();
            return this.validator;
        } else {
            return null;
        }
    }
}
