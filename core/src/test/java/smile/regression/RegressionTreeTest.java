/*******************************************************************************
 * Copyright (c) 2010-2019 Haifeng Li
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/

package smile.regression;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import smile.data.*;
import smile.data.formula.Formula;
import smile.math.MathEx;
import smile.sort.QuickSort;
import smile.validation.CrossValidation;
import smile.validation.LOOCV;
import smile.validation.Validation;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Haifeng Li
 */
public class RegressionTreeTest {
    
    public RegressionTreeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of predict method, of class RegressionTree.
     */
    @Test
    public void testLongley() {
        System.out.println("longley");

        RegressionTree model = RegressionTree.fit(Longley.formula, Longley.data, 2, 100);
        System.out.println("----- dot -----");
        System.out.println(model.dot());

        double[] importance = model.importance();
        System.out.println("----- importance -----");
        for (int i = 0; i < importance.length; i++) {
            System.out.format("%-15s %.4f%n", Longley.data.schema().fieldName(i), importance[i]);
        }

        double rmse = LOOCV.test(Longley.data, (x) -> RegressionTree.fit(Longley.formula, Longley.data, 2, 100));

        System.out.println("LOOCV MSE = " + rmse);
        assertEquals(1.5771480061596428, rmse, 1E-4);
    }

    public void test(String name, Formula formula, DataFrame data, double expected) {
        System.out.println(name);

        RegressionTree model = RegressionTree.fit(formula, data);
        System.out.println("----- dot -----");
        System.out.println(model);

        double[] importance = model.importance();
        System.out.println("----- importance -----");
        for (int i = 0; i < importance.length; i++) {
            System.out.format("%-15s %.4f%n", data.schema().fieldName(i), importance[i]);
        }

        double rmse = CrossValidation.test(10, data, x -> RegressionTree.fit(formula, x));
        System.out.format("10-CV RMSE = %.4f%n", rmse);
        assertEquals(expected, rmse, 1E-4);
    }

    /**
     * Test of learn method, of class RegressionTree.
     */
    @Test
    public void testAll() {
        test("CPU", CPU.formula, CPU.data, 88.6985);
        test("2dplanes", Planes.formula, Planes.data, 2.0978);
        test("abalone", Abalone.formula, Abalone.train, 2.5626);
        test("ailerons", Ailerons.formula, Ailerons.data, 0.0003);
        test("bank32nh", Bank32nh.formula, Bank32nh.data, 0.0983);
        test("autoMPG", AutoMPG.formula, AutoMPG.data, 3.8180);
        test("cal_housing", CalHousing.formula, CalHousing.data, 83802.5084);
        test("puma8nh", Puma8NH.formula, Puma8NH.data, 4.0458);
        test("kin8nm", Kin8nm.formula, Kin8nm.data, 0.2189);
    }
}
