package com.dyan.weiyan.dao;

import com.dyan.weiyan.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Dyan on 17/7/24.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, email, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{email},#{password},#{salt},#{headUrl})"})
    int addUser(User user);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    User selectById(int id);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where email = #{email}"})
    User selectByEmail(String email);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name = #{name}"})
    User selectByName(String name);
}
