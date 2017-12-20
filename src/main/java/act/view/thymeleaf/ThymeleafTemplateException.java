package act.view.thymeleaf;

/*-
 * #%L
 * ACT Thymeleaf
 * %%
 * Copyright (C) 2017 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
        Throwable t = rootCauseOf(this);
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
