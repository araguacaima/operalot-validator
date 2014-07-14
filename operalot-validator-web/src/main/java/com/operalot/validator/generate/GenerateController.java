package com.operalot.validator.generate;

import com.operalot.validator.Validator;
import com.operalot.validator.ValidatorBuildHashedFileFacade;
import com.operalot.validator.persistence.jpa.model.FieldEntity;
import com.operalot.validator.persistence.jpa.model.GameEntity;
import com.operalot.validator.persistence.jpa.model.ValidatorEntity;
import com.operalot.validator.persistence.jpa.repository.GamesRepository;
import com.operalot.validator.persistence.jpa.repository.ValidatorsRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
public class GenerateController {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private ValidatorsRepository validatorsRepository;

    private Validator validator;

    @Autowired
    private ValidatorBuildHashedFileFacade validatorBuildHashedFileFacade;

    private ValidatorEntity validatorEntity = new ValidatorEntity();

    private String game;

    private static final int BUFFER_SIZE = 4096;

    @RequestMapping(value = "generate")
    public String load() {
        return "generate/load";
    }

    @RequestMapping(value = "fill")
    public ModelAndView fill(@RequestParam("game") String game) {
        this.game = game;
        ModelAndView mav = new ModelAndView("generate/fill");
        mav.addObject("fields", getFields(game));
        mav.addObject("game", game);
        mav.addObject("validator", validatorEntity);
        return mav;
    }

    @RequestMapping(value = "save")
    public ModelAndView save(@RequestParam("fieldId") final String fieldId,
            @RequestParam("fieldValue") final String fieldValue) {
        ModelAndView mav = new ModelAndView("generate/fill");
        if (validatorEntity != null) {
            Set<FieldEntity> inputFieldEntities = validatorEntity.getInputFieldEntities();
            FieldEntity field = (FieldEntity) CollectionUtils.find(inputFieldEntities, new Predicate() {
                @Override public boolean evaluate(Object object) {
                    FieldEntity field = (FieldEntity) object;
                    return field.getId().equals(Long.parseLong(fieldId));
                }
            });
            if (field != null) {
                field.setValue(fieldValue);
                mav.addObject("fields", inputFieldEntities);
            }
            mav.addObject("validator", validatorEntity);
        }
        mav.addObject("game", game);
        return mav;
    }

    public Set<FieldEntity> getFields(String game) {
        validatorEntity = findValidator(game);
        return validatorEntity.getInputFieldEntities();
    }

    public ValidatorEntity findValidator(String game) {
        return validatorsRepository.getValidatorEntityByGameName(game);
    }

    @ModelAttribute("games")
    public Set<GameEntity> games() {
        Set<GameEntity> gameEntities = new LinkedHashSet<>();
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(-1L);
        gameEntity.setName("Select Game");
        gameEntities.add(gameEntity);
        gameEntities.addAll(gamesRepository.getAll());
        return gameEntities;
    }

    @RequestMapping(value = "/generate_files", method = RequestMethod.POST)
    @ModelAttribute("validator")
    public ModelAndView generate() {
        ModelAndView mav = new ModelAndView("generate/generate_files");
        this.validator = validatorBuildHashedFileFacade.performValidation(game, validatorEntity);
        mav.addObject("validator", this.validator);
        return mav;
    }

    @RequestMapping(value = "download_provider")
    @ModelAttribute("validator")
    public void download_provider(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // get absolute path of the application
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");
        String fullPath = appPath + validator.getOutgoingFacade().getProviderFile().getName();
        download(request, response, fullPath);
    }


    @RequestMapping(value = "download_operalot")
    @ModelAttribute("validator")
    public void download_operalot(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // get absolute path of the application
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");
        String fullPath = appPath + validator.getOutgoingFacade().getOperalotFile().getName();
        download(request, response, fullPath);
    }

    private void download(HttpServletRequest request,
            HttpServletResponse response, String fullPath) throws IOException {

        // get absolute path of the application
        ServletContext context = request.getServletContext();

        // construct the complete absolute path of the file
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();

    }

}
