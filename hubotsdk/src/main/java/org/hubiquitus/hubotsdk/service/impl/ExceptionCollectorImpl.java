/*
 * Copyright (c) Novedia Group 2012.
 *
 *     This file is part of Hubiquitus.
 *
 *     Hubiquitus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Hubiquitus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Hubiquitus.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.hubiquitus.hubotsdk.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.hubiquitus.hapi.model.ExceptionType;
import org.hubiquitus.hubotsdk.service.ExceptionCollector;

/*
 * Exception collector. Will collect all raised exceptions and returns a report string when needed
 */
public class ExceptionCollectorImpl implements ExceptionCollector {

    private Map<Exception, ExceptionType> exceptions = new HashMap<Exception, ExceptionType>();

    public void addException(Exception e, ExceptionType publishingError) {
        exceptions.put(e, publishingError);
    }

    public String getExceptions() {
        String ex = "";
        for (Exception e : exceptions.keySet()) {
            ex += " --- Exception: From: " + exceptions.get(e) + "; Stack trace: \"" + e.getMessage().replace("\n", ";") + " --- ";
        }
        ex = ex.replace("\"", "\\\"");
        return ex;
    }
}
