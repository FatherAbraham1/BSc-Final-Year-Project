package main;

import geneticClasses.IndividualType;
import geneticClasses.SelectionMethod;
import org.apache.spark.api.java.JavaRDD;
import problemdescription.FitnessEval;

import java.sql.Driver;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Michal Dorko on 09/03/16.
 * BSc Final Year project
 * King's College London
 * Map-Reduce library for Genetic Algorithms
 * Licensed under the Academic Free License version 3.0
 */
public class TravellingSalesmanMain {

    public static void main(String[] args) {
        int numberOfTheCities = 10;
        FitnessEval fitnessEval = new FitnessEval(numberOfTheCities, 900000);
        int chromosomeLength = numberOfTheCities;
        int populationSize = (int) (numberOfTheCities * Math.log(numberOfTheCities));
        int maxFit = Integer.MAX_VALUE;
        int numberOfTheGenerations = 10;
        SelectionMethod selectionMethod = SelectionMethod.rouletteWheel;
        boolean multipoint = false;
        int numberOfCrossPoints = 0;
        int convergence = 10;
        int sizeOfIsland = (int) (numberOfTheCities / Math.log(numberOfTheCities));
        int migrationRate = 10;
        GARunner.setEnableStatistics(true);
        GARunner gaRunner = GARunner.getGARunner(fitnessEval, IndividualType.IntegerPermutation, null, chromosomeLength, populationSize, maxFit, numberOfTheGenerations,
                selectionMethod, multipoint, numberOfCrossPoints);
        gaRunner.setConvergenceMax(convergence);
        Integer[] bestSolutionCities = (Integer[]) gaRunner.runIslandGA(sizeOfIsland, migrationRate); // first param is size of island, second migration time rate
        System.out.println("Number of the variables " + numberOfTheCities);
        System.out.println("Number of the generations " + numberOfTheGenerations);
        System.out.println("Population Size " + populationSize);
        System.out.println("Convergence " + convergence);
        System.out.println("Multipoint crossover " + multipoint);
        System.out.println("Number of crossover points " + numberOfCrossPoints);
        System.out.println("Selection method: " + selectionMethod);
        System.out.println("Size of island: " + sizeOfIsland);
        System.out.println("Migration rate: " + migrationRate);
        System.out.println("Solution: " + Arrays.toString(bestSolutionCities));
        System.out.println("Mean");
        gaRunner.getMean().stream().forEach(x -> System.out.print( x+ ","));
        System.out.println("\nStandard Deviation");
        gaRunner.getStd().stream().forEach(x -> System.out.print(x + ","));
        System.out.println("\nStandard Error");
        gaRunner.getStandardError().stream().forEach(x -> System.out.print(x + ","));
        System.out.println("\nAverageFitness over runs: " + gaRunner.getAverageFitnessOverGenerations());
        System.out.println("Number of individuals in final population with optimal fitness: " + gaRunner.getLastGenerationMaxFitness());
        System.out.println("One iteration running time: " + gaRunner.getOneIterationRunningTime());

    }

}
