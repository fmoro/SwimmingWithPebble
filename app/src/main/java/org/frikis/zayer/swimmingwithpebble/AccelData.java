package org.frikis.zayer.swimmingwithpebble;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by darkstar on 07/07/2017.
 */

public class AccelData {
    public long timestamp;
    public short x;
    public short y;
    public short z;
    public boolean did_vibrate;

    public static String CSV_HEADER = "Timestamp\tX(mG)\tY(mG)\tZ(mG)\tVibrate";

    AccelData(byte[] data) {
        /*ByteBuffer wrapBuffer = ByteBuffer.wrap(data);
        byte[] timestampByteBuffer = new byte[8];
        wrapBuffer.get(timestampByteBuffer, 0, 8);
        this.setTimestamp(timestampByteBuffer);*/


        byte[] timestampByteBuffer = {data[14], data[13], data[12], data[11], data[10], data[9], data[8], data[7]};
        this.setTimestamp(timestampByteBuffer);

        byte[] vibrateByteBuffer = {data[6]};
        this.setDidVibrate(vibrateByteBuffer);

        //byte[] xByteBuffer = new byte[2];
        //wrapBuffer.get(timestampByteBuffer, 0, 2);
        byte[] zByteBuffer = {data[5], data[4]};
        this.setZ(zByteBuffer);

        byte[] yByteBuffer = {data[3], data[2]};
        this.setY(yByteBuffer);

        byte[] xByteBuffer = {data[1], data[0]};
        this.setX(xByteBuffer);



        /*byte[] yByteBuffer = new byte[2];
        wrapBuffer.get(timestampByteBuffer, 0, 2);
        this.setX(yByteBuffer);

        byte[] zByteBuffer = new byte[2];
        wrapBuffer.get(timestampByteBuffer, 0, 2);
        this.setX(zByteBuffer);

        byte[] did_vibrateButeBuffer = new byte[1];
        wrapBuffer.get(timestampByteBuffer, 0, 1);
        this.setDidVibrate(did_vibrateButeBuffer);*/
    }

    private void setTimestamp(byte[] data) {
        LongBuffer longBuffer = ByteBuffer.wrap(data).asLongBuffer();
        this.timestamp = longBuffer.get();
        // this.timestamp = new Long(new BigInteger(1, data).longValue());
        //this.timestamp = wrapBuffer.getLong();
    }

    private void setX(byte[] data) {
        ShortBuffer shortBuffer = ByteBuffer.wrap(data).asShortBuffer();
        this.x = shortBuffer.get();
        //this.x = new Integer(new BigInteger(data).intValue());
        /*ByteBuffer wrapBuffer = ByteBuffer.wrap(data);
        this.x = wrapBuffer.getShort();*/
    }

    private void setY(byte[] data) {
        ShortBuffer shortBuffer = ByteBuffer.wrap(data).asShortBuffer();
        this.y = shortBuffer.get();
        /*ByteBuffer wrapBuffer = ByteBuffer.wrap(data);
        this.y = wrapBuffer.getShort();*/
        //this.y = new Integer(new BigInteger(data).intValue());
    }
    private void setZ(byte[] data) {
        ShortBuffer shortBuffer = ByteBuffer.wrap(data).asShortBuffer();
        this.z = shortBuffer.get();
        /*ByteBuffer wrapBuffer = ByteBuffer.wrap(data);
        this.z = wrapBuffer.getShort();*/
        //this.z = new Integer(new BigInteger(data).intValue());
    }

    private void setDidVibrate(byte[] data) {
        this.did_vibrate = data[0] != 0x00;
        //this.did_vibrate = ByteBuffer.wrap(data).asCharBuffer().get() == 0;
        /*ByteBuffer wrapBuffer = ByteBuffer.wrap(data);
        this.did_vibrate = wrapBuffer.get();*/

        //this.did_vibrate = new Boolean(new BigInteger(data).intValue() == 0 ? FALSE : TRUE);
    }

    public String toCSV() {
        return String.format("%d\t%d\t%d\t%d\t%b", this.timestamp, this.x, this.y, this.z, this.did_vibrate);
    }
}
