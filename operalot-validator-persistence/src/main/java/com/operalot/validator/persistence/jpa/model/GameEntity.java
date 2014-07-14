package com.operalot.validator.persistence.jpa.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Access(AccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery(name = GameEntity.GET_GAMES_COUNT, query = "select count(g) from GameEntity g"),
        @NamedQuery(name = GameEntity.GET_ALL_GAMES, query = "select g from GameEntity g order by g.name"),
        @NamedQuery(name = GameEntity.GET_GAME_BY_NAME, query = "select g from GameEntity g where g.name = :" +  GameEntity.PARAM_NAME + " order by g.name")
})
@Table(name = "game")
public class GameEntity implements BasicEntity {

    public static final String GET_GAMES_COUNT = "getGamesCount";
    public static final String GET_ALL_GAMES = "getAllGames";
    public static final String GET_GAME_BY_NAME = "getGameByName";
    public static final String PARAM_NAME = "name";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name", length = 100)
    private String name;

    private ValidatorEntity validatorEntity;


    public Long getId() {
        return id;
    }

    public void setId(Long entityId) {
        this.id = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(optional = false)
    @JoinColumn(
            name = "id", unique = true, nullable = false, updatable = false)
    public ValidatorEntity getValidatorEntity() {
        return validatorEntity;
    }

    public void setValidatorEntity(ValidatorEntity validatorEntity) {
        this.validatorEntity = validatorEntity;
    }
}
