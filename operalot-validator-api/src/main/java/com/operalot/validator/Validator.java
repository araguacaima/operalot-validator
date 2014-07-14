package com.operalot.validator;

import com.operalot.validator.facade.ValidatorIncomingFacade;
import com.operalot.validator.facade.ValidatorOutgoingFacade;

/**
 * Created by Alejandro Mendez on 09/07/2014.
 */
public interface Validator {

    ValidatorIncomingFacade getIncomingFacade();

    ValidatorOutgoingFacade getOutgoingFacade();

    void performValidation();
}
