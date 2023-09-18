package com.keita.riggs.system_info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HealthActuator implements HealthIndicator {
    private final DataSource dataSource;
    private final MetricsEndpoint metricsEndpoint;

    @Autowired
    public HealthActuator(DataSource dataSource, MetricsEndpoint metricsEndpoint) {
        this.dataSource = dataSource;
        this.metricsEndpoint = metricsEndpoint;
    }

    @Override
    public Health health() {
        String DATABASE_SERVICE = "Database Service";
        Map<String, Object> health = new HashMap<>();

        if (isDatabaseHealthGood()) {
            health.put("Database Service", "Service is running");
            health.put("uptime", String.valueOf(getUptime()));
            return Health.up().withDetails(health).build();
        }
        return Health.down().withDetail(DATABASE_SERVICE, "Service is not running").build();
    }

    private double getUptime() {
        return metricsEndpoint.metric("process.uptime", null).getMeasurements().get(0).getValue();
    }

    private boolean isDatabaseHealthGood() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM room");
            connection.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}
