package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    /**
     * 医院预约挂号详情
     */
    Map<String, Object> item(String hoscode);


    /**
     * 根据医院编号获取医院名称接口
     * @param hoscode
     * @return
     */
    String getName(String hoscode);


    /**
     * 医院详情
     * @param id
     * @return
     */
    Map<String, Object> show(String id);



    /**
     * 更新上线状态
     */
    void updateStatus(String id, Integer status);


    /**
     * 上传医院信息
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询医院
     * @param hospCode
     * @return
     */
    Hospital getByHospcode(String hospCode);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param hospitalQueryVo 查询条件
     * @return
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);


    /**
     * 根据医院名称获取医院列表
     * @param hosname
     * @return
     */
    List<Hospital> findByHosname(String hosname);


}
