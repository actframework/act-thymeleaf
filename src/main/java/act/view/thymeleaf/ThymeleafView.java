package act.view.thymeleaf;

import act.Act;
import act.app.App;
import act.util.ActContext;
import act.view.Template;
import act.view.View;
import org.osgl.util.C;
import org.osgl.util.IO;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThymeleafView extends View {

    public static final String ID = "thymeleaf";

    private TemplateEngine engine;
    private ClassLoaderTemplateResolver templateResolver;
    private IEngineConfiguration configuration;

    @Override
    public String name() {
        return ID;
    }

    @Override
    protected Template loadTemplate(String resourcePath, ActContext context) {
        if (!checkResource(resourcePath)) {
            return null;
        }
        try {
            return new ThymeleafTemplate(resourcePath, engine);
        } catch (TemplateProcessingException e) {
            throw new ThymeleafTemplateException(e);
        }
    }

    @Override
    protected void init(App app) {
        engine = new TemplateEngine();
        engine.setTemplateResolvers(prepareResolvers());
        configuration = engine.getConfiguration();
    }

    private Set<ITemplateResolver> prepareResolvers() {

        Set<ITemplateResolver> resolvers = new HashSet<ITemplateResolver>();

        String templateHome = templateHome();
        templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(templateHome);
        templateResolver.setCacheable(Act.isProd());
        resolvers.add(templateResolver);

        return resolvers;
    }

    private boolean checkResource(String path) {
        TemplateResolution resolution = templateResolver.resolveTemplate(configuration, null, path, null);
        ITemplateResource resource = resolution.getTemplateResource();
        return resource.exists();
    }

}
