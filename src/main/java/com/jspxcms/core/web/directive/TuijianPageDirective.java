package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspxcms.core.web.fore.InfoController;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * TuijianPageDirective
 * 
 * @author liufang
 * 
 */
public class TuijianPageDirective extends AbstractInfoListPageDirective implements
		TemplateDirectiveModel {
	protected final static Logger logger = LoggerFactory.getLogger(TuijianPageDirective.class);
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		logger.info("TuijianPageDirective---"+params.toString());
		super.doExecute1(env, params, loopVars, body, true);
	}
}
