package com.operalot.validator.persistence.jpa.model;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery(name = FieldEntity.GET_FIELD_COUNT, query = "select count(f) from FieldEntity f"),
        @NamedQuery(name = FieldEntity.GET_ALL_FIELDS, query = "select f from FieldEntity f order by f.name"),
        @NamedQuery(name = FieldEntity.GET_FIELD_BY_NAME,
                query = "select f from FieldEntity f where f.name = :" + FieldEntity.PARAM_NAME + " order by f.name")
})
@Table(name = "field")
public class FieldEntity implements BasicEntity {

    public static final String GET_FIELD_COUNT = "getFieldsCount";
    public static final String GET_ALL_FIELDS = "getAllFields";
    public static final String GET_FIELD_BY_NAME = "getFieldByName";
    public static final String PARAM_NAME = "name";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name", length = 100)
    private String name;

    @Basic
    @Column(name = "i18key", length = 100)
    private String i18key;

    @Basic
    @Column(name = "validatorFieldName", length = 100)
    private String validatorFieldName;

    @Transient
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getI18key() {
        return i18key;
    }

    public void setI18key(String i18key) {
        this.i18key = i18key;
    }

    public String getValidatorFieldName() {
        return validatorFieldName;
    }

    public void setValidatorFieldName(String validatorFieldName) {
        this.validatorFieldName = validatorFieldName;
    }
}
