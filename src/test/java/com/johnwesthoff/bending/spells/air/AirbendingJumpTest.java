package com.johnwesthoff.bending.spells.air;

import com.johnwesthoff.bending.entity.EffectEntity;
import com.johnwesthoff.bending.logic.World;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AirbendingJumpTest {

    private AirbendingJump airbendingJump = new AirbendingJump();

    @Test
    void properties() {
        assertThat(airbendingJump.ID).isEqualTo(1);
    }

    @Test
    void coolDown() {
        assertThat(airbendingJump.getCoolDown()).isEqualTo(20);
    }

    @Test
    void name() {
        assertThat(airbendingJump.getName()).isEqualTo("AirJump");
    }

    @Test
    void actionNetwork() {
        World world = mock(World.class);
        when(world.randomInt(40)).thenReturn(25);

        airbendingJump.getActionNetwork(world, 10, 20, 30, 40, 50, 100, mock(ByteBuffer.class));

        InOrder inOrder = inOrder(world);
        inOrder.verify(world).randomInt(40);
        inOrder.verify(world).addEntity(new EffectEntity(10, 20, 30, 40, 25, Color.WHITE).setID(100));
    }
}
