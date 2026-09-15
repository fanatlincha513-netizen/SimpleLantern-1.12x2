package com.fluffy2.simplelantern.blocks;

public class BlockLanternOff extends BlockLanternBase {

    public BlockLanternOff() {
        super("lantern_off");
    }

    @Override
    public boolean isLit() {
        return false;
    }
}
