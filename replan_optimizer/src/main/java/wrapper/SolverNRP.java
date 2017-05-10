package wrapper;

import entities.Employee;
import entities.Feature;
import entities.PlannedFeature;
import logic.NextReleaseProblem;
import logic.PlanningSolution;
import logic.PopulationFilter;
import logic.comparators.PlanningSolutionDominanceComparator;
import logic.operators.PlanningCrossoverOperator;
import logic.operators.PlanningMutationOperator;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.mocell.MOCellBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import java.util.List;
import java.util.Set;


public class SolverNRP {

    public enum AlgorithmType {
        NSGAII("NSGA-II"), MOCell("MOCell");

        private String name;

        AlgorithmType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /*
        TODO: If you have whatever number of features that fit exactly into the available hours it won't plan them.
        Most likely because of a < where there should be a <=. The problem is caused by hoursPerWeek,
        not by the employee availability (ex: 120 hours and three features of 40 hours each -> It only plans two of them)
        ACTUALLY not, the employee availability also causes trouble.
    */

    private Algorithm<List<PlanningSolution>> algorithm;
    private AlgorithmType algorithmType;


    public SolverNRP() {
        algorithm = null;
        algorithmType = AlgorithmType.NSGAII;
    }

    public SolverNRP(AlgorithmType algorithmType) {
        this();
        this.algorithmType = algorithmType;
    }


    private Algorithm<List<PlanningSolution>> createAlgorithm(AlgorithmType algorithmType, NextReleaseProblem problem) {
        CrossoverOperator<PlanningSolution> crossover;
        MutationOperator<PlanningSolution> mutation;
        SelectionOperator<List<PlanningSolution>, PlanningSolution> selection;

        crossover = new PlanningCrossoverOperator(problem);
        mutation = new PlanningMutationOperator(problem);
        selection = new BinaryTournamentSelection<>(new PlanningSolutionDominanceComparator());

        switch (algorithmType) {
            case NSGAII:
                return new NSGAIIBuilder<PlanningSolution>(problem, crossover, mutation)
                        .setSelectionOperator(selection)
                        .setMaxIterations(500)
                        .setPopulationSize(100)
                        .build();
            case MOCell:
                return new MOCellBuilder<PlanningSolution>(problem, crossover, mutation)
                        .setSelectionOperator(selection)
                        .setMaxEvaluations(5000)
                        .setPopulationSize(200)
                        .build();
            default:
                return createAlgorithm(AlgorithmType.NSGAII, problem);
        }
    }

    /* ---------------------------- */
    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }
    /* ---------------------------- */

    public PlanningSolution executeNRP(int nbWeeks, Number hoursPerweek, List<Feature> features, List<Employee> employees){

        EntitiesEvaluator ee = EntitiesEvaluator.getInstance();

        NextReleaseProblem problem = ee.nextReleaseProblemAddSkills(nbWeeks, hoursPerweek, features, employees);

        PlanningSolution solution = this.generatePlanningSolution(problem);

        clearSolutionIfNotValid(solution);

        return ee.planningSolution(solution);
    }

    public PlanningSolution executeNRP(int nbWeeks, Number hoursPerweek, List<Feature> features,
                                       List<Employee> employees, PlanningSolution previousSolution) {
        EntitiesEvaluator ee = EntitiesEvaluator.getInstance();



        NextReleaseProblem problem = ee.nextReleaseProblemAddSkills(nbWeeks, hoursPerweek, features, employees);

        problem.setPreviousSolution(previousSolution);

        PlanningSolution solution = this.generatePlanningSolution(problem);

        clearSolutionIfNotValid(solution);

        return ee.planningSolution(solution);
    }

    /*
        The generated solution might violate constraints in case the solver does not find a better one.
        In that case, the planning is invalid and should be cleared.
    */
    private void clearSolutionIfNotValid(PlanningSolution solution) {
        NumberOfViolatedConstraints<logic.PlanningSolution> numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
        if (numberOfViolatedConstraints.getAttribute(solution) > 0) {
            solution.getEmployeesPlanning().clear();
            for (PlannedFeature plannedFeature : solution.getPlannedFeatures())
                solution.unschedule(plannedFeature);
        }
    }

    private PlanningSolution generatePlanningSolution(NextReleaseProblem problem) {

        algorithm = createAlgorithm(algorithmType, problem);

        new AlgorithmRunner.Executor(algorithm).execute();

        List<PlanningSolution> population = algorithm.getResult();
        Set<PlanningSolution> bestSolutions = PopulationFilter.getBestSolutions(population);

        return bestSolutions.iterator().next();
    }



}
