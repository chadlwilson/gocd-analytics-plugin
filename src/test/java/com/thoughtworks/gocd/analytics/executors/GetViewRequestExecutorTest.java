/*
 * Copyright 2020 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.gocd.analytics.executors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.gocd.analytics.utils.Util;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetViewRequestExecutorTest {

    @Test
    public void shouldRenderTheTemplateInJSON() throws Exception {
        GoPluginApiResponse response = new GetViewRequestExecutor().execute();
        assertEquals(200, response.responseCode());
        Map<String, String> hashSet = new Gson().fromJson(response.responseBody(), new TypeToken<Map<String, String>>() {
        }.getType());
        assertEquals(Util.readResource("/plugin-settings.template.html"), hashSet.get("template"));
    }

    @Test
    public void allFieldsShouldBePresentInView() throws Exception {
        String template = Util.readResource("/plugin-settings.template.html").replaceAll("\\s+", " ");

        for (Map.Entry<String, com.thoughtworks.gocd.analytics.models.Field> fieldEntry : GetPluginConfigurationExecutor.FIELDS.entrySet()) {
            assertTrue(template.contains("ng-model=\"" + fieldEntry.getKey() + "\""));
            assertTrue(template.contains("<span class=\"form_error\" ng-show=\"GOINPUTNAME[" + fieldEntry.getKey() +
                    "].$error.server\">{{GOINPUTNAME[" + fieldEntry.getKey() +
                    "].$error.server}}</span>"));
        }
    }

}
