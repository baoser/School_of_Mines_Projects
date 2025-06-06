This project deserves 100 points because it features a movement/collision physics system,
a high score tracker, and uses various CSS3 features to make the webpage look good. The game
is easy to understand and follows platformer norms such as falling off the screen meaning
death while reaching the top leads you to the next screen. There are no bugs as far as we
are aware.

1. What CSS3 features did you use?

We used CSS3 to animate the title header as well as fade in the instructions
and high scores on the sides.


2. Did you use any cool JavaScript libraries?

jQuery is the only library we used for this project.


3. What nifty features of your program were a bear to implement?

The player movement physics were difficult to get right. It took a lot
of time messing with the various player movement stats to get it to a place
where it feels easy to control the player character and finish the game.
Some things that made it feel better to use are the addition of friction when
walking on platforms and vertical acceleration rather than a constant fall speed.

Platform collision detection was easier than expected since we decided to
allow the player to pass through all sides of platforms but the top, making it
so we only needed to check if the player is on top of a platform. However,
there were initially some issues with the player falling straight through platforms
due to the fall speed causing them to teleport straight through. This was fixed by
doing a downwards platform search based on how far the player will fall in the next game tick.