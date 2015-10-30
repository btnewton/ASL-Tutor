package com.brandtnewtonsoftware.asle.knn;

import com.brandtnewtonsoftware.asle.util.Database;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Brandt on 10/29/2015.
 */
public class Classifier {

    public Classifier() {



    }

    public Sign[] getSigns(String signValue) {

        Connection connection = Database.getConnection();

        Sign[] signs;
        ResultSet results = null;

        try {
            Statement stmt = connection.createStatement();
            results = stmt.executeQuery("SELECT COUNT(*) AS count FROM Signs");
            results.next();
            int resultCount = results.getInt("count");

            stmt = connection.createStatement();
            results = stmt.executeQuery("SELECT Signs.id, sign_value," +
                    "  thumb.id AS thumbId, thumb.metacarpal_x , thumb.metacarpal_y, thumb.metacarpal_z, thumb.distal_x, thumb.distal_y, thumb.distal_z," +
                    "  pointer.id AS pointerid, pointer.metacarpal_x , pointer.metacarpal_y, pointer.metacarpal_z, pointer.distal_x, pointer.distal_y, pointer.distal_z," +
                    "  middle.id AS middleId, middle.metacarpal_x , middle.metacarpal_y, middle.metacarpal_z, middle.distal_x, middle.distal_y, middle.distal_z," +
                    "  ring.id AS ringId, ring.metacarpal_x , ring.metacarpal_y, ring.metacarpal_z, ring.distal_x, ring.distal_y, ring.distal_z," +
                    "  pinky.id AS pinkyId, pinky.metacarpal_x , pinky.metacarpal_y, pinky.metacarpal_z, pinky.distal_x, pinky.distal_y, pinky.distal_z " +
                    "FROM Signs " +
                    "  INNER JOIN Fingers thumb ON thumb.id = Signs.thumb_finger" +
                    "  INNER JOIN Fingers pointer ON pointer.id = Signs.pointer_finger" +
                    "  INNER JOIN Fingers middle ON middle.id = Signs.middle_finger" +
                    "  INNER JOIN Fingers ring ON ring.id = Signs.ring_finger" +
                    "  INNER JOIN Fingers pinky ON pinky.id = Signs.pinky_finger " +
                    "WHERE sign_value LIKE " + signValue);

            signs = new Sign[resultCount];

            while (results.next()) {
                int signId = results.getInt(1);
                String value = results.getString(2);

                LiteFinger[] fingers = new LiteFinger[LiteFingerList.FINGER_COUNT];
                for (int fingerIndex = 0; fingerIndex < LiteFingerList.FINGER_COUNT; fingerIndex++) {
                    int offset = fingerIndex * 7 + 3;
                    int fingerId = results.getInt(offset);

                    float metacarpalX = results.getFloat(offset + 1);
                    float metacarpalY = results.getFloat(offset + 2);
                    float metacarpalZ = results.getFloat(offset + 3);
                    LiteBone metacarpal = new LiteBone(new Vector(metacarpalX, metacarpalY, metacarpalZ), Bone.Type.TYPE_METACARPAL);

                    float distalX = results.getFloat(offset + 4);
                    float distalY = results.getFloat(offset + 5);
                    float distalZ = results.getFloat(offset + 6);
                    LiteBone distal = new LiteBone(new Vector(distalX, distalY, distalZ), Bone.Type.TYPE_DISTAL);

                    fingers[fingerIndex] = new LiteFinger(fingerId, Finger.Type.swigToEnum(fingerIndex), metacarpal, distal);
                }
                signs[results.getRow() - 1] = new Sign(signId, value, new LiteFingerList(fingers));
            }
            results.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            signs = new Sign[0];
        }

        return signs;
    }
}
