package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    //根据数据id查询子数据列表

    /**
     *
     * @param id
     * @return
     */
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChlidData(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", id);
        List<Dict> list = baseMapper.selectList(queryWrapper);
        for (Dict dict : list) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return list;
    }

    @CacheEvict(value = "dict",allEntries = true)
    @Override
    public void exportData(HttpServletResponse response) {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importDictData(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 判断是否是父级——id
     *
     * @param id
     * @return
     */
    private boolean isChildren(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", id);
        Integer c = baseMapper.selectCount(queryWrapper);
        return c > 0;//
    }

    //根据上级编码与值获取数据字典名称
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
//如果value能唯一定位数据字典，parentDictCode可以传空，例如：省市区的value值能够唯一确定
        if(StringUtils.isEmpty(parentDictCode)) {
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("value", value));
            if(null != dict) {
                return dict.getName();
            }
        } else {
//            Dict parentDict = this.getByDictsCode(parentDictCode);
            Dict parentDict = baseMapper.selectById(parentDictCode);
            if(null == parentDict){
                return "";
            }
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", parentDict.getId()).eq("value", value));
            if(null != dict) {
                return dict.getName();
            }
        }
        return "";
    }


    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict codeDict = this.getDictBcyDictCode(dictCode);
        if(null == codeDict) {
            return null;
        }
        return this.findChlidData(codeDict.getId());
    }

    private Dict getDictBcyDictCode(String dictCode) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);
        return dict;
    }


}
