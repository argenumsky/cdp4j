/**
 * cdp4j - Chrome DevTools Protocol for Java
 * Copyright © 2017, 2018 WebFolder OÜ (support@webfolder.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.webfolder.cdp.type.accessibility;

import com.google.gson.annotations.SerializedName;

/**
 * Attributes which apply to widgets
 */
public enum AXWidgetAttributes {
    @SerializedName("autocomplete")
    Autocomplete("autocomplete"),

    @SerializedName("haspopup")
    Haspopup("haspopup"),

    @SerializedName("level")
    Level("level"),

    @SerializedName("multiselectable")
    Multiselectable("multiselectable"),

    @SerializedName("orientation")
    Orientation("orientation"),

    @SerializedName("multiline")
    Multiline("multiline"),

    @SerializedName("readonly")
    Readonly("readonly"),

    @SerializedName("required")
    Required("required"),

    @SerializedName("valuemin")
    Valuemin("valuemin"),

    @SerializedName("valuemax")
    Valuemax("valuemax"),

    @SerializedName("valuetext")
    Valuetext("valuetext");

    public final String value;

    AXWidgetAttributes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
