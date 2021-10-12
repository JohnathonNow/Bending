package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class AirRunTest {

    private AirRun airRun = new AirRun();

    @Test
    void properties() {
        assertThat(airRun.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airRun.getCoolDown()).isEqualTo(0);
    }

    @Test
    void name() {
        assertThat(airRun.getName()).isEqualTo("Air Run");
    }

    @Test
    void actionNetwork() {
        assertThatThrownBy(()->
                airRun.getActionNetwork(mock(World.class), 10, 20, 30, 40, 50, 100, mock(ByteBuffer.class)))
                .isInstanceOf(UnsupportedOperationException.class)
                .withFailMessage("Not supported yet.");
    }
}
