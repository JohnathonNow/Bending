package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AirAffinityTest {

    private AirAffinity airAffinity = new AirAffinity();

    @Test
    void properties() {
        assertThat(airAffinity.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airAffinity.getCoolDown()).isEqualTo(0);
    }

    @Test
    void name() {
        assertThat(airAffinity.getName()).isEqualTo("Air Affinity");
    }

    @Test
    void actionNetwork() {
        assertThatThrownBy(() ->
            airAffinity.getActionNetwork(mock(World.class), 0, 0, 0, 0, 0, 0, mock(ByteBuffer.class)))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not supported yet.");
    }
}
