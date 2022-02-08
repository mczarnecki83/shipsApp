package com.hobbyjoin.ships.exception;
import com.hobbyjoin.ships.Translator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidTokenAdvice {
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String noPermissionHandler(Model model,InvalidTokenException ex){
        model.addAttribute("status",403);
        Translator translator = Translator.getInstance();
        translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
        model.addAttribute("error",translator.translate(ex.getMessage()));
        return "error";
    }
}
