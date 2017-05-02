import entities.*;
import logic.PlanningSolution;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import wrapper.SolverNRP;

import java.util.*;

/**
 * Created by kredes on 27/03/2017.
 */
public class SolverNRPTest {
    private static SolverNRP solver;
    private static RandomThings random;
    private static Validator validator;

    /*   -------------
        | AUX METHODS |
         -------------
     */
    private <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }


    @BeforeClass
    public static void setUpBeforeClass() {
        solver = new SolverNRP();
        random = new RandomThings();
        validator = new Validator();
    }

    /**
     *  - Situation: We need to plan only one Feature that requires two Skills and we have
     * a Resource that only has one of them.
     *  - Expected: The generated solution should not have any PlannedFeature.
     */
    @Test
    public void notEnoughSkilledResourcesForFeature() {
        Skill commonSkill = new Skill("Common skill");
        Skill nonCommonSkill = new Skill("Non-common skill");

        List<Skill> featureSkills = new ArrayList<>();
        featureSkills.add(commonSkill);
        featureSkills.add(nonCommonSkill);
        List<Feature> features = Arrays.asList(
                new Feature("Test Feature", PriorityLevel.FIVE, 100.0, null, featureSkills)
        );

        List<Skill> employeeSkills = new ArrayList<>();
        employeeSkills.add(commonSkill);
        List<Employee> employees = Arrays.asList(
                new Employee("Test Employee", 41.0, employeeSkills)
        );

        PlanningSolution solution = solver.executeNRP(3, 40.0, features, employees);

        Assert.assertTrue(solution.getPlannedFeatures().isEmpty());
    }

    /**
     * Validate that precedences between features are respected even if there are enough employees
     * to work on them at the same time.
     */
    @Test
    public void precencesAreRespected() {
        Skill s1 = random.skill();

        List<Feature> features = random.featureList(2);
        List<Employee> employees = random.employeeList(2);

        employees.get(0).getSkills().add(s1);
        employees.get(1).getSkills().add(s1);

        features.get(0).getRequiredSkills().add(s1);
        features.get(1).getRequiredSkills().add(s1);

        features.get(1).getPreviousFeatures().add(features.get(0));

        PlanningSolution solution = solver.executeNRP(3, 40, features, employees);

        validator.validateDependencies(solution);
    }
    
    @Test
    public void featureDependingOnItselfIsNotPlanned() {
        Skill s1 = random.skill();
        Feature f1 = random.feature();
        Employee e1 = random.employee();

        e1.getSkills().add(s1);

        f1.getRequiredSkills().add(s1);

        f1.getPreviousFeatures().add(f1);

        PlanningSolution solution = solver.executeNRP(3, 40, asList(f1), asList(e1));
        Assert.assertTrue(solution.getPlannedFeatures().isEmpty());
    }


    @Test
    public void featuresCausingDependencyDeadlockAreNotPlanned() {
        Skill s1 = random.skill();
        List<Feature> features = random.featureList(2);
        List<Employee> employees = random.employeeList(2);

        Feature f0 = features.get(0);
        Feature f1 = features.get(1);

        f0.getRequiredSkills().add(s1);
        f1.getRequiredSkills().add(s1);

        employees.get(0).getSkills().add(s1);
        employees.get(1).getSkills().add(s1);

        f0.getPreviousFeatures().add(f1);
        f1.getPreviousFeatures().add(f0);

        PlanningSolution solution = solver.executeNRP(3, 40.0, features, employees);
        Assert.assertTrue(solution.getPlannedFeatures().isEmpty());
    }

    @Test
    public void testTest() {
        Skill s1 = random.skill();
        List<Feature> features = random.featureList(2);
        List<Employee> employees = random.employeeList(2);

        Feature f0 = features.get(0);
        Feature f1 = features.get(1);

        f0.getRequiredSkills().add(s1);
        f1.getRequiredSkills().add(s1);

        employees.get(0).getSkills().add(s1);
        employees.get(1).getSkills().add(s1);

        f1.getPreviousFeatures().add(f0);

        PlanningSolution solution = solver.executeNRP(3, 40.0, features, employees);

        validator.validateDependencies(solution);
    }
}
