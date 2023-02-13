/*
 * Better List Sorting is a Minecraft mod.
 *     Copyright (C) 2023 MagnusHJensen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dk.magnusjensen.betterlistsorting.util;

import java.util.List;

public enum ServerSortable {
    PLAYER_COUNT_DESC,
    PLAYER_COUNT_ASC,
    NAME_ASC,
    NAME_DESC,
    MANUAL;

    public static boolean isPlayerCount(ServerSortable sortable) {
        return List.of(PLAYER_COUNT_ASC, PLAYER_COUNT_DESC).contains(sortable);
    }

    public static boolean isName(ServerSortable sortable) {
        return List.of(NAME_ASC, NAME_DESC).contains(sortable);
    }
}
