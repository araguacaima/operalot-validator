package com.operalot.validator.facade;

import java.util.IllegalFormatConversionException;
import java.util.Map;

/**
 * Created by Alejandro Mendez on 09/07/2014.
 */
public interface ValidatorIncomingFacade {
    void fill(Map<String, String> args) throws IllegalArgumentException;

    Map<String, String> getFields() throws UnsupportedOperationException;
}
