package academy.render;

public class LocalBuffer {
    final byte[][][] colorBuffer;
    final int[][] hitCount;

    LocalBuffer(int width, int height) {
        this.colorBuffer = new byte[height][width][3];
        this.hitCount = new int[height][width];
    }

    public byte[][][] getColorBuffer() {
        return colorBuffer;
    }

    public int[][] getHitCount() {
        return hitCount;
    }
}
