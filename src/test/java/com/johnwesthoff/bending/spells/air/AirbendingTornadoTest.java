package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.entity.TornadoEntity;
import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AirbendingTornadoTest {

    private AirbendingTornado airbendingTornado = new AirbendingTornado();

    @Test
    void properties() {
        assertThat(airbendingTornado.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airbendingTornado.getCoolDown()).isEqualTo(50);
    }

    @Test
    void name() {
        assertThat(airbendingTornado.getName()).isEqualTo("Tornado");
    }

    @Test
    void actionNetwork() {
        World world = mock(World.class);

        airbendingTornado.getActionNetwork(world, 10, 20, 30, 40, 50, 100, mock(ByteBuffer.class));

        verify(world).addEntity(new TornadoEntity(10, 20, 45, 50).setID(100));
    }
}