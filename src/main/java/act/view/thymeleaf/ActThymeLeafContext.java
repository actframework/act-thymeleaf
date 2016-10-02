package act.view.thymeleaf;

import act.Act;
import act.util.ActContext;
import org.thymeleaf.context.AbstractContext;

import java.util.Locale;
import java.util.Map;

final class ActThymeLeafContext extends AbstractContext {
    ActThymeLeafContext(Map<String, Object> renderArgs) {
        super(locale(), renderArgs);
    }

    private static Locale locale() {
        Locale locale = ActContext.Base.currentContext().locale();
        if (null == locale) {
            locale = Act.appConfig().locale();
        }
        return locale;
    }
}
