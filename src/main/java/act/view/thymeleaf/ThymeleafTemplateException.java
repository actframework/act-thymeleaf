package act.view.thymeleaf;

import act.Act;
import act.app.SourceInfo;
import org.osgl.$;
import org.thymeleaf.exceptions.TemplateProcessingException;

import java.util.List;

public class ThymeleafTemplateException extends act.view.TemplateException {

    public ThymeleafTemplateException(Exception t) {
        super(t);
    }

    @Override
    protected void populateSourceInfo(Throwable t) {
        sourceInfo = getJavaSourceInfo(t);
        while (null != t) {
            t = t.getCause();
            if (null == t) {
                return;
            }
            if (t instanceof TemplateProcessingException) {
                TemplateProcessingException e = $.cast(t);
                Integer line = e.getLine();
                if (null == line) {
                    continue;
                }
                templateInfo = new ThymeleafSourceInfo(e);
            }
        }
        super.populateSourceInfo(t);
    }

    @Override
    public String errorMessage() {
        Throwable t = getCauseOrThis();
        return t.toString();
    }

    @Override
    protected boolean isTemplateEngineInvokeLine(String s) {
        return s.contains("ognl.OgnlRuntime.invokeMethod");
    }

    private static class ThymeleafSourceInfo extends SourceInfo.Base {

        ThymeleafSourceInfo(TemplateProcessingException e) {
            lineNumber = e.getLine();
            fileName = e.getTemplateName();
            lines = readTemplateSource(fileName);
        }

        private static List<String> readTemplateSource(String template) {
            ThymeleafView view = (ThymeleafView) Act.viewManager().view(ThymeleafView.ID);
            return view.loadContent(template);
        }
    }

}
