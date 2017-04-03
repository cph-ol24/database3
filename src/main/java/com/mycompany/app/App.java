package com.mycompany.app;

import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        IRepository sqlRepository = new SqlRepository();

        Random random = new Random(2000);

        int[] randomPersonIds = new int[20];
        for (int i = 0; i < randomPersonIds.length; i++) {
            randomPersonIds[i] = random.nextInt();
        }

        double[][] sqlResults = new double[20][5];

        for (int j = 0; j < sqlResults.length; j++) {
            for (int i = 0; i < sqlResults[j].length; i++) {

                Stopwatch stopwatch = new Stopwatch();
                List<Person> results = sqlRepository.findEndorments(randomPersonIds[j], i);

                sqlResults[j][i] = stopwatch.elapsedTime();

                System.out.println("Found (id: " + j + ", depth: " + i + "): " + results.size());
            }
        }

        System.out.println("Completed!");

        double[] sqlMedians = new double[5];

        for (int j = 0; j < sqlResults.length; j++) {
            for (int i = 0; i < sqlResults[j].length; i++) {
                sqlMedians[i] += sqlResults[j][i];
            }
        }

        for (int i = 0; i < sqlMedians.length; i++) {
            System.out.println("Depth #" + i + ", totalTime: " + sqlMedians[i] + "ms, median: " + (sqlMedians[i] / 20) + "ms");
        }
    }
}
