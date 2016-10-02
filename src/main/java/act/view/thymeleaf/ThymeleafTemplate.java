package act.view.thymeleaf;

import act.view.TemplateBase;
import org.osgl.$;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.util.Map;

public class ThymeleafTemplate extends TemplateBase {

    private TemplateEngine engine;
    private String template;

    ThymeleafTemplate(String template, TemplateEngine engine) {
        this.engine = $.notNull(engine);
        this.template = $.notNull(template);
    }

    @Override
    protected String render(Map<String, Object> renderArgs) {
        try {
            return engine.process(template, new ActThymeLeafContext(renderArgs));
        } catch (TemplateProcessingException e) {
            throw new ThymeleafTemplateException(e);
        }
    }
}
