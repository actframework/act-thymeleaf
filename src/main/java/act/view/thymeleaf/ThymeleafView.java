package act.view.thymeleaf;

import act.Act;
import act.app.App;
import act.view.Template;
import act.view.View;
import org.osgl.util.S;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.HashSet;
import java.util.Set;

public class ThymeleafView extends View {

    public static final String ID = "thymeleaf";

    private TemplateEngine engine;
    private TemplateEngine stringResourceEngine;
    private ClassLoaderTemplateResolver templateResolver;
    private IEngineConfiguration configuration;
    private String suffix;

    @Override
    public String name() {
        return ID;
    }

    @Override
    protected Template loadTemplate(String resourcePath) {
        if (!checkResource(resourcePath)) {
            if (resourcePath.endsWith(suffix)) {
                return null;
            }
            return loadTemplate(S.concat(resourcePath, suffix));
        }
        try {
            return new ThymeleafTemplate(resourcePath, engine);
        } catch (TemplateProcessingException e) {
            throw new ThymeleafTemplateException(e);
        }
    }

    @Override
    protected Template loadInlineTemplate(String content) {
        try {
            return new ThymeleafTemplate(content, stringResourceEngine);
        } catch (TemplateProcessingException e) {
            throw new ThymeleafTemplateException(e);
        }
    }

    @Override
    protected void init(App app) {
        engine = new TemplateEngine();
        engine.setTemplateResolvers(prepareResolvers());
        configuration = engine.getConfiguration();
        suffix = app.config().get("view.thymeleaf.suffix");
        if (null == suffix) {
            suffix = ".thymeleaf";
        } else {
            suffix = suffix.startsWith(".") ? suffix : S.concat(".", suffix);
        }
        initStringResourceEngine();
    }

    private void initStringResourceEngine() {
        stringResourceEngine = new TemplateEngine();
        stringResourceEngine.setTemplateResolver(new StringTemplateResolver());
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
