/*
 * Copyright 2015 Viliam Repan (lazyman)
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

package sk.lazyman.gizmo.util;

import org.hibernate.cfg.EJB3NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lazyman
 */
public class GizmoNamingStrategy extends EJB3NamingStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(GizmoNamingStrategy.class);

    @Override
    public String classToTableName(String className) {
        //change camel case to underscore delimited
        className = className.replaceAll(String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"
        ), "_");

        String result = "g_" + className.toLowerCase();
        LOG.trace("classToTableName {} to {}", new Object[]{className, result});

        return result;
    }

    @Override
    public String tableName(String tableName) {
        String result = "g_" + super.tableName(tableName);
        LOG.trace("tableName {} to {}", new Object[]{tableName, result});

        return result;
    }
}
