package com.rebiekong.bdt.fly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel
public class ResponseData {
    public ResponseData() {
    }

    public ResponseData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    @ApiModelProperty
    private List<Map<String, Object>> data;
}
