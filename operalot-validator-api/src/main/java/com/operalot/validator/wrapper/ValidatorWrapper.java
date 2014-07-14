package com.operalot.validator.wrapper;

import com.operalot.validator.Validator;
import com.operalot.validator.facade.ValidatorIncomingFacade;
import com.operalot.validator.persistence.jpa.model.FieldEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alejandro Mendez on 11/07/2014.
 */
public class ValidatorWrapper {
    public static Validator bindFieldEntitiesToValidator(Validator validator, Set<FieldEntity> fieldEntities) {
        if (fieldEntities != null && validator != null) {
            ValidatorIncomingFacade incomingFacade = validator.getIncomingFacade();
            Map<String, String> map = new HashMap<>();
            for (FieldEntity fieldEntity : fieldEntities) {
                map.put(fieldEntity.getValidatorFieldName(), fieldEntity.getValue());
            }
            incomingFacade.fill(map);
        }
        return validator;
    }
}
