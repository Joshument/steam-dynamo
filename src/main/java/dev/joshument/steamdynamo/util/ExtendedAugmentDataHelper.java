package dev.joshument.steamdynamo.util;

import cofh.core.util.helpers.AugmentDataHelper;
import net.minecraft.nbt.CompoundTag;

// need to use bools so uh fun  lol ?
public final class ExtendedAugmentDataHelper {
    private ExtendedAugmentDataHelper() {

    }
    public static ExtendedBuilder builder() {
        return new ExtendedBuilder();
    }

    public static class ExtendedBuilder {
        CompoundTag augmentData = new CompoundTag();
        public CompoundTag build() {
            return augmentData.isEmpty() ? null : augmentData;
        }
    }
}
