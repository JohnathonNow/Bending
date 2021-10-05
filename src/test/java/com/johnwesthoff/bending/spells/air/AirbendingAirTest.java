package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AirbendingAirTest {

    private AirbendingAir airbendingAir = new AirbendingAir();

    @Test
    void properties() {
        assertThat(airbendingAir.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airbendingAir.getCoolDown()).isEqualTo(7);
    }

    @Test
    void name() {
        assertThat(airbendingAir.getName()).isEqualTo("Air Clear");
    }

    @Test
    void actionNetwork() {
        World world = mock(World.class);

        airbendingAir.getActionNetwork(world, 0, 0, 0, 0, 0, 0, mock(ByteBuffer.class));

        verify(world).clearCircleOnGround(0, 0, 24);
    }
}
