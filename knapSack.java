import java.util.*;
import java.io.*;

public class knapSack {

	private Project[] projects;
	private int capacity;

	public Solution knapSackSolver(Project[] projects, int capacity) {
		this.projects = projects;
		this.capacity = capacity;

		Solution result = solve();
		return result;
	}

	private static class Project {
		// ...
		public String projectName;
		public int empWorkWeeks;
		public int projectProfit;

		public Project(String projectName, int empWorkWeeks, int projectProfit) {
			this.projectName = projectName;
			this.empWorkWeeks = empWorkWeeks;
			this.projectProfit = projectProfit;
		}
	}

	private class Solution {
		public ArrayList<Project> projectSolution;
		public int[][] matrix;

		public Solution(ArrayList<Project> projectSolution, int[][] matrix) {
			this.projectSolution = projectSolution;
			this.matrix = matrix;
		}
	}

	public Solution solve() {
		int NB_PROJECTS = projects.length;
		// Using a matrix to store the max value at each n-th project
		int[][] matrix = new int[NB_PROJECTS + 1][capacity + 1];

		// first line is initialized to 0
		for (int i = 0; i <= capacity; i++)
			matrix[0][i] = 0;

		// iterate on projects
		for (int i = 1; i <= NB_PROJECTS; i++) {
			// iterate on each capacity
			for (int j = 0; j <= capacity; j++) {
				if (projects[i - 1].empWorkWeeks > j)
					matrix[i][j] = matrix[i - 1][j];
				else
					// Maximizing project profit at this rank in the matrix
					matrix[i][j] = Math.max(matrix[i - 1][j],
							matrix[i - 1][j - projects[i - 1].empWorkWeeks] + projects[i - 1].projectProfit);
			}
		}

		int res = matrix[NB_PROJECTS][capacity];
		int w = capacity;
		ArrayList<Project> projectsSolution = new ArrayList<>();
		projectsSolution.sort(null);
		for (int i = NB_PROJECTS; i > 0 && res > 0; i--) {
			if (res != matrix[i - 1][w]) {
				projectsSolution.add(projects[i - 1]);
				// we remove items value and weight
				res -= projects[i - 1].projectProfit;
				w -= projects[i - 1].empWorkWeeks;
			}
		}

		Solution solution = new Solution(projectsSolution, matrix);

		return solution;
	}

	public static void main(String[] args) throws FileNotFoundException {
		// ...
		// Here is code for opening a text file and reading from it,
		// and storing the information in an ArrayList of Project.
		// You would need to write the Project class yourself,
		// and also declare and initialize inputFileName (String).
		Scanner userInput = new Scanner(System.in);
		String inputFileName, outputFileName;
		int allowedWorkWeeks;
		int selectedProfit = 0;

		// get the allowed number of work weeks available in total
		System.out.println("Enter the number of available employee work weeks: ");
		allowedWorkWeeks = userInput.nextInt();
		userInput.nextLine();

		// get the input text file with project array
		System.out.println("Enter the name of input file: ");
		inputFileName = userInput.nextLine().trim();

		// get the output text file with project array
		System.out.println("Enter the name of output file: ");
		outputFileName = userInput.nextLine().trim();
		userInput.close();

		Scanner dataFile = new Scanner(new File(inputFileName));
		ArrayList<Project> projects = new ArrayList<>();
		while (dataFile.hasNextLine()) {
			String line = dataFile.nextLine();
			String[] split = line.split(" ");
			projects.add(new Project(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
		}
		dataFile.close();

		System.out.print("Number of Projects: " + projects.size());

		Project[] projectList = new Project[projects.size()];
		projects.toArray(projectList);

		knapSack kSack = new knapSack();
		Solution selectedProject = kSack.knapSackSolver(projectList, allowedWorkWeeks);

		// Here is code for writing the output to a text file
		PrintWriter outputFile = new PrintWriter(outputFileName);

		// Display the number of projects read from the input text file
		outputFile.print("Number of projects available: " + projects.size());
		outputFile.print(System.lineSeparator());
		outputFile.print("Available employee work weeks: " + allowedWorkWeeks);
		outputFile.print(System.lineSeparator());
		outputFile.print("Number of projects chosen: " + selectedProject.projectSolution.size());
		outputFile.print(System.lineSeparator());

		for (int i = 0; i < selectedProject.projectSolution.size(); i++) {
			selectedProfit += selectedProject.projectSolution.get(i).projectProfit;
		}
		outputFile.print("Total Profit: " + selectedProfit);
		outputFile.print(System.lineSeparator());

		for (int i = 0; i < selectedProject.projectSolution.size(); i++) {
			outputFile.print(selectedProject.projectSolution.get(i).projectName + " ");
			outputFile.print(selectedProject.projectSolution.get(i).empWorkWeeks + " ");
			outputFile.print(selectedProject.projectSolution.get(i).projectProfit);
			outputFile.print(System.lineSeparator());
		}

		outputFile.close();
		System.out.println("Done");
	}
}