package com.airline.authservice.common;

import com.airline.authservice.exception.BadRequestCustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;
import java.util.Objects;

@Component
public class LocaleLanguage {

    public Locale getLocale (){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String acceptLanguageHeader = request.getHeader(ConstantManager.HEADER_ACCEPT_LANGUAGE);
            if (acceptLanguageHeader == null || acceptLanguageHeader.isEmpty()) {
                return new Locale(ConstantManager.SERBIAN_LOCALE_SHORTCUT);
            }
            if (!ConstantManager.ACCEPTABLE_LANGUAGE_HEADER.contains(acceptLanguageHeader)) {
                throw new BadRequestCustomException(ConstantManager.BAD_LANGUAGE_IN_HEADER_EXCEPTION_MESSAGE, ConstantManager.BAD_LANGUAGE_IN_HEADER_EXCEPTION_CODE);
            }
            return request.getLocale();
        }catch (BadRequestCustomException be){
            throw be;
        } catch (Exception ex){
            return new Locale(ConstantManager.SERBIAN_LOCALE_SHORTCUT);
        }
    }
}
