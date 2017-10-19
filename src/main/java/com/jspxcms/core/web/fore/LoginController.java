package com.jspxcms.core.web.fore;

import static com.jspxcms.core.security.CmsAuthenticationFilter.FALLBACK_URL_PARAM;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.util.Encodes;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.UserStatus;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.service.UserStatusService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;

/**
 * LoginController
 * 
 * @author liufang
 * 
 */
@Controller
public class LoginController {
	public static final String LOGIN_URL = "/login.jspx";
	public static final String LOGIN_TEMPLATE = "sys_member_login.html";
	public static final String LOGIN_INCLUDE_TEMPLATE = "sys_member_login_include.html";
	public static final String LOGIN_AJAX_TEMPLATE = "sys_member_login_ajax.html";
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserStatusService userStatusService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = { LOGIN_URL,
			Constants.SITE_PREFIX_PATH + "" + LOGIN_URL })
	public String login(String fallbackUrl, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		
		logger.info("fallbackUrl--"+fallbackUrl);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		String macAddress = ((HttpServletRequest) request).getHeader("User-Agent")+ request.getRemoteAddr();
		UserStatus userStatus = userStatusService.getByMacAddress(macAddress);
		if(userStatus != null&&userStatus.getStatus()==1&&(new Date().getTime() - userStatus.getLastDate().getTime())/(1000)<1800){
			modelMap.addAttribute("useracc", userStatus.getUserName());
			modelMap.addAttribute("userpass", Encodes.unicode2String(userService.get(userStatus.getUserId()).getPasswordS()));
			modelMap.addAttribute("userid", userStatus.getUserId());
		}
		else{
			modelMap.addAttribute("useracc","");
			modelMap.addAttribute("userpass", "");
			modelMap.addAttribute("userid", -1);
		}
		
		return site.getTemplate(LOGIN_TEMPLATE);
	}

	@RequestMapping(value = { "/login_include.jspx",
			Constants.SITE_PREFIX_PATH + "/login_include.jspx" })
	public String loginInclude(String fallbackUrl, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		Servlets.setNoCacheHeader(response);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return site.getTemplate(LOGIN_INCLUDE_TEMPLATE);
	}

	@RequestMapping(value = { "/login_ajax.jspx",
			Constants.SITE_PREFIX_PATH + "/login_ajax.jspx" })
	public String loginAjax(String fallbackUrl, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		Servlets.setNoCacheHeader(response);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return site.getTemplate(LOGIN_AJAX_TEMPLATE);
	}

	@RequestMapping(value = { "/login.jspx",
			Constants.SITE_PREFIX_PATH + "/login.jspx" }, method = RequestMethod.POST)
	public String loginFail(
			@RequestParam(DEFAULT_USERNAME_PARAM) String username,
			String fallbackUrl, HttpServletRequest request,
			RedirectAttributes ra) {
		logger.info("username--------------"+username);
		Object errorName = request
				.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		logger.info("errorName--------------"+errorName.toString());
		if (errorName != null&&!errorName.equals("com.jspxcms.common.security.IncorrectCaptchaException")) {
			ra.addFlashAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, errorName);
		}
		ra.addFlashAttribute(DEFAULT_USERNAME_PARAM, username);
		ra.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		logger.info("fallbackUrl--------------"+fallbackUrl);
		return "redirect:login.jspx";
	}
}
