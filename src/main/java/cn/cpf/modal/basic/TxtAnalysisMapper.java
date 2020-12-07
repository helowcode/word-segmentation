package cn.cpf.modal.basic;

import cn.cpf.modal.entity.TxtAnalysis;

public interface TxtAnalysisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TxtAnalysis record);

    int insertSelective(TxtAnalysis record);

    TxtAnalysis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxtAnalysis record);

    int updateByPrimaryKey(TxtAnalysis record);
}