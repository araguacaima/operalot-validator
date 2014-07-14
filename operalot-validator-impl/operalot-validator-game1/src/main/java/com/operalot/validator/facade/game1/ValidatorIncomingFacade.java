package com.operalot.validator.facade.game1;

import com.operalot.validator.Game;
import com.operalot.validator.persistence.jpa.repository.ValidatorsRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Alejandro Mendez on 10/07/2014.
 */

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class ValidatorIncomingFacade extends Game
        implements com.operalot.validator.facade.ValidatorIncomingFacade, ApplicationContextAware {

    private static ApplicationContext context;

    private String field1;
    private String field2;
    private Integer field3;
    private String field4;
    private Float field5;
    private String field6;
    private String field7;
    private Integer field8;
    private String field9;
    private Float field10;

    @Autowired
    private ValidatorsRepository validatorsRepository;

    @Override
    public void fill(Map<String, String> args) throws IllegalArgumentException {

        try {
            BeanUtils.populate(this, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Incoming map argument does not suite with this ValidatorIncomingFacade implementation", e);
        }
    }

    @Override
    public Map<String, String> getFields() throws UnsupportedOperationException {
        try {
            return BeanUtils.describe(this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public Integer getField3() {
        return field3;
    }

    public void setField3(Integer field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public Float getField5() {
        return field5;
    }

    public void setField5(Float field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public Integer getField8() {
        return field8;
    }

    public void setField8(Integer field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public Float getField10() {
        return field10;
    }

    public void setField10(Float field10) {
        this.field10 = field10;
    }
}
