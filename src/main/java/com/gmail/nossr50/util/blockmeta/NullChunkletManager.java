package com.gmail.nossr50.util.blockmeta;

import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * A ChunkletManager implementation that does nothing and returns false for all checks.
 *
 * Useful for turning off Chunklets without actually doing much work
 */
public class NullChunkletManager implements ChunkletManager {
    @Override
    public void loadChunklet(int cx, int cy, int cz, World world) {
    }

    @Override
    public void unloadChunklet(int cx, int cy, int cz, World world) {
    }

    @Override
    public void loadChunk(int cx, int cz, World world) {
    }

    @Override
    public void unloadChunk(int cx, int cz, World world) {
    }

    @Override
    public void chunkLoaded(int cx, int cz, World world) {
    }

    @Override
    public void chunkUnloaded(int cx, int cz, World world) {
    }

    @Override
    public void saveWorld(World world) {
    }

    @Override
    public void unloadWorld(World world) {
    }

    @Override
    public void loadWorld(World world) {
    }

    @Override
    public void saveAll() {
    }

    @Override
    public void unloadAll() {
    }

    @Override
    public boolean isTrue(int x, int y, int z, World world) {
        return false;
    }

    @Override
    public boolean isTrue(Block block) {
        return false;
    }

    @Override
    public void setTrue(int x, int y, int z, World world) {
    }

    @Override
    public void setTrue(Block block) {
    }

    @Override
    public void setFalse(int x, int y, int z, World world) {
    }

    @Override
    public void setFalse(Block block) {
    }

    @Override
    public void cleanUp() {
    }
}
