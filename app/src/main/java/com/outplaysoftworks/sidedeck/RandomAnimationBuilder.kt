package com.outplaysoftworks.sidedeck

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable

import java.util.ArrayList
import java.util.Random

/** This class contains methods to create animations consisting of a randomized sequence of
 * predefined drawables
 * .
 */
class RandomAnimationBuilder
/**
 * Used to create an animation from a series of drawables with the frames in
 * a completely random order with uniform duration
 * @param drawables Arraylist of drawables to use for individual frames
 * @param duration Total animation duration
 * @param frameCount Total frames for animation
 */
(private val drawables: ArrayList<Drawable>, private val duration: Int?, private val frameCount: Int?) {
    val frameDuration: Int?
    private val random = Random(System.nanoTime())

    init {
        this.frameDuration = duration!! / frameCount!!
    }

    /**
     * Contructs an Animation Drawable using resources in the RandomAnimationBuilder object
     * @return Animation Drawable ready to use
     */
    fun makeAnimation(): AnimationDrawable {
        val animationDrawable = AnimationDrawable()
        val max = drawables.size
        for (i in 0 until frameCount) {
            val rand = random.nextInt(max)
            animationDrawable.addFrame(drawables[rand], frameDuration!!)
        }
        return animationDrawable
    }

    /**
     * Constructs an animation Drawable using resources in the RandomAnimationBuilder object
     * @param allowIdenticalConsecutiveFrames If true, allows consecutive frames to use the same image
     * @return Animation drawable ready to use
     */
    fun makeAnimation(allowIdenticalConsecutiveFrames: Boolean): AnimationDrawable {
        val animationDrawable = AnimationDrawable()
        val max = drawables.size
        var lastRandomNumber: Int
        var rand = 0
        var i = 0
        while (i < frameCount) {
            lastRandomNumber = rand
            rand = random.nextInt(max)
            if (allowIdenticalConsecutiveFrames || lastRandomNumber != rand) {
                animationDrawable.addFrame(drawables[rand], frameDuration!!)
                i++
            }
        }
        return animationDrawable
    }
}