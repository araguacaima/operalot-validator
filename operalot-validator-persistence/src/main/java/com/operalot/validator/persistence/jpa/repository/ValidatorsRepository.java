package com.operalot.validator.persistence.jpa.repository;

import com.operalot.validator.persistence.jpa.model.GameEntity;
import com.operalot.validator.persistence.jpa.model.ValidatorEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class ValidatorsRepository {

    @PersistenceContext
    private EntityManager manager;

    public ValidatorEntity get(String id) {
        return manager.find(ValidatorEntity.class, Long.parseLong(id));
    }

    public GameEntity getGameEntityByValidatorId(String id) {
        return manager.find(ValidatorEntity.class, Long.parseLong(id)).getGameEntity();
    }

    public ValidatorEntity getValidatorEntityByGameName(String name) {
        ValidatorEntity singleResult = null;
        try {
            singleResult = manager.createNamedQuery(ValidatorEntity.GET_VALIDATOR_BY_GAME_NAME, ValidatorEntity.class).setParameter(GameEntity.PARAM_NAME, name).getSingleResult();
        } catch (NoResultException ignored) {
        }
        return singleResult;


    }

    public Set<ValidatorEntity> getAll() {
        return new LinkedHashSet<>(manager.createNamedQuery(ValidatorEntity.GET_ALL_VALIDATORS, ValidatorEntity.class).getResultList());
    }

    public int getCountAll() {
        Query query = manager.createNamedQuery(ValidatorEntity.GET_VALIDATORS_COUNT);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }


}