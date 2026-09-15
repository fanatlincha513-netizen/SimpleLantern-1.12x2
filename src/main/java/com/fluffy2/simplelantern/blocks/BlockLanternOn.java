package com.fluffy2.simplelantern.blocks;

import com.fluffy2.simplelantern.ModConfig;

public class BlockLanternOn extends BlockLanternBase {

    public BlockLanternOn() {
        super("lantern_on");
        // уровень света фиксируется при регистрации, поэтому конфиг применяется после перезапуска
        setLightLevel(ModConfig.lanternLightValue / 15.0F);
    }

    @Override
    public boolean isLit() {
        return true;
    }
}
