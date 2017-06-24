package com.inmobi.microq.dao;

import com.inmobi.microq.models.MSTestCase;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author prathik.raj
 */
@Slf4j
public class MSTestCaseDAO {

    /**
     * Returns all MSTestCases for given pull request and service, 0 means unassigned
     * @param service Name of the service (usually git repo)
     * @param pullRequest Pull requests against this service
     * @return MSTestCases for the service and pullRequest
     */
    public List<MSTestCase> getTestCasesForPullRequest(@NonNull String service, @NonNull Integer pullRequest)
            throws MSTSDaoException {

        log.debug("fetching pr for: " + service + " and pull request: " + String.valueOf(pullRequest));
        List<MSTestCase> msTestCases = new ArrayList<>();
        try {

            MySQLConnectionPool connectionPool = MySQLConnectionPool.getInstance();
            Connection connection = connectionPool.getDs().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mstspr WHERE " +
                    "service = ? AND pull_request = ?");
            preparedStatement.setString(1, service);
            preparedStatement.setInt(2, pullRequest);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                MSTestCase msTestCase = new MSTestCase();
                msTestCase.setId(rs.getInt("id"));
                msTestCase.setName(rs.getString("name"));
                msTestCase.setHttpMethod(rs.getString("http_method"));
                msTestCase.setExpectedResponse(rs.getString("expected_response"));
                msTestCase.setHost(rs.getString("host"));
                msTestCase.setUser(rs.getString("user"));
                msTestCase.setService(rs.getString("service"));
                msTestCase.setPullRequest(rs.getInt("pull_request"));
                msTestCase.setBody(rs.getString("body"));
                msTestCases.add(msTestCase);
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ConfigurationException e) {
            throw new MSTSDaoException(e.getMessage());
        }
        log.debug("fetched tc: " + msTestCases.toString());
        return msTestCases;
    }

    public List<MSTestCase> getTestCasesForService(@NonNull String service) throws MSTSDaoException {
        log.debug("Fetching all test cases for: " + service);
        List<MSTestCase> msTestCases = new ArrayList<>();
        try {

            MySQLConnectionPool connectionPool = MySQLConnectionPool.getInstance();
            Connection connection = connectionPool.getDs().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mstspr WHERE " +
                    "service = ?");
            preparedStatement.setString(1, service);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                MSTestCase msTestCase = new MSTestCase();
                msTestCase.setName(rs.getString("name"));
                msTestCase.setId(rs.getInt("id"));
                msTestCase.setHttpMethod(rs.getString("http_method"));
                msTestCase.setExpectedResponse(rs.getString("expected_response"));
                msTestCase.setHost(rs.getString("host"));
                msTestCase.setUser(rs.getString("user"));
                msTestCase.setService(rs.getString("service"));
                msTestCase.setPullRequest(rs.getInt("pull_request"));
                msTestCase.setBody(rs.getString("body"));
                msTestCases.add(msTestCase);
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ConfigurationException e) {
            throw new MSTSDaoException(e.getMessage());
        }
        log.debug("fetched tc: " + msTestCases.toString());
        return msTestCases;
    }

    public void updateTestCase(@NonNull MSTestCase testCase) throws MSTSDaoException {
        if(testCase.getId() == null) {
            throw new MSTSDaoException("ID is null, maybe you wanted to call store?");
        }

        try {

            MySQLConnectionPool connectionPool = MySQLConnectionPool.getInstance();
            Connection connection = connectionPool.getDs().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE mstspr set" +
                    " http_method = ?" +
                    ", expected_response = ?, host = ?, user = ?, service = ?, pull_request = ?, body = ?, name = ? " +
                    "WHERE " +
                    "id = ?" );
            preparedStatement.setString(1, testCase.getHttpMethod());
            preparedStatement.setString(2, testCase.getExpectedResponse());
            preparedStatement.setString(3, testCase.getHost());
            preparedStatement.setString(4, testCase.getUser());
            preparedStatement.setString(5, testCase.getService());

            if(testCase.getPullRequest() == null) {
                preparedStatement.setInt(6, 0);
            } else {
                preparedStatement.setInt(6, testCase.getPullRequest());
            }
            preparedStatement.setString(7, testCase.getBody());
            preparedStatement.setString(8, testCase.getName());
            preparedStatement.setInt(9, testCase.getId());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ConfigurationException e) {
            throw new MSTSDaoException(e.getMessage());
        }
    }

    /**
     * Stores test case
     * @param testCase Test case that needs to be stored
     */
    public void storeTestCase(@NonNull MSTestCase testCase) throws MSTSDaoException {
        log.debug("inserting test case: " + testCase.toString());
        if(testCase.getId() != null) {
            throw new MSTSDaoException("ID is not null, maybe you wanted to update?");
        }

        try {

            MySQLConnectionPool connectionPool = MySQLConnectionPool.getInstance();
            Connection connection = connectionPool.getDs().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mstspr (id, http_method" +
                    ", expected_response, host, user, service, pull_request, body, name) VALUES (NULL, " +
                    "?, ?, ?, ?, ?, ?, ?, ?)" );
            preparedStatement.setString(1, testCase.getHttpMethod());
            preparedStatement.setString(2, testCase.getExpectedResponse());
            preparedStatement.setString(3, testCase.getHost());
            preparedStatement.setString(4, testCase.getUser());
            preparedStatement.setString(5, testCase.getService());

            if(testCase.getPullRequest() == null) {
                preparedStatement.setInt(6, 0);
            } else {
                preparedStatement.setInt(6, testCase.getPullRequest());
            }
            preparedStatement.setString(7, testCase.getBody());
            preparedStatement.setString(8, testCase.getName());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ConfigurationException e) {
            throw new MSTSDaoException(e.getMessage());
        }
    }
}
