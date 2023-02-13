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

package dk.magnusjensen.betterlistsorting.mixin;

import dk.magnusjensen.betterlistsorting.util.ServerListPlayerComparator;
import dk.magnusjensen.betterlistsorting.util.ServerSortable;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerScreenMixin extends Screen {
    @Shadow private ServerList servers;
    @Shadow protected ServerSelectionList serverSelectionList;
    private CycleButton<ServerSortable> sortButton;

    protected JoinMultiplayerScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {

        this.sortButton = this.addRenderableWidget(
            CycleButton.<ServerSortable>builder(serverSortable -> new TextComponent(serverSortable.name()))
                .withValues(ServerSortable.values())
                .withInitialValue(ServerSortable.MANUAL)
                .create(this.width - 100, 5, 100, 20, new TextComponent("Sort"), (pCycleButton, pValue) -> {
                    if (ServerSortable.isPlayerCount(pValue)) {
                        List<ServerData> servers = List.copyOf(this.servers.serverList);
                        this.servers.serverList.clear();
                        this.servers.serverList.addAll(servers.stream().sorted(new ServerListPlayerComparator(pValue == ServerSortable.PLAYER_COUNT_ASC)).toList());
                        this.servers.save();
                        this.serverSelectionList.updateOnlineServers(this.servers);
                    } else if (pValue == ServerSortable.MANUAL) {
                        pCycleButton.setValue(ServerSortable.PLAYER_COUNT_DESC);
                    } else if (ServerSortable.isName(pValue)) {
                        List<ServerData> servers = List.copyOf(this.servers.serverList);
                        this.servers.serverList.clear();
                        this.servers.serverList.addAll(servers.stream().sorted((s1, s2) -> pValue == ServerSortable.NAME_ASC
                            ? s1.name.compareToIgnoreCase(s2.name)
                            : s2.name.compareToIgnoreCase(s1.name)
                            ).toList());
                        this.servers.save();
                        this.serverSelectionList.updateOnlineServers(this.servers);
                    }


                })
        );
    }


}


