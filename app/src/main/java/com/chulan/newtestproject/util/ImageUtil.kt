package com.chulan.newtestproject.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Decode and sample down a bitmap from resources to the requested width and
 * height.
 *
 * @param res
 * The resources object containing the image data
 * @param resId
 * The resource id of the image data
 * @param reqWidth
 * The requested width of the resulting bitmap
 * @param reqHeight
 * The requested height of the resulting bitmap
 * @param cache
 * The ImageCache used to find candidate bitmaps for use with
 * inBitmap
 * @return A bitmap sampled down from the original with the same aspect
 * ratio and dimensions that are equal to or greater than the
 * requested width and height
 */
fun decodeSampledBitmapFromResource(res: Resources?,
                                    resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {

    // BEGIN_INCLUDE (read_bitmap_dimensions)
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//    BitmapFactory.decodeResource(res, resId, options)
//
//    // Calculate inSampleSize
//    options.inSampleSize = calculateInSampleSize(options, reqWidth,
//            reqHeight)
    // END_INCLUDE (read_bitmap_dimensions)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    // TODO(wzx) : 等于期望大小
    options.outWidth = reqWidth
    options.outHeight = reqHeight
    return BitmapFactory.decodeResource(res, resId, options)
}


/**
 * Calculate an inSampleSize for use in a
 * [BitmapFactory.Options] object when decoding
 * bitmaps using the decode* methods from
 * [BitmapFactory]. This implementation calculates
 * the closest inSampleSize that is a power of 2 and will result in the
 * final decoded bitmap having a width and height equal to or larger than
 * the requested width and height.
 *
 * @param options
 * An options object with out* params already populated (run
 * through a decode* method with inJustDecodeBounds==true
 * @param reqWidth
 * The requested width of the resulting bitmap
 * @param reqHeight
 * The requested height of the resulting bitmap
 * @return The value to be used for inSampleSize
 */
fun calculateInSampleSize(options: BitmapFactory.Options,
                          reqWidth: Int, reqHeight: Int): Int {
    // BEGIN_INCLUDE (calculate_sample_size)
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and
        // keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize > reqHeight
                && halfWidth / inSampleSize > reqWidth) {
            inSampleSize *= 2
        }

        // This offers some additional logic in case the image has a strange
        // aspect ratio. For example, a panorama may have a much larger
        // width than height. In these cases the total pixels might still
        // end up being too large to fit comfortably in memory, so we should
        // be more aggressive with sample down the image (=larger
        // inSampleSize).
        var totalPixels = width * height / inSampleSize.toLong()

        // Anything more than 2x the requested pixels we'll sample down
        // further
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toLong()
        while (totalPixels > totalReqPixelsCap) {
            inSampleSize *= 2
            totalPixels /= 2
        }
    }
    return inSampleSize
    // END_INCLUDE (calculate_sample_size)
}
