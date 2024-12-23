package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemoryDemoApplication {
    public static void main(String[] args) throws Exception {
        // Выделение большого массива, как в исходном примере
        var arr = new byte[1_000_000_000];

        // Список для хранения ссылок на объекты, которые будут перемещены в старшее поколение
        List<byte[]> retainedObjects = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // Запись в большой массив
            arr[i] = 1;

            // Создание нового объекта (1KB массива) в Eden
            byte[] edenObject = new byte[1024]; // 1KB
            // По желанию можно удерживать некоторые объекты для предотвращения их сборки
            if (i % 50_000 == 0) {
                retainedObjects.add(edenObject); // Удерживаем объект
                // Ограничиваем размер списка, чтобы не заполнить старшее поколение
                if (retainedObjects.size() > 1000) {
                    retainedObjects.remove(0);
                }
            }

            // Каждые 10_000 итераций делаем паузу
            if (i % 10_000 == 0) {
                System.out.println("Пауза на 1 секунду");
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }

            // Каждые 100_000 итераций запускаем сборку мусора
            if (i % 100_000 == 0) {
                System.out.println("Запуск GC");
                System.gc();
            }
        }

        System.out.println("Длина массива: " + arr.length);
    }
}