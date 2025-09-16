import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.regex.*;

public class PolynomialInterpolation {

    public static double lagrangePolynomial(double[] x, double[] y, double value) {
        int n = x.length;
        double result = 0.0;

        for (int i = 0; i < n; i++) {
            double term = y[i];
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    term = term * (value - x[j]) / (x[i] - x[j]);
                }
            }
            result += term;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("input.json")));

        // Extract n and k
        Pattern nkPattern = Pattern.compile("\"n\"\\s*:\\s*(\\d+).*?\"k\"\\s*:\\s*(\\d+)", Pattern.DOTALL);
        Matcher nkMatch = nkPattern.matcher(content);
        int n = 0, k = 0;
        if (nkMatch.find()) {
            n = Integer.parseInt(nkMatch.group(1));
            k = Integer.parseInt(nkMatch.group(2));
        }

        double[] x = new double[k];
        double[] y = new double[k];
        int index = 0;

        // Regex to capture each root block
        Pattern rootPattern = Pattern.compile("\"(\\d+)\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"(\\d+)\",\\s*\"value\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = rootPattern.matcher(content);

        while (m.find() && index < k) {
            int xi = Integer.parseInt(m.group(1));
            int base = Integer.parseInt(m.group(2));
            String valueStr = m.group(3);

            BigInteger decimalVal = new BigInteger(valueStr, base);

            x[index] = xi;
            y[index] = decimalVal.doubleValue();
            index++;
        }
        double constantC = lagrangePolynomial(x, y, 0.0);
        System.out.println("\nConstant term c = " + constantC);
    }
}
