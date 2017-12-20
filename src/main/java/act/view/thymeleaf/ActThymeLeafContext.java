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
