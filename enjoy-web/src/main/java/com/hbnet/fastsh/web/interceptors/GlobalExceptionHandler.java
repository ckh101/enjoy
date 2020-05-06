package com.hbnet.fastsh.web.interceptors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ModelAndView exceptionHandler(HttpServletRequest req, Exception e) throws Exception {
		ModelAndView view = new ModelAndView();
		e.printStackTrace();
		return view;
	}

}
