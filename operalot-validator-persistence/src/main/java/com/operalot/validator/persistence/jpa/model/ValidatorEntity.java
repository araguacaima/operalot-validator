package com.operalot.validator.persistence.jpa.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Access(AccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery(name = ValidatorEntity.GET_VALIDATORS_COUNT, query = "select count(v) from ValidatorEntity v"),
        @NamedQuery(name = ValidatorEntity.GET_ALL_VALIDATORS,
                query = "select v from ValidatorEntity v order by v.name"),
        @NamedQuery(name = ValidatorEntity.GET_VALIDATOR_BY_NAME,
                query = "select v from ValidatorEntity v where v.name = :" + ValidatorEntity.PARAM_NAME),
        @NamedQuery(name = ValidatorEntity.GET_VALIDATOR_BY_GAME_NAME,
                query = "select v from ValidatorEntity v, GameEntity g where v.gameEntity = g AND g.name = :"
                        + GameEntity.PARAM_NAME + " order by v.name")
})
@Table(name = "validator")
public class ValidatorEntity implements BasicEntity {

    public static final String GET_VALIDATORS_COUNT = "getValidatorsCount";
    public static final String GET_ALL_VALIDATORS = "getAllValidator";
    public static final String GET_VALIDATOR_BY_NAME = "getValidatorByName";
    public static final String GET_VALIDATOR_BY_GAME_NAME = "getValidatorByGameName";
    public static final String PARAM_NAME = "name";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name", length = 100)
    private String name;

    @Basic
    @Column(name = "incomingFacadeClassName")
    private String incomingFacadeClassName;

    @Basic
    @Column(name = "outgoingFacadeClassName")
    private String outgoingFacadeClassName;

    @Basic
    @Column(name = "facadeClassName")
    private String facadeClassName;

    private GameEntity gameEntity;
    private Set<FieldEntity> inputFieldEntities = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long entityId) {
        this.id = entityId;
    }

    public String getIncomingFacadeClassName() {
        return incomingFacadeClassName;
    }

    public void setIncomingFacadeClassName(String className) {
        this.incomingFacadeClassName = className;
    }

    public String getOutgoingFacadeClassName() {
        return outgoingFacadeClassName;
    }

    public void setOutgoingFacadeClassName(String outgoingFacadeClassName) {
        this.outgoingFacadeClassName = outgoingFacadeClassName;
    }

    public String getFacadeClassName() {
        return facadeClassName;
    }

    public void setFacadeClassName(String facadeClassName) {
        this.facadeClassName = facadeClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(optional = false, mappedBy = "validatorEntity")
    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public void setGameEntity(GameEntity gameEntity) {
        this.gameEntity = gameEntity;
    }

    @OneToMany()
    @OrderBy("name ASC")
    public Set<FieldEntity> getInputFieldEntities() {
        return inputFieldEntities;
    }

    public void setInputFieldEntities(Set<FieldEntity> fieldEntities) {
        this.inputFieldEntities = fieldEntities;
    }

    public void addInputFieldEntities(FieldEntity fieldEntity) {
        if (fieldEntity != null) {
            this.inputFieldEntities.add(fieldEntity);
        }
    }

}
