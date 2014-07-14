package com.operalot.validator.factory;

import com.operalot.validator.Validator;
import com.operalot.validator.ValidatorFacade;
import com.operalot.validator.facade.ValidatorIncomingFacade;
import com.operalot.validator.facade.ValidatorOutgoingFacade;
import com.operalot.validator.persistence.jpa.model.FieldEntity;
import com.operalot.validator.persistence.jpa.model.GameEntity;
import com.operalot.validator.persistence.jpa.model.ValidatorEntity;
import com.operalot.validator.persistence.jpa.repository.GamesRepository;
import com.operalot.validator.wrapper.ValidatorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by Alejandro Mendez on 09/07/2014.
 */

@SuppressWarnings("SpringJavaAutowiringInspection") @Component
public class ValidatorBuilder implements ApplicationContextAware {
    //TODO Carga dinámica de las clases configuradas
    private static ApplicationContext context;

    @Autowired
    private GamesRepository gamesRepository;

    public Validator buildNonFilledValidator(String gameName) {

        GameEntity gameEntity = gamesRepository.getGameEntityByGameName(gameName);
        if (gameEntity == null) {
            throw new IllegalArgumentException("Game of type '" + gameName + "' is not found on db");
        } else {
            ValidatorEntity validatorEntity = gameEntity.getValidatorEntity();
            if (validatorEntity == null) {
                throw new UnsupportedOperationException(
                        "Game of type '" + gameName + "' does not have a Validator configured on db");
            } else {
                return buildNonFilledValidator(validatorEntity);
            }
        }
    }

    public Validator buildNonFilledValidator(ValidatorEntity validatorEntity) {

        if (validatorEntity == null) {
            throw new UnsupportedOperationException("Can not find a valid Validator from null entry");
        } else {
            final String incomingFacadeClassName = validatorEntity.getIncomingFacadeClassName();
            final String outgoingFacadeClassName = validatorEntity.getOutgoingFacadeClassName();
            final String facadeClassName = validatorEntity.getFacadeClassName();
            try {
                return new Validator() {
                    public ValidatorIncomingFacade incomingFacade;
                    public ValidatorOutgoingFacade outgoingFacade;
                    public ValidatorFacade validatorFacade;

                    private void registerSpringBean(Class<?> clazz) {
                        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
                        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
                        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                        beanDefinition.setBeanClass(clazz);
                        beanDefinition.setAutowireCandidate(true);
                        registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
                        factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
                    }

                    private void initializeIncomingFacade() {
                        try {
                            Class<?> clazz = Class.forName(incomingFacadeClassName);
                            registerSpringBean(clazz);
                            incomingFacade = (ValidatorIncomingFacade) context.getBean(clazz.getSimpleName());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public ValidatorIncomingFacade getIncomingFacade() {
                        if (incomingFacade == null) {
                            initializeIncomingFacade();
                        }
                        return incomingFacade;
                    }

                    private void initializeOutgoingFacade() {
                        try {
                            Class<?> clazz = Class.forName(outgoingFacadeClassName);
                            registerSpringBean(clazz);
                            outgoingFacade = (ValidatorOutgoingFacade) context.getBean(clazz.getSimpleName());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public ValidatorOutgoingFacade getOutgoingFacade() {
                        if (outgoingFacade == null) {
                            initializeOutgoingFacade();
                        }
                        return outgoingFacade;
                    }

                    @Override
                    public void performValidation() {
                        try {
                            Class<?> clazz = Class.forName(facadeClassName);
                            registerSpringBean(clazz);
                            validatorFacade = (ValidatorFacade) context.getBean(clazz.getSimpleName());
                            if (outgoingFacade == null) {
                                initializeOutgoingFacade();
                            }
                            validatorFacade.validate(incomingFacade, outgoingFacade);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                };
            } catch (Throwable t) {
                throw new IllegalStateException("Either incomingFacadeClassName of type '" + incomingFacadeClassName
                        + "' or outgoingFacadeClassName '" + outgoingFacadeClassName
                        + "' could not be launched because of the following exception: " + t.getLocalizedMessage());
            }
        }
    }

    public Validator buildFilledValidator(String gameName, Set<FieldEntity> fieldEntities) {

        GameEntity gameEntity = gamesRepository.getGameEntityByGameName(gameName);
        if (gameEntity == null) {
            throw new IllegalArgumentException("Game of type '" + gameName + "' is not found on db");
        } else {
            ValidatorEntity validatorEntity = gameEntity.getValidatorEntity();
            if (validatorEntity == null) {
                throw new UnsupportedOperationException(
                        "Game of type '" + gameName + "' does not have a Validator configured on db");
            } else {
                return buildFilledValidator(validatorEntity, fieldEntities);
            }
        }
    }

    public Validator buildFilledValidator(ValidatorEntity validatorEntity, Set<FieldEntity> fieldEntities) {

        if (validatorEntity == null) {
            throw new UnsupportedOperationException("Can not find a valid Validator from null entry");
        } else if (fieldEntities == null || fieldEntities.isEmpty()) {
            throw new UnsupportedOperationException("Can not fill a Validator with a null or empty incoming fields");
        } else {
            return ValidatorWrapper.bindFieldEntitiesToValidator(buildNonFilledValidator(validatorEntity), fieldEntities);
        }
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
}
