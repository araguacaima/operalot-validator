package com.operalot.validator;

import com.operalot.validator.persistence.jpa.model.GameEntity;
import com.operalot.validator.persistence.jpa.repository.GamesRepository;
import com.operalot.validator.persistence.jpa.repository.ValidatorsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Alejandro Mendez on 09/07/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-api-applicationContext.xml"})
public class ValidatorBuildHashedFileFacadeTest {

    @Autowired
    private GamesRepository gamesRepository;


    @Autowired
    private ValidatorsRepository validatorsRepository;

    @Autowired
    private ValidatorBuildHashedFileFacade validatorBuildHashedFileFacade;

    @Before
    public void setUp() {

    }

    @Test
    public void testValidatorBuildHashedFileFacade() {
        String gameName = getGameName();
        Validator validator= validatorBuildHashedFileFacade.performValidation(gameName, validatorsRepository.getValidatorEntityByGameName(gameName));
        Assert.assertNotNull(validator.getOutgoingFacade().getProviderFile());
    }

    private String getGameName() {
        GameEntity gameEntity = gamesRepository.getGameEntityByGameName("game1");
        if (gameEntity != null) {
            return gameEntity.getName();
        } else {
            return null;
        }
    }

    public GamesRepository getGamesRepository() {
        return gamesRepository;
    }

    public void setGamesRepository(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }
}
