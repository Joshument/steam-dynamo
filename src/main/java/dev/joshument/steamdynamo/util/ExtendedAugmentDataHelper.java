package dev.joshument.steamdynamo.util;

import cofh.core.common.item.IAugmentItem;
import cofh.core.util.helpers.AugmentDataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import static cofh.lib.util.constants.NBTTags.TAG_AUGMENT_DATA;
import static cofh.lib.util.constants.NBTTags.TAG_TYPE;

// need to use bools so uh fun  lol ?
// can't extend AugmentDataHelper because I need a different builder method. so over for me
public final class ExtendedAugmentDataHelper {
    private ExtendedAugmentDataHelper() {

    }

    public static boolean isAugmentItem(ItemStack stack) {

        return AugmentDataHelper.isAugmentItem(stack);
    }

    public static boolean hasAugmentData(ItemStack stack) {

        return AugmentDataHelper.hasAugmentData(stack);
    }

    @Nullable
    public static CompoundTag getAugmentData(ItemStack augment) {

        return AugmentDataHelper.getAugmentData(augment);
    }

    public static String getAugmentType(ItemStack augment) {
        return AugmentDataHelper.getAugmentType(augment);
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
