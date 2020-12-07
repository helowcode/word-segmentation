package cn.cpf.modal.basic;

import cn.cpf.modal.entity.FileInfo;

public interface FileInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FileInfo record);

    int insertSelective(FileInfo record);

    FileInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FileInfo record);

    int updateByPrimaryKey(FileInfo record);
}