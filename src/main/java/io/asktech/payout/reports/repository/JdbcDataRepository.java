package io.asktech.payout.reports.repository;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import io.asktech.payout.reports.customInterface.DataRepository;
import io.asktech.payout.reports.dto.ReportData;



@Repository
public class JdbcDataRepository implements DataRepository{

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Override
    public List<ReportData> executeQuery(String query) {
        return jdbcTemplate.query(
        		query,               
                (rs, rowNum) ->
                       new ReportData(
                    		rs.getString("data")
                        )
        );
    }
}
