package net.malpiszon.fundallocator.controllers.advices;

import java.beans.PropertyEditorSupport;

import net.malpiszon.fundallocator.models.InvestmentType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class InvestmentTypeAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(InvestmentType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(InvestmentType.getInvestmentType(text));
            }
        });
    }
}
