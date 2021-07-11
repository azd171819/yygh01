package com.atguigu.yygh.hosp.mapper;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface HospitalSetMapper extends BaseMapper<HospitalSet> {

    HospitalSet selectOne(QueryWrapper<HospitalSet> hoscode);


}
