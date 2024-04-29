package com.grogu.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentcontrollerServlet
 */
@WebServlet("/StudentcontrollerServlet")
public class StudentcontrollerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		//create our student db util .. and pass in the conn poll / datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//read the "command" parameter
		String theCommand = request.getParameter("command");
		
		if(theCommand == null) {
			theCommand = "LIST";
		}
		
		switch(theCommand) {
		
		//route to the appropriate method
		case "LIST":
				listStudents(request,response);
				break;
				
		case "ADD":
			addStudents(request,response);
			break;
			
		case "LOAD":
			try {
				loadStudent(request,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "UPDATE":
			try {
				updateStudent(request,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "DELETE":
			try {
				deleteStudent(request,response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		default:
			//list the students in MVC fashion
			listStudents(request,response);
		}
		
		
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student id from the form data
		String StudentId = request.getParameter("studentId");
		
		//delete student from DB
		studentDbUtil.deletStudent(StudentId);
		
		//send the user back to students page
		listStudents(request,response);
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student information from the form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student obj base on form data
		Student theStudent = new Student(id,firstName,lastName,email);
		
		//perform update on DB
		studentDbUtil.updateStudent(theStudent);
		
		//send them back to main view
		listStudents(request,response);
		
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response){
		// read student id from form data
		
		String theStudentId = request.getParameter("studentId");
		
		//get student from database (DB util)
		Student theStudent;
		try {
			theStudent = StudentDbUtil.getStudent(theStudentId);
			
			//place student in the request attribute
			request.setAttribute("THE_STUDENT", theStudent);
			
			//send to jps page: update-student-from.jsp
			RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
			dispatcher.forward(request, response);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error en metodo load");
		}
	
		
		
		
	}



	private void addStudents(HttpServletRequest request, HttpServletResponse response) {
		
		//read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		
		//create a new student object
		Student theStudent = new Student(firstName,lastName,email);
		
		
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		//send back to main
		listStudents(request, response);
		
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) {
		 
		
		
		
			try {
				//get  students from db util 
				List<Student>  students = studentDbUtil.getStudents();
				
				//add students to the request
				request.setAttribute("STUDENT_LIST", students);
				
				//send to JSP page (view)
				RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
				dispatcher.forward(request, response);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		
		
		
		
	}

}
