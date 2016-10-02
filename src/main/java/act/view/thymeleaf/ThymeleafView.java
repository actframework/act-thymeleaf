package act.view.thymeleaf;

import act.Act;
import act.app.App;
import act.util.ActContext;
import act.view.Template;
import act.view.View;
import org.osgl.util.C;
import org.osgl.util.IO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThymeleafView extends View {

    public static final String ID = "thymeleaf";

    private TemplateEngine engine;

    @Override
    public String name() {
        return ID;
    }

    @Override
    protected Template loadTemplate(String resourcePath, ActContext context) {
        try {
            return new ThymeleafTemplate(resourcePath, engine);
        } catch (TemplateProcessingException e) {
            throw new ThymeleafTemplateException(e);
        }
    }

    @Override
    protected void init(App app) {
        initEngine();
    }

    List<String> loadContent(String template) {
        File file = new File(templateRootDir(), template);
        if (file.exists() && file.canRead()) {
            return IO.readLines(file);
        }
        return C.list();
    }

    private void initEngine() {
        engine = new TemplateEngine();
        engine.setTemplateResolvers(prepareResolvers());
    }

    private Set<ITemplateResolver> prepareResolvers() {

        Set<ITemplateResolver> resolvers = new HashSet<ITemplateResolver>();
        App app = App.instance();

        String templateHome = templateHome();
        for (Class<? extends AbstractConfigurableTemplateResolver> c: C.listOf(ClassLoaderTemplateResolver.class)) {
            AbstractConfigurableTemplateResolver resolver = app.getInstance(c);
            resolver.setPrefix(templateHome);
            resolver.setCacheable(Act.isProd());
            resolvers.add(resolver);
        }

        return resolvers;
    }

}
