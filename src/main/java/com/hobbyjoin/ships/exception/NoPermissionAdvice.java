package com.hobbyjoin.ships.exception;
import com.hobbyjoin.ships.Translator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
@ControllerAdvice
public class NoPermissionAdvice {
    @ExceptionHandler(NoPermissionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noPermissionHandler(Model model,NoPermissionException ex){
        model.addAttribute("status",404);
        Translator translator = Translator.getInstance();
        translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
        model.addAttribute("error",translator.translate(ex.getMessage()));
        return "error";
    }
}
