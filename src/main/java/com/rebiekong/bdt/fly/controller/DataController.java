package com.rebiekong.bdt.fly.controller;

import com.rebiekong.bdt.fly.ResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 所有项目指定时间点数据
     *
     * @param time 指定的时间点
     * @return 数据
     */
    @ApiOperation("所有项目指定时间点数据")
    @GetMapping(path = "/data/all/point/data")
    public ResponseData p0(@ApiParam(name = "time", value = "指定的时间点", example = "2018-01-05 12:00:00") @RequestParam(name = "time", required = false, defaultValue = "") String time) {
        return p2("", time);
    }

    /**
     * 所有项目指定时间点数据
     *
     * @return 数据
     */
    @ApiOperation("所有项目指定时间点数据")
    @GetMapping(path = "/data/current/summary")
    public ResponseData p1() {
        return new ResponseData(jdbcTemplate.queryForList("SELECT * FROM disney_summary"));
    }

    /**
     * 单个项目指定时间点数据
     *
     * @param project 项目名
     * @param time    指定的时间点
     * @return 数据
     */
    @GetMapping(path = "/data/project/point/data")
    @ApiOperation("单个项目指定时间点数据")
    public ResponseData p2(
            @ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false, defaultValue = "") String project,
            @ApiParam(name = "time", value = "指定的时间点", example = "2018-01-05 12:00:01") @RequestParam(name = "time", required = false, defaultValue = "") String time
    ) {
        return p3(project, time, 0);
    }

    /**
     * 单个项目指定时间点数据
     *
     * @param project 项目名
     * @param time    指定的时间点
     * @return 数据
     */
    @GetMapping(path = "/data/project/point-before/avg")
    @ApiOperation("单个项目指定时间点前一定时间区间平均数据")
    public ResponseData p3(
            @ApiParam(name = "project", value = "项目名", example = "雷鸣山漂流") @RequestParam(name = "project", required = false, defaultValue = "") String project,
            @ApiParam(name = "time", value = "指定的时间点", example = "2018-01-05 12:00:01") @RequestParam(name = "time", required = false, defaultValue = "") String time,
            @ApiParam(name = "time_flap", value = "时间区间（分钟）", example = "60") @RequestParam(name = "time", required = false, defaultValue = "0") int length
    ) {
        if (!project.equals("")) {
            return new ResponseData(jdbcTemplate.queryForList("SELECT flag_date,project,queue_wait FROM disney WHERE toStartOfFiveMinute(time)=toStartOfFiveMinute(toDateTime(?)) AND project=? ORDER BY queue_wait desc", time, project));
        } else {
            return new ResponseData(jdbcTemplate.queryForList("SELECT flag_date,project,queue_wait FROM disney WHERE toStartOfFiveMinute(time)=toStartOfFiveMinute(toDateTime(?)) ORDER BY queue_wait desc", time));
        }
    }

    /**
     * 项目名清单
     *
     * @return 数据
     */
    @GetMapping(path = "/data/project/names")
    @ApiOperation("项目名清单")
    @Cacheable(cacheNames = "data-item-names")
    public ResponseData n0() {
        return new ResponseData(jdbcTemplate.queryForList("SELECT project,avg(queue_wait) AS waiting FROM default.disney GROUP BY project HAVING waiting > 0 ORDER BY waiting DESC"));
    }


}
