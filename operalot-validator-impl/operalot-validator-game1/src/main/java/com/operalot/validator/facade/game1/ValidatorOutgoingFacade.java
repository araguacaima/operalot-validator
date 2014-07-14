package com.operalot.validator.facade.game1;

import com.operalot.validator.Game;

import java.io.File;

/**
 * Created by Alejandro Mendez on 10/07/2014.
 */
public class ValidatorOutgoingFacade extends Game implements com.operalot.validator.facade.ValidatorOutgoingFacade {

    @Override
    public File getProviderFile() {
        //TODO Implement algorithm for the generation of Provider File for game 1
        return new File("");
    }

    @Override
    public File getOperalotFile() {
        //TODO Implement algorithm for the generation of Operalot File for game 1 (It's recommended to implement a
        //TODO Interface thru an abstract class that can be inheritaced from specific implementations of this method,
        //TODO because of in almost all cases this process will be the same independiently of the game
        return new File("");
    }
}
