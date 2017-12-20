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
