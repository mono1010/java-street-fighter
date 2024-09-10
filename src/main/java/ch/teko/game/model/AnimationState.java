package ch.teko.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.teko.game.Main;
/**
 * The {@code AnimationState} class manages the animation state of a Fighter.
 */
public class AnimationState {

    // Velocity in the X and Y directions
    public float velocityX, velocityY;

    // Flag to indicate if the character can run
    public boolean canRun = true;

    // Variables to manage animation timing and iterations
    private int currentAnimationIterations = 0;
    private int animationIterations = 0;
    private int tickesUsedForAnimation = 0;
    private int maxTicksForAnimation = 0;

    // Asset manager and animation handler
    private AssetsManager assetsManager;
    private Animate animate;

    // Current and previous animation states
    private Optional<Asset.State> currentAnimation;
    private Optional<Asset.State> previousAnimation;

    // List of wanted animations that can be triggered
    private List<Asset.State> wantedAnimations = new ArrayList<>();

    // Logger for logging animation events
    private Logger log = LogManager.getLogger(Main.class);

    /**
     * Constructs an {@code AnimationState} object that manages animations for a character.
     * Initializes the animation system with an {@code Animate} instance and {@code AssetsManager}.
     *
     * @param animate the {@code Animate} instance responsible for handling frame animations.
     * @param assetsManager the {@code AssetsManager} instance used for loading and managing assets.
     */
    public AnimationState(Animate animate, AssetsManager assetsManager) {
        this.animate = animate;
        this.assetsManager = assetsManager;
        this.currentAnimation = Optional.empty();
        this.previousAnimation = Optional.empty();
    }

    /**
     * Resets the animation state, including the current animation iterations,
     * ticks used, and the ability to run. This effectively stops the current animation.
     */
    void reset() {
        this.currentAnimationIterations = 0;
        this.animationIterations = 0;
        this.tickesUsedForAnimation = 0;
        this.maxTicksForAnimation = 0;
        this.canRun = true;
    }

    /**
     * Checks if the given number of animation iterations have been reached.
     *
     * @param iterations the number of iterations to check against.
     * @return {@code true} if the current animation has completed the given number of iterations, otherwise {@code false}.
     */
    boolean reached(int iterations) {
        return iterations >= this.animationIterations;
    }

    /**
     * Updates the animation state on every game tick. It manages the chaining of animations,
     * updates the number of ticks used, and resets or triggers new animations when necessary.
     */
    void onTick() {
        this.wantedAnimations.clear();
        boolean reachedEnd = this.animate.onTick();

        this.tickesUsedForAnimation += 1;
        if (this.maxTicksForAnimation == 0) {
            if (reachedEnd && this.animationIterations != 0)
                this.currentAnimationIterations += 1;

            if (!reached(this.currentAnimationIterations) && this.animationIterations != 0)
                return;
        } else {
            if (this.maxTicksForAnimation > this.tickesUsedForAnimation)
                return;
        }

        previousAnimation = currentAnimation;
        if (!this.triggerChainedAnimation()) {
            this.currentAnimation = Optional.empty();
            this.previousAnimation = Optional.empty();
            this.reset();
        }
    }

    /**
     * Triggers a chained animation based on the current animation state.
     * For example, a jump animation will chain into a fall animation.
     *
     * @return {@code true} if a chained animation was triggered, otherwise {@code false}.
     */
    boolean triggerChainedAnimation() {
        Asset.State currentAnimationState = this.currentAnimation.orElse(Asset.State.IDLE);
        if (currentAnimationState == Asset.State.JUMP) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.FALL));
            currentAnimation = Optional.of(Asset.State.FALL);
            int ticksUsed = this.tickesUsedForAnimation;
            this.reset();
            this.maxTicksForAnimation = ticksUsed;
            log.info("triggered chained fall animation");
            return true;
        }

        return false;
    }

    /**
     * Triggers a specific animation based on the given animation state.
     *
     * @param animation the {@code Asset.State} representing the desired animation to trigger.
     */
    void triggerAnimation(Asset.State animation) {
        this.wantedAnimations.add(animation);

        Asset.State currentAnimationState = this.currentAnimation.orElse(Asset.State.IDLE);
        if (currentAnimationState == Asset.State.JUMP || currentAnimationState == Asset.State.FALL)
            return;

        if (animation == Asset.State.JUMP) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.JUMP));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            log.info("triggered JUMP animation");
            return;
        }

        if (wantedAnimations.contains(Asset.State.JUMP))
            return;

        if (currentAnimationState == Asset.State.ATTACK1 || currentAnimationState == Asset.State.ATTACK2)
            return;

        if (animation == Asset.State.ATTACK1) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.ATTACK1));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            this.canRun = false;
            return;
        }

        if (wantedAnimations.contains(Asset.State.ATTACK1))
            return;

        if (currentAnimationState == Asset.State.ATTACK2)
            return;

        if (animation == Asset.State.ATTACK2) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.ATTACK2));
            currentAnimation = Optional.of(animation);
            this.reset();
            this.animationIterations = 1;
            this.canRun = false;
            return;
        }

        if (wantedAnimations.contains(Asset.State.ATTACK2))
            return;

        if (animation == Asset.State.RUN) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.RUN));
            currentAnimation = Optional.of(animation);
            this.reset();
            return;
        }

        if (wantedAnimations.contains(Asset.State.RUN))
            return;

        if (animation == Asset.State.IDLE) {
            this.animate.animate(this.assetsManager.getAsset(Asset.State.IDLE));
            currentAnimation = Optional.of(animation);
            this.reset();
            return;
        }
    }
}
