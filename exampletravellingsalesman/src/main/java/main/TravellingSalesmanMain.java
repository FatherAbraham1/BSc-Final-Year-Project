package main;

import geneticClasses.IndividualType;
import geneticClasses.SelectionMethod;
import problemdescription.FitnessEval;

import java.util.Arrays;

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
        int populationSize = 20;
        int maxFit = Integer.MAX_VALUE;
        int numberOfTheGenerations = 30;
        SelectionMethod selectionMethod = SelectionMethod.tournament;
        boolean multipoint = false;
        int numberOfCrossPoints = 3;
        int convergence = 10;
        GARunner.setEnableStatistics(true);
        GARunner gaRunner = GARunner.getGARunner(fitnessEval, IndividualType.IntegerPermutation, null, chromosomeLength, populationSize, maxFit, numberOfTheGenerations,
                selectionMethod, multipoint, numberOfCrossPoints);
        gaRunner.setConvergenceMax(convergence);
        Integer[] bestSolutionCities = (Integer[]) gaRunner.runIslandGA(5, 10);
        System.out.println("Number of the variables " + numberOfTheCities);
        System.out.println("Number of the generations " + numberOfTheGenerations);
        System.out.println("Population Size " + populationSize);
        System.out.println("Convergence " + convergence);
        System.out.println("Multipoint crossover " + multipoint);
        System.out.println("Number of crossover points " + numberOfCrossPoints);
        System.out.println("Selection method: " + selectionMethod);
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
