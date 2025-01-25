import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String outputPath = ".";
        String prefix = "";
        boolean appendMode = false;
        boolean shortStats = false;
        boolean fullStats = false;
        List<String> inputFiles = new ArrayList<>();
        
        //  Обработка строки 
        for (int i = 0; i < args.length; i++) {
            switch(args[i]){
                case "-o": // задание пути результатов -o /some/path -p result_
                    if (i + 1 < args.length && !args[i + 1].startsWith("-"))
                        outputPath = args[++i];
                    else {
                        System.err.println("Ошибка: -o требует указания пути.");
                        return;
                    }
                    break;
                case "-p": // задание префикса имен выходных файлов
                    if (i+1 < args.length && !args[i + 1].startsWith("-"))
                        prefix = args[++i];
                    else {
                        System.err.println("Ошибка: -p требует указания префикса.");
                        return;
                    }
                    break;
                case "-a": // добавление в существующие файлы
                    appendMode = true;
                    break;
                case "-s": // краткая статистика
                    shortStats = true;
                    break;
                case "-f": // полная статистика
                    fullStats = true;
                    break;
                default:
                    if (!args[i].startsWith("-")) {
                        inputFiles.add(args[i]);
                    } else {
                        System.err.println("Ошибка: неизвестный аргумент " + args[i]);
                        return;
                    }
            }
        }
        if (inputFiles.isEmpty()) {
            System.err.println("Ошибка: необходимо указать хотя бы один входной файл.");
            return;
        }

        try {
            DataFilter dataFilter = new DataFilter(outputPath, prefix, appendMode, shortStats, fullStats);
            dataFilter.processFiles(inputFiles);
        } catch(Exception e){
            System.err.println("Ошибка выполнения программы: " + e.getMessage());
        }
    }
}