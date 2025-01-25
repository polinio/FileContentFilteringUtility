import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataFilter {
    private final String outputPath;
    private final String prefix;
    private final boolean appendMode;
    private final boolean shortStats;
    private final boolean fullStats;

    public DataFilter(String outputPath, String prefix, boolean appendMode, boolean shortStats, boolean fullStats) {
        Path path = Paths.get(outputPath);
        if (!path.isAbsolute()) {
            // преобразуем относительный путь в абсолютный относительно текущей рабочей директории
            path = Paths.get(System.getProperty("user.dir"), outputPath);
        }
        this.outputPath = path.normalize().toString(); // нормализуем путь для устранения ".." и "."
        this.prefix = prefix;
        this.appendMode = appendMode;
        this.shortStats = shortStats;
        this.fullStats = fullStats;
    }

    // обработка файлов
    public void processFiles(List<String> inputFiles) throws IOException {
        List<BigInteger> integers = new ArrayList<>();
        List<BigDecimal> doubles = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String fileName: inputFiles) {
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), "UTF-8"))){
                String line;
                while ((line = bufferedReader.readLine())!=null){
                    processLine(line, integers, doubles, strings);
                }
            }
            catch (IOException e){
                System.err.println("Ошибка чтения файла: " + fileName + ". " + e.getMessage());
            }
        }
        writeResults("integers.txt", integers, "Целые числа");
        writeResults("floats.txt", doubles, "Вещественные числа");
        writeResults("strings.txt", strings, "Строки");
    }

    // обработка строки и запись в соответствующий массив
    private void processLine(String line, List<BigInteger> integers, List<BigDecimal> doubles, List<String> strings) {
        try {
            if (line.matches("^-?\\d+$")) {
                integers.add(new BigInteger(line));
            } else if (line.matches("^-?\\d*\\.\\d+(E-?\\d+)?$")) {
                doubles.add(new BigDecimal(line));
            } else strings.add(line);
        } catch (NumberFormatException e) {
            // в случае ошибки добавляем в строковый список
            strings.add(line);
        }
    }

    // запись результатов
    private <T> void writeResults (String fileName, List<T> data, String dataType) throws IOException {
        if (data.isEmpty())
            return;
        Path path = Paths.get(outputPath, prefix+fileName);
        Files.createDirectories(path.getParent()); // создание директории, если её нет

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path.toFile(), appendMode), "UTF-8"))){
            for (T item: data){
                bufferedWriter.write(item.toString());
                bufferedWriter.newLine();
            }
        }
        if (shortStats || fullStats && !data.isEmpty()) {
            printStatistics(data,dataType);
        }
    }

    // вывод статистики
    private <T> void printStatistics(List<T> data, String dataType) {
        System.out.println(dataType + ":");

        // краткая статистка
        if (shortStats && !data.isEmpty()) {
            System.out.println("  Количество: " + data.size());
        }

        // полная статистика
        if (fullStats && !data.isEmpty()) {
            System.out.println("  Количество: " + data.size());
            if (data.get(0) instanceof Number) {
                printNumericStatistics(data);
            } else if (data.get(0) instanceof String) {
                printStringStatistics(data);
            }
        }
    }

    // статистика для чисел
    private <T> void printNumericStatistics(List<T> data) {
        double min = data.stream()
                .mapToDouble(d -> ((Number) d).doubleValue())
                .min()
                .orElse(Double.NaN);
        double max = data.stream()
                .mapToDouble(d -> ((Number) d).doubleValue())
                .max()
                .orElse(Double.NaN);
        double sum = data.stream()
                .mapToDouble(d -> ((Number) d).doubleValue())
                .sum();
        System.out.println("  Минимум: " + min);
        System.out.println("  Максимум: " + max);
        System.out.println("  Сумма: " + sum);
        System.out.println("  Среднее: " + (sum / data.size()));
    }

    // статистика для строк
    private <T> void printStringStatistics(List<T> data) {
        List<String> stringData = data.stream()
                .map(Object::toString)
                .toList(); // Преобразуем в список строк
        System.out.println("  Самая короткая строка: " +
                stringData.stream().min(Comparator.comparingInt(String::length)).orElse(""));
        System.out.println("  Самая длинная строка: " +
                stringData.stream().max(Comparator.comparingInt(String::length)).orElse(""));
    }
}
