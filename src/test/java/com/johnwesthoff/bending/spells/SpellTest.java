package com.johnwesthoff.bending.spells;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpellTest {

    private static final int EXPECTED_SPELL_COUNT = 32;
    private static final int EXPECTED_PASSIVE_COUNT = 10;

    @BeforeAll
    static void setUp() {
        Spell.init();
    }

    @Test
    void spellLookups() {
        assertThat(Spell.spellLookup).hasSize(EXPECTED_SPELL_COUNT + EXPECTED_PASSIVE_COUNT);
    }

    @Test
    void registeredSpells() {

        assertThat(Spell.spells).hasSize(EXPECTED_SPELL_COUNT);
        assertThat(Spell.spellnames).hasSize(EXPECTED_SPELL_COUNT);
        assertThat(Spell.spellimages).hasSize(EXPECTED_SPELL_COUNT);
        assertThat(Spell.spelltips).hasSize(EXPECTED_SPELL_COUNT);

        for (int spellIndex = 0; spellIndex < Spell.spells.size(); spellIndex++) {
            assertThat(Spell.spells.get(spellIndex).subID).isEqualTo(spellIndex);
        }
    }

    @Test
    void registeredPassives() {

        assertThat(Spell.passives).hasSize(EXPECTED_PASSIVE_COUNT);
        assertThat(Spell.passivenames).hasSize(EXPECTED_PASSIVE_COUNT);
        assertThat(Spell.passiveimages).hasSize(EXPECTED_PASSIVE_COUNT);
        assertThat(Spell.passivetips).hasSize(EXPECTED_PASSIVE_COUNT);
    }
}
