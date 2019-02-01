package com.rebiekong.bdt.fly.controller;

import com.rebiekong.bdt.fly.ResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/2018")
public class Data2018Controller {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Data2018Controller(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 单项目每小时每月分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目每小时每月分布")
    @GetMapping(path = "/project/date-hour/avg")
    public ResponseData p0(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  day_of_month,hour_of_day,avg(queue_wait) as delay FROM disney_2018 WHERE project<>'' AND project IS NOT NULL GROUP BY day_of_month,hour_of_day"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  day_of_month,hour_of_day,avg(queue_wait) as delay FROM disney_2018 WHERE project=? GROUP BY day_of_month,hour_of_day", project));
        }
    }

    /**
     * 单项目每小时一星期七天分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目每小时一星期七天分布")
    @GetMapping(path = "/project/week-hour/avg")
    public ResponseData p1(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  day_of_week,hour_of_day,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY day_of_week,hour_of_day"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  day_of_week,hour_of_day,floor(avg(queue_wait)) as delay FROM disney_2018 WHERE project=? GROUP BY day_of_week,hour_of_day", project));
        }
    }

    /**
     * 单项目每小时各月份分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目每小时各月份分布")
    @GetMapping(path = "/project/month-hour/avg")
    public ResponseData p2(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  month_of_year,hour_of_day,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY month_of_year,hour_of_day"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  month_of_year,hour_of_day,floor(avg(queue_wait)) as delay FROM disney_2018 WHERE project=? GROUP BY month_of_year,hour_of_day", project));
        }
    }

    /**
     * 单项目每日各月份分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目每日各月份分布")
    @GetMapping(path = "/project/month-date/avg")
    public ResponseData p3(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  month_of_year,day_of_month,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY month_of_year,day_of_month"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  month_of_year,day_of_month,floor(avg(queue_wait)) as delay FROM disney_2018 WHERE project=? GROUP BY month_of_year,day_of_month", project));
        }
    }

    /**
     * 单项目所有时间分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目所有时间分布")
    @GetMapping(path = "/project/day/avg")
    public ResponseData p4(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT date,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY date"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT date,floor(avg(queue_wait)) as delay FROM disney_2018 WHERE project=? GROUP BY date", project));
        }
    }

    /**
     * 单项目所有时间分布
     *
     * @param project 项目名
     * @return 数据
     */
    @ApiOperation("单项目所有时间分布")
    @GetMapping(path = "/project/day/w-avg")
    public ResponseData p5(@ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false) String project) {
        if (project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT  min(date) as date,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY week_of_year ORDER BY date ASC"));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT   min(date) as date,floor(avg(queue_wait)) as delay FROM disney_2018 WHERE project=? GROUP BY week_of_year ORDER BY date ASC", project));
        }
    }

    /**
     * 所有项目所有时间分布
     *
     * @return 数据
     */
    @ApiOperation("所有项目所有时间分布")
    @GetMapping(path = "/all/day/w-avg")
    public ResponseData p6() {
        return new ResponseData(jdbcTemplate.queryForList("SELECT a.* FROM (SELECT  date,project,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY date,project) as a cross join (SELECT project,floor(avg(queue_wait)) as delay FROM disney_2018 GROUP BY project) as b where a.project='翱翔•飞越地平线' AND a.project = b.project and b.delay > 40"));
    }
}
