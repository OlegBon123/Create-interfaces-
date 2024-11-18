import java.io.*;
import java.util.*;

public class Main {

    public interface IntProc {
        int apply(int a);
    }

    public interface ArrayIntProcess {
        double avg(int[] a);
        double avg(File f);
        int[] applyToArray(int[] a, IntProc cond);
        File applyToFile(File f, IntProc cond);
    }

    public static class ArrayIntProcessor implements ArrayIntProcess {

        @Override
        public double avg(int[] a) {
            return Arrays.stream(a).average().orElse(0);
        }

        @Override
        public double avg(File f) {
            try (Scanner scanner = new Scanner(f)) {
                List<Integer> numbers = new ArrayList<>();
                while (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                }
                return numbers.stream().mapToInt(Integer::intValue).average().orElse(0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public int[] applyToArray(int[] a, IntProc cond) {
            for (int i = 0; i < a.length; i++) {
                a[i] = cond.apply(a[i]);
            }
            return a;
        }

        @Override
        public File applyToFile(File f, IntProc cond) {
            List<Integer> numbers = new ArrayList<>();
            try (Scanner scanner = new Scanner(f)) {
                while (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            List<Integer> transformed = new ArrayList<>();
            for (int num : numbers) {
                transformed.add(cond.apply(num));
            }

            File outputFile = new File("output.txt");
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                for (int num : transformed) {
                    writer.print(num + " ");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return outputFile;
        }
    }

    public static void main(String[] args) {

        ArrayIntProcessor processor = new ArrayIntProcessor();

        int[] array = {1, 2, 3, 4, 5};

        System.out.println("Avg (int array): " + processor.avg(array));

        File inputFile = new File("input.txt");
        System.out.println("Avg (file): " + processor.avg(inputFile));

        IntProc multiplyByTwo = (int a) -> a * 2;

        int[] transformedArray = processor.applyToArray(array, multiplyByTwo);
        System.out.println("Transformed array: " + Arrays.toString(transformedArray));

        File transformedFile = processor.applyToFile(inputFile, multiplyByTwo);
        System.out.println("Transformed file saved to: " + transformedFile.getAbsolutePath());
    }
}
