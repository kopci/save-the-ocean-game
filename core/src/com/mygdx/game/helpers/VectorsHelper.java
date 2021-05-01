package com.mygdx.game.helpers;

import com.badlogic.gdx.math.Vector2;

public class VectorsHelper {

    /**
     * Method calculates current position of circle position vector.
     *
     * @param currentDegrees - current rotation of submarine (from starting point in degrees)
     * @param distanceFromCenter - straight distance between center and trashCatcher
     * @param centerPoint - submarine origin (screen center)
     * @return direction vector for each trashCatcher
     */
    public static Vector2 getCirclePositionVector(float currentDegrees, float distanceFromCenter, Vector2 centerPoint) {
        float radians = (float)Math.toRadians(currentDegrees);

        float x = (((float)Math.cos(radians)) * distanceFromCenter) + centerPoint.x;
        float y = (((float)Math.sin(radians)) * distanceFromCenter) + centerPoint.y;

        return new Vector2(x, y);
    }

}
