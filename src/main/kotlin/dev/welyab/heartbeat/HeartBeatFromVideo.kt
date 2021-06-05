package dev.welyab.heartbeat

import org.jcodec.common.io.NIOUtils
import org.jcodec.api.FrameGrab
import org.jcodec.scale.AWTUtil
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Paths
import java.util.Locale

fun BufferedImage.getColorAverage(): Double {
    var sum = 0.0
    for (x in 0 until getWidth(null)) {
        for (y in 0 until getHeight(null)) {
            val rgb = Color(getRGB(x, y))
            sum += (rgb.red + rgb.green + rgb.blue) / 3.0
        }
    }
    return sum / (getWidth(null) * getHeight(null))
}

fun main() {
    val videoDir = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources")
    val videPath = videoDir.resolve(Paths.get("heart-video.mp4"))
    val grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(videPath.toFile()))
    generateSequence { grab.nativeFrame }
        .map { AWTUtil.toBufferedImage(it) }
        .map { it.getColorAverage() }
        .forEach {
            println("%.4f".format(Locale.US, it))
        }
}
