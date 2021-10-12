package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.entity.GustEntity;
import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AirbendingGustTest {

    private AirbendingGust airbendingGust = new AirbendingGust();

    @Test
    void properties() {
        assertThat(airbendingGust.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airbendingGust.getCoolDown()).isEqualTo(20);
    }

    @Test
    void name() {
        assertThat(airbendingGust.getName()).isEqualTo("AirGust");
    }

    @Test
    void actionNetwork() {
        World world = mock(World.class);

        airbendingGust.getActionNetwork(world, 10, 20, 30, 40, 50, 100, mock(ByteBuffer.class));

        verify(world, atLeastOnce()).addEntity(new GustEntity(10, 20, 45, 19, 50).setID(100));
        verify(world, atLeastOnce()).addEntity(new GustEntity(10, 20, 30, 40, 50).setID(100));
        verify(world, atLeastOnce()).addEntity(new GustEntity(10, 20, 5, 49, 50).setID(100));
    }
}
