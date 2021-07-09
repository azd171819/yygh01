package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.sf.jsqlparser.statement.select.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.PushBuilder;
import java.util.List;
import java.util.Random;

//医院设置
@CrossOrigin
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 查询医院所有信息
     *
     * @return
     */
    @GetMapping("findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        if (!list.isEmpty()) {
            return Result.ok(list);
        } else {
            return Result.fail();
        }
    }

    @DeleteMapping({"id"})
    public Result removeHospSet(@PathVariable Long id) {

        boolean flag = hospitalSetService.removeById(id);

        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }


    /**
     * //3.条件查询带分页
     *
     * @param current
     * @param limit
     * @param hospitalQueryVo
     * @return
     * @RequestBody(required = false) json格式传递参数 required = false 可以为空
     */
    @PostMapping("/findPageHostSet/{current}/{limit}")
    public Result findPageHostSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody (required = false) HospitalQueryVo hospitalQueryVo) {
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalQueryVo.getHosname();//医院名称
        String hoscode = hospitalQueryVo.getHoscode();//医院编号
        if (!StringUtils.isEmpty(hospitalQueryVo.getHosname())) {
            queryWrapper.like("hosname", hospitalQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalQueryVo.getHoscode())) {
            queryWrapper.eq("hoscode", hospitalQueryVo.getHoscode());
        }

        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);
        return Result.ok(hospitalSetPage);
    }

    /**
     *  //4.添加医院设置
     * @param hospitalSet
     * @return
     */
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1 可以用 0 不能用
        hospitalSet.setStatus(1);
        //设置签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(random.nextInt(1000) + "" + System.currentTimeMillis()));
        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    //5.根据id获取医院设置
    @GetMapping("/getHospitalSet/{id}")
    public Result getHospitalSet(@PathVariable Long id){

        HospitalSet byId = hospitalSetService.getById(id);
        if(null != byId){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }


    //6.修改医院设置
    @PostMapping("/updateHospitaSet")
    public Result updateHospitaSet(@RequestBody HospitalSet hospitalSet){
        boolean b = hospitalSetService.updateById(hospitalSet);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    //7批量删除医院设置
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitSet(@RequestBody List<Long> idList){

        hospitalSetService.removeByIds(idList);
        return Result.ok();

    }
    //8.设置锁定和解锁
    @PutMapping("/lockHospitSet/{id}/{status}")
    public Result lockHospitSet(@PathVariable Long id,
                                @PathVariable Integer status){

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();

    }

    //9.发送签名密钥
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id){

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signkey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();

        //TODO 发送短信

        return Result.ok();


    }




}
