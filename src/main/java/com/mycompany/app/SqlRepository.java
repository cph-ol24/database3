package com.mycompany.app;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class SqlRepository implements IRepository {
    private static final String depthOne = "SELECT id, name, job, birthday FROM social_network.t_user WHERE "
            + "t_user.id IN (SELECT sourceId FROM social_network.t_endorsement WHERE targetId = ?);";

    private static final String depthTwo = "SELECT id, name, job, birthday FROM social_network.t_user WHERE "
            + "t_user.id IN (SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ( "
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId = ?));";

    private static final String depthThree = "SELECT id, name, job, birthday FROM social_network.t_user WHERE "
            + "t_user.id IN (SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ( "
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ( "
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId = ?)));";

    private static final String depthFour = "SELECT id, name, job, birthday FROM social_network.t_user WHERE "
            + "t_user.id IN (SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ( "
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ("
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ("
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId = ?))));";

    private static final String depthFive = "SELECT id, name, job, birthday FROM social_network.t_user WHERE "
            + "t_user.id IN (SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ( "
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ("
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ("
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId IN ("
            + "SELECT sourceId FROM social_network.t_endorsement WHERE targetId = ?)))));";

    private static final String[] sqlLookup = new String[] { depthOne, depthTwo, depthThree, depthFour, depthFive };

    public List<Person> findEndorments(int personId, int depth) {
        String sql = sqlLookup[depth];

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3307/social_network";
        String user = "root";
        String password = "pwd";

        List<Person> results = new ArrayList<Person>();

        try {

            con = DriverManager.getConnection(url, user, password);

            st = con.prepareStatement(sql);
            st.setInt(1, personId);

            rs = st.executeQuery();

            while (rs.next()) {
                Person person = new Person(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                results.add(person);
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.err.println(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                System.err.println(ex);
            }
        }

        return results;
    }
}