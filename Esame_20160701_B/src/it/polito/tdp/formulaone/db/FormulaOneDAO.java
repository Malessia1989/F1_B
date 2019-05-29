package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Adiacenza;
import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors(Map<Integer, Constructor> idMap) {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				
				if(idMap.get(rs.getInt("constructorId")) == null) {
					
				Constructor c= new Constructor(rs.getInt("constructorId"), rs.getString("name"));
				
				constructors.add(c);				
				idMap.put(c.getConstructorId(), c);
				}else {
					constructors.add(idMap.get(rs.getInt("constructorId")));
				}
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}


	public static void main(String[] args) {
		FormulaOneDAO dao = new FormulaOneDAO() ;
		
		List<Integer> years = dao.getAllYearsOfRace() ;
		System.out.println(years);
		
		List<Season> seasons = dao.getAllSeasons() ;
		System.out.println(seasons);

		
		List<Circuit> circuits = dao.getAllCircuits();
		System.out.println(circuits);

		//List<Constructor> constructors = dao.getAllConstructors();
		//System.out.println(constructors);
		
	}

	public List<Adiacenza> getAdiacenze(Circuit c, Map<Integer, Constructor> idMap) {
		
		String sql="SELECT r1.constructorId id1  ,r2.constructorId id2 ,COUNT(*) AS peso " + 
				"FROM results r1, results r2, races r " + 
				"WHERE r1.raceId = r2.raceId AND r1.POSITION < r2.POSITION AND r1.constructorId > r2.constructorId " + 
				"AND r.raceId= r1.raceId AND r.circuitId =? " + 
				"GROUP BY r1.constructorId, r2.constructorId ";
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, c.getCircuitId());
			ResultSet rs = st.executeQuery();

			List<Adiacenza> list = new ArrayList<>();
			while (rs.next()) {
				Constructor c1= idMap.get(rs.getInt("id1"));
				Constructor c2= idMap.get(rs.getInt("id2"));
				
				Adiacenza adj=new Adiacenza(c1, c2, rs.getDouble("peso"));
				list.add(adj);
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	
	}
	
}
