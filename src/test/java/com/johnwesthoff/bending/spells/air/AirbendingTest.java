package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AirbendingTest {

    private Airbending airbending = new Airbending();

    @Test
    void properties() {
        assertThat(airbending.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airbending.getCoolDown()).isEqualTo(10);
    }

    @Test
    void name() {
        assertThat(airbending.getName()).isEqualTo("AirBall");
    }

    @Test
    void actionNetwork() {
        World world = mock(World.class);

        airbending.getActionNetwork(world, 0, 0, 0, 0, 0, 0, mock(ByteBuffer.class));

        verify(world).playSound("aircast.wav");
    }
}
