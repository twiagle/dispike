package com.twiagle.dispike.customer.dao;

import com.twiagle.dispike.common.entities.SpikeUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SpikeUserDao {
    final String TABLE_NAME = "spike_user";

    @Select("select * from " + TABLE_NAME + " where id = #{id}")
    public SpikeUser getByID(@Param("id") long id);

    @Update("update " + TABLE_NAME + "set password = #{password} where id={id}")
    public void update(SpikeUser toBeUpdate);

    @Insert("insert into "+ TABLE_NAME + "(id, nickname, password, salt, head, register_date, last_login_date, login_count)" +
            "values (#{id}, #{nickname}, #{password}, #{salt}, #{head}, #{registerDate}, #{lastLoginDate}, #{loginCount)")
    long insertUser(SpikeUser newUser);
}
