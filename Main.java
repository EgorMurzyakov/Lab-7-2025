import functions.*;
import threads.*;
import functions.basic.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		// ----- Задание номер 1 -----
		System.out.println("----- Задание 1 -----");

		// Пример для ArrayTabulatedFunction
		FunctionPoint[] points = {
				new FunctionPoint(0, 0),
				new FunctionPoint(1, 1),
				new FunctionPoint(2, 4)
		};
		TabulatedFunction arrayFunc = new ArrayTabulatedFunction(points);

		System.out.println("ArrayTabulatedFunction:");
		for (FunctionPoint p : arrayFunc) {
			System.out.println(p);
		}

		// Пример для LinkedListTabulatedFunction
		TabulatedFunction listFunc = new LinkedListTabulatedFunction(points);

		System.out.println("LinkedListTabulatedFunction:");
		for (FunctionPoint p : listFunc) {
			System.out.println(p);
		}

		// ----- Задание номер 2 -----
		System.out.println("\n\n");
		System.out.println("----- Задание 2 -----");

		// Создаем с фабрикой по умолчанию (ArrayTabulatedFunction)
		Function f = new Cos();
		TabulatedFunction tf;
		tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
		System.out.println("Фабрика по умолчанию: " + tf.getClass());

		// Меняем фабрику на LinkedListTabulatedFunction
		TabulatedFunctions.setTabulatedFunctionFactory(new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
		tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
		System.out.println("LinkedList фабрика: " + tf.getClass());

		// Возвращаем фабрику ArrayTabulatedFunction
		TabulatedFunctions.setTabulatedFunctionFactory(new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
		tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
		System.out.println("Array фабрика: " + tf.getClass());

		// ----- Задание номер 3 -----
		System.out.println("\n\n");
		System.out.println("----- Задание 3 -----");

		TabulatedFunction tabF;

		tabF = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 3);
		System.out.println(tabF.getClass());
		System.out.println(tabF);

		tabF = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
		System.out.println(tabF.getClass());
		System.out.println(tabF);

		tabF = TabulatedFunctions.createTabulatedFunction(LinkedListTabulatedFunction.class,
				new FunctionPoint[] {
						new FunctionPoint(0, 0),
						new FunctionPoint(10, 10)
				}
		);
		System.out.println(tabF.getClass());
		System.out.println(tabF);

		tabF = TabulatedFunctions.tabulate(
				LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
		System.out.println(tabF.getClass());
		//System.out.println(tabF);
		for (FunctionPoint p : tabF) {
			System.out.println(p);
		}
	}
}