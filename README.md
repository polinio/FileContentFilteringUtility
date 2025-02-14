# Утилита фильтрации содержимого файлов

## Описание проекта

Утилита предназначена для обработки текстовых файлов, содержащих перемешанные данные (целые числа, строки и вещественные числа). Она фильтрует данные по типу и записывает их в отдельные выходные файлы, сохраняя статистику и предоставляя гибкую настройку через параметры командной строки.

### Основной функционал
- **Фильтрация данных**: Разделение входных данных на три категории — целые числа, вещественные числа и строки — с сохранением в отдельные файлы (по умолчанию - integers.txt, floats.txt, strings.txt).
- **Гибкость настройки путей и имен файлов**: Возможность указания пути для выходных файлов с помощью опции `-o` и задания префикса их имен с помощью опции `-p`.
- **Режим добавления**: Поддержка добавления данных в существующие файлы с использованием опции `-a`. По умолчанию файлы перезаписываются.
- **Сбор статистики**: 
  - Краткая статистика (`-s`): Количество элементов каждого типа.
  - Полная статистика (`-f`): Для чисел — минимум, максимум, сумма, среднее. Для строк — длина самой короткой и самой длинной строк.
- **Обработка ошибок**: Утилита устойчива к ошибкам, при необходимости информирует пользователя о невозможности продолжить выполнение.
- **Автоматическое создание файлов**: Выходные файлы создаются только при наличии данных соответствующего типа.

### Используемые технологии
- **Язык программирования**: Java (версия 17).
- **Дополнительные зависимости**: Отсутствуют (используется только стандартная библиотека Java).

### Примеры запуска

Простейший запуск с указанием входных файлов
```bash
java -jar util.jar in1.txt in2.txt
```

Указание префикса для имен файлов
```bash
java -jar util.jar -p test- in1.txt in2.txt
```

Режим добавления данных в существующие файлы, указание префикса
```bash
java -jar util.jar -a -p sample- in1.txt in2.txt
```

Указание пути для выходных файлов, полная статистика, указание префикса
```bash
java -jar util.jar -f -o /output/results/ -p result- in1.txt in2.txt
```

Указание пути для выходных файлов, краткая статистика, указание префикса
```bash
java -jar util.jar -s -o /output/results/ -p result- in1.txt in2.txt
```

### Требования
- **Версия Java**: 17

### Инструкция по сборке и запуску
1. Клонируйте репозиторий и перейдите в директорию проекта:
   ```bash
   git clone https://github.com/polinio/FileContentFilteringUtility.git
   ```
   ```bash
   cd FileContentFilteringUtility
   ```
2. Запустите утилиту с необходимыми параметрами через командную строку:
   ```bash
   java -jar util.jar [опции] [файлы]
   ```
