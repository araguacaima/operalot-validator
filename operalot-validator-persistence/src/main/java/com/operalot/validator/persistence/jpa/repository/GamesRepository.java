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
public class GamesRepository {

    @PersistenceContext
    private EntityManager manager;

    public GameEntity get(String id) {
        return manager.find(GameEntity.class, Long.parseLong(id));
    }

    public ValidatorEntity getValidatorEntityByGameId(String id) {
        return manager.find(GameEntity.class, Long.parseLong(id)).getValidatorEntity();
    }

    public GameEntity getGameEntityByGameName(String name) {
        GameEntity singleResult = null;
        try {
            singleResult = manager.createNamedQuery(GameEntity.GET_GAME_BY_NAME, GameEntity.class).setParameter(GameEntity.PARAM_NAME, name).getSingleResult();
        } catch (NoResultException ignored) {
        }
        return singleResult;
    }

    public Set<GameEntity> getAll() {
        return new LinkedHashSet<>(manager.createNamedQuery(GameEntity.GET_ALL_GAMES, GameEntity.class).getResultList());
    }

    public int getCountAll() {
        Query query = manager.createNamedQuery(GameEntity.GET_GAMES_COUNT);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }


}