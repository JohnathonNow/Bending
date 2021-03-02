package com.johnwesthoff.bending.networking;

import java.util.HashMap;

import com.johnwesthoff.bending.networking.handlers.AiEvent;
import com.johnwesthoff.bending.networking.handlers.ChargeEvent;
import com.johnwesthoff.bending.networking.handlers.DeathEvent;
import com.johnwesthoff.bending.networking.handlers.DestroyEvent;
import com.johnwesthoff.bending.networking.handlers.DigEvent;
import com.johnwesthoff.bending.networking.handlers.DrainEvent;
import com.johnwesthoff.bending.networking.handlers.EntireWorldEvent;
import com.johnwesthoff.bending.networking.handlers.ExpandWorldEvent;
import com.johnwesthoff.bending.networking.handlers.FillEvent;
import com.johnwesthoff.bending.networking.handlers.FirePuffEvent;
import com.johnwesthoff.bending.networking.handlers.FreezeEvent;
import com.johnwesthoff.bending.networking.handlers.HurtEvent;
import com.johnwesthoff.bending.networking.handlers.IdEvent;
import com.johnwesthoff.bending.networking.handlers.LeaveEvent;
import com.johnwesthoff.bending.networking.handlers.LoginEvent;
import com.johnwesthoff.bending.networking.handlers.MapEvent;
import com.johnwesthoff.bending.networking.handlers.MessageEvent;
import com.johnwesthoff.bending.networking.handlers.MoveEvent;
import com.johnwesthoff.bending.networking.handlers.PingEvent;
import com.johnwesthoff.bending.networking.handlers.PuddleEvent;
import com.johnwesthoff.bending.networking.handlers.SandEvent;
import com.johnwesthoff.bending.networking.handlers.ScoreEvent;
import com.johnwesthoff.bending.networking.handlers.SpellEvent;
import com.johnwesthoff.bending.networking.handlers.SteamEvent;
import com.johnwesthoff.bending.networking.handlers.TurnEvent;
import com.johnwesthoff.bending.util.network.NetworkMessage;

public class NetworkManager {
    private static final NetworkManager _instance = new NetworkManager();
    private final HashMap<Byte, NetworkEvent> handlers;

    public NetworkManager() {
        this.handlers = new HashMap<>();
        this.register(new AiEvent());
        this.register(new ChargeEvent());
        this.register(new DeathEvent());
        this.register(new DestroyEvent());
        this.register(new DigEvent());
        this.register(new DrainEvent());
        this.register(new EntireWorldEvent());
        this.register(new ExpandWorldEvent());
        this.register(new FillEvent());
        this.register(new FreezeEvent());
        this.register(new HurtEvent());
        this.register(new IdEvent());
        this.register(new LeaveEvent());
        this.register(new LoginEvent());
        this.register(new MapEvent());
        this.register(new MessageEvent());
        this.register(new MoveEvent());
        this.register(new PuddleEvent());
        this.register(new SandEvent());
        this.register(new ScoreEvent());
        this.register(new SpellEvent());
        this.register(new SteamEvent());
        this.register(new FirePuffEvent());
        this.register(new TurnEvent());
        this.register(new PingEvent());
    }

    public NetworkEvent getHandler(NetworkMessage m) {
        NetworkEvent handler = this.handlers.get(m.getId());
        if (handler == null) {
            throw new UnsupportedOperationException("Called an unregisterd handler for id " + m.getId());
        }
        return handler;
    }

    public void register(NetworkEvent i) {
        if (this.handlers.put(i.getId(), i) != null) {
            throw new UnsupportedOperationException("Registered conflicting handlers for id " + i.getId());
        }
    }

    public static NetworkManager getInstance() {
        return _instance;
    }

}
