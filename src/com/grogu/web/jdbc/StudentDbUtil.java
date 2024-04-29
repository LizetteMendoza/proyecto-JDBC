package com.grogu.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	
	private static DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		
		dataSource = theDataSource;
	}
	
	public List<Student>  getStudents( )throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
		
		//get a connection
			myConn = dataSource.getConnection();
	
		//create sql statement
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
		
		//execute query
			myRs = myStmt.executeQuery(sql);
		
		//process result wet
			while(myRs.next()) {
				
				//retrive data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//create new student obj
				Student tempStudent = new Student(id,firstName,lastName,email);

				//add it to list of students
				students.add(tempStudent);
			}
		
			return students;
			
		}finally {
			
			//close JDBC objects
			close(myConn,myStmt,myRs);
			
			
		}
	
	}
	
	//Close method
	private static void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if(myConn !=null) {
				myConn.close();
			}
			if(myStmt != null) {
				myStmt.close();
			}
			if(myRs != null) {
				myRs.close();
			}
			
			
		}catch(Exception exc){
			exc.printStackTrace();
		}		
	}
	
	//Close method
		private static void close(Connection myConn, Statement myStmt) {
			try {
				if(myConn !=null) {
					myConn.close();
				}
				if(myStmt != null) {
					myStmt.close();
				}
				
				
			}catch(Exception exc){
				exc.printStackTrace();
			}		
		}

	public void addStudent(Student theStudent) {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
	
		
		try {
			
			//get db connection
			myConn = dataSource.getConnection();
			
			//create SQL for insert
			String sql = "insert into student (first_name,last_name,email) values(?,?,?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			//set the param values for the student
			myStmt.setString(1,theStudent.getFirstName());
			myStmt.setString(2,theStudent.getLastName());
			myStmt.setString(3,theStudent.getEmail());	
			
			//execute SQL insert
			myStmt.execute();
		
		}catch(Exception e){
			
		}finally {
			
			//close JDBC objects
			close(myConn,myStmt);
			
			
		}
		
	}

	public static Student getStudent(String theStudentId) throws Exception{
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = dataSource.getConnection();
			
			//create SQL to get selected student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set parameters
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set now
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				
				//use the studentID during contruction
				theStudent = new Student(studentId,firstName,lastName,email);
			}else {
				throw new Exception("Could not find student id: "+studentId);
			}
			
			return theStudent;
		}finally {
			
			close(myConn,myStmt,myRs);
		}
		
		
	}

	public void updateStudent(Student theStudent){
		
		Connection myConn= null;
		PreparedStatement myStmt = null;
		
		try{
			
		
		//get DB connection
		myConn = dataSource.getConnection();
		
		
		//Create SQL update statement
		String sql = "update student set first_name=?, last_name=?, email=? where id=?";
		
		//prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		//set parameters
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		
		//execute SQL statement
		myStmt.execute();
		
		}catch(Exception e) {
			System.out.println("erro en update");
		}finally {
			close(myConn,myStmt);
		}
	}

	public void deletStudent(String studentId) {
		Connection myConn= null;
		PreparedStatement myStmt = null;
		
		try{
		 //convert student id to int
		int theStudentId = Integer.parseInt(studentId);
		
		//get DB connection
		myConn = dataSource.getConnection();
		
		
		//Create SQL update statement
		String sql = "delete from student where id=?";
		
		
		//prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		//set parameters
		myStmt.setInt(1, theStudentId);
		
		
		//execute SQL statement
		myStmt.execute();
		
		
		
		
		}catch(Exception e) {
			System.out.println("error en delete method");
			
		}finally{
			close(myConn,myStmt);
		}
		
	}

	

}
