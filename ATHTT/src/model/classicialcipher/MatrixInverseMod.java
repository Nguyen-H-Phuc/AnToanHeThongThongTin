package model.classicialcipher;

import java.util.Arrays;

public class MatrixInverseMod {
	public static int[][] inverseModulo2x2(int[][] matrix) {
        int a = matrix[0][0];
        int b = matrix[0][1];
        int c = matrix[1][0];
        int d = matrix[1][1];
        int modulo = 178;

        // 1. Tính định thức modulo 178
        long det = ((long)a * d - (long)b * c) % modulo;
        if (det < 0) {
            det += modulo; // Đảm bảo định thức là số dương trong khoảng [0, modulo-1]
        }

        // 2. Tìm nghịch đảo modulo của định thức
        long detInverse = modInverse(det, modulo);

        // Nếu định thức không có nghịch đảo modulo, ma trận không khả nghịch
        if (detInverse == -1) {
            System.out.println("Ma trận không khả nghịch modulo " + modulo + " vì định thức không có nghịch đảo modulo.");
            return null;
        }

        // 3. Tính ma trận nghịch đảo
        int[][] inverse = new int[2][2];
        inverse[0][0] = (int) ((detInverse * d) % modulo);
        inverse[0][1] = (int) ((detInverse * (-b + modulo)) % modulo); // Cộng modulo để xử lý số âm
        inverse[1][0] = (int) ((detInverse * (-c + modulo)) % modulo); // Cộng modulo để xử lý số âm
        inverse[1][1] = (int) ((detInverse * a) % modulo);

        return inverse;
    }

    // Thuật toán Extended Euclidean để tìm nghịch đảo modulo
    public static long modInverse(long a, long m) {
        long m0 = m;
        long y = 0, x = 1;

        if (m == 1)
            return 0;

        while (a > 1) {
            long q = a / m;
            long t = m;

            m = a % m;
            a = t;
            t = y;

            y = x - q * y;
            x = t;
        }

        if (x < 0)
            x += m0;

        return x;
    }

    public static void main(String[] args) {
        int[][] matrix = {{2, 3}, {1, 4}};
        int[][] inverse = inverseModulo2x2(matrix);

        if (inverse != null) {
            System.out.println("Ma trận ban đầu:");
            printMatrix(matrix);
            System.out.println("\nMa trận nghịch đảo modulo 178:");
            printMatrix(inverse);

            // Kiểm tra lại bằng cách nhân ma trận với ma trận nghịch đảo (kết quả phải là ma trận đơn vị modulo 178)
            int modulo = 178;
            int[][] identity = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        identity[i][j] = (identity[i][j] + (matrix[i][k] * inverse[k][j]) % modulo) % modulo;
                    }
                }
            }
            System.out.println("\nMa trận tích (A * A^-1) modulo 178:");
            printMatrix(identity);
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
