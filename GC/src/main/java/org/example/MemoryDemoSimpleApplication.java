package org.example;

/*
-Xms1024m
-Xmx1024m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
*/

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc*:debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/


public class MemoryDemoSimpleApplication {
    public static void main(String[] args) throws Exception {
        var arr = new byte[1_000_000_000];
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            arr[i] = 1;

            if (i % 10_000 == 0) {
                System.out.println("Пауза на 1 секунду");
                Thread.sleep(java.util.concurrent.TimeUnit.SECONDS.toMillis(1));
            }

            if (i % 100_000 == 0) {
                System.out.println("Запуск GC");
                System.gc();
            }
        }

        System.out.println("Длина массива: " + arr.length);
    }
}