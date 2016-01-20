package geneticClasses;

/**
 * Created by Michal Dorko on 30/10/15.
 * BSc Final Year project
 * King's College London
 * Map-Reduce library for Genetic Algorithms
 * Licensed under the Academic Free License version 3.0
 */
public class Population {

    private Individual[] individuals;
    private int sizeOfPopulation;
    private int sumOfFitnesses;


    public Population(int sizeOfPopulation) {
        this.sizeOfPopulation = sizeOfPopulation;
        individuals = new Individual[this.sizeOfPopulation];
    }

    public void initializePopulationBinary() {
        for(int i = 0; i < sizeOfPopulation; i++) {
            BinaryIndividual individual = new BinaryIndividual();
            individual.generateRandomIndividual();
            this.individuals[i] = individual;
        }
    }

    public void initializePopulationString() {
        for(int i = 0; i < sizeOfPopulation; i++) {
            StringIndividual individual = new StringIndividual();
            individual.generateRandomIndividual();
            this.individuals[i] = individual;
        }
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public int getSizeOfPopulation() {
        return sizeOfPopulation;
    }

    public void saveIndividual(Individual individual, int index) {
        this.individuals[index] = individual;
    }

    public Individual getFittestIndividual() {
        Individual fittestIndividual = null;
        int maxFitness = 0;
        for (Individual i : individuals) {
            if (i.getFitness() > maxFitness) {
                fittestIndividual = i;
                maxFitness = i.getFitness();
            }
        }
        // If whole population has fitness 0 then return first individual
        if (fittestIndividual == null) {
            fittestIndividual = individuals[0];
        }
        return  fittestIndividual;
    }

    public int getSumOfFitnesses() {
        return sumOfFitnesses;
    }

    public void calculateSumOfFitnesses() {
        for (Individual bi: individuals) {
            this.sumOfFitnesses += bi.getFitness();
        }
    }

}
